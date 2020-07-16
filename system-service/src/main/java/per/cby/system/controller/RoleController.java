package per.cby.system.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import per.cby.frame.web.controller.AbstractController;
import per.cby.system.model.Role;
import per.cby.system.service.RoleService;

import io.swagger.annotations.Api;

/**
 * 角色请求控制器
 * 
 * @author chenboyang
 *
 */
@Validated
@RestController("__ROLE_CONTROLLER__")
@RequestMapping("/role")
@Api(value = "角色Controller", tags = "角色前端控制器接口")
public class RoleController extends AbstractController<RoleService, Role> {

}
