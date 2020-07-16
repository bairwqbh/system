package per.cby.system.filter;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

import per.cby.frame.web.constant.ResponseResult;
import per.cby.frame.web.filter.AbstractGatewayFilter;
import per.cby.frame.web.session.SessionManager;
import per.cby.system.constant.SystemConstants;
import per.cby.system.service.AuthenService;

import reactor.core.publisher.Mono;

/**
 * token校验过滤器
 * 
 * @author chenboyang
 * @since 2019年7月15日
 *
 */
public class TokenFilter extends AbstractGatewayFilter implements SystemConstants {

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private AuthenService authenService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (match(exchange)) {
            ServerHttpRequest request = exchange.getRequest();
            List<String> list = request.getHeaders().get(TOKEN);
            if (CollectionUtils.isEmpty(list) && MapUtils.isNotEmpty(request.getQueryParams())) {
                list = request.getQueryParams().get(TOKEN);
            }
            if (CollectionUtils.isEmpty(list)) {
                return resultWrite(exchange, ResponseResult.NO_TOKEN.result());
            }
            String token = list.get(0);
            if (!authenService.isAuthen(token, request)) {
                return resultWrite(exchange, ResponseResult.TOKEN_ERROR.result());
            }
            sessionManager.touch(token);
        }
        return chain.filter(exchange);
    }

}
