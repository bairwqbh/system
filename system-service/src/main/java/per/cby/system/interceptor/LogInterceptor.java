package per.cby.system.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import per.cby.system.service.WebLogService;

import lombok.RequiredArgsConstructor;

/**
 * 系统请求统一日志记录拦截器
 * 
 * @author chenboyang
 *
 */
@RequiredArgsConstructor
public class LogInterceptor extends HandlerInterceptorAdapter {

    /**
     * WEB日志服务接口
     */
    private final WebLogService webLogService;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        webLogService.save(request, handler, ex);
    }

}
