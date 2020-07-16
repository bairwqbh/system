package per.cby.system.constant;

/**
 * 文件存储常量
 * 
 * @author chenboyang
 *
 */
public interface StorageConstants {

    /** 系统存储板块 */
    String SYSTEM_FILE_BUCKET = "system_file_bucket";

    /** 系统文件存储 */
    String SYSTEM_FILE_STORAGE = "system_file_storage";

    /** MongoDB存储板块 */
    String MONGO_GRIDFS_BUCKET = "mongo_gridfs_bucket";

    /** MongoDB文件存储 */
    String MONGO_GRIDFS_STORAGE = "mongo_gridfs_storage";

    /** 阿里云对象存储板块 */
    String ALIYUN_OSS_BUCKET = "aliyun_oss_bucket";

    /** 阿里云对象存储 */
    String ALIYUN_OSS_STORAGE = "aliyun_oss_storage";

    /** 七牛云存储板块 */
    String QINIU_OSS_BUCKET = "qiniu_oss_bucket";

    /** 七牛云对象存储 */
    String QINIU_OSS_STORAGE = "qiniu_oss_storage";

}
