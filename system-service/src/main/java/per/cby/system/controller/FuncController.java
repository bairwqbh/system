package per.cby.system.controller;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import per.cby.frame.web.annotation.RequestModel;
import per.cby.frame.web.controller.AbstractController;
import per.cby.system.dto.FuncDto;
import per.cby.system.model.Func;
import per.cby.system.service.FuncService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 功能 前端控制器
 * </p>
 *
 * @author chenboyang
 * @since 2019-03-07
 */
@Validated
@RestController("__FUNC_CONTROLLER__")
@RequestMapping("/func")
@Api(value = "功能Controller", tags = "功能前端控制器接口")
public class FuncController extends AbstractController<FuncService, Func> {

    @PutMapping("/up")
    @ApiOperation("功能上移")
    public boolean up(@RequestBody Func func) {
        return baseService.up(func);
    }

    @PutMapping("/down")
    @ApiOperation("功能下移")
    public boolean down(@RequestBody Func func) {
        return baseService.down(func);
    }

    @GetMapping("/tree")
    @ApiOperation("根据条件获取功能树")
    public List<Func> tree(@RequestModel FuncDto funcDto) {
        return baseService.tree(funcDto);
    }

    @GetMapping("/menu")
    @ApiOperation("根据条件获取功能菜单树")
    public List<Func> menu(@RequestModel FuncDto funcDto) {
        return baseService.menu(funcDto);
    }

}
