package per.cby.system.service;

import java.util.Collection;
import java.util.List;

import per.cby.frame.common.base.BaseService;
import per.cby.system.model.Api;

/**
 * <p>
 * 接口表 服务类
 * </p>
 *
 * @author chenboyang
 * @since 2019-03-07
 */
public interface ApiService extends BaseService<Api> {

    /**
     * 根据功能编号集获取接口编号列表
     * 
     * @param funcIds 功能编号集
     * @return 接口编号列表
     */
    List<String> apiIdList(Collection<String> funcIds);

    /**
     * 获取接口树
     * 
     * @return 接口树
     */
    List<Api> tree();

}
