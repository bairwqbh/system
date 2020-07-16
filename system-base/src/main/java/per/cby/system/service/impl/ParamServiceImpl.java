package per.cby.system.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import per.cby.frame.common.base.AbstractService;
import per.cby.system.mapper.ParamMapper;
import per.cby.system.model.Param;
import per.cby.system.service.ParamService;

/**
 * <p>
 * 参数表 : 用于配置业务参数 服务实现类
 * </p>
 *
 * @author chenboyang
 * @since 2018-01-26
 */
@Service("__PARAM_SERVICE__")
public class ParamServiceImpl extends AbstractService<ParamMapper, Param> implements ParamService {

    @Override
    protected Wrapper<Param> queryWrapper(Map<String, Object> param) {
        return new LambdaQueryWrapper<Param>().and(param.get("keyword") != null, w -> w
                .like(Param::getParamKey, param.get("keyword")).or().like(Param::getParamLabel, param.get("keyword")))
                .orderByDesc(Param::getId);
    }

    @Override
    public String get(String key) {
        Param param = lambdaQuery().eq(Param::getParamKey, key).one();
        if (param != null && Boolean.TRUE.equals(param.getEnable())) {
            return param.getParamValue();
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean set(String key, String value) {
        boolean result = false;
        Param param = lambdaQuery().eq(Param::getParamKey, key).one();
        if (param != null) {
            result = updateById(param.setParamValue(value));
        } else {
            param = new Param().setParamKey(key).setParamValue(value);
            result = save(param);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(String key) {
        return lambdaUpdate().eq(Param::getParamKey, key).remove();
    }

}
