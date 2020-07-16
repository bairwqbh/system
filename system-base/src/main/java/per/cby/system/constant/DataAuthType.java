package per.cby.system.constant;

import per.cby.system.constant.SystemConstants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 数据权限类型枚举
 * 
 * @author chenboyang
 *
 */
@Getter
@RequiredArgsConstructor
public enum DataAuthType {

    ORG(SystemConstants.ORG_AUTH, "机构权限"),
    AREA(SystemConstants.AREA_AUTH, "地区权限");

    private final String code;
    private final String desc;

}
