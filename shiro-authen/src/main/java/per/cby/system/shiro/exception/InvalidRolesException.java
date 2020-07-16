package per.cby.system.shiro.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * 无效角色异常
 * 
 * @author chenboyang
 *
 */
public class InvalidRolesException extends AuthenticationException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 构建无效角色异常
	 */
	public InvalidRolesException() {
		super();
	}

	/**
	 * 构建无效角色异常
	 * 
	 * @param message
	 *            消息
	 */
	public InvalidRolesException(String message) {
		super(message);
	}

	/**
	 * 构建无效角色异常
	 * 
	 * @param cause
	 *            原因
	 */
	public InvalidRolesException(Throwable cause) {
		super(cause);
	}

	/**
	 * 构建无效角色异常
	 * 
	 * @param message
	 *            消息
	 * @param cause
	 *            原因
	 */
	public InvalidRolesException(String message, Throwable cause) {
		super(message, cause);
	}

}
