package per.cby.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import per.cby.frame.common.base.BaseMapper;
import per.cby.system.model.Dict;

/**
 * <p>
 * 字典表 : 用于存放各业务的类型字典 Mapper 接口
 * </p>
 *
 * @author chenboyang
 * @since 2018-01-26
 */
@Repository("__DICT_MAPPER__")
public interface DictMapper extends BaseMapper<Dict> {

    /**
     * 获取当前最大排序值
     * 
     * @param parentId 上级编码
     * @return 排序值
     */
    @Select("SELECT MAX(sort) FROM sys_dict WHERE parent_id = #{parentId}")
    Integer currSort(@Param("parentId") String parentId);

    /**
     * 查询是否包含下级
     * 
     * @param wrapper 参数
     * @return 字典列表
     */
    @Select("SELECT id, (IF ((SELECT COUNT(1) FROM sys_dict t WHERE t.parent_id = sys_dict.code) >= 1, TRUE, FALSE)) AS has_child FROM sys_dict ${ew.customSqlSegment}")
    List<Dict> hasChild(@Param(Constants.WRAPPER) Wrapper<Dict> wrapper);

}
