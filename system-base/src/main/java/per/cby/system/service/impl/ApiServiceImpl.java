package per.cby.system.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import per.cby.frame.common.base.AbstractService;
import per.cby.frame.common.tree.TreeableService;
import per.cby.system.constant.SystemDict.RelateType;
import per.cby.system.mapper.ApiMapper;
import per.cby.system.model.Api;
import per.cby.system.model.Relate;
import per.cby.system.service.ApiService;
import per.cby.system.service.RelateService;

/**
 * <p>
 * 接口表 服务实现类
 * </p>
 *
 * @author chenboyang
 * @since 2019-03-07
 */
@Service("__API_SERVICE__")
public class ApiServiceImpl extends AbstractService<ApiMapper, Api> implements ApiService {

    @Autowired
    private TreeableService<Api> treeableService;

    @Autowired
    private RelateService relateService;

    @Override
    protected Wrapper<Api> queryWrapper(Map<String, Object> param) {
        return new LambdaQueryWrapper<Api>().eq(param.get("url") != null, Api::getUrl, param.get("url"))
                .orderByDesc(Api::getId);
    }

    @Override
    public List<String> apiIdList(Collection<String> funcIds) {
        if (CollectionUtils.isEmpty(funcIds)) {
            return null;
        }
        List<Relate> list = relateService.list(new LambdaQueryWrapper<Relate>()
                .eq(Relate::getRelateType, RelateType.FUNC_API).in(Relate::getObjectId, funcIds));
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.stream().filter(relate -> StringUtils.isNotBlank(relate.getTargetId())).map(Relate::getTargetId)
                .distinct().collect(Collectors.toList());
    }

    @Override
    public List<Api> tree() {
        return treeableService.tree(list());
    }

}
