package per.cby.system.service.impl;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import per.cby.frame.common.base.AbstractService;
import per.cby.frame.common.exception.BusinessAssert;
import per.cby.frame.common.tree.TreeableService;
import per.cby.frame.common.util.BaseUtil;
import per.cby.frame.common.util.JudgeUtil;
import per.cby.system.constant.SystemConstants;
import per.cby.system.mapper.OrgMapper;
import per.cby.system.mapper.UserMapper;
import per.cby.system.model.Org;
import per.cby.system.model.User;
import per.cby.system.service.DictService;
import per.cby.system.service.OrgService;

/**
 * <p>
 * 机构表 : 系统操作员的机构管理 服务实现类
 * </p>
 *
 * @author chenboyang
 * @since 2018-01-26
 */
@Service("__ORG_SERVICE__")
@SuppressWarnings("unchecked")
public class OrgServiceImpl extends AbstractService<OrgMapper, Org> implements OrgService, SystemConstants {

    @Autowired
    protected TreeableService<Org> treeableService;

    @Autowired
    protected UserMapper userMapper;

    @Autowired
    protected DictService dictService;

    @Override
    protected Wrapper<Org> queryWrapper(Map<String, Object> param) {
        return new LambdaQueryWrapper<Org>()
                .inSql(param.get(ORG_AUTH) != null, Org::getOrgId, String.valueOf(param.get(ORG_AUTH)))
//                .inSql(param.get(AREA_AUTH) != null, Org::getAreaId, String.valueOf(param.get(AREA_AUTH)))
                .like(param.get("orgName") != null, Org::getOrgName, param.get("orgName"))
                .eq(param.get("orgId") != null, Org::getOrgId, param.get("orgId"))
                .eq(param.get("parentId") != null, Org::getParentId, param.get("parentId"))
                .and(param.get("keyword") != null, w -> w.like(Org::getOrgId, param.get("keyword")).or()
                        .like(Org::getOrgName, param.get("keyword")))
                .orderByAsc(Org::getParentId, Org::getSort);
    }

    @Override
    protected List<Org> valueQueryResult(List<Org> list) {
        if (CollectionUtils.isEmpty(list)) {
            return list;
        }
        List<Long> idList = list.stream().map(Org::getId).collect(Collectors.toList());
        LambdaQueryWrapper<Org> wrapper = new LambdaQueryWrapper<Org>();
        wrapper.in(Org::getId, idList);
        List<Org> hasChildList = baseMapper.hasChild(wrapper);
        hasChildList.removeIf(t -> !t.isHasChild());
        if (CollectionUtils.isNotEmpty(hasChildList)) {
            Map<Long, Org> map = list.stream().collect(Collectors.toMap(Org::getId, Function.identity()));
            hasChildList.forEach(t -> map.get(t.getId()).setHasChild(true));
        }
        return list;
    }
 
    @Override
    public List<Org> tree(String parentId) {
        if (parentId == null) {
            return treeableService.tree(list(new LambdaQueryWrapper<Org>().orderByAsc(Org::getParentId, Org::getSort)));
        } else {
            List<Org> list = downRecursive(parentId);
            if (CollectionUtils.isNotEmpty(list)) {
                return Optional.ofNullable(getOne(new LambdaQueryWrapper<Org>().eq(Org::getOrgId, parentId)))
                        .map(Org::getParentId).map(pId -> treeableService.tree(list, pId))
                        .orElseGet(() -> treeableService.tree(list));
            }
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(Org record) {
        if (record == null) {
            return false;
        }
        valiExist(Org::getOrgId, record.getOrgId(), "组织机构编号已存在！");
        if (record.getSort() == null || record.getSort().intValue() <= 0) {
            Integer sort = baseMapper.currSort(record.getParentId());
            if (sort == null || sort.intValue() <= 0) {
                sort = 1;
            } else {
                sort++;
            }
            record.setSort(sort);
        }
        return record.insert();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Serializable id) {
        Org org = getById(id);
        if (org == null) {
            return false;
        }
        int count = userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getOrgId, org.getOrgId()));
        BusinessAssert.isTrue(count <= 0, "机构有用户存在，无法删除！");
        return org.deleteById();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean up(Org org) {
        int num = 0;
        Integer sort = org.getSort();
        Org previous = getOne(new LambdaQueryWrapper<Org>().eq(Org::getParentId, org.getParentId())
                .lt(Org::getSort, sort).orderByDesc(Org::getSort));
        if (previous != null) {
            org.setSort(previous.getSort());
            previous.setSort(sort);
            num = baseMapper.updateBatchById(BaseUtil.newArrayList(org, previous));
        }
        return num == 2;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean down(Org org) {
        int num = 0;
        Integer sort = org.getSort();
        Org next = getOne(new LambdaQueryWrapper<Org>().eq(Org::getParentId, org.getParentId()).gt(Org::getSort, sort)
                .orderByAsc(Org::getSort));
        if (next != null) {
            org.setSort(next.getSort());
            next.setSort(sort);
            num = baseMapper.updateBatchById(BaseUtil.newArrayList(org, next));
        }
        return num == 2;
    }

    @Override
    public List<Org> downRecursive(String... orgIds) {
        List<Org> result = BaseUtil.newArrayList();
        if (ArrayUtils.isEmpty(orgIds)) {
            return result;
        }
        List<Org> list = valueQueryResult(list(new LambdaQueryWrapper<Org>().in(Org::getOrgId, Arrays.asList(orgIds))));
        result = list;
        while (list.stream().anyMatch(Org::isHasChild)) {
            list = valueQueryResult(list(new LambdaQueryWrapper<Org>().in(Org::getParentId,
                    list.stream().filter(Org::isHasChild).map(Org::getOrgId).collect(Collectors.toList()))));
            if (CollectionUtils.isEmpty(list)) {
                break;
            }
            result.addAll(list);
        }
        return result;
    }

    @Override
    public List<Org> upRecursive(String... orgIds) {
        List<Org> result = BaseUtil.newArrayList();
        if (ArrayUtils.isEmpty(orgIds)) {
            return result;
        }
        List<Org> list = valueQueryResult(list(new LambdaQueryWrapper<Org>().in(Org::getOrgId, Arrays.asList(orgIds))));
        result = list;
        while (list.stream().anyMatch(
                t -> JudgeUtil.isAllNotEqual(t.getParentId(), TreeableService.TOP_CODE, TreeableService.TOP_LEVEL))) {
            list = valueQueryResult(list(new LambdaQueryWrapper<Org>().in(Org::getOrgId, list.stream().filter(
                    t -> JudgeUtil.isAllNotEqual(t.getParentId(), TreeableService.TOP_CODE, TreeableService.TOP_LEVEL))
                    .map(Org::getParentId).collect(Collectors.toList()))));
            if (CollectionUtils.isEmpty(list)) {
                break;
            }
            result.addAll(list);
        }
        return result;
    }

    @Override
    public String orgAuth(String orgId) {
        if (StringUtils.isNotBlank(orgId)
                && JudgeUtil.isOneEqual(TreeableService.TOP_LEVEL, TreeableService.TOP_CODE)) {
            return null;
        }
        List<Org> list = downRecursive(orgId);
        if (CollectionUtils.isEmpty(list)) {
            return "$";
        }
        return list.stream().map(Org::getOrgId).collect(Collectors.joining(","));
    }

}
