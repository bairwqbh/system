package per.cby.system.model;

import java.util.List;

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
 * 功能表
 * </p>
 *
 * @author chenboyang
 * @since 2019-03-07
 */
@Data
@TableName("sys_func")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "Func对象", description = "功能表")
public class Func extends BaseModel<Func> implements Treeable<Func> {

    private static final long serialVersionUID = 1L;

    @AutoId
    @TableField(fill = FieldFill.INSERT)
    @Size(max = 32)
    @ApiModelProperty("功能编号")
    private String funcId;

    @Size(max = 32)
    @ApiModelProperty("功能名称")
    private String funcName;

    @Size(max = 32)
    @ApiModelProperty("功能类型")
    private String funcType;

    @Size(max = 32)
    @ApiModelProperty("上级编码")
    @TableField(fill = FieldFill.INSERT)
    @DefaultValue(TreeableService.TOP_LEVEL)
    private String parentId;

    @Size(max = 32)
    @ApiModelProperty("系统编号")
    private String sysId;

    @Size(max = 32)
    @ApiModelProperty("图标")
    private String icon;

    @Size(max = 64)
    @ApiModelProperty("链接路径")
    private String path;

    @Size(max = 32)
    @ApiModelProperty("认证标识")
    private String auth;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("是否显示")
    private Boolean display;

    /** 是否有子集 */
    @TableField(exist = false)
    private boolean hasChild;

    /** 上级菜单 */
    @TableField(exist = false)
    private Func parent;

    /** 子集菜单 */
    @TableField(exist = false)
    private List<Func> children;

    @Override
    public String getKey() {
        return funcId;
    }

    @Override
    public String getLabel() {
        return funcName;
    }

    @Override
    public String getParentKey() {
        return parentId;
    }

}
