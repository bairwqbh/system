package per.cby.system.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import per.cby.frame.common.base.AbstractService;
import per.cby.frame.common.exception.BusinessAssert;
import per.cby.frame.common.useable.EncryptEnum;
import per.cby.frame.common.useable.Useable;
import per.cby.frame.common.util.BaseUtil;
import per.cby.frame.common.util.StringUtil;
import per.cby.system.biz.BusinessService;
import per.cby.system.constant.SystemConstants;
import per.cby.system.constant.SystemDict.AuthMode;
import per.cby.system.constant.SystemDict.AuthType;
import per.cby.system.constant.SystemDict.RelateType;
import per.cby.system.mapper.UserMapper;
import per.cby.system.model.Auth;
import per.cby.system.model.Org;
import per.cby.system.model.Relate;
import per.cby.system.model.Role;
import per.cby.system.model.User;
import per.cby.system.service.ApiService;
import per.cby.system.service.AuthService;
import per.cby.system.service.FuncService;
import per.cby.system.service.OrgService;
import per.cby.system.service.RelateService;
import per.cby.system.service.RoleService;
import per.cby.system.service.UserService;

/**
 * <p>
 * 用户表 : 系统管理用户表 服务实现类
 * </p>
 *
 * @author chenboyang
 * @since 2018-01-26
 */
@Service("__USER_SERVICE__")
public class UserServiceImpl extends AbstractService<UserMapper, User> implements UserService, SystemConstants {

    /**
     * 用户默认密码
     */
    private final String DEFAULT_PASSWORD = "123456";

    @Autowired
    private BusinessService businessService;

    @Autowired
    private AuthService authService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private FuncService funcService;

    @Autowired
    private ApiService apiService;

    @Autowired
    private RelateService relateService;

    @Autowired
    private OrgService orgService;

    @Override
    protected Wrapper<User> queryWrapper(Map<String, Object> param) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>();
        wrapper.and(StringUtil.isNotEmpty(param.get("keyword")),
                w -> w.like(User::getUserId, param.get("keyword")).or().like(User::getRealName, param.get("keyword")));
//        wrapper.inSql(param.get(ORG_AUTH) != null, User::getOrgId, String.valueOf(param.get(ORG_AUTH)));
//        wrapper.inSql(param.get(AREA_AUTH) != null, User::getAreaId, String.valueOf(param.get(AREA_AUTH)));
        wrapper.eq(param.get("userId") != null, User::getUserId, param.get("userId"));
        if (StringUtil.isNotEmpty(param.get("orgId"))) {
            List<String> orgIds = businessService.orgRecursive(param.get("orgId").toString());
            if (CollectionUtils.isNotEmpty(orgIds)) {
                wrapper.in(User::getOrgId, orgIds);
            }
        }
        wrapper.orderByDesc(User::getId);
        return wrapper;
    }

    @Override
    protected List<User> valueQueryResult(List<User> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            List<String> userIds = list.stream().map(User::getUserId).collect(Collectors.toList());
            Wrapper<Relate> wrapper = new LambdaQueryWrapper<Relate>().in(Relate::getObjectId, userIds)
                    .eq(Relate::getRelateType, RelateType.USER_ROLE.getCode());
            List<Relate> relateList = relateService.list(wrapper);
            if (CollectionUtils.isNotEmpty(relateList)) {
                Map<String, List<String>> map = relateList.stream().collect(Collectors.groupingBy(Relate::getObjectId,
                        Collectors.mapping(Relate::getTargetId, Collectors.toList())));
                list.forEach(user -> {
                    if (map.containsKey(user.getUserId())) {
                        List<String> roleIds = map.get(user.getUserId());
                        if (CollectionUtils.isNotEmpty(roleIds)) {
                            user.setRoleId(roleIds.get(0));
                        }
                    }
                });
            }
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(User record) {
        boolean result = false;
        BusinessAssert.notNull(record, "用户数据为空，无法新增！");
        valiExist(User::getUserId, record.getUserId(), "用户名已存在！");
        record.setPassword(BaseUtil.md5Encode(DEFAULT_PASSWORD));
        result = record.insert();
        if (result) {
            setUserRoleAuth(record);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(User user) {
        boolean result = super.updateById(user);
        if (result) {
            setUserRoleAuth(user);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Serializable id) {
        User user = getById(id);
        BusinessAssert.notNull(user, "用户不存在，无法删除！");
        BusinessAssert.isTrue(!SystemConstants.ADMIN.equals(user.getUserId()), "超级管理员不能删除！");
        authService.removeAuth(user.getUserId());
        return user.deleteById();
    }

    /**
     * 设置用户角色权限
     * 
     * @param user 用户
     */
    private void setUserRoleAuth(User user) {
        if (StringUtils.isNotBlank(user.getRoleId())) {
            authService.authorize(
                    new Auth().setAuthType(AuthType.FUNC.getCode()).setRelateType(RelateType.USER_ROLE.getCode())
                            .setAuthMode(AuthMode.IN.getCode()).setObjectId(user.getUserId())
                            .setObjectName(user.getRealName()).setTargetList(BaseUtil.newArrayList(user.getRoleId())));
        } else {
            authService.removeAuth(user.getUserId(), RelateType.USER_ROLE);
        }
    }

    @Override
    public User getUser(String userId) {
        return getOne(new LambdaQueryWrapper<User>().eq(User::getUserId, userId));
    }

    @Override
    public Org getOrg(String userId) {
        User user = getUser(userId);
        if (user != null && StringUtils.isNotBlank(user.getOrgId())) {
            return orgService.lambdaQuery().eq(Org::getOrgId, user.getOrgId()).one();
        }
        return null;
    }

    @Override
    public List<String> getRoles(String userId) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<Role>();
        boolean isRole = authService.loadAuth(userId, RelateType.USER_ROLE, wrapper, Role::getRoleId);
        if (!isRole) {
            return null;
        }
        List<Role> list = roleService.list(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.stream().filter(role -> StringUtils.isNotBlank(role.getRoleId())).map(Role::getRoleId).distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAuths(String userId) {
        return funcService.authList(getRoles(userId));
    }

    @Override
    public List<String> getApis(String userId) {
        return apiService.apiIdList(getAuths(userId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean resetPwd(User user) {
        BusinessAssert.notNull(user, "用户数据为空，无法重置密码！");
        user.setPassword(passwordEncode(DEFAULT_PASSWORD));
        return super.updateById(user);
    }

    @Override
    public Useable getAccount(String account) {
        return getUser(account);
    }

    @Override
    public EncryptEnum encrypt() {
        return PASSWORD_ENCRYPT;
    }

}
