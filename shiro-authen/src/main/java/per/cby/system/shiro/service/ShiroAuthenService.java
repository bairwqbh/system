package per.cby.system.shiro.service;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import per.cby.frame.common.exception.BusinessAssert;
import per.cby.system.service.AuthenService;

/**
 * Shiro系统权限访问接口
 * 
 * @author chenboyang
 *
 */
public class ShiroAuthenService implements AuthenService {

    @Override
    public boolean isAuthen() {
        return SecurityUtils.getSubject().isAuthenticated();
    }

    @Override
    public String login(String username, String password) {
        Subject subject = SecurityUtils.getSubject();
        subject.login(new UsernamePasswordToken(username, password));
        BusinessAssert.isTrue(subject.isAuthenticated(), "登录失败！");
        return subject.getSession().getId().toString();
    }

    @Override
    public boolean logout() {
        SecurityUtils.getSubject().logout();
        return true;
    }

}
