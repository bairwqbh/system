package per.cby.system.service;

import java.util.Map;

import per.cby.frame.common.base.BaseService;
import per.cby.system.model.Attach;

/**
 * <p>
 * 附件表 : 用于存放系统各业务的附件信息 服务类
 * </p>
 *
 * @author chenboyang
 * @since 2018-01-26
 */
public interface AttachService extends BaseService<Attach> {

    /**
     * 根据参数删除附件
     * 
     * @param param 参数
     * @return 操作结果
     */
    boolean remove(Map<String, Object> param);

}
