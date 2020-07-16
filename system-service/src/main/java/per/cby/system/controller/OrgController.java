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
import per.cby.system.model.Org;
import per.cby.system.service.OrgService;
import per.cby.system.util.SystemWebUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 组织机构请求控制器
 * 
 * @author chenboyang
 *
 */
@Validated
@RestController("__ORG_CONTROLLER__")
@RequestMapping("/org")
@Api(value = "组织机构Controller", tags = "组织机构前端控制器接口")
public class OrgController extends AbstractController<OrgService, Org> {

    @GetMapping("/tree")
    @ApiOperation("查询机构树状数据")
    public List<Org> tree(@ApiParam("上级编号") @RequestParam(required = false) String parentId) {
        if (parentId == null) {
            parentId = SystemWebUtil.getNotPlatformOrgId();
        }
        return baseService.tree(parentId);
    }

    @PutMapping("/up")
    @ApiOperation("查询菜单树状数据")
    public boolean up(@RequestBody Org org) {
        return baseService.up(org);
    }

    @PutMapping("/down")
    @ApiOperation("机构上移")
    public boolean down(@RequestBody Org org) {
        return baseService.down(org);
    }

    @GetMapping
    @ApiOperation("获取机构权限")
    public String orgRecursive(@ApiParam(value = "机构编号", required = true) @RequestParam String orgId) {
        return baseService.orgAuth(orgId);
    }

}
