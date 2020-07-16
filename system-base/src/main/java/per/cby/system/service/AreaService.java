package per.cby.system.service;

import java.util.List;

import per.cby.frame.common.base.BaseService;
import per.cby.system.model.Area;

/**
 * <p>
 * 地区表 服务类
 * </p>
 *
 * @author chenboyang
 * @since 2018-01-26
 */
public interface AreaService extends BaseService<Area> {

    /** 地区树容器键 */
    String AREA_TREE = "area_tree";

    /**
     * 根据上级地区编码加载地区树状列表
     * 
     * @param areaId 上级地区编码
     * @return 树状列表
     */
    List<Area> tree(String areaId);

    /**
     * 向下递归查询地区
     * 
     * @param areaIds 地区编号
     * @return 地区列表
     */
    List<Area> downRecursive(String... areaIds);

    /**
     * 向上递归查询地区
     * 
     * @param areaIds 地区编号
     * @return 地区列表
     */
    List<Area> upRecursive(String... areaIds);

    /**
     * 获取地区权限
     * 
     * @param areaId 地区编号
     * @return 地区权限
     */
    String areaAuth(String areaId);

}
