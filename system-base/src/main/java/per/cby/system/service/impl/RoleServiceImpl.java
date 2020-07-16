package per.cby.system.service.impl;

import java.io.Serializable;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import per.cby.frame.common.base.AbstractService;
import per.cby.frame.common.exception.BusinessAssert;
import per.cby.system.constant.SystemConstants;
import per.cby.system.constant.SystemDict.RelateType;
import per.cby.system.mapper.RoleMapper;
import per.cby.system.model.Role;
import per.cby.system.service.AuthService;
import per.cby.system.service.RoleService;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author chenboyang
 * @since 2018-01-26
 */
@Service("__ROLE_SERVICE__")
public class RoleServiceImpl extends AbstractService<RoleMapper, Role> implements RoleService {

    @Autowired
    private AuthService authService;

    @Override
    protected Wrapper<Role> queryWrapper(Map<String, Object> param) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<Role>();
        if (param.get("userId") != null) {
            authService.loadAuth(String.valueOf(param.get("userId")), RelateType.USER_ROLE, wrapper, Role::getRoleId);
        }
        wrapper.eq(param.get("enable") != null, Role::getEnable, param.get("enable")).and(param.get("keyword") != null,
                w -> w.like(Role::getRoleId, param.get("keyword")).or().like(Role::getRoleName, param.get("keyword")))
                .orderByDesc(Role::getId);
        return wrapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(Role record) {
        BusinessAssert.notNull(record, "传递的角色数据为空，无法新增！");
        valiExist(Role::getRoleId, record.getRoleId(), "角色编号已存在！");
        return record.insert();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Serializable id) {
        Role role = getById(id);
        BusinessAssert.notNull(role, "角色不存在，无法删除！");
        BusinessAssert.isTrue(!SystemConstants.ADMIN.equals(role.getRoleId()), "超级管理员角色不能删除！");
        authService.removeAuth(role.getRoleId());
        return role.deleteById();
    }

}
