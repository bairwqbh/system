package per.cby.system.aspect;

import java.time.LocalDateTime;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import per.cby.frame.common.config.properties.SysProperties;
import per.cby.frame.common.log.LogInter;
import per.cby.frame.common.util.ExceptionUtil;
import per.cby.frame.common.util.JsonUtil;
import per.cby.system.constant.SystemDict.LogType;
import per.cby.system.model.Log;
import per.cby.system.service.LogService;

/**
 * 日志拦截切面
 * 
 * @author chenboyang
 *
 */
@Aspect
@Component("__LOG_ASPECT__")
public class LogAspect {

    @Autowired
    private LogService logService;

    @Autowired
    private SysProperties sysProperties;

    /**
     * 切入点
     */
    @Pointcut("@annotation(logInter)")
    private void pointcut() {

    }

    /**
     * 执行前拦截
     * 
     * @param point    连接点
     * @param logInter 日志信息
     */
    @Before("@annotation(logInter)")
    public void before(JoinPoint point, LogInter logInter) {

    }

    /**
     * 执行后拦截
     * 
     * @param point    连接点
     * @param logInter 日志信息
     */
    @After("@annotation(logInter)")
    public void after(JoinPoint point, LogInter logInter) {
        Log log = new Log();
        log.setLogType(LogType.OPREATION.getCode());
        saveLog(log, point, logInter);
    }

    /**
     * 返回后拦截
     * 
     * @param returnValue 返回值
     */
    @AfterReturning(value = "@annotation(logInter)", returning = "returnValue")
    public void afterReturning(Object returnValue, LogInter logInter) {

    }

    /**
     * 异常抛出时拦截
     * 
     * @param throwable 异常
     * @param logInter  日志信息
     */
    @AfterThrowing(value = "@annotation(logInter)", throwing = "throwable")
    public void afterThrowing(JoinPoint point, Throwable throwable, LogInter logInter) {
        Log log = new Log();
        log.setLogType(LogType.EXCEPTION.getCode());
        log.setException(ExceptionUtil.getStackTraceAsString(throwable));
        saveLog(log, point, logInter);
    }

    /**
     * 方法执行时拦截控制
     * 
     * @param proceedingJoinPoint 执行进程点
     * @return 返回执行结果
     * @throws Throwable 抛出异常
     */
    @Around("@annotation(logInter)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint, LogInter logInter) throws Throwable {
        return proceedingJoinPoint.proceed();
    }

    /**
     * 保存日志记录
     * 
     * @param log      日志信息
     * @param point    连接点
     * @param logInter 日志信息
     */
    @Async
    private void saveLog(Log log, JoinPoint point, LogInter logInter) {
        log.setSysId(sysProperties.getName());
        log.setInfo(logInter.value());
        log.setPath(point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName());
        log.setParam(JsonUtil.toJson(point.getArgs()));
        log.setRecordTime(LocalDateTime.now());
        logService.save(log);
    }

}
