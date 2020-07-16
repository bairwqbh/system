package per.cby.system.biz;

import java.util.List;

/**
 * 业务服务接口
 * 
 * @author chenboyang
 *
 */
public interface BusinessService {

    /**
     * 获取机构权限
     * 
     * @param orgId 机构编号
     * @return 机构权限
     */
    List<String> orgRecursive(String orgId);

    /**
     * 获取地区权限
     * 
     * @param areaId 地区编码
     * @return 地区权限
     */
    List<String> areaRecursive(String areaId);

}
