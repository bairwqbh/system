package per.cby.system.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户登录参数
 * 
 * @author chenboyang
 * @since 2019年7月23日
 *
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "LoginDto对象", description = "用户登录参数")
public class LoginDto {

    @NotBlank
    @Size(max = 32)
    @ApiModelProperty("用户名")
    private String username;

    @NotBlank
    @Size(max = 32)
    @ApiModelProperty("密码")
    private String password;

}
