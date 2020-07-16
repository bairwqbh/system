package per.cby.system.shiro.session;

import java.io.Serializable;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;

import per.cby.frame.web.constant.WebConstant;

/**
 * REST接口会话管理器
 * 
 * @author chenboyang
 *
 */
public class RestSessionManager extends DefaultWebSessionManager implements WebConstant {

    /** 会话标识来源 */
    private static final String REFERENCED_SESSION_ID_SOURCE = "Stateless request";

    /** 当前连接用户编号 */
    public static final ThreadLocal<String> USER_ID = new ThreadLocal<String>();

    /** 构建REST会话管理器 */
    public RestSessionManager() {
        super();
    }

    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        String sessionId = getTokenFromRequest(request);
        if (StringUtils.isNotBlank(sessionId)) {
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, REFERENCED_SESSION_ID_SOURCE);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, sessionId);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            return sessionId;
        } else {
            return super.getSessionId(request, response);
        }
    }

    /**
     * 从请求信息中获取Token值
     * 
     * @param request 请求信息
     * @return Token值
     */
    private String getTokenFromRequest(ServletRequest request) {
        String token = WebUtils.toHttp(request).getHeader(TOKEN);
        if (StringUtils.isEmpty(token)) {
            token = WebUtils.toHttp(request).getParameter(TOKEN);
        }
        return token;
    }

}
