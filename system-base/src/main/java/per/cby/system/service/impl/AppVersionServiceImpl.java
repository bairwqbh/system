package per.cby.system.service.impl;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import per.cby.frame.common.base.AbstractService;
import per.cby.frame.common.file.storage.StorageType;
import per.cby.frame.common.util.JudgeUtil;
import per.cby.system.mapper.AppVersionMapper;
import per.cby.system.model.AppVersion;
import per.cby.system.model.Attach;
import per.cby.system.service.AppVersionService;
import per.cby.system.service.AttachService;

/**
 * <p>
 * 应用版本表 服务实现类
 * </p>
 *
 * @author chenboyang
 * @since 2018-03-21
 */
@Service
public class AppVersionServiceImpl extends AbstractService<AppVersionMapper, AppVersion> implements AppVersionService {

    @Autowired
    private AttachService attachService;

    @Override
    protected Wrapper<AppVersion> queryWrapper(Map<String, Object> param) {
        return new LambdaQueryWrapper<AppVersion>()
                .eq(param.get("appId") != null, AppVersion::getAppId,
                        param.get("appId"))
                .and(param.get("keyword") != null, w -> w.like(AppVersion::getVersionNo, param.get("keyword")).or()
                        .like(AppVersion::getContent, param.get("keyword")))
                .orderByDesc(AppVersion::getId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(AppVersion appVersion, File file) {
        if (JudgeUtil.isOneNull(appVersion, file)) {
            return false;
        }
        return save(appVersion.setAttach(Attach.createAttath(file)));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(AppVersion appVersion) {
        boolean result = false;
        appVersion.setVersionSerial(baseMapper.genSerial(appVersion.getAppId()));
        appVersion.setPublishTime(LocalDateTime.now());
        result = super.save(appVersion);
        if (result && appVersion.getAttach() != null) {
            appVersion.getAttach().setStorage(StorageType.MONGO).setBucket(STORAGE_BUCKET).setBucketName(BUCKET_NAME)
                    .setDomainId(DOMAIN).setRowId(appVersion.getId().toString()).setFieldId(APK_FIELD);
            attachService.save(appVersion.getAttach());
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Serializable id) {
        boolean result = false;
        AppVersion appVersion = getById(id);
        result = super.removeById(id);
        if (result && appVersion != null) {
            Attach attach = attachService.getOne(new LambdaQueryWrapper<Attach>().eq(Attach::getBucket, STORAGE_BUCKET)
                    .eq(Attach::getDomainId, DOMAIN).eq(Attach::getRowId, appVersion.getId())
                    .eq(Attach::getFieldId, APK_FIELD));
            if (attach != null) {
                attachService.removeById(attach.getId());
            }
        }
        return result;
    }

    @Override
    public AppVersion currVersion(String appId) {
        Wrapper<AppVersion> wrapper = new LambdaQueryWrapper<AppVersion>().eq(AppVersion::getAppId, appId)
                .orderByDesc(AppVersion::getVersionSerial);
        AppVersion appVersion = getOne(wrapper);
        Attach attach = attachService.getOne(
                new LambdaQueryWrapper<Attach>().eq(Attach::getBucket, STORAGE_BUCKET).eq(Attach::getDomainId, DOMAIN)
                        .eq(Attach::getRowId, appVersion.getId()).eq(Attach::getFieldId, APK_FIELD));
        appVersion.setAttach(attach);
        return appVersion;
    }

}
