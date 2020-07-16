package per.cby.system.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import per.cby.frame.web.annotation.RequestModel;
import per.cby.frame.web.controller.AbstractController;
import per.cby.system.model.AppVersion;
import per.cby.system.model.Attach;
import per.cby.system.service.AppVersionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 版本信息请求控制器
 */
@Validated
@RestController
@RequestMapping("/appVersion")
@Api(value = "版本信息Controller", tags = "版本信息前端控制器接口")
public class AppVersionController extends AbstractController<AppVersionService, AppVersion> {

    @PostMapping("/save")
    @ApiOperation("保存版本附件信息")
    public boolean save(@RequestModel AppVersion appVersion, MultipartFile file) {
        if (file != null) {
            appVersion.setAttach(Attach.createAttath(file));
        }
        return baseService.save(appVersion);
    }

    @GetMapping("/currVersion")
    @ApiOperation("获取应用最新版本信息")
    public Object currVersion(@ApiParam(value = "应用编号", required = true) @RequestParam String appId) {
        return baseService.currVersion(appId);
    }

}
