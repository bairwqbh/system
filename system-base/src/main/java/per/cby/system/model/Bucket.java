package per.cby.system.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import per.cby.frame.common.base.BaseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 附件存储模块表 : 用于存放系统各业务的附件信息
 * </p>
 *
 * @author chenboyang
 * @since 2018-01-26
 */
@Data
@Accessors(chain = true)
@TableName("sys_bucket")
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "Bucket对象", description = "附件存储模块")
public class Bucket extends BaseModel<Bucket> {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @Size(max = 32)
    @ApiModelProperty("存储板块")
    private String bucket;

    @Size(max = 32)
    @ApiModelProperty("板块名称")
    private String name;

    @Size(max = 32)
    @ApiModelProperty("存储类型")
    private String storage;

    @Size(max = 256)
    @ApiModelProperty("描述")
    private String description;

    /** 附件数量 */
    @TableField(exist = false)
    private long count;

}
