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
 * 纵向数据模型表 : 各业务基础表的纵向数据模型扩展表
 * </p>
 *
 * @author chenboyang
 * @since 2018-01-26
 */
@Data
@Accessors(chain = true)
@TableName("sys_vertical_domain")
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "VerticalDomain对象", description = "纵向数据模型")
public class VerticalDomain extends BaseModel<VerticalDomain> {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @Size(max = 32)
    @ApiModelProperty("模型标识")
    private String domainId;

    @NotBlank
    @Size(max = 32)
    @ApiModelProperty("行标识")
    private String rowId;

    @NotBlank
    @Size(max = 32)
    @ApiModelProperty("字段标识")
    private String fieldId;

    @Size(max = 1000)
    @ApiModelProperty("字段值")
    private String fieldValue;

}
