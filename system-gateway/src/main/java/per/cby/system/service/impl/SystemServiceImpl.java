package per.cby.system.service.impl;

import java.net.URI;

import org.apache.commons.collections4.MapUtils;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import per.cby.frame.common.exception.BusinessAssert;
import per.cby.frame.common.util.HttpUtil;
import per.cby.system.service.SystemService;

/**
 * 系统通用服务实现
 * 
 * @author chenboyang
 * @since 2019年11月6日
 *
 */
@Service
public class SystemServiceImpl implements SystemService {

    @Override
    public Object proxy(String url, ServerHttpRequest request) {
        BusinessAssert.hasText(url, "请求地址不能为空！");
        BusinessAssert.isTrue(url.startsWith("http://") || url.startsWith("https://"), "请求地址格式错误！");
        BusinessAssert.notNull(request, "请求数据为空无法执行！");
        RequestEntity<?> requestEntity = null;
        URI uri = URI.create(url);
        HttpHeaders headers = new HttpHeaders();
        request.getHeaders().forEach(headers::put);
        headers.remove(HttpHeaders.ACCEPT_ENCODING);
        headers.remove(HttpHeaders.CONTENT_LENGTH);
        headers.setContentType(request.getHeaders().getContentType());
        HttpMethod method = request.getMethod();
        MultiValueMap<String, String> queryParams = request.getQueryParams();
        int contentLength = (int) request.getHeaders().getContentLength();
        if (MapUtils.isNotEmpty(queryParams)) {
            requestEntity = new RequestEntity<MultiValueMap<String, String>>(queryParams, headers, method, uri);
        } else if (contentLength > 0) {
            byte[] body = new byte[contentLength];
            request.getBody().subscribe(buffer -> {
                buffer.read(body);
                DataBufferUtils.release(buffer);
            });
            requestEntity = new RequestEntity<byte[]>(body, headers, method, uri);
        } else {
            requestEntity = new RequestEntity<>(headers, method, uri);
        }
        return HttpUtil.restTemplate().exchange(requestEntity, byte[].class).getBody();
    }

}
