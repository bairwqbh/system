package per.cby.system.shiro.realm;

import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import per.cby.system.biz.BusinessService;
import per.cby.system.constant.SystemConstants;
import per.cby.system.model.User;
import per.cby.system.service.UserService;
import per.cby.system.shiro.exception.InvalidRolesException;
import per.cby.system.shiro.session.RestSessionManager;

/**
 * 权限校验器
 * 
 * @author chenboyang
 *
 */
@SuppressWarnings("unchecked")
public class AuthRealm extends AuthorizingRealm implements SystemConstants {

	@Autowired(required = false)
	private UserService userService;

	@Autowired(required = false)
	private BusinessService businessService;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SimpleAuthorizationInfo authorizationInfo = null;
		if (principals != null) {
			authorizationInfo = new SimpleAuthorizationInfo();
			Set<String> roles = (Set<String>) SecurityUtils.getSubject().getSession().getAttribute(SystemConstants.ROLES);
			Set<String> permissions = (Set<String>) SecurityUtils.getSubject().getSession().getAttribute(SystemConstants.PERMISSIONS);
			authorizationInfo.addRoles(roles);
			authorizationInfo.addStringPermissions(permissions);
		}
		return authorizationInfo;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String username = ((UsernamePasswordToken) token).getUsername();
		User user = userService.getUser(username);
		if (user == null) {
			throw new UnknownAccountException("用户不存在！");
		}
		if (user.getEnable() == null || !user.getEnable().booleanValue()) {
			throw new DisabledAccountException("帐号已被禁用！");
		}
		RestSessionManager.USER_ID.set(user.getUserId());
		Set<String> roles = userService.getRoles(username).stream()
				.filter(StringUtils::isNotBlank).collect(Collectors.toSet());
		if (CollectionUtils.isEmpty(roles)) {
			throw new InvalidRolesException("用户无有效角色或角色已被禁用！");
		}
        Set<String> permissions = userService.getAuths(username).stream().filter(StringUtils::isNotBlank)
                .collect(Collectors.toSet());
		SecurityUtils.getSubject().getSession().setAttribute(SystemConstants.USER, user);
		SecurityUtils.getSubject().getSession().setAttribute(SystemConstants.ROLES, roles);
		SecurityUtils.getSubject().getSession().setAttribute(SystemConstants.PERMISSIONS, permissions);
		if (!ADMIN.equals(user.getUserId()) && !roles.contains(ADMIN)) {
		    SecurityUtils.getSubject().getSession().setAttribute(ORG_AUTH, businessService.orgRecursive(user.getOrgId()));
		    SecurityUtils.getSubject().getSession().setAttribute(AREA_AUTH, businessService.areaRecursive(user.getAreaId()));
		}
		return new SimpleAuthenticationInfo(username, user.getPassword(), getName());
	}

	@Override
	protected Object getAuthorizationCacheKey(PrincipalCollection principals) {
		return SecurityUtils.getSubject().getSession().getId();
	}

}
