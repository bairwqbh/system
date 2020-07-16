package per.cby.system.shiro.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.springframework.http.MediaType;

import per.cby.frame.common.util.JsonUtil;
import per.cby.frame.web.constant.ResponseResult;

/**
 * Shiro登录指令验证过滤器
 * 
 * @author chenboyang
 *
 */
public class ShiroTokenFilter extends PathMatchingFilter {

	/**
	 * 包含的路径模式
	 */
	private final List<String> includePatterns = new ArrayList<String>();

	/**
	 * 排除的路径模式
	 */
	private final List<String> excludePatterns = new ArrayList<String>();

	/**
	 * 增加包含的路径模式
	 * 
	 * @param patterns
	 *            路径模式
	 * @return 验证过滤器
	 */
	public ShiroTokenFilter addPathPatterns(String... patterns) {
		addPathPatterns(Arrays.asList(patterns));
		return this;
	}

	/**
	 * 增加包含的路径模式
	 * 
	 * @param patterns
	 *            路径模式
	 * @return 验证过滤器
	 */
	public ShiroTokenFilter addPathPatterns(List<String> patterns) {
		includePatterns.addAll(patterns);
		return this;
	}

	/**
	 * 增加排除的路径模式
	 * 
	 * @param patterns
	 *            路径模式
	 * @return 验证过滤器
	 */
	public ShiroTokenFilter excludePathPatterns(String... patterns) {
		return excludePathPatterns(Arrays.asList(patterns));
	}

	/**
	 * 增加排除的路径模式
	 * 
	 * @param patterns
	 *            路径模式
	 * @return 验证过滤器
	 */
	public ShiroTokenFilter excludePathPatterns(List<String> patterns) {
		excludePatterns.addAll(patterns);
		return this;
	}

	@Override
	protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {
		HttpServletRequest req = (HttpServletRequest) request;
		if (!isHttp(req) || !isMatch(req)) {
			return true;
		}
		return isAuthen(response);
	}

	/**
	 * 验证是否为Http请求
	 * 
	 * @param request
	 *            请求信息
	 * @return 是否为Http请求
	 */
	private boolean isHttp(HttpServletRequest request) {
		HttpServletRequest req = (HttpServletRequest) request;
		String upgrade = req.getHeader("Upgrade");
		if ("websocket".equals(upgrade)) {
			return false;
		}
		return true;
	}

	/**
	 * 验证请求路径是否匹配
	 * 
	 * @param request
	 *            请求信息
	 * @return 是否匹配
	 */
	private boolean isMatch(HttpServletRequest request) {
		String uri = request.getRequestURI();
		for (String pattern : excludePatterns) {
			if (pathMatcher.matches(pattern, uri)) {
				return false;
			}
		}
		for (String pattern : includePatterns) {
			if (pathMatcher.matches(pattern, uri)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 验证当前请求是否通过认证
	 * 
	 * @param response
	 *            响应信息
	 * @return 是否通过认证
	 */
	private boolean isAuthen(ServletResponse response) throws IOException {
		boolean isAuthen = SecurityUtils.getSubject().isAuthenticated();
		if (!isAuthen) {
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(JsonUtil.toJson(ResponseResult.UNAUTHENTICATED));
			response.getWriter().close();
		}
		return isAuthen;
	}

}
