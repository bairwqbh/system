package per.cby.system.model;

import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import per.cby.frame.common.base.BaseModel;
import per.cby.frame.common.db.mybatis.annotation.GeometryField;
import per.cby.frame.common.tree.Treeable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 地区表
 * </p>
 *
 * @author chenboyang
 * @since 2018-01-26
 */
@Data
@TableName("sys_area")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "Area对象", description = "地区")
public class Area extends BaseModel<Area> implements Treeable<Area> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("地区编码")
    private String areaId;

    @ApiModelProperty("地区名称")
    private String areaName;

    @ApiModelProperty("上级地区编码")
    private String parentId;

    @ApiModelProperty("地区等级")
    private Integer level;

    @ApiModelProperty("经度")
    private Double x;

    @ApiModelProperty("纬度")
    private Double y;

    @GeometryField
    @ApiModelProperty("空间几何数据")
    private String shape;

    @TableField(exist = false)
    @ApiModelProperty("是否拥有下级")
    private boolean hasChild;

    /** 上级地区 */
    @TableField(exist = false)
    private Area parent;

    /** 下级地区 */
    @TableField(exist = false)
    private List<Area> children;

    @Override
    public String getKey() {
        return areaId;
    }

    @Override
    public String getLabel() {
        return areaName;
    }

    @Override
    public String getParentKey() {
        return parentId;
    }

}
