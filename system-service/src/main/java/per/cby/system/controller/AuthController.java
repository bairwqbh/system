package per.cby.system.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import per.cby.frame.web.controller.AbstractController;
import per.cby.system.model.Auth;
import per.cby.system.service.AuthService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * 权限表 前端控制器
 * </p>
 *
 * @author chenboyang
 * @since 2019-03-07
 */
@Validated
@RestController
@RequestMapping("/auth")
@Api(value = "权限表Controller", tags = "权限表前端控制器接口")
public class AuthController extends AbstractController<AuthService, Auth> {

    @GetMapping("/getAuth")
    @ApiOperation("根据对象编号及关系类型查询权限信息")
    public Auth getAuth(@ApiParam(value = "对象编号", required = true) @RequestParam String objectId,
            @ApiParam(value = "关系类型", required = true) @RequestParam String relateType) {
        return baseService.getAuth(objectId, relateType);
    }

    @ApiOperation("通用授权")
    @PostMapping("/authorize")
    public boolean authorize(@RequestBody Auth auth) {
        baseService.authorize(auth);
        return true;
    }

}
