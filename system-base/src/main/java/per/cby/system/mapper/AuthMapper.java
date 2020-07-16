package per.cby.system.mapper;

import org.springframework.stereotype.Repository;

import per.cby.frame.common.base.BaseMapper;
import per.cby.system.model.Auth;

/**
 * <p>
 * 权限表 Mapper 接口
 * </p>
 *
 * @author chenboyang
 * @since 2019-03-07
 */
@Repository("__AUTH_MAPPER__")
public interface AuthMapper extends BaseMapper<Auth> {

}
