package per.cby.system.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import per.cby.frame.common.base.AbstractService;
import per.cby.frame.common.sys.storage.SystemStorage;
import per.cby.frame.common.tree.TreeCompare;
import per.cby.frame.common.tree.TreeableService;
import per.cby.frame.common.util.BaseUtil;
import per.cby.frame.common.util.JudgeUtil;
import per.cby.system.constant.SystemConstants;
import per.cby.system.mapper.AreaMapper;
import per.cby.system.model.Area;
import per.cby.system.service.AreaService;

/**
 * <p>
 * 地区表 服务实现类
 * </p>
 *
 * @author chenboyang
 * @since 2018-01-26
 */
@Service
@SuppressWarnings("unchecked")
public class AreaServiceImpl extends AbstractService<AreaMapper, Area> implements AreaService, SystemConstants {

    @Autowired
    private TreeableService<Area> treeableService;

    @Autowired
    private SystemStorage systemStorage;

    @Override
    protected Wrapper<Area> queryWrapper(Map<String, Object> param) {
        return new LambdaQueryWrapper<Area>()
//                .inSql(param.get(AREA_AUTH) != null, Area::getId, String.valueOf(param.get(AREA_AUTH)))
                .eq(param.get("parentId") != null, Area::getParentId, param.get("parentId"))
                .and(param.get("keyword") != null, w -> w.like(Area::getAreaId, param.get("keyword")).or()
                        .like(Area::getAreaName, param.get("keyword")))
                .orderByAsc(Area::getId);
    }

    @Override
    protected List<Area> valueQueryResult(List<Area> list) {
        if (CollectionUtils.isEmpty(list)) {
            return list;
        }
        List<Long> idList = list.stream().map(Area::getId).collect(Collectors.toList());
        LambdaQueryWrapper<Area> wrapper = new LambdaQueryWrapper<Area>();
        wrapper.in(Area::getId, idList);
        List<Area> hasChildList = baseMapper.hasChild(wrapper);
        hasChildList.removeIf(t -> !t.isHasChild());
        if (CollectionUtils.isNotEmpty(hasChildList)) {
            Map<Long, Area> map = list.stream().collect(Collectors.toMap(Area::getId, Function.identity()));
            hasChildList.forEach(t -> map.get(t.getId()).setHasChild(true));
        }
        return list;
    }

    @Override
    public List<Area> tree(String areaId) {
        List<Area> area = systemStorage.getOrSet(AREA_TREE, () -> treeableService
                .tree(list(new LambdaQueryWrapper<Area>().orderByAsc(Area::getParentId, Area::getId))));
        if (areaId == null) {
            return area;
        } else if (CollectionUtils.isNotEmpty(area)) {
            Area node = getTreeNode(area, areaId);
            if (node != null) {
                return BaseUtil.newArrayList(node);
            }
        }
        return null;
    }

    @Override
    public List<Area> downRecursive(String... areaIds) {
        List<Area> result = BaseUtil.newArrayList();
        if (ArrayUtils.isEmpty(areaIds)) {
            return result;
        }
        List<Area> list = valueQueryResult(
                list(new LambdaQueryWrapper<Area>().in(Area::getAreaId, Arrays.asList(areaIds))));
        result = list;
        while (list.stream().anyMatch(Area::isHasChild)) {
            list = valueQueryResult(list(new LambdaQueryWrapper<Area>().in(Area::getParentId,
                    list.stream().filter(Area::isHasChild).map(Area::getAreaId).collect(Collectors.toList()))));
            if (CollectionUtils.isEmpty(list)) {
                break;
            }
            result.addAll(list);
        }
        return result;
    }

    @Override
    public List<Area> upRecursive(String... areaIds) {
        List<Area> result = BaseUtil.newArrayList();
        if (ArrayUtils.isEmpty(areaIds)) {
            return result;
        }
        List<Area> list = valueQueryResult(
                list(new LambdaQueryWrapper<Area>().in(Area::getAreaId, Arrays.asList(areaIds))));
        result = list;
        while (list.stream().anyMatch(
                t -> JudgeUtil.isAllNotEqual(t.getParentId(), TreeableService.TOP_CODE, TreeableService.TOP_LEVEL))) {
            list = valueQueryResult(list(new LambdaQueryWrapper<Area>().in(Area::getAreaId, list.stream().filter(
                    t -> JudgeUtil.isAllNotEqual(t.getParentId(), TreeableService.TOP_CODE, TreeableService.TOP_LEVEL))
                    .map(Area::getParentId).collect(Collectors.toList()))));
            if (CollectionUtils.isEmpty(list)) {
                break;
            }
            result.addAll(list);
        }
        return result;
    }

    @Override
    public String areaAuth(String areaId) {
        if (StringUtils.isNotBlank(areaId) && JudgeUtil.isOneEqual(TreeableService.TOP_LEVEL, TreeableService.TOP_CODE,
                TreeCompare.NATIONAL_SUFFIX)) {
            return null;
        }
        List<Area> list = downRecursive(areaId);
        if (CollectionUtils.isEmpty(list)) {
            return "$";
        }
        return list.stream().map(Area::getAreaId).collect(Collectors.joining(","));
    }

    /**
     * 根据地区编码获取地区树节点
     * 
     * @param area     地区树
     * @param areaCode 地区编码
     * @return 树节点
     */
    private Area getTreeNode(List<Area> area, String areaCode) {
        for (Area t : area) {
            if (areaCode.equals(t.getAreaId())) {
                return t;
            } else if (CollectionUtils.isNotEmpty(t.getChildren()) && TreeCompare.isBelong(areaCode, t.getAreaId())) {
                Area child = getTreeNode(area, areaCode);
                if (child != null) {
                    return child;
                }
            }
        }
        return null;
    }

}
