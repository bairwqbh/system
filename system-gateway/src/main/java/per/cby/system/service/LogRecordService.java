package per.cby.system.service;

import java.util.function.Consumer;

import org.springframework.http.server.reactive.ServerHttpRequest;

import per.cby.system.model.Log;

/**
 * 日志记录服务
 * 
 * @author chenboyang
 * @since 2019年11月6日
 *
 */
public interface LogRecordService {

    /**
     * 业务请求保存日志执行方法
     * 
     * @param request 请求信息
     */
    default void save(ServerHttpRequest request) {
        save(null, request);
    }

    /**
     * 业务请求保存日志执行方法
     * 
     * @param info    操作信息
     * @param request 请求信息
     */
    default void save(String info, ServerHttpRequest request) {
        save(info, request, null);
    }

    /**
     * 业务请求保存日志执行方法
     * 
     * @param info     操作信息
     * @param request  请求信息
     * @param consumer 回调处理
     */
    void save(String info, ServerHttpRequest request, Consumer<Log> consumer);

}
