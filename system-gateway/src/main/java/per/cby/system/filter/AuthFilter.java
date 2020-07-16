package per.cby.system.filter;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

import per.cby.frame.common.constant.FlagString;
import per.cby.frame.common.exception.BusinessAssert;
import per.cby.frame.web.constant.ResponseResult;
import per.cby.frame.web.exception.TokenException;
import per.cby.frame.web.filter.AbstractGatewayFilter;
import per.cby.frame.web.session.Session;
import per.cby.frame.web.session.SessionManager;
import per.cby.system.constant.SystemConstants;
import per.cby.system.service.ParamService;

import reactor.core.publisher.Mono;

/**
 * 认证过滤器
 * 
 * @author chenboyang
 * @since 2019年7月15日
 *
 */
public class AuthFilter extends AbstractGatewayFilter implements SystemConstants {

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private ParamService paramService;
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        List<String> list = request.getHeaders().get(TOKEN);
        TokenException.isTrue(CollectionUtils.isNotEmpty(list), ResponseResult.NO_TOKEN.getMessage());
        String apiCheck = paramService.get(API_CHECK);
        if (FlagString.TRUE.equals(apiCheck)) {
            String token = list.get(0);
            Session session = sessionManager.getSession(token);
            List<String> apis = session.getAttribute(APIS);
            String path = request.getURI().getPath();
            BusinessAssert.notEmpty(apis, "无权访问此接口！");
            if (HttpMethod.DELETE.equals(request.getMethod())) {
                BusinessAssert.isTrue(apis.stream().anyMatch(path::startsWith), "无权访问此接口！");
            } else {
                BusinessAssert.isTrue(apis.contains(path), "无权访问此接口！");
            }
        }
        return chain.filter(exchange);
    }

}
