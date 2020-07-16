package per.cby.system.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import per.cby.frame.common.base.AbstractService;
import per.cby.frame.common.exception.BusinessAssert;
import per.cby.frame.common.util.JudgeUtil;
import per.cby.system.biz.AuthSwitcher;
import per.cby.system.constant.SystemDict.AuthMode;
import per.cby.system.mapper.AuthMapper;
import per.cby.system.model.Auth;
import per.cby.system.model.Relate;
import per.cby.system.service.AuthService;
import per.cby.system.service.RelateService;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author chenboyang
 * @since 2019-03-07
 */
@Service
public class AuthServiceImpl extends AbstractService<AuthMapper, Auth> implements AuthService {

    @Autowired
    private RelateService relateService;

    @Override
    protected com.baomidou.mybatisplus.core.conditions.Wrapper<Auth> queryWrapper(Map<String, Object> param) {
        return new LambdaQueryWrapper<Auth>().orderByDesc(Auth::getId);
    }

    @Override
    public Auth getAuth(String objectId, String relateType) {
        Auth auth = getOne(
                new LambdaQueryWrapper<Auth>().eq(Auth::getRelateType, relateType).eq(Auth::getObjectId, objectId));
        if (auth != null
                && JudgeUtil.isAllNotEqual(auth.getAuthMode(), AuthMode.ALL.getCode(), AuthMode.NONE.getCode())) {
            auth.setTargetList(getTargetList(auth.getObjectId(), auth.getRelateType()));
        }
        return auth;
    }

    @Override
    public void switchAuth(String objectId, String relateType, AuthSwitcher authSwitcher) {
        Optional.ofNullable(getAuth(objectId, relateType)).ifPresent(auth -> {
            if (AuthMode.ALL.getCode().equals(auth.getAuthMode())) {
                authSwitcher.all();
            } else if (AuthMode.NONE.getCode().equals(auth.getAuthMode())) {
                authSwitcher.none();
            } else if (AuthMode.IN.getCode().equals(auth.getAuthMode())) {
                authSwitcher.in(getTargetList(objectId, relateType));
            } else if (AuthMode.EXC.getCode().equals(auth.getAuthMode())) {
                authSwitcher.exc(getTargetList(objectId, relateType));
            }
        });
    }

    @Override
    public <T> boolean loadAuth(String objectId, String relateType, LambdaQueryWrapper<T> wrapper,
            SFunction<T, ?> column) {
        AtomicBoolean isAuth = new AtomicBoolean();
        switchAuth(objectId, relateType, new AuthSwitcher() {
            @Override
            public void none() {
                isAuth.set(false);
                wrapper.eq(column, AuthMode.NONE.getCode());
            }

            @Override
            public void in(List<String> targetList) {
                isAuth.set(true);
                wrapper.in(column, getTargetList(objectId, relateType));
            }

            @Override
            public void exc(List<String> targetList) {
                isAuth.set(true);
                wrapper.notIn(column, getTargetList(objectId, relateType));
            }

            @Override
            public void all() {
                isAuth.set(true);
            }
        });
        return isAuth.get();
    }

    @Override
    public boolean isBind(String objectId) {
        return retBool(relateService.count(new LambdaQueryWrapper<Relate>().eq(Relate::getTargetId, objectId)));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeAuth(String objectId) {
        remove(new LambdaQueryWrapper<Auth>().eq(Auth::getObjectId, objectId));
        relateService.remove(new LambdaQueryWrapper<Relate>().eq(Relate::getObjectId, objectId).or()
                .eq(Relate::getTargetId, objectId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeAuth(String objectId, String relateType) {
        remove(new LambdaQueryWrapper<Auth>().eq(Auth::getObjectId, objectId).eq(Auth::getRelateType, relateType));
        relateService.remove(new LambdaQueryWrapper<Relate>().eq(Relate::getObjectId, objectId)
                .eq(Relate::getRelateType, relateType));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void authorize(Auth auth) {
        BusinessAssert.notNull(auth, "权限信息为空！");
        if (auth.getId() != null) {
            auth.setId(null);
        }
        removeAuth(auth.getObjectId(), auth.getRelateType());
        auth.insert();
        if (CollectionUtils.isNotEmpty(auth.getTargetList())
                && JudgeUtil.isAllNotEqual(auth.getAuthMode(), AuthMode.ALL.getCode(), AuthMode.NONE.getCode())) {
            List<Relate> relateList = auth.getTargetList().stream().map(targetId -> new Relate()
                    .setObjectId(auth.getObjectId()).setTargetId(targetId).setRelateType(auth.getRelateType()))
                    .collect(Collectors.toList());
            relateService.saveBatch(relateList);
        }
    }

    /**
     * 根据对象的权限信息
     * 
     * @param objectId   对象编号
     * @param relateType 关系类型
     * @return 目标编号列表
     */
    private List<String> getTargetList(String objectId, String relateType) {
        return relateService.getTargetList(objectId, relateType);
    }

}
