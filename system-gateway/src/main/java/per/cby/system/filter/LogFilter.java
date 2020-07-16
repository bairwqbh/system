package per.cby.system.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.web.server.ServerWebExchange;

import per.cby.frame.web.filter.AbstractGatewayFilter;
import per.cby.system.constant.SystemConstants;
import per.cby.system.service.LogRecordService;

import reactor.core.publisher.Mono;

/**
 * 操作日志记录过滤器
 * 
 * @author chenboyang
 * @since 2019年7月15日
 *
 */
public class LogFilter extends AbstractGatewayFilter implements SystemConstants {

    @Autowired
    private LogRecordService logRecordService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logRecordService.save(exchange.getRequest());
        return chain.filter(exchange);
    }

}
