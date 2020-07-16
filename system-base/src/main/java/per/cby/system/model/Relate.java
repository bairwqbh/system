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
 * 关联关系表
 * </p>
 *
 * @author chenboyang
 * @since 2019-03-07
 */
@Data
@TableName("sys_relate")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "Relate对象", description = "关联关系表")
public class Relate extends BaseModel<Relate> {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @Size(max = 32)
    @ApiModelProperty("对象编号")
    private String objectId;

    @NotBlank
    @Size(max = 32)
    @ApiModelProperty("目标编号")
    private String targetId;

    @NotBlank
    @Size(max = 32)
    @ApiModelProperty("关系类型")
    private String relateType;

}
