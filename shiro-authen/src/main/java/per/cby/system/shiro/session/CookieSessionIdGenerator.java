package per.cby.system.shiro.session;

import java.io.Serializable;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;

import per.cby.frame.common.util.BaseUtil;

/**
 * Cookie会话标识生成器
 * 
 * @author chenboyang
 *
 */
public class CookieSessionIdGenerator implements SessionIdGenerator {

	/**
	 * 盐值
	 */
	private static final String SALT = "zAq!2#4RfVgY7*9)_206d7d5ed8s92dae1i710f6ahc9fc7f4";

	@Override
	public Serializable generateId(Session session) {
		String userId = RestSessionManager.USER_ID.get();
		RestSessionManager.USER_ID.remove();
		return BaseUtil.md5Encode(userId + SALT + session.getHost());
	}

}
