package per.cby.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import per.cby.frame.web.constant.ResponseResult;
import per.cby.system.dto.LoginDto;
import per.cby.system.dto.ModPwdDto;
import per.cby.system.service.AuthenService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 权限认证前端控制接口
 * 
 * @author chenboyang
 *
 */
@Validated
@RestController
@RequestMapping("/authen")
public class AuthenController {

    @Autowired
    private AuthenService authenService;

    @GetMapping("/isAuthen")
    @ApiOperation("令牌权限是否有效")
    public Object isAuthen(@ApiParam(value = "令牌", required = true) @RequestHeader String token,
            ServerHttpRequest request) {
        return ResponseResult.SUCCESS.data(authenService.isAuthen(token, request));
    }

    @PostMapping("/login")
    @ApiOperation("用户登录系统")
    public Object login(@RequestBody LoginDto loginDto, ServerHttpRequest request) {
        return ResponseResult.SUCCESS.data(authenService.login(loginDto, request));
    }

    @DeleteMapping("/logout")
    @ApiOperation("用户退出系统")
    public Object logout(@ApiParam(value = "令牌", required = true) @RequestHeader String token,
            ServerHttpRequest request) {
        return ResponseResult.SUCCESS.data(authenService.logout(token, request));
    }

    @GetMapping("/info")
    @ApiOperation("登陆用户认证信息")
    public Object info(@ApiParam(value = "令牌", required = true) @RequestHeader String token,
            ServerHttpRequest request) {
        return ResponseResult.SUCCESS.data(authenService.info(token, request));
    }

    @PutMapping("/modPwd")
    @ApiOperation("用户密码修改")
    public Object modPwd(@ApiParam(value = "令牌", required = true) @RequestHeader String token,
            @RequestBody ModPwdDto modPwdDto, ServerHttpRequest request) {
        return ResponseResult.SUCCESS.data(authenService.modPwd(token, modPwdDto, request));
    }

}
