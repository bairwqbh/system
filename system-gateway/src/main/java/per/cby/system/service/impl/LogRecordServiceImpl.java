package per.cby.system.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

import per.cby.frame.common.util.BaseUtil;
import per.cby.frame.common.util.JsonUtil;
import per.cby.frame.common.util.ThreadPoolUtil;
import per.cby.frame.redis.storage.hash.RedisHashStorage;
import per.cby.frame.web.session.Session;
import per.cby.frame.web.session.SessionManager;
import per.cby.system.constant.SystemConstants;
import per.cby.system.constant.SystemDict;
import per.cby.system.model.Api;
import per.cby.system.model.Log;
import per.cby.system.model.User;
import per.cby.system.service.ApiService;
import per.cby.system.service.LogRecordService;
import per.cby.system.service.LogService;

import lombok.extern.slf4j.Slf4j;

/**
 * 日志记录服务实现
 * 
 * @author chenboyang
 * @since 2019年11月6日
 *
 */
@Slf4j
@Service
public class LogRecordServiceImpl implements LogRecordService, SystemConstants {

    @Autowired(required = false)
    @Qualifier(API_CACHE_HASH)
    private RedisHashStorage<String, Api> apiCacheHash;

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private LogService logService;

    @Autowired
    private ApiService apiService;

    @Override
    public void save(String info, ServerHttpRequest request, Consumer<Log> consumer) {
        try {
            if (HttpMethod.GET.equals(request.getMethod())) {
                return;
            }
            List<String> list = request.getHeaders().get(TOKEN);
            if (CollectionUtils.isEmpty(list)) {
                return;
            }
            String token = list.get(0);
            Session session = sessionManager.getSession(token);
            ThreadPoolUtil.execute(() -> {
                String path = request.getURI().getPath().replace("//", "/");
                String url = new String(path);
                if (HttpMethod.DELETE.equals(request.getMethod())) {
                    url = url.substring(0, url.lastIndexOf("/"));
                }
                Log log = new Log();
                log.setInfo(info);
                if (apiCacheHash != null) {
                    Api api = apiCacheHash.get(url);
                    if (api == null) {
                        api = apiService.find(BaseUtil.newHashMap("url", url));
                        if (api != null) {
                            apiCacheHash.put(url, api);
                        }
                    }
                    if (api != null) {
                        log.setSysId(api.getSysId());
                        if (StringUtils.isBlank(log.getInfo())) {
                            String desc = StringUtils.isNotBlank(api.getDescription()) ? api.getDescription()
                                    : api.getApiName();
                            log.setInfo(desc);
                        }
                    }
                }
                User user = Optional.ofNullable(session).map(t -> t.getAttribute(USER)).map(JsonUtil::toJson)
                        .map(json -> JsonUtil.toObject(json, User.class)).orElse(null);
                if (user != null) {
                    log.setUserId(user.getUserId());
                }
                log.setLogType(SystemDict.LogType.OPREATION.getCode());
                log.setPath(path);
                StringBuilder sb = new StringBuilder();
                sb.append(JsonUtil.toJson(request.getQueryParams()));

//                request.getBody().subscribe(buffer -> {
//                    byte[] bytes = new byte[buffer.readableByteCount()];
//                    buffer.read(bytes);
//                    DataBufferUtils.release(buffer);
//                    sb.append(new String(bytes, StandardCharsets.UTF_8));
//                });

                log.setParam(sb.toString());
                log.setIp(request.getRemoteAddress().getHostString());
                log.setRecordTime(LocalDateTime.now());
                Optional.ofNullable(consumer).ifPresent(c -> c.accept(log));
                logService.save(log);
            });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
