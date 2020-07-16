package per.cby.system.biz;

import java.util.List;

/**
 * <p>
 * 权限切换器
 * </p>
 *
 * @author chenboyang
 * @since 2019-03-07
 */
public interface AuthSwitcher {

    /**
     * 全量权限
     */
    void all();

    /**
     * 无权限
     */
    void none();

    /**
     * 包含权限
     */
    void in(List<String> targetList);

    /**
     * 排他权限
     */
    void exc(List<String> targetList);

}
