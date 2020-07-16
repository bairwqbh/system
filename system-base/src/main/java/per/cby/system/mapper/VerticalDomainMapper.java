package per.cby.system.mapper;

import org.springframework.stereotype.Repository;

import per.cby.frame.common.base.BaseMapper;
import per.cby.system.model.VerticalDomain;

/**
 * <p>
 * 纵向数据模型表 : 各业务基础表的纵向数据模型扩展表 Mapper 接口
 * </p>
 *
 * @author chenboyang
 * @since 2018-01-26
 */
@Repository("__VERTICAL_DOMAIN_MAPPER__")
public interface VerticalDomainMapper extends BaseMapper<VerticalDomain> {

}
