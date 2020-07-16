package per.cby.system.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import per.cby.frame.common.base.BaseMapper;
import per.cby.system.model.AppVersion;

/**
 * <p>
 * 应用版本表 Mapper 接口
 * </p>
 *
 * @author chenboyang
 * @since 2018-03-21
 */
@Repository("__APP_VERSION_MAPPER__")
public interface AppVersionMapper extends BaseMapper<AppVersion> {

    /**
     * 生成应用新版本序列
     * 
     * @param appId 应用编号
     * @return 版本序列
     */
    @Select("SELECT CASE WHEN MAX(version_serial) IS NOT NULL THEN MAX(version_serial) + 1 ELSE 1 END FROM sys_app_version WHERE app_id = #{appId}")
    Integer genSerial(@Param("appId") String appId);

}
