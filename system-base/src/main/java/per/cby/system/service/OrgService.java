package per.cby.system.service;

import java.util.List;

import per.cby.frame.common.base.BaseService;
import per.cby.system.model.Org;

/**
 * <p>
 * 机构表 : 系统操作员的机构管理 服务类
 * </p>
 *
 * @author chenboyang
 * @since 2018-01-26
 */
public interface OrgService extends BaseService<Org> {

    /**
     * 根据上级机构编码加载机构树状列表
     * 
     * @param parentId 上级机构编码
     * @return 树状列表
     */
    List<Org> tree(String parentId);

    /**
     * 上移
     * 
     * @param org 条件参数
     * @return 操作是否成功
     */
    boolean up(Org org);

    /**
     * 下移
     * 
     * @param org 条件参数
     * @return 操作是否成功
     */
    boolean down(Org org);

    /**
     * 向下递归查询机构
     * 
     * @param orgIds 机构编号
     * @return 机构列表
     */
    List<Org> downRecursive(String... orgIds);

    /**
     * 向上递归查询机构
     * 
     * @param orgIds 机构编号
     * @return 机构列表
     */
    List<Org> upRecursive(String... orgIds);

    /**
     * 获取机构权限
     * 
     * @param orgId 机构编号
     * @return 机构权限
     */
    String orgAuth(String orgId);

}
