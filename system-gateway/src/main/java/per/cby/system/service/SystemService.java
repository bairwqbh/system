package per.cby.system.service;

import org.springframework.http.server.reactive.ServerHttpRequest;

/**
 * 系统通用服务
 * 
 * @author chenboyang
 * @since 2019年11月6日
 *
 */
public interface SystemService {

    /**
     * HTTP反向代理服务
     * 
     * @param url     请求地址
     * @param request 请求信息
     * @return 响应结果
     */
    Object proxy(String url, ServerHttpRequest request);

}
