package per.cby.system.model;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import per.cby.frame.common.base.BaseModel;
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
 * 字典表 : 用于存放各业务的类型字典
 * </p>
 *
 * @author chenboyang
 * @since 2018-01-26
 */
@Data
@TableName("sys_dict")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "Dict对象", description = "字典")
public class Dict extends BaseModel<Dict> implements Treeable<Dict> {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @Size(max = 32)
    @ApiModelProperty("字典代码")
    private String code;

    @Size(max = 32)
    @ApiModelProperty("字典名称")
    private String name;

    @Size(max = 32)
    @ApiModelProperty("上级编码")
    @TableField(fill = FieldFill.INSERT)
    @DefaultValue(TreeableService.TOP_CODE)
    private String parentId;

    @ApiModelProperty("排序")
    private Integer sort;

    @Size(max = 256)
    @ApiModelProperty("描述")
    private String description;

    /** 是否拥有下级 */
    @TableField(exist = false)
    private boolean hasChild;

    /** 上级字典 */
    @TableField(exist = false)
    private Dict parent;

    /** 下级字典 */
    @TableField(exist = false)
    private List<Dict> children;

    @Override
    public String getKey() {
        return code;
    }

    @Override
    public String getLabel() {
        return name;
    }

    @Override
    public String getParentKey() {
        return parentId;
    }

}
