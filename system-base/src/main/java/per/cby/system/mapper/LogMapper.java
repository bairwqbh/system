package per.cby.system.mapper;

import org.springframework.stereotype.Repository;

import per.cby.frame.common.base.BaseMapper;
import per.cby.system.model.Log;

/**
 * <p>
 * 日志表 Mapper 接口
 * </p>
 *
 * @author chenboyang
 * @since 2018-01-26
 */
@Repository("__LOG_MAPPER__")
public interface LogMapper extends BaseMapper<Log> {

}
