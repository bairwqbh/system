package per.cby.system.controller;

import java.util.List;
import java.util.Map;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import per.cby.frame.web.controller.AbstractController;
import per.cby.system.model.Dict;
import per.cby.system.service.DictService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 基础字典请求控制器
 * 
 * @author chenboyang
 *
 */
@Validated
@RestController
@RequestMapping("/dict")
@Api(value = "字典Controller", tags = "字典前端控制器接口")
public class DictController extends AbstractController<DictService, Dict> {

    @GetMapping("/tree")
    @ApiOperation("查询菜单树状数据")
    public List<Dict> tree() {
        return baseService.tree();
    }

    @PutMapping("/up")
    @ApiOperation("菜单上移")
    public boolean up(@RequestBody Dict dict) {
        return baseService.up(dict);
    }

    @PutMapping("/down")
    @ApiOperation("菜单下移")
    public boolean down(@RequestBody Dict dict) {
        return baseService.down(dict);
    }

    @GetMapping("/dictList")
    @ApiOperation("根据父级代码获取字典")
    public List<?> dictList(@ApiParam(value = "父级代码", required = true) @RequestParam String parentId) {
        return baseService.dictList(parentId);
    }

    @GetMapping("/dictMap")
    @ApiOperation("查询业务字典全集")
    public Map<String, Map<String, String>> dictMap() {
        return baseService.dictMap();
    }

}
