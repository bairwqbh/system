package per.cby.system.dto;

import java.util.Collection;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 功能参数
 * </p>
 *
 * @author chenboyang
 * @since 2019-06-19
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "FuncDto对象", description = "功能参数")
public class FuncDto {

    @ApiModelProperty("系统编号")
    private String sysId;

    @ApiModelProperty("角色编号列表")
    private Collection<String> roleIds;

}
