package per.cby.system.mapper;

import org.springframework.stereotype.Repository;

import per.cby.frame.common.base.BaseMapper;
import per.cby.system.model.User;

/**
 * <p>
 * 用户表 : 系统管理用户表 Mapper 接口
 * </p>
 *
 * @author chenboyang
 * @since 2018-01-26
 */
@Repository("__USER_MAPPER__")
public interface UserMapper extends BaseMapper<User> {

}
