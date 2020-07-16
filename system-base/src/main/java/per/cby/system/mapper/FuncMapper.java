package per.cby.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import per.cby.frame.common.base.BaseMapper;
import per.cby.system.model.Func;

/**
 * <p>
 * 功能表 Mapper 接口
 * </p>
 *
 * @author chenboyang
 * @since 2019-03-07
 */
@Repository("__FUNC_MAPPER__")
public interface FuncMapper extends BaseMapper<Func> {

    /**
     * 获取当前最大排序值
     * 
     * @param parentId 上级编号
     * @return 排序值
     */
    @Select("SELECT MAX(sort) FROM sys_func WHERE parent_id = #{parentId}")
    Integer currSort(@Param("parentId") String parentId);

    /**
     * 查询是否包含下级
     * 
     * @param wrapper 参数
     * @return 功能列表
     */
    @Select("SELECT id, (IF ((SELECT COUNT(1) FROM sys_func t WHERE t.parent_id = sys_func.func_id) >= 1, TRUE, FALSE)) AS has_child FROM sys_func ${ew.customSqlSegment}")
    List<Func> hasChild(@Param(Constants.WRAPPER) Wrapper<Func> wrapper);

}
