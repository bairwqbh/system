package per.cby.system.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import per.cby.frame.web.controller.AbstractController;
import per.cby.system.model.Param;
import per.cby.system.service.ParamService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 系统参数请求控制器
 * 
 * @author chenboyang
 *
 */
@Validated
@RestController("__PARAM_CONTROLLER__")
@RequestMapping("/param")
@Api(value = "系统参数Controller", tags = "系统参数前端控制器接口")
public class ParamController extends AbstractController<ParamService, Param> {

    @PostMapping("/get")
    @ApiOperation("新增记录")
    public String get(@ApiParam(value = "参数键", required = true) @RequestParam String key) {
        return baseService.get(key);
    }

    @PostMapping("/set")
    @ApiOperation("新增记录")
    public boolean set(@ApiParam(value = "参数键", required = true) @RequestParam String key,
            @ApiParam(value = "参数值", required = true) @RequestParam String value) {
        return baseService.set(key, value);
    }

    @PostMapping("/delete")
    @ApiOperation("新增记录")
    public boolean delete(@ApiParam(value = "参数键", required = true) @RequestParam String key) {
        return baseService.delete(key);
    }

}
