package per.cby.system.service;

/**
 * 系统权限接口
 * 
 * @author chenboyang
 *
 */
public interface AuthenService {

    /**
     * 访问用户是否在线
     * 
     * @return 是否在线
     */
    boolean isAuthen();

    /**
     * 用户登录系统
     * 
     * @param username 用户名
     * @param password 密码
     * @return 访问令牌
     */
    String login(String username, String password);

    /**
     * 用户退出系统
     * 
     * @return 是否退出
     */
    boolean logout();

}
