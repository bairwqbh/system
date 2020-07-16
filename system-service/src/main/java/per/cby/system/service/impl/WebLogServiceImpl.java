package per.cby.system.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Consumer;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;

import per.cby.frame.common.util.ExceptionUtil;
import per.cby.frame.common.util.ThreadPoolUtil;
import per.cby.system.constant.SystemDict;
import per.cby.system.model.Log;
import per.cby.system.model.User;
import per.cby.system.service.LogService;
import per.cby.system.service.SystemService;
import per.cby.system.service.WebLogService;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * WEB日志服务接口实现
 * 
 * @author chenboyang
 * @since 2019年11月25日
 *
 */
@Slf4j
@Service("__WEB_LOG_SERVICE__")
public class WebLogServiceImpl implements WebLogService {

    @Autowired
    private LogService logService;

    @Autowired
    private SystemService systemService;
    
    @Override
    public void save(HttpServletRequest request, Object handler, Exception ex) {
        try {
            setReqInfo(new Log(), request, t -> {
                setBizInfo(t, handler);
                setExInfo(t, ex);
                logService.save(t);
            });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void save(String info, HttpServletRequest request, Consumer<Log> consumer) {
        try {
            setReqInfo(new Log().setInfo(info), request, t -> {
                Optional.ofNullable(consumer).ifPresent(c -> c.accept(t));
                logService.save(t);
            });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 设置日志请求信息
     * 
     * @param log      日志信息
     * @param request  请求信息
     * @param consumer 回调处理
     */
    private void setReqInfo(Log logInfo, HttpServletRequest request, Consumer<Log> consumer) {
        if (logInfo == null || request == null) {
            return;
        }
        String method = request.getMethod().toUpperCase();
        if (HttpMethod.GET.name().equals(method)) {
            return;
        }
        String path = request.getRequestURI().replace("//", "/");
        String ip = request.getRemoteAddr();
        StringBuilder param = new StringBuilder();
        if (StringUtils.isNotBlank(request.getQueryString())) {
            param.append(request.getQueryString());
        }
        if (request.getContentLength() > 0) {
            try (BufferedReader br = request.getReader()) {
                String str;
                while ((str = br.readLine()) != null) {
                    param.append(str);
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        User user = systemService.currUser();
        ThreadPoolUtil.execute(() -> {
            if (user != null) {
                logInfo.setUserId(user.getUserId());
            }
            logInfo.setLogType(SystemDict.LogType.OPREATION.getCode());
            logInfo.setPath(path);
            logInfo.setParam(param.toString());
            logInfo.setIp(ip);
            Optional.ofNullable(consumer).ifPresent(c -> c.accept(logInfo));
        });
    }

    /**
     * 设置日志处理信息
     * 
     * @param log     日志信息
     * @param handler 业务处理器
     */
    private void setBizInfo(Log log, Object handler) {
        if (log != null && handler != null && handler instanceof HandlerMethod) {
            Method handlerMethod = ((HandlerMethod) handler).getMethod();
            ApiOperation apiOperation = handlerMethod.getAnnotation(ApiOperation.class);
            if (apiOperation != null) {
                log.setInfo(apiOperation.value());
            }
        }
    }

    /**
     * 设置日志异常信息
     * 
     * @param log       日志信息
     * @param throwable 异常
     */
    private void setExInfo(Log log, Throwable throwable) {
        if (log != null && throwable != null) {
            String exInfo = ExceptionUtil.getStackTraceAsString(throwable);
            log.setException(exInfo);
        }
    }

}
