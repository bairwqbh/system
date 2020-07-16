package per.cby.system.service;

import per.cby.frame.common.base.BaseService;
import per.cby.system.model.Param;

/**
 * <p>
 * 参数表 : 用于配置业务参数 服务类
 * </p>
 *
 * @author chenboyang
 * @since 2018-01-26
 */
public interface ParamService extends BaseService<Param> {

    /**
     * 获取参数值
     * 
     * @param key 参数键
     * @return 参数值
     */
    String get(String key);

    /**
     * 设置参数
     * 
     * @param key   参数键
     * @param value 参数值
     * @return 操作结果
     */
    boolean set(String key, String value);

    /**
     * 删除参数
     * 
     * @param key 参数键
     * @return 操作结果
     */
    boolean delete(String key);

}
