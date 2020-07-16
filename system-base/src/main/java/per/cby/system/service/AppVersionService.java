package per.cby.system.service;

import java.io.File;

import per.cby.frame.common.base.BaseService;
import per.cby.system.model.AppVersion;

/**
 * <p>
 * 应用版本表 服务类
 * </p>
 *
 * @author chenboyang
 * @since 2018-03-21
 */
public interface AppVersionService extends BaseService<AppVersion> {

    /** 存储板块 */
    String STORAGE_BUCKET = "system.app.version";

    /** 板块名称 */
    String BUCKET_NAME = "应用版本";

    /** 业务领域 */
    String DOMAIN = "sys_app_version";

    /** 应用包字段 */
    String APK_FIELD = "apk";

    /**
     * 新增APP版本信息
     * 
     * @param appVersion APP版本信息
     * @param file       文件
     * @return 操作结果
     */
    boolean save(AppVersion appVersion, File file);

    /**
     * 获取应用最新版本信息
     * 
     * @param appId 应用编号
     * @return 版本信息
     */
    AppVersion currVersion(String appId);

}
