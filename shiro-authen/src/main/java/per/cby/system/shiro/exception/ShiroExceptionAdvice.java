package per.cby.system.shiro.exception;

import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ConcurrentAccessException;
import org.apache.shiro.authc.CredentialsException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.pam.UnsupportedTokenException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import per.cby.frame.web.constant.ResponseResult;

/**
 * MVC接口统一异常处理
 * 
 * @author chenboyang
 *
 */
@RestControllerAdvice
public class ShiroExceptionAdvice {

	/**
	 * 认证异常异常处理
	 * 
	 * @param e
	 *            异常信息
	 * @return 结果数据
	 */
	@ExceptionHandler(AuthenticationException.class)
	public Object handle(AuthenticationException e) {
		return ResponseResult.AUTHENTICATION;
	}

	/**
	 * 用户异常处理
	 * 
	 * @param e
	 *            异常信息
	 * @return 结果数据
	 */
	@ExceptionHandler(AccountException.class)
	public Object handler(AccountException e) {
		return ResponseResult.ACCOUNT;
	}

	/**
	 * 并发访问异常处理
	 * 
	 * @param e
	 *            异常信息
	 * @return 结果数据
	 */
	@ExceptionHandler(ConcurrentAccessException.class)
	public Object handler(ConcurrentAccessException e) {
		return ResponseResult.CONCURRENT_ACCESS;
	}

	/**
	 * 证书异常处理
	 * 
	 * @param e
	 *            异常信息
	 * @return 结果数据
	 */
	@ExceptionHandler(CredentialsException.class)
	public Object handler(CredentialsException e) {
		return ResponseResult.CREDENTIALS;
	}

	/**
	 * 不支持的Token异常处理
	 * 
	 * @param e
	 *            异常信息
	 * @return 结果数据
	 */
	@ExceptionHandler(UnsupportedTokenException.class)
	public Object handler(UnsupportedTokenException e) {
		return ResponseResult.UNSUPPORTED_TOKEN;
	}

	/**
	 * 未通过认证异常处理
	 * 
	 * @param e
	 *            异常信息
	 * @return 结果数据
	 */
	@ExceptionHandler(UnauthenticatedException.class)
	public Object handle(UnauthenticatedException e) {
		return ResponseResult.UNAUTHENTICATED;
	}

	/**
	 * 登录密码错误异常处理
	 * 
	 * @param e
	 *            异常信息
	 * @return 结果数据
	 */
	@ExceptionHandler(IncorrectCredentialsException.class)
	public Object handle(IncorrectCredentialsException e) {
		return ResponseResult.INCORRECT_CREDENTIALS;
	}

	/**
	 * 登录失败次数过多异常处理
	 * 
	 * @param e
	 *            异常信息
	 * @return 结果数据
	 */
	@ExceptionHandler(ExcessiveAttemptsException.class)
	public Object handle(ExcessiveAttemptsException e) {
		return ResponseResult.EXCESSIVE_ATTEMPTS;
	}

	/**
	 * 帐号已被锁定异常处理
	 * 
	 * @param e
	 *            异常信息
	 * @return 结果数据
	 */
	@ExceptionHandler(LockedAccountException.class)
	public Object handle(LockedAccountException e) {
		return ResponseResult.LOCKED_ACCOUNT;
	}

	/**
	 * 帐号已被锁定异常处理
	 * 
	 * @param e
	 *            异常信息
	 * @return 结果数据
	 */
	@ExceptionHandler(DisabledAccountException.class)
	public Object handle(DisabledAccountException e) {
		return ResponseResult.DISABLED_ACCOUNT;
	}

	/**
	 * 帐号已过期异常处理
	 * 
	 * @param e
	 *            异常信息
	 * @return 结果数据
	 */
	@ExceptionHandler(ExpiredCredentialsException.class)
	public Object handle(ExpiredCredentialsException e) {
		return ResponseResult.EXPIRED_CREDENTIALS;
	}

	/**
	 * 帐号不存在异常处理
	 * 
	 * @param e
	 *            异常信息
	 * @return 结果数据
	 */
	@ExceptionHandler(UnknownAccountException.class)
	public Object handle(UnknownAccountException e) {
		return ResponseResult.UNKNOWN_ACCOUNT;
	}

	/**
	 * 您没有相应的授权异常处理
	 * 
	 * @param e
	 *            异常信息
	 * @return 结果数据
	 */
	@ExceptionHandler(UnauthorizedException.class)
	public Object handle(UnauthorizedException e) {
		return ResponseResult.UNAUTHORIZED;
	}

	/**
	 * 认证异常异常处理
	 * 
	 * @param e
	 *            异常信息
	 * @return 结果数据
	 */
	@ExceptionHandler(InvalidRolesException.class)
	public Object handle(InvalidRolesException e) {
		return ResponseResult.INVALID_ROLES;
	}

}
