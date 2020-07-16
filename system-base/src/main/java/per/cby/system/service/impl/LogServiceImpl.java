package per.cby.system.service.impl;

import java.time.LocalDateTime;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import per.cby.frame.common.base.AbstractService;
import per.cby.frame.common.util.StringUtil;
import per.cby.system.constant.SystemConstants;
import per.cby.system.constant.SystemDict.LogType;
import per.cby.system.mapper.LogMapper;
import per.cby.system.model.Log;
import per.cby.system.service.LogService;

/**
 * <p>
 * 日志表 服务实现类
 * </p>
 *
 * @author chenboyang
 * @since 2018-01-26
 */
@Service("__LOG_SERVICE__")
public class LogServiceImpl extends AbstractService<LogMapper, Log> implements LogService, SystemConstants {

    @Override
    protected Wrapper<Log> queryWrapper(Map<String, Object> param) {
        return new LambdaQueryWrapper<Log>()
                .eq(StringUtil.isNotBlank(param.get("sysId")), Log::getSysId, param.get("sysId"))
                .ge(param.get("startTime") != null, Log::getRecordTime, param.get("startTime"))
                .le(param.get("endTime") != null, Log::getRecordTime, param.get("endTime"))
                .and(param.get("keyword") != null,
                        w -> w.like(Log::getInfo, param.get("keyword")).or().like(Log::getUserId, param.get("keyword")))
                .orderByDesc(Log::getRecordTime);
    }

    @Override
    public boolean save(Log log) {
        if (log == null) {
            return false;
        }
        if (StringUtils.isBlank(log.getLogType())) {
            if (StringUtils.isNotBlank(log.getException())) {
                log.setLogType(LogType.EXCEPTION.getCode());
                if (log.getException().length() > 1000) {
                    log.setException(log.getException().substring(0, 1000));
                }
            } else {
                log.setLogType(LogType.OPREATION.getCode());
            }
        }
        if (StringUtils.isNotBlank(log.getParam()) && log.getParam().length() > 1000) {
            log.setParam(log.getParam().substring(0, 1000));
        }
        if (log.getRecordTime() == null) {
            log.setRecordTime(LocalDateTime.now());
        }
        return super.save(log);
    }

}
