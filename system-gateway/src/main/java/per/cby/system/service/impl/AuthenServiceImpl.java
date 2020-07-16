package per.cby.system.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

import per.cby.frame.common.exception.BusinessAssert;
import per.cby.frame.common.util.BaseUtil;
import per.cby.frame.common.util.JsonUtil;
import per.cby.frame.web.constant.ResponseResult;
import per.cby.frame.web.exception.TokenException;
import per.cby.frame.web.session.Session;
import per.cby.frame.web.session.SessionManager;
import per.cby.system.biz.BusinessService;
import per.cby.system.constant.SystemConstants;
import per.cby.system.dto.LoginDto;
import per.cby.system.dto.ModPwdDto;
import per.cby.system.model.Org;
import per.cby.system.model.User;
import per.cby.system.service.AuthenService;
import per.cby.system.service.LogRecordService;
import per.cby.system.service.UserService;

/**
 * 权限认证服务接口实现
 * 
 * @author chenboyang
 *
 */
@Service
public class AuthenServiceImpl implements AuthenService, SystemConstants {

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private UserService userService;

    @Autowired
    private LogRecordService logRecordService;

    @Autowired(required = false)
    private BusinessService businessService;

    @Override
    public boolean isAuthen(String token, ServerHttpRequest request) {
        if (!sessionManager.hasSession(token)) {
            return false;
        }
        Session session = sessionManager.getSession(token);
        String host = request.getRemoteAddress().getHostString();
        if (!Objects.equals(session.getHost(), host)) {
            return false;
        }
        return true;
    }

    @Override
    public String login(LoginDto loginDto, ServerHttpRequest request) {
        BusinessAssert.notNull(loginDto, "请传递登录信息！");
        BusinessAssert.hasText(loginDto.getUsername(), "请传递用户名！");
        BusinessAssert.hasText(loginDto.getPassword(), "请传递密码！");
        // 根据登录信息查询用户
        User user = userService.getUser(loginDto.getUsername());
        BusinessAssert.notNull(user, "用户名不存在！");
        BusinessAssert.isTrue(userService.passwordEncode(loginDto.getPassword()).equals(user.getPassword()), "密码错误！");
        BusinessAssert.isTrue(user.getEnable() != null && user.getEnable().booleanValue(), "帐号已被禁用！");
        String ip = request.getRemoteAddress().getHostString();
        Session session = sessionManager.createSession(loginDto.getUsername(), ip);
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
        logRecordService.save("用户登录系统", request, log -> {
            log.setUserId(loginDto.getUsername());
            log.setParam("username=" + loginDto.getUsername());
        });
        return session.getId();
    }

    @Override
    public boolean logout(String token, ServerHttpRequest request) {
        TokenException.isTrue(isAuthen(token, request), ResponseResult.TOKEN_ERROR.getMessage());
        logRecordService.save("用户退出系统", request, log -> log.setPath("/authen/logout"));
        sessionManager.stop(token);
        return true;
    }

    @Override
    public Map<String, Object> info(String token, ServerHttpRequest request) {
        TokenException.isTrue(isAuthen(token, request), ResponseResult.TOKEN_ERROR.getMessage());
        Session session = sessionManager.getSession(token);
        BusinessAssert.notNull(session, "用户登录会话为空！");
        Map<String, Object> map = BaseUtil.newHashMap();
        map.put(ORG, session.getAttribute(ORG));
        map.put(USER, session.getAttribute(USER));
        map.put(ROLES, session.getAttribute(ROLES));
        map.put(AUTHS, session.getAttribute(AUTHS));
        map.put(APIS, session.getAttribute(APIS));
        return map;
    }

    @Override
    public boolean modPwd(String token, ModPwdDto modPwdDto, ServerHttpRequest request) {
        TokenException.isTrue(isAuthen(token, request), ResponseResult.TOKEN_ERROR.getMessage());
        String enOldPwd = modPwdDto.getOldPwd();
        String enNewPwd = modPwdDto.getNewPwd();
        BusinessAssert.hasText(enOldPwd, "旧密码不能为空！");
        BusinessAssert.hasText(enNewPwd, "新密码不能为空！");
        BusinessAssert.isTrue(enOldPwd.length() >= 6 && enOldPwd.length() <= 24, "旧密码必须为6-24位字符！");
        BusinessAssert.isTrue(enNewPwd.length() >= 6 && enNewPwd.length() <= 24, "新密码必须为6-24位字符！");
        BusinessAssert.isTrue(!Objects.equals(enNewPwd, enOldPwd), "输入的新密码与旧密码不能相同！");
        Session session = sessionManager.getSession(token);
        User user = JsonUtil.toObject(JsonUtil.toJson(session.getAttribute(USER)), User.class);
        BusinessAssert.notNull(user, "未获取到用户登陆信息，修改密码操作失败！");
        enOldPwd = userService.passwordEncode(enOldPwd);
        enNewPwd = userService.passwordEncode(enNewPwd);
        BusinessAssert.isTrue(Objects.equals(user.getPassword(), BaseUtil.md5Encode(enOldPwd)), "输入的旧密码错误！");
        User modUser = new User();
        modUser.setId(user.getId());
        modUser.setPassword(enNewPwd);
        BusinessAssert.isTrue(userService.updateById(modUser), "修改密码操作失败！");
        user.setPassword(enNewPwd);
        user.setUpdateTime(LocalDateTime.now());
        sessionManager.setAttribute(USER, user, session);
        logRecordService.save("用户密码修改", request, log -> log.setParam(null));
        return true;
    }

}
