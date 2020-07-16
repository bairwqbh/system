package per.cby.system.controller;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import per.cby.frame.web.controller.AbstractController;
import per.cby.system.model.Api;
import per.cby.system.service.ApiService;

import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 接口表 前端控制器
 * </p>
 *
 * @author chenboyang
 * @since 2019-03-07
 */
@Validated
@RestController("__API_CONTROLLER__")
@RequestMapping("/api")
@io.swagger.annotations.Api(value = "接口表Controller", tags = "接口表前端控制器接口")
public class ApiController extends AbstractController<ApiService, Api> {

    @GetMapping("/tree")
    @ApiOperation("查询接口树状数据")
    public List<Api> tree() {
        return baseService.tree();
    }

}
