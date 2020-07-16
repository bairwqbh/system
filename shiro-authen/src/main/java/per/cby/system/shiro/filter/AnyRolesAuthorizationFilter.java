package per.cby.system.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

/**
 * 多角色权限认证过滤器
 * 
 * @author chenboyang
 *
 */
public class AnyRolesAuthorizationFilter extends AuthorizationFilter {

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {
		Subject subject = getSubject(request, response);
		String[] roles = (String[]) mappedValue;
		if (ArrayUtils.isNotEmpty(roles)) {
			for (int i = 0; i < roles.length; i++) {
				if (subject.hasRole(roles[i])) {
					return true;
				}
			}
		} else {
			return true;
		}
		return false;
	}

}
