package per.cby.system.service;

import java.util.Map;

import org.springframework.http.server.reactive.ServerHttpRequest;

import per.cby.system.dto.LoginDto;
import per.cby.system.dto.ModPwdDto;

/**
 * 权限认证服务
 * 
 * @author chenboyang
 *
 */
public interface AuthenService {

    /**
     * 令牌当前是否有效
     * 
     * @param token   令牌
     * @param request 请求信息
     * @return 是否有效
     */
    public boolean isAuthen(String token, ServerHttpRequest request);

    /**
     * 用户登录系统
     * 
     * @param loginDto 登录信息
     * @param request  请求信息
     * @return 有效令牌
     */
    String login(LoginDto loginDto, ServerHttpRequest request);

    /**
     * 用户退出系统
     * 
     * @param token   令牌
     * @param request 请求信息
     * @return 是否退出成功
     */
    boolean logout(String token, ServerHttpRequest request);

    /**
     * 登陆用户认证信息
     * 
     * @param token   令牌
     * @param request 请求信息
     * @return 业务信息
     */
    Map<String, Object> info(String token, ServerHttpRequest request);

    /**
     * 修改登录密码
     * 
     * @param token     令牌
     * @param modPwdDto 修改密码参数
     * @param request   请求信息
     * @return 是否修改成功
     */
    boolean modPwd(String token, ModPwdDto modPwdDto, ServerHttpRequest request);

}
