package per.cby.system.service;

import java.util.List;
import java.util.Map;

import per.cby.system.dto.LoginDto;
import per.cby.system.dto.ModPwdDto;
import per.cby.system.model.Org;
import per.cby.system.model.User;

/**
 * 系统通用服务
 * 
 * @author chenboyang
 * @since 2019年11月6日
 *
 */
public interface SystemService {

    /**
     * 令牌当前是否有效
     * 
     * @return 是否有效
     */
    public boolean isAuthen();

    /**
     * 用户登录系统
     * 
     * @param loginDto 登录信息
     * @return 有效令牌
     */
    String login(LoginDto loginDto);

    /**
     * 用户退出系统
     * 
     * @return 是否退出成功
     */
    boolean logout();

    /**
     * 登陆用户认证信息
     * 
     * @return 业务信息
     */
    Map<String, Object> info();

    /**
     * 修改登录密码
     * 
     * @param modPwdDto 修改密码参数
     * @return 是否修改成功
     */
    boolean modPwd(ModPwdDto modPwdDto);

    /**
     * 获取当前用户
     * 
     * @return 用户
     */
    User currUser();

    /**
     * 获取当前用户机构
     * 
     * @return 机构
     */
    Org currOrg();

    /**
     * 获取当前用户角色
     * 
     * @return 角色列表
     */
    List<String> currRoles();

}
