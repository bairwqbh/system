package per.cby.system.constant;

import per.cby.system.biz.DictEnumApi;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;

/**
 * 系统字典枚举
 * 
 * @author chenboyang
 *
 */
@UtilityClass
public class SystemDict {

    /**
     * 系统标识
     * 
     * @author chenboyang
     *
     */
    @Getter
    @RequiredArgsConstructor
    public enum SystemFlag implements DictEnumApi {

        SYSTEM_MANAGER("system_manager", "系统管理"),
        MANAGER("manager", "后台管理");

        private final String code;
        private final String desc;

    }

    /**
     * 日志类型
     * 
     * @author chenboyang
     *
     */
    @Getter
    @RequiredArgsConstructor
    public enum LogType implements DictEnumApi {

        OPREATION("opreation", "操作日志"),
        EXCEPTION("exception", "异常日志");

        private final String code;
        private final String desc;

    }

    /**
     * 机构类型
     * 
     * @author chenboyang
     *
     */
    @Getter
    @RequiredArgsConstructor
    public enum OrgType implements DictEnumApi {

        PLATFORM("platform", "平台"),
        SUPERVISE("supervise", "监管"),
        OPERATE("operate", "运营");

        private final String code;
        private final String desc;

    }

    /**
     * 功能类型
     * 
     * @author chenboyang
     *
     */
    @Getter
    @RequiredArgsConstructor
    public enum FuncType implements DictEnumApi {

        SYS("sys", "系统"),
        DIR("dir", "目录"),
        MENU("menu", "菜单"),
        BTN("btn", "按钮");

        private final String code;
        private final String desc;

    }

    /**
     * 权限类型
     * 
     * @author chenboyang
     *
     */
    @Getter
    @RequiredArgsConstructor
    public enum AuthType implements DictEnumApi {

        FUNC("func", "功能权限"),
        API("api", "接口权限"),
        DATA("data", "数据权限");

        private final String code;
        private final String desc;

    }

    /**
     * 关系类型
     * 
     * @author chenboyang
     *
     */
    @Getter
    @RequiredArgsConstructor
    public enum RelateType implements DictEnumApi {

        USER_ROLE("user_role", "用户角色关系"),
        ROLE_FUNC("role_func", "角色功能关系"),
        FUNC_API("func_api", "功能接口关系");

        private final String code;
        private final String desc;

    }

    /**
     * 权限模式
     * 
     * @author chenboyang
     *
     */
    @Getter
    @RequiredArgsConstructor
    public enum AuthMode implements DictEnumApi {

        ALL("all", "全量"),
        NONE("none", "无"),
        IN("in", "包含"),
        EXC("exc", "排他");

        private final String code;
        private final String desc;

    }

    /**
     * 存储类型
     * 
     * @author chenboyang
     *
     */
    @Getter
    @RequiredArgsConstructor
    public enum StorageType implements DictEnumApi {

        SYSTEM_FILE("system_file", "系统文件"),
        MONGO_GRIDFS("mongo_gridfs", "MongoDB文件存储"),
        ALIYUN_OSS("aliyun_oss", "阿里云对象存储"),
        QINIU_OSS("qiniu_oss", "七牛云对象存储");

        private final String code;
        private final String desc;

    }

}
