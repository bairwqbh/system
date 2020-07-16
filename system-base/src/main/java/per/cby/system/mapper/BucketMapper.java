package per.cby.system.mapper;

import org.springframework.stereotype.Repository;

import per.cby.frame.common.base.BaseMapper;
import per.cby.system.model.Bucket;

/**
 * <p>
 * 附件存储模块表 : 用于存放系统各业务的附件信息 Mapper 接口
 * </p>
 *
 * @author chenboyang
 * @since 2018-01-26
 */
@Repository("__BUCKET_MAPPER__")
public interface BucketMapper extends BaseMapper<Bucket> {

}
