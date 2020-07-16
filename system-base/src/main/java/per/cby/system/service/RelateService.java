package per.cby.system.service;

import java.util.List;

import per.cby.frame.common.base.BaseService;
import per.cby.system.biz.DictEnumApi;
import per.cby.system.model.Relate;

/**
 * <p>
 * 关联关系表 服务类
 * </p>
 *
 * @author chenboyang
 * @since 2019-03-07
 */
public interface RelateService extends BaseService<Relate> {

    /**
     * 根据对象的权限信息
     * 
     * @param objectId   对象编号
     * @param relateType 关系类型
     * @return 目标编号列表
     */
    default List<String> getTargetList(String objectId, DictEnumApi relateType) {
        return getTargetList(objectId, relateType.getCode());
    }

    /**
     * 根据对象的权限信息
     * 
     * @param objectId   对象编号
     * @param relateType 关系类型
     * @return 目标编号列表
     */
    List<String> getTargetList(String objectId, String relateType);

}
