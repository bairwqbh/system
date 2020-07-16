package per.cby.system.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import per.cby.frame.web.controller.ApplyController;
import per.cby.system.dto.LoginDto;
import per.cby.system.dto.ModPwdDto;
import per.cby.system.service.SystemService;

import io.swagger.annotations.ApiOperation;

/**
 * 系统通用请求控制器
 */
@Validated
@RestController("__SYSTEM_CONTROLLER__")
public class SystemController extends ApplyController {

    @Autowired
    private SystemService systemService;

    @GetMapping("/authen/isAuthen")
    @ApiOperation("令牌权限是否有效")
    public boolean isAuthen() {
        return systemService.isAuthen();
    }

    @PostMapping("/authen/login")
    @ApiOperation("用户登录系统")
    public String login(@RequestBody LoginDto loginDto) {
        return systemService.login(loginDto);
    }

    @DeleteMapping("/authen/logout")
    @ApiOperation("用户退出系统")
    public boolean logout() {
        return systemService.logout();
    }

    @GetMapping("/authen/info")
    @ApiOperation("登陆用户认证信息")
    public Map<String, Object> info() {
        return systemService.info();
    }

    @PutMapping("/authen/modPwd")
    @ApiOperation("用户密码修改")
    public boolean modPwd(@RequestBody ModPwdDto modPwdDto) {
        return systemService.modPwd(modPwdDto);
    }

}
