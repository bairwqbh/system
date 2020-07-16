package per.cby.system.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import per.cby.frame.common.model.Page;
import per.cby.system.model.Log;
import per.cby.system.service.LogService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 系统日志请求控制器
 * 
 * @author chenboyang
 *
 */
@Validated
@RestController("__LOG_CONTROLLER__")
@RequestMapping("/log")
@Api(value = "系统日志Controller", tags = "系统日志前端控制器接口")
public class LogController {

    @Autowired
    private LogService baseService;

    @PostMapping("/save")
    @ApiOperation("保存日志记录")
    public boolean save(@RequestBody Log log) {
        return baseService.save(log);
    }

    @GetMapping("/page")
    @ApiOperation("分页查询日志记录")
    public Page<Log> page(Page<Log> page, @RequestParam Map<String, Object> map) {
        return baseService.page(page, map);
    }

}
