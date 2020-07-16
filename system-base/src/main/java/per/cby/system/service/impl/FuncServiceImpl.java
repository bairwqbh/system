package per.cby.system.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
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
import per.cby.system.constant.SystemConstants;
import per.cby.system.constant.SystemDict.AuthMode;
import per.cby.system.constant.SystemDict.FuncType;
import per.cby.system.constant.SystemDict.RelateType;
import per.cby.system.dto.FuncDto;
import per.cby.system.mapper.FuncMapper;
import per.cby.system.model.Auth;
import per.cby.system.model.Func;
import per.cby.system.service.AuthService;
import per.cby.system.service.FuncService;

/**
 * <p>
 * 功能表 服务实现类
 * </p>
 *
 * @author chenboyang
 * @since 2019-03-07
 */
@Service("__FUNC_SERVICE__")
@SuppressWarnings("unchecked")
public class FuncServiceImpl extends AbstractService<FuncMapper, Func> implements FuncService {

    @Autowired
    private TreeableService<Func> treeableService;

    @Autowired
    private AuthService authService;

    @Override
    protected Wrapper<Func> queryWrapper(Map<String, Object> param) {
        return new LambdaQueryWrapper<Func>()
                .like(param.get("funcName") != null, Func::getFuncName, param.get("funcName"))
                .eq(param.get("parentId") != null, Func::getParentId, param.get("parentId"))
                .eq(param.get("sysId") != null, Func::getSysId, param.get("sysId"))
                .and(param.get("keyword") != null, w -> w.like(Func::getFuncId, param.get("keyword")).or()
                        .like(Func::getFuncName, param.get("keyword")))
                .orderByAsc(Func::getParentId, Func::getSort);
    }

    @Override
    protected List<Func> valueQueryResult(List<Func> list) {
        if (CollectionUtils.isEmpty(list)) {
            return list;
        }
        List<Long> idList = list.stream().map(Func::getId).collect(Collectors.toList());
        LambdaQueryWrapper<Func> wrapper = new LambdaQueryWrapper<Func>();
        wrapper.in(Func::getId, idList);
        List<Func> hasChildList = baseMapper.hasChild(wrapper);
        hasChildList.removeIf(t -> !t.isHasChild());
        if (CollectionUtils.isNotEmpty(hasChildList)) {
            Map<Long, Func> map = list.stream().collect(Collectors.toMap(Func::getId, Function.identity()));
            hasChildList.forEach(t -> map.get(t.getId()).setHasChild(true));
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(Func record) {
        boolean result = false;
        if (record != null) {
            if (record.getSort() == null || record.getSort().intValue() <= 0) {
                Integer sort = baseMapper.currSort(record.getParentId());
                if (sort == null || sort.intValue() <= 0) {
                    sort = 1;
                } else {
                    sort++;
                }
                record.setSort(sort);
            }
            result = record.insert();
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Serializable id) {
        Func func = getById(id);
        BusinessAssert.notNull(func, "功能不存在，无法删除！");
        authService.removeAuth(func.getFuncId());
        return func.deleteById();
    }

    @Override
    public boolean up(Func func) {
        int num = 0;
        Integer sort = func.getSort();
        Func previous = getOne(new LambdaQueryWrapper<Func>().eq(Func::getParentId, func.getParentId())
                .lt(Func::getSort, sort).orderByDesc(Func::getSort));
        if (previous != null) {
            func.setSort(previous.getSort());
            previous.setSort(sort);
            num = baseMapper.updateBatchById(BaseUtil.newArrayList(func, previous));
        }
        return num == 2;
    }

    @Override
    public boolean down(Func func) {
        int num = 0;
        Integer sort = func.getSort();
        Func next = getOne(new LambdaQueryWrapper<Func>().eq(Func::getParentId, func.getParentId())
                .gt(Func::getSort, sort).orderByAsc(Func::getSort));
        if (next != null) {
            func.setSort(next.getSort());
            next.setSort(sort);
            num = baseMapper.updateBatchById(BaseUtil.newArrayList(func, next));
        }
        return num == 2;
    }

    @Override
    public List<Func> list(FuncDto funcDto) {
        LambdaQueryWrapper<Func> wrapper = new LambdaQueryWrapper<Func>().eq(StringUtils.isNotBlank(funcDto.getSysId()),
                Func::getSysId, funcDto.getSysId());
        if (CollectionUtils.isNotEmpty(funcDto.getRoleIds())) {
            List<Auth> authList = funcDto.getRoleIds().stream()
                    .map(roleId -> authService.getAuth(roleId, RelateType.ROLE_FUNC)).collect(Collectors.toList());
            authList.removeIf(Objects::isNull);
            boolean isAll = authList.stream().anyMatch(auth -> AuthMode.ALL.getCode().equals(auth.getAuthMode()));
            if (isAll == false) {
                List<String> inList = new ArrayList<String>();
                List<String> excList = new ArrayList<String>();
                authList.forEach(auth -> {
                    if (CollectionUtils.isNotEmpty(auth.getTargetList())) {
                        if (AuthMode.IN.getCode().equals(auth.getAuthMode())) {
                            inList.addAll(auth.getTargetList());
                        } else if (AuthMode.EXC.getCode().equals(auth.getAuthMode())) {
                            excList.addAll(auth.getTargetList());
                        }
                    }
                });
                if (inList.isEmpty() && excList.isEmpty()) {
                    return null;
                } else if (!inList.isEmpty() && excList.isEmpty()) {
                    wrapper.in(Func::getFuncId, inList);
                } else if (inList.isEmpty() && !excList.isEmpty()) {
                    wrapper.notIn(Func::getFuncId, excList);
                } else if (!inList.isEmpty() && !excList.isEmpty()) {
                    wrapper.notIn(Func::getFuncId, excList.removeAll(inList));
                }
            }
        }
        wrapper.orderByAsc(Func::getParentId, Func::getSort);
        return list(wrapper);
    }

    @Override
    public List<Func> tree(FuncDto funcDto) {
        return treeableService.tree(list(funcDto));
    }

    @Override
    public List<String> authList(Collection<String> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return null;
        }
        List<Func> list = list(new FuncDto().setRoleIds(roleIds));
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.stream().filter(func -> StringUtils.isNotBlank(func.getAuth())).map(Func::getAuth).distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<Func> menu(FuncDto funcDto) {
        if (StringUtils.isBlank(funcDto.getSysId()) || CollectionUtils.isEmpty(funcDto.getRoleIds())) {
            return null;
        }
        List<Func> list = list(funcDto);
        if (CollectionUtils.isEmpty(list)) {
            return list;
        }
        list.removeIf(func -> FuncType.BTN.getCode().equals(func.getFuncType()));
        if (!funcDto.getRoleIds().contains(SystemConstants.ADMIN)) {
            list.removeIf(func -> func.getDisplay() != true);
        }
        return treeableService.tree(list, list.get(0).getFuncId());
    }

}
