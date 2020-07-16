package per.cby.system.service;

import java.util.List;

import per.cby.frame.common.base.BaseService;
import per.cby.frame.common.useable.UseableService;
import per.cby.system.model.Org;
import per.cby.system.model.User;

/**
 * <p>
 * 用户表 : 系统管理用户表 服务类
 * </p>
 *
 * @author chenboyang
 * @since 2018-01-26
 */
public interface UserService extends BaseService<User>, UseableService {

    /**
     * 根据用户编号获取用户信息
     * 
     * @param userId 用户编号
     * @return 用户信息
     */
    User getUser(String userId);

    /**
     * 根据用户编号获取组织机构
     * 
     * @param userId 用户编号
     * @return 组织机构
     */
    Org getOrg(String userId);

    /**
     * 根据用户编号获取用户角色标识
     * 
     * @param userId 用户编号
     * @return 角色标识
     */
    List<String> getRoles(String userId);

    /**
     * 根据用户编号获取用户权限标识
     * 
     * @param userId 用户编号
     * @return 权限标识
     */
    List<String> getAuths(String userId);

    /**
     * 根据用户编号获取接口标识
     * 
     * @param userId 用户编号
     * @return 接口列表
     */
    List<String> getApis(String userId);

    /**
     * 重置用户密码
     * 
     * @param user 用户
     * @return 操作记录数
     */
    boolean resetPwd(User user);

}
