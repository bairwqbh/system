package per.cby.system.model;

import java.time.LocalDate;
import java.util.Optional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import per.cby.frame.common.base.BaseModel;
import per.cby.frame.common.useable.Useable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户表 : 系统管理用户表
 * </p>
 *
 * @author chenboyang
 * @since 2018-01-26
 */
@Data
@TableName("sys_user")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "User对象", description = "用户")
public class User extends BaseModel<User> implements Useable {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @Size(max = 32)
    @ApiModelProperty("用户编号")
    private String userId;

    @Size(max = 32)
    @ApiModelProperty("密码")
    private String password;

    @Size(max = 32)
    @ApiModelProperty("真实姓名")
    private String realName;

    @Size(max = 32)
    @ApiModelProperty("性别")
    private String gender;

    @ApiModelProperty("出生年月")
    private LocalDate birthday;

    @Size(max = 32)
    @ApiModelProperty("机构代码")
    private String orgId;

    @Size(max = 32)
    @ApiModelProperty("机构名称")
    private String orgName;

    @Size(max = 32)
    @ApiModelProperty("所属地区编码")
    private String areaId;

    @Size(max = 64)
    @ApiModelProperty("所属地区名称")
    private String areaName;

    @Size(max = 16)
    @ApiModelProperty("手机")
    private String phone;

    @Size(max = 256)
    @ApiModelProperty("家庭住址")
    private String address;

    @Size(max = 128)
    @ApiModelProperty("电子邮箱")
    private String email;

    @ApiModelProperty("是否启用")
    private Boolean enable;

    @ApiModelProperty("角色编号")
    @TableField(exist = false)
    private String roleId;

    @Override
    public String account() {
        return userId;
    }

    @Override
    public String password() {
        return password;
    }

    @Override
    public boolean enable() {
        return Optional.ofNullable(enable).orElse(false);
    }

}
