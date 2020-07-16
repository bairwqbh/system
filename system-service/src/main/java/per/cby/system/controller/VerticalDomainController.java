package per.cby.system.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import per.cby.frame.web.controller.AbstractController;
import per.cby.system.model.VerticalDomain;
import per.cby.system.service.VerticalDomainService;

import io.swagger.annotations.Api;

/**
 * 纵向数据模型请求控制器
 * 
 * @author chenboyang
 *
 */
@Validated
@RestController("__VERTICAL_DOMAIN_CONTROLLER__")
@RequestMapping("/verticalDomain")
@Api(value = "纵向数据模型Controller", tags = "纵向数据模型前端控制器接口")
public class VerticalDomainController extends AbstractController<VerticalDomainService, VerticalDomain> {

}
