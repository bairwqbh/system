package per.cby.system.mapper;

import org.springframework.stereotype.Repository;

import per.cby.frame.common.base.BaseMapper;
import per.cby.system.model.Param;

/**
 * <p>
 * 参数表 : 用于配置业务参数 Mapper 接口
 * </p>
 *
 * @author chenboyang
 * @since 2018-01-26
 */
@Repository("__PARAM_MAPPER__")
public interface ParamMapper extends BaseMapper<Param> {

}
