package per.cby.system.service.impl;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import per.cby.frame.common.exception.BusinessAssert;
import per.cby.frame.web.service.ApplyService;
import per.cby.frame.web.session.Session;
import per.cby.frame.web.session.SessionManager;
import per.cby.system.biz.BusinessService;
import per.cby.system.constant.SystemConstants;
import per.cby.system.model.Org;
import per.cby.system.model.User;
import per.cby.system.service.AuthenService;
import per.cby.system.service.UserService;
import per.cby.system.service.WebLogService;

/**
 * 简单系统权限服务
 * 
 * @author chenboyang
 * @since 2020年3月23日
 *
 */
public class SimpleAuthenService implements AuthenService, SystemConstants {

    @Autowired
    private ApplyService applyService;

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private UserService userService;

    @Autowired
    private WebLogService webLogService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private BusinessService businessService;

    @Override
    public boolean isAuthen() {
        return applyService.isOnline();
    }

    @Override
    public String login(String username, String password) {
        BusinessAssert.hasText(username, "请传递用户名！");
        BusinessAssert.hasText(password, "请传递密码！");
        // 根据登录信息查询用户
        User user = userService.getUser(username);
        BusinessAssert.notNull(user, "用户名不存在！");
        BusinessAssert.isTrue(userService.passwordEncode(password).equals(user.getPassword()), "密码错误！");
        BusinessAssert.isTrue(user.getEnable() != null && user.getEnable().booleanValue(), "帐号已被禁用！");
        String ip = applyService.request().getRemoteAddr();
        Session session = sessionManager.createSession(username, ip);
        BusinessAssert.notNull(session, "登陆失败！");
        // 根据用户查询机构、角色、权限、接口
        Org org = userService.getOrg(user.getUserId());
        List<String> roles = userService.getRoles(user.getUserId());
        BusinessAssert.notEmpty(roles, "用户无有效角色或角色已被禁用！");
        List<String> auths = userService.getAuths(user.getUserId());
        List<String> apis = userService.getApis(user.getUserId());
        session.attribute().put(ORG, org);
        session.attribute().put(USER, user);
        session.attribute().put(ROLES, roles);
        session.attribute().put(AUTHS, auths);
        session.attribute().put(APIS, apis);
        // 根据用户查询数据权限
        if (!ADMIN.equals(user.getUserId()) && !roles.contains(ADMIN)) {
            session.attribute().put(ORG_AUTH, businessService.orgRecursive(user.getOrgId()));
            session.attribute().put(AREA_AUTH, businessService.areaRecursive(user.getAreaId()));
        }
        sessionManager.touch(session);
        webLogService.save("用户登录系统", request, log -> {
            log.setUserId(username);
            log.setParam("username=" + username);
        });
        return session.getId();
    }

    @Override
    public boolean logout() {
        User user = applyService.getSessionAttribute(USER, User.class);
        webLogService.save("用户退出系统", request,
                log -> Optional.ofNullable(user).map(User::getUserId).ifPresent(log::setUserId));
        sessionManager.stop();
        return true;
    }

}
