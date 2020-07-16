package per.cby.system.controller;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import per.cby.frame.web.controller.AbstractController;
import per.cby.system.model.User;
import per.cby.system.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 用户请求控制器
 * 
 * @author chenboyang
 *
 */
@Validated
@RestController("__USER_CONTROLLER__")
@RequestMapping("/user")
@Api(value = "用户Controller", tags = "用户前端控制器接口")
public class UserController extends AbstractController<UserService, User> {

    @GetMapping("/getUser")
    @ApiOperation("获取用户信息")
    public User getUser(@ApiParam(value = "用户编号", required = true) @RequestParam String userId) {
        return baseService.getUser(userId);
    }

    @GetMapping("/getRoles")
    @ApiOperation("获取用户角色列表")
    public List<String> getRoles(@ApiParam(value = "用户编号", required = true) @RequestParam String userId) {
        return baseService.getRoles(userId);
    }

    @GetMapping("/getAuths")
    @ApiOperation("获取用户权限列表")
    public List<String> getAuths(@ApiParam(value = "用户编号", required = true) @RequestParam String userId) {
        return baseService.getAuths(userId);
    }

    @GetMapping("/getApis")
    @ApiOperation("获取用户接口列表")
    public List<String> getApis(@ApiParam(value = "用户编号", required = true) @RequestParam String userId) {
        return baseService.getApis(userId);
    }

    @PutMapping("/resetPwd")
    @ApiOperation("重置用户密码")
    public boolean resetPwd(@RequestBody User user) {
        return baseService.resetPwd(user);
    }

}
