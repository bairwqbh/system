package per.cby.system.mapper;

import org.springframework.stereotype.Repository;

import per.cby.frame.common.base.BaseMapper;
import per.cby.system.model.Role;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author chenboyang
 * @since 2018-01-26
 */
@Repository("__ROLE_MAPPER__")
public interface RoleMapper extends BaseMapper<Role> {

}
