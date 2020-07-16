package per.cby.system.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import per.cby.frame.web.controller.AbstractController;
import per.cby.system.model.Bucket;
import per.cby.system.service.BucketService;

import io.swagger.annotations.Api;

/**
 * 附件板块信息请求控制器
 * 
 * @author chenboyang
 *
 */
@Validated
@RestController
@RequestMapping("/bucket")
@Api(value = "附件板块Controller", tags = "附件板块前端控制器接口")
public class BucketController extends AbstractController<BucketService, Bucket> {

}
