package per.cby.system.model;

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
 * 角色表
 * </p>
 *
 * @author chenboyang
 * @since 2018-01-26
 */
@Data
@TableName("sys_role")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "Role对象", description = "角色")
public class Role extends BaseModel<Role> {

    private static final long serialVersionUID = 1L;

    @AutoId
    @TableField(fill = FieldFill.INSERT)
    @Size(max = 32)
    @ApiModelProperty("角色编号")
    private String roleId;

    @Size(max = 32)
    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("是否启用")
    private Boolean enable;

    @Size(max = 256)
    @ApiModelProperty("描述")
    private String description;

}
