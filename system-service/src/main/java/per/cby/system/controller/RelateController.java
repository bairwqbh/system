package per.cby.system.controller;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import per.cby.frame.web.controller.AbstractController;
import per.cby.system.model.Relate;
import per.cby.system.service.RelateService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * 关联关系表 前端控制器
 * </p>
 *
 * @author chenboyang
 * @since 2019-03-07
 */
@Validated
@RestController("__RELATE_CONTROLLER__")
@RequestMapping("/relate")
@Api(value = "关联关系表Controller", tags = "关联关系表前端控制器接口")
public class RelateController extends AbstractController<RelateService, Relate> {

    @GetMapping("/getTargetList")
    @ApiOperation("根据对象编号及关系类型查询目标列表")
    public List<String> getTargetList(@ApiParam(value = "对象编号", required = true) @RequestParam String objectId,
            @ApiParam(value = "关系类型", required = true) @RequestParam String relateType) {
        return baseService.getTargetList(objectId, relateType);
    }

}
