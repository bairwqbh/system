package per.cby.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import per.cby.frame.common.base.BaseService;
import per.cby.system.biz.AuthSwitcher;
import per.cby.system.biz.DictEnumApi;
import per.cby.system.model.Auth;

/**
 * <p>
 * 权限表 服务类
 * </p>
 *
 * @author chenboyang
 * @since 2019-03-07
 */
public interface AuthService extends BaseService<Auth> {

    /**
     * 获取对象的权限信息
     * 
     * @param objectId   对象编号
     * @param relateType 关系类型
     * @return 权限信息
     */
    default Auth getAuth(String objectId, DictEnumApi relateType) {
        return getAuth(objectId, relateType.getCode());
    }

    /**
     * 获取对象的权限信息
     * 
     * @param objectId   对象编号
     * @param relateType 关系类型
     * @return 权限信息
     */
    Auth getAuth(String objectId, String relateType);

    /**
     * 根据对象编号及关系类型切换权限
     * 
     * @param objectId     对象编号
     * @param relateType   关系类型
     * @param authSwitcher 权限切换器
     */
    default void switchAuth(String objectId, DictEnumApi relateType, AuthSwitcher authSwitcher) {
        switchAuth(objectId, relateType.getCode(), authSwitcher);
    }

    /**
     * 根据对象编号及关系类型切换权限
     * 
     * @param objectId     对象编号
     * @param relateType   关系类型
     * @param authSwitcher 权限切换器
     */
    void switchAuth(String objectId, String relateType, AuthSwitcher authSwitcher);

    /**
     * 根据对象编号及关系类型加载权限
     * 
     * @param objectId   对象编号
     * @param relateType 关系类型
     * @param wrapper    参数封装器
     * @param column     字段
     * @return 是否拥有权限
     */
    default <T> boolean loadAuth(String objectId, DictEnumApi relateType, LambdaQueryWrapper<T> wrapper,
            SFunction<T, ?> column) {
        return loadAuth(objectId, relateType.getCode(), wrapper, column);
    }

    /**
     * 根据对象编号及关系类型加载权限
     * 
     * @param objectId   对象编号
     * @param relateType 关系类型
     * @param wrapper    参数封装器
     * @param column     字段
     * @return 是否拥有权限
     */
    <T> boolean loadAuth(String objectId, String relateType, LambdaQueryWrapper<T> wrapper, SFunction<T, ?> column);

    /**
     * 根据对象编号判断对象是否被绑定
     * 
     * @param objectId 对象编号
     */
    boolean isBind(String objectId);

    /**
     * 根据对象编号移除对象相关所有权限
     * 
     * @param objectId 对象编号
     */
    void removeAuth(String objectId);

    /**
     * 根据对象编号和关系类型移除对象相关权限
     * 
     * @param objectId   对象编号
     * @param relateType 关系类型
     */
    default void removeAuth(String objectId, DictEnumApi relateType) {
        removeAuth(objectId, relateType.getCode());
    }

    /**
     * 根据对象编号和关系类型移除对象相关权限
     * 
     * @param objectId   对象编号
     * @param relateType 关系类型
     */
    void removeAuth(String objectId, String relateType);

    /**
     * 为对象授权
     * 
     * @param auth 授权信息
     */
    void authorize(Auth auth);

}
