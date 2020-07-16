package per.cby.system.service;

import per.cby.frame.common.base.BaseService;
import per.cby.system.model.Bucket;

/**
 * <p>
 * 附件存储模块表 : 用于存放系统各业务的附件信息 服务类
 * </p>
 *
 * @author chenboyang
 * @since 2018-01-26
 */
public interface BucketService extends BaseService<Bucket> {

    /** 存储板块后缀 */
    String BUCKET_SUFFIX = ".files";

}
