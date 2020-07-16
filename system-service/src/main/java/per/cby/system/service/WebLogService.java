package per.cby.system.service;

import java.util.function.Consumer;

import javax.servlet.http.HttpServletRequest;

import per.cby.system.model.Log;

/**
 * WEB日志服务
 * 
 * @author chenboyang
 * @since 2019年11月25日
 *
 */
public interface WebLogService {

    /**
     * 请求拦截器保存日志执行方法
     * 
     * @param request 请求信息
     * @param handler 业务处理器
     * @param ex      异常
     */
    void save(HttpServletRequest request, Object handler, Exception ex);

    /**
     * 业务请求保存日志执行方法
     * 
     * @param info    操作信息
     * @param request 请求信息
     */
    default void save(String info, HttpServletRequest request) {
        save(info, request, null);
    }

    /**
     * 业务请求保存日志执行方法
     * 
     * @param info     操作信息
     * @param request  请求信息
     * @param consumer 回调处理
     */
    void save(String info, HttpServletRequest request, Consumer<Log> consumer);

}
