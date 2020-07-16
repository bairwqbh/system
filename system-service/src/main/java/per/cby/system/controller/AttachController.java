package per.cby.system.controller;

import java.util.Map;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import per.cby.frame.common.util.BaseUtil;
import per.cby.frame.web.annotation.RequestModel;
import per.cby.frame.web.controller.AbstractController;
import per.cby.system.model.Attach;
import per.cby.system.service.AttachService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 附件信息请求控制器
 * 
 * @author chenboyang
 *
 */
@Validated
@RestController
@RequestMapping("/attach")
@Api(value = "附件信息Controller", tags = "附件信息前端控制器接口")
public class AttachController extends AbstractController<AttachService, Attach> {

    @PostMapping("/save")
    @ApiOperation("保存附件信息")
    public boolean save(@RequestModel Attach attach, MultipartFile file) {
        if (attach != null && file != null) {
            Attach attachInfo = Attach.createAttath(file);
            attach.setOriginalName(attachInfo.getOriginalName());
            attach.setType(attachInfo.getType());
            attach.setSize(attachInfo.getSize());
            attach.setInput(attachInfo.getInput());
        }
        return baseService.save(attach);
    }

    @ApiOperation("根据参数删除附件")
    @DeleteMapping("/remove/{bucket}/{domainId}/{targetId}/{fieldId}")
    public boolean remove(@ApiParam(value = "板块", required = true) @PathVariable("bucket") String bucket,
            @ApiParam(value = "领域编号", required = true) @PathVariable("domainId") String domainId,
            @ApiParam(value = "目标编号", required = true) @PathVariable("targetId") String targetId,
            @ApiParam(value = "字段编号", required = true) @PathVariable("fieldId") String fieldId) {
        Map<String, Object> map = BaseUtil.newHashMap();
        map.put("bucket", bucket);
        map.put("domainId", domainId);
        map.put("targetId", targetId);
        map.put("fieldId", fieldId);
        return baseService.remove(map);
    }

}
