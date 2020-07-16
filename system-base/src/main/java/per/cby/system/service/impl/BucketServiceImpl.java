package per.cby.system.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import per.cby.frame.common.base.AbstractService;
import per.cby.frame.common.exception.BusinessAssert;
import per.cby.frame.common.file.storage.bucket.BucketManager;
import per.cby.frame.common.util.SpringContextUtil;
import per.cby.system.mapper.AttachMapper;
import per.cby.system.mapper.BucketMapper;
import per.cby.system.model.Attach;
import per.cby.system.model.Bucket;
import per.cby.system.service.BucketService;

/**
 * <p>
 * 附件存储模块表 : 用于存放系统各业务的附件信息 服务实现类
 * </p>
 *
 * @author chenboyang
 * @since 2018-01-26
 */
@Service
public class BucketServiceImpl extends AbstractService<BucketMapper, Bucket> implements BucketService {

    /**
     * 附件信息Mapper接口
     */
    @Autowired
    private AttachMapper attachMapper;

    @Override
    protected Wrapper<Bucket> queryWrapper(Map<String, Object> param) {
        return new LambdaQueryWrapper<Bucket>().and(param.get("keyword") != null,
                w -> w.like(Bucket::getName, param.get("keyword")).or().like(Bucket::getBucket, param.get("keyword")))
                .orderByDesc(Bucket::getId);
    }

    @Override
    protected List<Bucket> valueQueryResult(List<Bucket> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(bucket -> {
                BucketManager bucketManager = bucketManager(bucket.getStorage());
                if (bucketManager != null) {
                    bucket.setCount(bucketManager.count(bucket.getBucket()));
                }
            });
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(Bucket record) {
        boolean result = false;
        if (record != null) {
            BusinessAssert.isTrue(
                    count(new LambdaQueryWrapper<Bucket>().eq(Bucket::getBucket, record.getBucket())) <= 0, "存储板块已存在！");
            result = record.insert();
            BucketManager bucketManager = bucketManager(record.getStorage());
            if (result && bucketManager != null) {
                bucketManager.create(record.getBucket());
            }
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Serializable id) {
        boolean result = false;
        Bucket bucket = getById(id);
        result = super.removeById(id);
        BucketManager bucketManager = bucketManager(bucket.getStorage());
        if (bucket != null && result && StringUtils.isNotBlank(bucket.getBucket()) && bucketManager != null) {
            attachMapper.delete(new LambdaQueryWrapper<Attach>().eq(Attach::getBucket, bucket.getBucket()));
            bucketManager.delete(bucket.getBucket());
        }
        return result;
    }

    /**
     * 获取板块管理
     * 
     * @param storage 存储类型
     * @return 板块管理
     */
    private BucketManager bucketManager(String storage) {
        return SpringContextUtil.getBean(storage + "_bucket");
    }

}
