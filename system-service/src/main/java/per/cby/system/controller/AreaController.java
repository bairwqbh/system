package per.cby.system.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import per.cby.system.model.Area;
import per.cby.system.service.AreaService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 地区信息请求控制器
 * 
 * @author chenboyang
 *
 */
@Validated
@RestController
@RequestMapping("/area")
@Api(value = "地区信息Controller", tags = "地区信息前端控制器接口")
public class AreaController {

    @Autowired
    private AreaService baseService;

    @GetMapping("/list")
    @ApiOperation("查询地区列表")
    public List<Area> list(@RequestParam Map<String, Object> map) {
        return baseService.list(map);
    }

    @GetMapping("/tree")
    @ApiOperation("查询地区树状列表")
    public List<Area> tree(@ApiParam("地区编码") @RequestParam(required = false) String areaId) {
        return baseService.tree(areaId);
    }

    @GetMapping
    @ApiOperation("获取地区权限")
    public String areaRecursive(@ApiParam(value = "地区编号", required = true) @RequestParam String areaId) {
        return baseService.areaAuth(areaId);
    }

}
