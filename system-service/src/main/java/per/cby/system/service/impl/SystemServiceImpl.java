package per.cby.system.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import per.cby.frame.common.exception.BusinessAssert;
import per.cby.frame.common.util.BaseUtil;
import per.cby.frame.common.util.JsonUtil;
import per.cby.frame.web.service.ApplyService;
import per.cby.frame.web.session.Session;
import per.cby.frame.web.session.SessionManager;
import per.cby.system.constant.SystemConstants;
import per.cby.system.dto.LoginDto;
import per.cby.system.dto.ModPwdDto;
import per.cby.system.model.Org;
import per.cby.system.model.User;
import per.cby.system.service.AuthenService;
import per.cby.system.service.SystemService;
import per.cby.system.service.UserService;
import per.cby.system.service.WebLogService;

/**
 * 系统通用服务实现
 * 
 * @author chenboyang
 * @since 2019年11月6日
 *
 */
@Service("__SYSTEM_SERVICE__")
@SuppressWarnings("unchecked")
public class SystemServiceImpl implements SystemService, SystemConstants {

    @Autowired
    private ApplyService applyService;

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private WebLogService webLogService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private AuthenService authenService;

    @Autowired
    private UserService userService;

    @Override
    public boolean isAuthen() {
        return authenService.isAuthen();
    }

    @Override
    public String login(LoginDto loginDto) {
        BusinessAssert.notNull(loginDto, "请传递登录信息！");
        return authenService.login(loginDto.getUsername(), loginDto.getPassword());
    }

    @Override
    public boolean logout() {
        return authenService.logout();
    }

    @Override
    public Map<String, Object> info() {
        Session session = sessionManager.getSession();
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
    public boolean modPwd(ModPwdDto modPwdDto) {
        String enOldPwd = modPwdDto.getOldPwd();
        String enNewPwd = modPwdDto.getNewPwd();
        BusinessAssert.hasText(enOldPwd, "旧密码不能为空！");
        BusinessAssert.hasText(enNewPwd, "新密码不能为空！");
        BusinessAssert.isTrue(enOldPwd.length() >= 6 && enOldPwd.length() <= 24, "旧密码必须为6-24位字符！");
        BusinessAssert.isTrue(enNewPwd.length() >= 6 && enNewPwd.length() <= 24, "新密码必须为6-24位字符！");
        BusinessAssert.isTrue(!Objects.equals(enNewPwd, enOldPwd), "输入的新密码与旧密码不能相同！");
        Session session = sessionManager.getSession();
        User user = JsonUtil.toObject(JsonUtil.toJson(session.getAttribute(USER)), User.class);
        BusinessAssert.notNull(user, "未获取到用户登陆信息，修改密码操作失败！");
        enOldPwd = userService.passwordEncode(enOldPwd);
        enNewPwd = userService.passwordEncode(enNewPwd);
        BusinessAssert.isTrue(Objects.equals(user.getPassword(), enOldPwd), "输入的旧密码错误！");
        User modUser = new User();
        modUser.setId(user.getId());
        modUser.setPassword(enNewPwd);
        BusinessAssert.isTrue(modUser.updateById(), "修改密码操作失败！");
        user.setPassword(enNewPwd);
        user.setUpdateTime(LocalDateTime.now());
        sessionManager.setAttribute(USER, user, session);
        webLogService.save("用户密码修改", request, log -> log.setParam(null));
        return true;
    }

    @Override
    public User currUser() {
        return applyService.getSessionAttribute(USER, User.class);
    }

    @Override
    public Org currOrg() {
        return applyService.getSessionAttribute(ORG, Org.class);
    }

    @Override
    public List<String> currRoles() {
        return applyService.getSessionAttribute(ROLES, List.class);
    }

}
