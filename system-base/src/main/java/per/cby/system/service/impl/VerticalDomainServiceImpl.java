package per.cby.system.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import per.cby.frame.common.base.AbstractService;
import per.cby.system.mapper.VerticalDomainMapper;
import per.cby.system.model.VerticalDomain;
import per.cby.system.service.VerticalDomainService;

/**
 * <p>
 * 纵向数据模型表 : 各业务基础表的纵向数据模型扩展表 服务实现类
 * </p>
 *
 * @author chenboyang
 * @since 2018-01-26
 */
@Service("__VERTICAL_DOMAIN_SERVICE__")
public class VerticalDomainServiceImpl extends AbstractService<VerticalDomainMapper, VerticalDomain>
        implements VerticalDomainService {

    @Override
    protected Wrapper<VerticalDomain> queryWrapper(Map<String, Object> param) {
        return new LambdaQueryWrapper<VerticalDomain>().orderByDesc(VerticalDomain::getId);
    }

}
