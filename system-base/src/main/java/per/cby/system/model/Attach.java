package per.cby.system.model;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.validation.constraints.Size;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import per.cby.frame.common.base.BaseModel;
import per.cby.frame.common.util.IDUtil;
import per.cby.frame.common.util.StringUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 附件表 : 用于存放系统各业务的附件信息
 * </p>
 *
 * @author chenboyang
 * @since 2018-01-26
 */
@Data
@Slf4j
@Accessors(chain = true)
@TableName("sys_attach")
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "Attach对象", description = "附件")
public class Attach extends BaseModel<Attach> {

    private static final long serialVersionUID = 1L;

    @Size(max = 32)
    @ApiModelProperty("模型标识")
    private String domainId;

    @Size(max = 32)
    @ApiModelProperty("所属目标编号")
    private String rowId;

    @Size(max = 32)
    @ApiModelProperty("字段标识")
    private String fieldId;

    @Size(max = 64)
    @ApiModelProperty("原名称")
    private String originalName;

    @Size(max = 64)
    @ApiModelProperty("真实名称")
    private String name;

    @Size(max = 32)
    @ApiModelProperty("附件类型")
    private String type;

    @ApiModelProperty("附件大小")
    private Long size;

    @Size(max = 32)
    @ApiModelProperty("存储类型")
    private String storage;

    @Size(max = 32)
    @ApiModelProperty("存储板块")
    private String bucket;

    @Size(max = 32)
    @ApiModelProperty("板块名称")
    private String bucketName;

    @Size(max = 256)
    @ApiModelProperty("预签名地址")
    private String presignedUrl;

    /** 文件输入流 */
    @JsonIgnore
    @TableField(exist = false)
    private InputStream input;

    /**
     * 根据文件创建附件信息
     * 
     * @param file 文件
     * @return 附件信息
     */
    public static Attach createAttath(File file) {
        Attach attach = null;
        try {
            attach = new Attach();
            attach.setOriginalName(file.getName());
            attach.setType(StringUtil.getFileNameSuffix(attach.getOriginalName()));
            attach.setSize(file.length());
            attach.setInput(FileUtils.openInputStream(file));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return attach;
    }

    /**
     * 根据Spring多部文件创建附件信息
     * 
     * @param file 文件
     * @return 附件信息
     */
    public static Attach createAttath(MultipartFile file) {
        Attach attach = null;
        try {
            attach = new Attach();
            attach.setOriginalName(file.getOriginalFilename());
            attach.setType(StringUtil.getFileNameSuffix(attach.getOriginalName()));
            if (StringUtils.isNotBlank(attach.getType())) {
                attach.setName(IDUtil.createUUID32() + "." + attach.getType());
            } else {
                attach.setName(IDUtil.createUUID32());
            }
            attach.setSize(file.getSize());
            attach.setInput(file.getInputStream());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return attach;
    }

}
