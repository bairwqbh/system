package per.cby.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import per.cby.frame.common.base.BaseMapper;
import per.cby.system.model.Area;

/**
 * <p>
 * 地区表 Mapper 接口
 * </p>
 *
 * @author chenboyang
 * @since 2018-01-26
 */
@Repository("__AREA_MAPPER__")
public interface AreaMapper extends BaseMapper<Area> {

    /**
     * 查询是否包含下级
     * 
     * @param wrapper 参数
     * @return 地区列表
     */
    @Select("SELECT id, (IF ((SELECT COUNT(1) FROM sys_area t WHERE t.parent_id = sys_area.area_id) >= 1, TRUE, FALSE)) AS has_child FROM sys_area ${ew.customSqlSegment}")
    List<Area> hasChild(@Param(Constants.WRAPPER) Wrapper<Area> wrapper);

}
