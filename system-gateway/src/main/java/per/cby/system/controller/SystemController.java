package per.cby.system.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import per.cby.frame.web.constant.ResponseResult;
import per.cby.frame.web.exception.TokenException;
import per.cby.system.service.AuthenService;
import per.cby.system.service.SystemService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 系统通用请求控制器
 */
@Validated
@RestController
public class SystemController {

    @Autowired
    private SystemService systemService;

    @Autowired
    private AuthenService authenService;

    /**
     * HTTP反向代理服务
     * 
     * @param url     请求地址
     * @param request 请求信息
     * @return 响应结果
     */
    @RequestMapping("/proxy/**")
    @ApiOperation("HTTP反向代理服务")
    public Object proxy(@ApiParam(value = "令牌", required = true) @RequestHeader String token,
            ServerHttpRequest request) {
        TokenException.isTrue(authenService.isAuthen(token, request), ResponseResult.TOKEN_ERROR.getMessage());
        URI uri = request.getURI();
        String url = uri.toString().replaceFirst(uri.getScheme(), "").replaceFirst("://", "")
                .replaceFirst(uri.getAuthority(), "").replaceFirst("/proxy/", "");
        return systemService.proxy(url, request);
    }

}
