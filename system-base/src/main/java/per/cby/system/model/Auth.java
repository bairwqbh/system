package per.cby.system.model;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import per.cby.frame.common.base.BaseModel;
import per.cby.frame.common.db.mybatis.annotation.AutoId;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 权限表
 * </p>
 *
 * @author chenboyang
 * @since 2019-03-07
 */
@Data
@TableName("sys_auth")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "Auth对象", description = "权限表")
public class Auth extends BaseModel<Auth> {

    private static final long serialVersionUID = 1L;

    @AutoId
    @TableField(fill = FieldFill.INSERT)
    @Size(max = 32)
    @ApiModelProperty("权限编号")
    private String authId;

    @Size(max = 32)
    @ApiModelProperty("权限类型")
    private String authType;

    @NotBlank
    @Size(max = 32)
    @ApiModelProperty("关系类型")
    private String relateType;

    @NotBlank
    @Size(max = 32)
    @ApiModelProperty("权限模式")
    private String authMode;

    @NotBlank
    @Size(max = 32)
    @ApiModelProperty("对象编号")
    private String objectId;

    @Size(max = 32)
    @ApiModelProperty("对象名称")
    private String objectName;

    @ApiModelProperty("是否启用")
    private Boolean enable;

    @TableField(exist = false)
    @ApiModelProperty("目标编号列表")
    private List<String> targetList;

}
