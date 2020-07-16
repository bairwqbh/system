package per.cby.system.model;

import java.time.LocalDateTime;

import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.TableName;
import per.cby.frame.common.base.BaseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 日志表
 * </p>
 *
 * @author chenboyang
 * @since 2018-01-26
 */
@Data
@TableName("sys_log")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "Log对象", description = "日志")
public class Log extends BaseModel<Log> {

    private static final long serialVersionUID = 1L;

    @Size(max = 32)
    @ApiModelProperty("日志类型")
    private String logType;

    @Size(max = 32)
    @ApiModelProperty("系统编号")
    private String sysId;

    @Size(max = 1000)
    @ApiModelProperty("操作信息")
    private String info;

    @Size(max = 512)
    @ApiModelProperty("请求路径")
    private String path;

    @Size(max = 1000)
    @ApiModelProperty("参数")
    private String param;

    @Size(max = 64)
    @ApiModelProperty("IP地址")
    private String ip;

    @Size(max = 32)
    @ApiModelProperty("操作用户")
    private String userId;

    @Size(max = 1000)
    @ApiModelProperty("异常信息")
    private String exception;

    @ApiModelProperty("记录时间")
    private LocalDateTime recordTime;

}
