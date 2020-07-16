package per.cby.system.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 修改密码参数
 * 
 * @author chenboyang
 * @since 2019年7月23日
 *
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "ModPwdDto对象", description = "修改密码参数")
public class ModPwdDto {

    @NotBlank
    @Size(min = 6, max = 32)
    @ApiModelProperty("旧密码")
    private String oldPwd;

    @NotBlank
    @Size(min = 6, max = 32)
    @ApiModelProperty("新密码")
    private String newPwd;

}
