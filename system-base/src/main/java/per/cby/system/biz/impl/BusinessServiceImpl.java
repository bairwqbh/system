package per.cby.system.biz.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import per.cby.frame.common.tree.TreeableService;
import per.cby.frame.common.util.BaseUtil;
import per.cby.frame.common.util.JudgeUtil;
import per.cby.system.biz.BusinessService;
import per.cby.system.model.Area;
import per.cby.system.model.Org;
import per.cby.system.service.AreaService;
import per.cby.system.service.OrgService;

/**
 * 业务服务接口实现类
 * 
 * @author chenboyang
 *
 */
@Service("__BUSINESS_SERVICE__")
public class BusinessServiceImpl implements BusinessService {

    @Autowired
    protected OrgService orgService;

    @Autowired
    protected AreaService areaService;

    @Override
    public List<String> orgRecursive(String orgId) {
        List<String> result = BaseUtil.newArrayList("$");
        if (orgId == null) {
            return result;
        }
        if (JudgeUtil.isOneEqual(orgId, TreeableService.TOP_CODE, TreeableService.TOP_LEVEL)) {
            return null;
        }
        List<Org> list = orgService.downRecursive(orgId);
        if (CollectionUtils.isNotEmpty(list)) {
            result = list.stream().map(Org::getOrgId).collect(Collectors.toList());
        }
        return result;
    }

    @Override
    public List<String> areaRecursive(String areaId) {
        List<String> result = BaseUtil.newArrayList("$");
        if (areaId == null) {
            return result;
        }
        if (JudgeUtil.isOneEqual(areaId, TreeableService.TOP_CODE, TreeableService.TOP_LEVEL)) {
            return null;
        }
        List<Area> list = areaService.downRecursive(areaId);
        if (CollectionUtils.isNotEmpty(list)) {
            result = list.stream().map(Area::getAreaId).collect(Collectors.toList());
        }
        return result;
    }

}
