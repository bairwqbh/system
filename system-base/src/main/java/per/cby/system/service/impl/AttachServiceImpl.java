package per.cby.system.service.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import per.cby.frame.common.base.AbstractService;
import per.cby.frame.common.file.storage.storage.FileStorage;
import per.cby.frame.common.util.IDUtil;
import per.cby.frame.common.util.SpringContextUtil;
import per.cby.frame.common.util.StringUtil;
import per.cby.system.mapper.AttachMapper;
import per.cby.system.model.Attach;
import per.cby.system.service.AttachService;

/**
 * <p>
 * 附件表 : 用于存放系统各业务的附件信息 服务实现类
 * </p>
 *
 * @author chenboyang
 * @since 2018-01-26
 */
@Service
public class AttachServiceImpl extends AbstractService<AttachMapper, Attach> implements AttachService {

    @Override
    protected Wrapper<Attach> queryWrapper(Map<String, Object> param) {
        return new LambdaQueryWrapper<Attach>()
                .eq(param.get("domainId") != null, Attach::getDomainId, param.get("domainId"))
                .eq(param.get("targetId") != null, Attach::getRowId, param.get("targetId"))
                .eq(param.get("fieldId") != null, Attach::getFieldId, param.get("fieldId"))
                .eq(param.get("bucket") != null, Attach::getBucket, param.get("bucket"))
                .inSql(param.get("ids") != null, Attach::getId, (String) param.get("ids"))
                .inSql(param.get("targetIds") != null, Attach::getRowId, (String) param.get("targetIds"))
                .and(param.get("keyword") != null, w -> w.like(Attach::getOriginalName, param.get("keyword")).or()
                        .like(Attach::getBucket, param.get("keyword")))
                .orderByDesc(Attach::getId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(Attach record) {
        boolean result = false;
        if (record != null) {
            FileStorage fileStorage = fileStorage(record.getStorage());
            if (StringUtils.isBlank(record.getType()) && StringUtils.isNotBlank(record.getOriginalName())) {
                record.setType(StringUtil.getFileNameSuffix(record.getOriginalName()));
            }
            if (StringUtils.isBlank(record.getName())) {
                StringBuilder sb = new StringBuilder(IDUtil.createUUID32());
                if (StringUtils.isNotBlank(record.getType())) {
                    if (!record.getType().startsWith(".")) {
                        sb.append(".");
                    }
                    sb.append(record.getType());
                }
                record.setName(sb.toString());
            }
            if (fileStorage != null && record.getInput() != null) {
                if (StringUtils.isNotBlank(record.getBucket())) {
                    record.setPresignedUrl(
                            fileStorage.save(record.getBucket(), record.getName(), record.getInput()));
                } else {
                    record.setPresignedUrl(fileStorage.save(record.getName(), record.getInput()));
                }
            }
            result = record.insert();
            if (!result && fileStorage != null) {
                if (StringUtils.isNotBlank(record.getBucket())) {
                    fileStorage.remove(record.getBucket(), record.getName());
                } else {
                    fileStorage.remove(record.getName());
                }
            }
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(Attach record) {
        boolean result = false;
        if (record != null) {
            FileStorage fileStorage = fileStorage(record.getStorage());
            if (record.getInput() != null && fileStorage != null) {
                if (StringUtils.isNotBlank(record.getName())) {
                    if (StringUtils.isNotBlank(record.getBucket())) {
                        fileStorage.remove(record.getBucket(), record.getName());
                    } else {
                        fileStorage.remove(record.getName());
                    }
                } else {
                    StringBuilder sb = new StringBuilder(IDUtil.createUUID32());
                    if (StringUtils.isNotBlank(record.getType())) {
                        if (!record.getType().startsWith(".")) {
                            sb.append(".");
                        }
                        sb.append(record.getType());
                    }
                    record.setName(sb.toString());
                }
                if (StringUtils.isNotBlank(record.getBucket())) {
                    record.setPresignedUrl(
                            fileStorage.save(record.getBucket(), record.getName(), record.getInput()));
                } else {
                    record.setPresignedUrl(fileStorage.save(record.getName(), record.getInput()));
                }
            }
            result = record.updateById();
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveBatch(Collection<Attach> attachList) {
        boolean result = false;
        if (CollectionUtils.isNotEmpty(attachList)) {
            attachList.stream().filter(attach -> attach.getInput() != null).forEach(attach -> {
                FileStorage fileStorage = fileStorage(attach.getStorage());
                if (fileStorage != null) {
                    if (StringUtils.isBlank(attach.getName())) {
                        StringBuilder sb = new StringBuilder(IDUtil.createUUID32());
                        if (StringUtils.isNotBlank(attach.getType())) {
                            if (!attach.getType().startsWith(".")) {
                                sb.append(".");
                            }
                            sb.append(attach.getType());
                        }
                        attach.setName(sb.toString());
                    }
                    if (StringUtils.isNotBlank(attach.getBucket())) {
                        attach.setPresignedUrl(
                                fileStorage.save(attach.getBucket(), attach.getName(), attach.getInput()));
                    } else {
                        attach.setPresignedUrl(fileStorage.save(attach.getName(), attach.getInput()));
                    }
                }
            });
            result = super.saveBatch(attachList);
            if (!result) {
                attachList.forEach(attach -> {
                    FileStorage fileStorage = fileStorage(attach.getStorage());
                    if (StringUtils.isNotBlank(attach.getBucket()) && fileStorage != null) {
                        fileStorage.remove(attach.getBucket(), attach.getName());
                    } else {
                        fileStorage.remove(attach.getName());
                    }
                });
            }
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateBatchById(Collection<Attach> attachList) {
        boolean result = false;
        if (CollectionUtils.isNotEmpty(attachList)) {
            attachList.stream().filter(attach -> attach.getInput() != null).forEach(attach -> {
                FileStorage fileStorage = fileStorage(attach.getStorage());
                if (fileStorage != null) {
                    if (StringUtils.isNotBlank(attach.getName())) {
                        if (StringUtils.isNotBlank(attach.getBucket())) {
                            fileStorage.remove(attach.getBucket(), attach.getName());
                        } else {
                            fileStorage.remove(attach.getName());
                        }
                    } else {
                        StringBuilder sb = new StringBuilder(IDUtil.createUUID32());
                        if (StringUtils.isNotBlank(attach.getType())) {
                            if (!attach.getType().startsWith(".")) {
                                sb.append(".");
                            }
                            sb.append(attach.getType());
                        }
                        attach.setName(sb.toString());
                    }
                    if (StringUtils.isNotBlank(attach.getBucket())) {
                        attach.setPresignedUrl(
                                fileStorage.save(attach.getBucket(), attach.getName(), attach.getInput()));
                    } else {
                        attach.setPresignedUrl(fileStorage.save(attach.getName(), attach.getInput()));
                    }
                }
            });
            result = super.updateBatchById(attachList);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Serializable id) {
        boolean result = false;
        Attach attach = getById(id);
        if (attach == null) {
            return result;
        }
        result = attach.deleteById();
        FileStorage fileStorage = fileStorage(attach.getStorage());
        if (result && fileStorage != null) {
            if (StringUtils.isNotBlank(attach.getBucket())) {
                fileStorage.remove(attach.getBucket(), attach.getName());
            } else {
                fileStorage.remove(attach.getName());
            }
        }
        return result;
    }

    @Override
    public boolean remove(Map<String, Object> param) {
        boolean result = false;
        List<Attach> attachList = null;
        LambdaQueryWrapper<Attach> wrapper = (LambdaQueryWrapper<Attach>) queryWrapper(param);
        if (MapUtils.isNotEmpty(wrapper.getParamNameValuePairs())) {
            attachList = list(wrapper);
            result = remove(wrapper);
        }
        if (result && CollectionUtils.isNotEmpty(attachList)) {
            attachList.forEach(attach -> {
                FileStorage fileStorage = fileStorage(attach.getStorage());
                if (StringUtils.isNotBlank(attach.getBucket()) && fileStorage != null) {
                    fileStorage.remove(attach.getBucket(), attach.getName());
                } else {
                    fileStorage.remove(attach.getName());
                }
            });
        }
        return result;
    }

    /**
     * 获取文件存储服务
     * 
     * @param storage 存储类型
     * @return 文件存储服务
     */
    private FileStorage fileStorage(String storage) {
        return SpringContextUtil.getBean(storage + "_storage");
    }

}
