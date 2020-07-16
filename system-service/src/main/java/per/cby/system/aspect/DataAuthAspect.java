package per.cby.system.aspect;

import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import per.cby.frame.common.util.BaseUtil;
import per.cby.frame.web.service.ApplyService;
import per.cby.system.annotation.DataAuth;
import per.cby.system.constant.DataAuthType;

/**
 * 数据权限切面拦截器
 * 
 * @author chenboyang
 *
 */
@Aspect
@SuppressWarnings("unchecked")
@Component("__DATA_AUTH_ASPECT__")
public class DataAuthAspect {

    @Autowired
    private ApplyService applyService;

    /**
     * 调用接口前验证
     * 
     * @param point    信息连接点
     * @param dataAuth 数据权限信息
     */
    @Before("@annotation(dataAuth)")
    public void before(JoinPoint point, DataAuth dataAuth) {
        if (ArrayUtils.isEmpty(point.getArgs()) || ArrayUtils.isEmpty(dataAuth.value())) {
            return;
        }
        Map<String, Object> map = getMap(point.getArgs());
        if (map == null) {
            return;
        }
        DataAuthType[] dataAuthTypes = BaseUtil.derepeat(dataAuth.value());
        for (DataAuthType dataAuthType : dataAuthTypes) {
            String auth = applyService.getSessionAttribute(dataAuthType.getCode());
            if (StringUtils.isNotBlank(auth)) {
                map.put(dataAuthType.getCode(), auth);
            }
        }
    }

    /**
     * 获取参数容器
     * 
     * @param args 参数集
     * @return 参数容器
     */
    private Map<String, Object> getMap(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof Map) {
                return (Map<String, Object>) arg;
            } else if (arg instanceof Map) {
                return ((Map<String, Object>) arg);
            }
        }
        return null;
    }

}
