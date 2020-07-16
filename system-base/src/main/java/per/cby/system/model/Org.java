package per.cby.system.model;

import java.util.List;

import javax.validation.constraints.Email;
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
 * 机构表 : 系统操作员的机构管理
 * </p>
 *
 * @author chenboyang
 * @since 2018-01-26
 */
@Data
@TableName("sys_org")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "Org对象", description = "机构")
public class Org extends BaseModel<Org> implements Treeable<Org> {

    private static final long serialVersionUID = 1L;

    @AutoId
    @TableField(fill = FieldFill.INSERT)
    @Size(max = 32)
    @ApiModelProperty("机构代码")
    private String orgId;

    @Size(max = 32)
    @ApiModelProperty("机构名称")
    private String orgName;

    @Size(max = 32)
    @ApiModelProperty("机构类型")
    private String orgType;

    @Size(max = 32)
    @ApiModelProperty("上级机构编号")
    @TableField(fill = FieldFill.INSERT)
    @DefaultValue(TreeableService.TOP_LEVEL)
    private String parentId;

    @ApiModelProperty("排序")
    private Integer sort;

    @Size(max = 32)
    @ApiModelProperty("所属地区编码")
    private String areaId;

    @Size(max = 64)
    @ApiModelProperty("所属地区名称")
    private String areaName;

    @Size(max = 32)
    @ApiModelProperty("联系人")
    private String person;

    @Size(max = 16)
    @ApiModelProperty("联系电话")
    private String tel;

    @Size(max = 256)
    @ApiModelProperty("地址")
    private String address;

    @Email
    @Size(max = 128)
    @ApiModelProperty("电子邮箱")
    private String email;

    @Size(max = 16)
    @ApiModelProperty("传真")
    private String fax;

    @Size(max = 256)
    @ApiModelProperty("描述")
    private String description;

    /** 是否拥有下级 */
    @TableField(exist = false)
    private boolean hasChild;

    /** 上级机构 */
    @TableField(exist = false)
    private Org parent;

    /** 下级机构 */
    @TableField(exist = false)
    private List<Org> children;

    @Override
    public String getKey() {
        return orgId;
    }

    @Override
    public String getLabel() {
        return orgName;
    }

    @Override
    public String getParentKey() {
        return parentId;
    }

}
