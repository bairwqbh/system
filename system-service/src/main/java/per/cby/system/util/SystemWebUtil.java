package per.cby.system.util;

import per.cby.frame.common.util.SpringContextUtil;
import per.cby.system.constant.SystemConstants;
import per.cby.system.constant.SystemDict.OrgType;
import per.cby.system.model.Org;
import per.cby.system.model.User;
import per.cby.system.service.SystemService;

import lombok.experimental.UtilityClass;

/**
 * 系统Web业务帮助类
 * 
 * @author chenboyang
 * 
 */

@UtilityClass
public class SystemWebUtil {

    /**
     * 获取系统业务服务
     * 
     * @return 系统业务服务
     */
    public SystemService systemService() {
        return SpringContextUtil.getBean(SystemService.class);
    }

    /**
     * 当前用户是否为超级管理员
     * 
     * @return 判断结果
     */
    public boolean isAdmin() {
        return isAdmin(systemService().currUser());
    }

    /**
     * 用户是否为超级管理员
     * 
     * @param user 用户
     * @return 判断结果
     */
    public boolean isAdmin(User user) {
        return user != null && SystemConstants.ADMIN.equals(user.getUserId());
    }

    /**
     * 当前用户是否为超级管理员
     * 
     * @return 判断结果
     */
    public boolean isNotAdmin() {
        return isNotAdmin(systemService().currUser());
    }

    /**
     * 用户是否为超级管理员
     * 
     * @param user 用户
     * @return 判断结果
     */
    public boolean isNotAdmin(User user) {
        return user != null && !SystemConstants.ADMIN.equals(user.getUserId());
    }

    /**
     * 获取当前非平台机构的机构编号
     * 
     * @return 机构编号
     */
    public String getNotPlatformOrgId() {
        Org org = systemService().currOrg();
        if (org != null && !OrgType.PLATFORM.getCode().equals(org.getOrgType())) {
            return org.getOrgId();
        }
        return null;
    }

}
