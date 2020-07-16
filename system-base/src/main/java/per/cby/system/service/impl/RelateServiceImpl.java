package per.cby.system.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import per.cby.frame.common.base.AbstractService;
import per.cby.system.mapper.RelateMapper;
import per.cby.system.model.Relate;
import per.cby.system.service.RelateService;

/**
 * <p>
 * 关联关系表 服务实现类
 * </p>
 *
 * @author chenboyang
 * @since 2019-03-07
 */
@Service("__RELATE_SERVICE__")
public class RelateServiceImpl extends AbstractService<RelateMapper, Relate> implements RelateService {

    @Override
    protected Wrapper<Relate> queryWrapper(Map<String, Object> param) {
        return new LambdaQueryWrapper<Relate>().orderByDesc(Relate::getId);
    }

    @Override
    public List<String> getTargetList(String objectId, String relateType) {
        return list(new LambdaQueryWrapper<Relate>().eq(Relate::getObjectId, objectId).eq(Relate::getRelateType,
                relateType)).stream().map(Relate::getTargetId).collect(Collectors.toList());
    }

}
