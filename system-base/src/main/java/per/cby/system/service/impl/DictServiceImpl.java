package per.cby.system.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import per.cby.frame.common.base.AbstractService;
import per.cby.frame.common.model.cache.DataCache;
import per.cby.frame.common.tree.TreeableService;
import per.cby.frame.common.util.BaseUtil;
import per.cby.frame.common.util.IDUtil;
import per.cby.frame.common.util.JudgeUtil;
import per.cby.system.constant.SystemConstants;
import per.cby.system.mapper.DictMapper;
import per.cby.system.model.Dict;
import per.cby.system.service.DictService;

/**
 * <p>
 * 字典表 : 用于存放各业务的类型字典 服务实现类
 * </p>
 *
 * @author chenboyang
 * @since 2018-01-26
 */
@Service("__DICT_SERVICE__")
@SuppressWarnings("unchecked")
public class DictServiceImpl extends AbstractService<DictMapper, Dict> implements DictService {

    @Autowired
    private TreeableService<Dict> treeableService;

    /**
     * 字典缓存过期时间（单位：秒）
     */
    private final int timeout = 60;

    @Autowired(required = false)
    @Qualifier("simpleDataCache")
    private DataCache<String, Map<String, Map<String, String>>> dataCache;

    @Override
    protected Wrapper<Dict> queryWrapper(Map<String, Object> param) {
        return new LambdaQueryWrapper<Dict>().like(param.get("name") != null, Dict::getName, param.get("name"))
                .eq(param.get("parentId") != null, Dict::getParentId, param.get("parentId"))
                .and(param.get("keyword") != null,
                        w -> w.like(Dict::getCode, param.get("keyword")).or().like(Dict::getName, param.get("keyword")))
                .orderByAsc(Dict::getParentId, Dict::getSort);
    }

    @Override
    protected List<Dict> valueQueryResult(List<Dict> list) {
        if (CollectionUtils.isEmpty(list)) {
            return list;
        }
        List<Long> idList = list.stream().map(Dict::getId).collect(Collectors.toList());
        LambdaQueryWrapper<Dict> wrapper = new LambdaQueryWrapper<Dict>();
        wrapper.in(Dict::getId, idList);
        List<Dict> hasChildList = baseMapper.hasChild(wrapper);
        hasChildList.removeIf(t -> !t.isHasChild());
        if (CollectionUtils.isNotEmpty(hasChildList)) {
            Map<Long, Dict> map = list.stream().collect(Collectors.toMap(Dict::getId, Function.identity()));
            hasChildList.forEach(t -> map.get(t.getId()).setHasChild(true));
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(Dict record) {
        if (record != null) {
            if (StringUtils.isBlank(record.getCode())) {
                record.setCode(IDUtil.createTimeId());
            }
            if (StringUtils.isBlank(record.getParentId())) {
                record.setParentId(TreeableService.TOP_CODE);
            }
            if (record.getSort() == null || record.getSort().intValue() <= 0) {
                Integer sort = baseMapper.currSort(record.getParentId());
                if (sort == null || sort.intValue() <= 0) {
                    sort = 1;
                } else {
                    sort++;
                }
                record.setSort(sort);
            }
            return record.insert();
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean up(Dict dict) {
        int num = 0;
        Integer sort = dict.getSort();
        Dict previous = getOne(new LambdaQueryWrapper<Dict>().eq(Dict::getParentId, dict.getParentId())
                .lt(Dict::getSort, sort).orderByDesc(Dict::getSort));
        if (previous != null) {
            dict.setSort(previous.getSort());
            previous.setSort(sort);
            num = baseMapper.updateBatchById(BaseUtil.newArrayList(dict, previous));
        }
        return num == 2;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean down(Dict dict) {
        int num = 0;
        Integer sort = dict.getSort();
        Dict next = getOne(new LambdaQueryWrapper<Dict>().eq(Dict::getParentId, dict.getParentId())
                .gt(Dict::getSort, sort).orderByAsc(Dict::getSort));
        if (next != null) {
            dict.setSort(next.getSort());
            next.setSort(sort);
            num = baseMapper.updateBatchById(BaseUtil.newArrayList(dict, next));
        }
        return num == 2;
    }

    @Override
    public Map<String, Map<String, String>> dictMap() {
        if (dataCache == null) {
            return queryDictMap();
        }
        if (!dataCache.has(SystemConstants.DICT)) {
            dataCache.set(SystemConstants.DICT, queryDictMap(), timeout, TimeUnit.SECONDS);
        }
        return dataCache.get(SystemConstants.DICT);
    }

    /**
     * 查询字典集合
     * 
     * @return 字典集合
     */
    private Map<String, Map<String, String>> queryDictMap() {
        List<Dict> list = tree();
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.stream().filter(tree -> tree != null && CollectionUtils.isNotEmpty(tree.getChildren()))
                .collect(Collectors.toMap(Dict::getCode,
                        tree -> tree.getChildren().stream().collect(Collectors.toMap(Dict::getCode, Dict::getName))));
    }

    @Override
    public List<Map<String, String>> getDict(String parentId) {
        if (StringUtils.isBlank(parentId)) {
            return null;
        }
        Map<String, Map<String, String>> dictMap = dictMap();
        if (MapUtils.isEmpty(dictMap) || !dictMap.containsKey(parentId)) {
            return null;
        }
        Map<String, String> infoMap = dictMap.get(parentId);
        if (MapUtils.isEmpty(infoMap)) {
            return null;
        }
        return infoMap.entrySet().stream().map(entry -> {
            Map<String, String> item = new HashMap<String, String>();
            item.put("code", entry.getKey());
            item.put("name", entry.getValue());
            return item;
        }).collect(Collectors.toList());
    }

    @Override
    public String dictName(String code) {
        if (StringUtils.isNotBlank(code)) {
            if (MapUtils.isNotEmpty(dictMap())) {
                for (Map<String, String> map : dictMap().values()) {
                    if (MapUtils.isNotEmpty(map) && map.containsKey(code)) {
                        return map.get(code);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public String dictName(String parentId, String code) {
        if (JudgeUtil.isAllNotBlank(parentId, code) && MapUtils.isNotEmpty(dictMap())) {
            Map<String, String> map = dictMap().get(parentId);
            if (MapUtils.isNotEmpty(map)) {
                return map.get(code);
            }
        }
        return null;
    }

    @Override
    public List<?> dictList(String parentId) {
        List<Map<String, String>> list = getDict(parentId);
        if (CollectionUtils.isNotEmpty(list)) {
            return list;
        } else {
            return list(new LambdaQueryWrapper<Dict>().eq(Dict::getParentId, parentId).orderByAsc(Dict::getParentId,
                    Dict::getSort));
        }
    }

    @Override
    public List<Dict> tree() {
        return treeableService.tree(list(new LambdaQueryWrapper<Dict>().orderByAsc(Dict::getParentId, Dict::getSort)));
    }

}
