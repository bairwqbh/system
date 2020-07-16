package per.cby.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import per.cby.frame.common.base.BaseMapper;
import per.cby.system.model.Org;

/**
 * <p>
 * 机构表 : 系统操作员的机构管理 Mapper 接口
 * </p>
 *
 * @author chenboyang
 * @since 2018-01-26
 */
@Repository("__ORG_MAPPER__")
public interface OrgMapper extends BaseMapper<Org> {

    /**
     * 获取当前最大排序值
     * 
     * @param parentId 上级编码
     * @return 排序值
     */
    @Select("SELECT MAX(sort) FROM sys_org WHERE parent_id = #{parentId}")
    Integer currSort(@Param("parentId") String parentId);

    /**
     * 查询是否包含下级
     * 
     * @param wrapper 参数
     * @return 功能列表
     */
    @Select("SELECT id, (IF ((SELECT COUNT(1) FROM sys_org t WHERE t.parent_id = sys_org.org_id) >= 1, TRUE, FALSE)) AS has_child FROM sys_org ${ew.customSqlSegment}")
    List<Org> hasChild(@Param(Constants.WRAPPER) Wrapper<Org> wrapper);

}
