package per.cby.system.constant;

import per.cby.frame.common.useable.EncryptEnum;

/**
 * 系统常量类
 * 
 * @author chenboyang
 *
 */
public interface SystemConstants {

    /** 系统编号 */
    String SYS_ID = "sysId";

    /** 字典 */
    String DICT = "dict";

    /** 用户 */
    String USER = "user";

    /** 用户编号 */
    String USER_ID = "userId";

    /** 组织机构 */
    String ORG = "org";

    /** 角色 */
    String ROLE = "role";

    /** 角色组 */
    String ROLES = "roles";

    /** 权限 */
    String AUTHS = "auths";

    /** 接口 */
    String APIS = "apis";

    /** 角色编号组 */
    String ROLE_IDS = "roleIds";

    /** 菜单 */
    String MENU = "menu";

    /** 权限 */
    String PERMISSIONS = "permissions";

    /** 数据权限 */
    String DATA_PERMISSIONS = "dataPermissions";

    /** 管理员 */
    String ADMIN = "admin";

    /** 是否启用 */
    String ENABLE = "enable";

    /** 系统标识 */
    String SYSTEM_FLAG = "system_flag";

    /** 日志类型 */
    String LOG_TYPE = "log_type";

    /** 机构类型 */
    String ORG_TYPE = "org_type";

    /** 功能类型 */
    String FUNC_TYPE = "func_type";

    /** 权限类型 */
    String AUTH_TYPE = "auth_type";

    /** 关系类型 */
    String RELATE_TYPE = "relate_type";

    /** 权限模型 */
    String AUTH_MODE = "auth_mode";

    /** 接口缓存实例名称 */
    String API_CACHE_HASH = "api_cache_hash";

    /** 令牌 */
    String TOKEN = "token";

    /** 接口校验 */
    String API_CHECK = "api_check";

    /** 权限权限 */
    String ORG_AUTH = "orgAuth";

    /** 地区权限 */
    String AREA_AUTH = "areaAuth";

    /** 密码加密算法 */
    EncryptEnum PASSWORD_ENCRYPT = EncryptEnum.MD5;

}
