package per.cby.system.model;

import java.time.LocalDateTime;

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
 * 应用版本表
 * </p>
 *
 * @author chenboyang
 * @since 2018-03-21
 */
@Data
@Accessors(chain = true)
@TableName("sys_app_version")
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "AppVersion对象", description = "应用版本")
public class AppVersion extends BaseModel<AppVersion> {

    private static final long serialVersionUID = 1L;

    @Size(max = 32)
    @ApiModelProperty("应用编号")
    private String appId;

    @NotBlank
    @Size(max = 32)
    @ApiModelProperty("版本号")
    private String versionNo;

    @ApiModelProperty("版本序列")
    private Integer versionSerial;

    @Size(max = 4000)
    @ApiModelProperty("内容描述")
    private String content;

    @ApiModelProperty("是否强制更新")
    private Boolean enforce;

    @Size(max = 32)
    @ApiModelProperty("发布人")
    private String publisher;

    @ApiModelProperty("发布时间")
    private LocalDateTime publishTime;

    /** 版本附件信息 */
    @TableField(exist = false)
    private Attach attach;

}
