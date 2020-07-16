package per.cby.system.service;

import java.util.List;
import java.util.Map;

import per.cby.frame.common.base.BaseService;
import per.cby.system.model.Dict;

/**
 * <p>
 * 字典表 : 用于存放各业务的类型字典 服务类
 * </p>
 *
 * @author chenboyang
 * @since 2018-01-26
 */
public interface DictService extends BaseService<Dict> {

    /**
     * 上移
     * 
     * @param dict 条件参数
     * @return 操作是否成功
     */
    boolean up(Dict dict);

    /**
     * 下移
     * 
     * @param dict 条件参数
     * @return 操作是否成功
     */
    boolean down(Dict dict);

    /**
     * 获取字典集合
     * 
     * @return 集合
     */
    Map<String, Map<String, String>> dictMap();

    /**
     * 根据父级代码获取字典
     * 
     * @param parentId 父级代码
     * @return 字典
     */
    List<Map<String, String>> getDict(String parentId);

    /**
     * 根据字典代码获取字典名称
     * 
     * @param code 字典代码
     * @return 字典名称
     */
    String dictName(String code);

    /**
     * 根据字典父级代码和字典代码获取字典名称
     * 
     * @param parentId 父级代码
     * @param code       字典代码
     * @return 字典名称
     */
    String dictName(String parentId, String code);

    /**
     * 根据父级代码获取字典
     * 
     * @param parentId 父级代码
     * @return 字典
     */
    List<?> dictList(String parentId);

    /**
     * 加载字典树状列表
     * 
     * @return 树状列表
     */
    List<Dict> tree();

}
