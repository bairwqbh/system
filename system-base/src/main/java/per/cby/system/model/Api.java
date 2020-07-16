package per.cby.system.model;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import per.cby.frame.common.base.BaseModel;
import per.cby.frame.common.db.mybatis.annotation.AutoId;
import per.cby.frame.common.db.mybatis.annotation.DefaultValue;
import per.cby.frame.common.tree.Treeable;
import per.cby.frame.common.tree.TreeableService;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 接口表
 * </p>
 *
 * @author chenboyang
 * @since 2019-03-07
 */
@Data
@TableName("sys_api")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "Api对象", description = "接口表")
public class Api extends BaseModel<Api> implements Treeable<Api> {

    private static final long serialVersionUID = 1L;

    @AutoId
    @TableField(fill = FieldFill.INSERT)
    @Size(max = 32)
    @ApiModelProperty("接口编号")
    private String apiId;

    @NotBlank
    @Size(max = 32)
    @ApiModelProperty("接口名称")
    private String apiName;

    @Size(max = 32)
    @ApiModelProperty("接口类型")
    private String apiType;

    @Size(max = 256)
    @ApiModelProperty("接口地址")
    private String url;

    @Size(max = 32)
    @ApiModelProperty("上级编码")
    @TableField(fill = FieldFill.INSERT)
    @DefaultValue(TreeableService.TOP_LEVEL)
    private String parentId;

    @Size(max = 32)
    @ApiModelProperty("系统编号")
    private String sysId;

    @Size(max = 256)
    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("是否启用")
    private Boolean enable;

    /** 上级目录 */
    @TableField(exist = false)
    private Api parent;

    /** 子集接口 */
    @TableField(exist = false)
    private List<Api> children;

    @Override
    public String getKey() {
        return apiId;
    }

    @Override
    public String getLabel() {
        return apiName;
    }

    @Override
    public String getParentKey() {
        return parentId;
    }

}
