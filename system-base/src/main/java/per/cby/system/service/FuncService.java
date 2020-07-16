package per.cby.system.service;

import java.util.Collection;
import java.util.List;

import per.cby.frame.common.base.BaseService;
import per.cby.system.dto.FuncDto;
import per.cby.system.model.Func;

/**
 * <p>
 * 功能表 服务类
 * </p>
 *
 * @author chenboyang
 * @since 2019-03-07
 */
public interface FuncService extends BaseService<Func> {

    /**
     * 上移
     * 
     * @param func 条件参数
     * @return 操作是否成功
     */
    boolean up(Func func);

    /**
     * 下移
     * 
     * @param func 条件参数
     * @return 操作是否成功
     */
    boolean down(Func func);

    /**
     * 根据条件获取功能列表
     * 
     * @param funcDto 功能参数
     * @return 功能列表
     */
    List<Func> list(FuncDto funcDto);

    /**
     * 根据条件获取功能树
     * 
     * @param funcDto 功能参数
     * @return 功能树
     */
    List<Func> tree(FuncDto funcDto);

    /**
     * 根据条件获取功能权限标识列表
     * 
     * @param roleIds 角色编号列表
     * @return 权限标识列表
     */
    List<String> authList(Collection<String> roleIds);

    /**
     * 根据条件获取功能菜单树
     * 
     * @param funcDto 功能参数
     * @return 菜单功能树
     */
    List<Func> menu(FuncDto funcDto);

}
