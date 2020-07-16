package per.cby.system.model;

import javax.validation.constraints.NotBlank;
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
 * 参数表 : 用于配置业务参数
 * </p>
 *
 * @author chenboyang
 * @since 2018-01-26
 */
@Data
@TableName("sys_param")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "Param对象", description = "参数")
public class Param extends BaseModel<Param> {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @Size(max = 32)
    @ApiModelProperty("参数键")
    private String paramKey;

    @Size(max = 1000)
    @ApiModelProperty("参数值")
    private String paramValue;

    @Size(max = 32)
    @ApiModelProperty("参数名称")
    private String paramLabel;

    @ApiModelProperty("是否启用")
    private Boolean enable;

    @Size(max = 256)
    @ApiModelProperty("描述")
    private String description;

}
