package com.pumpkin.disease.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pumpkin.disease.entity.Menu;
import com.pumpkin.disease.response.security.menu.MenuDetailResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author Pumpkin
 * @since 2022-11-30 10:18:24
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 查询指定菜单信息
     *
     * @param menuId 菜单id
     * @return {@link  MenuDetailResponse} 菜单
     */
    MenuDetailResponse getMenuById(@Param("menuId") Long menuId);

    /**
     * 查询菜单id集合
     * @return 菜单id集合
     */
    Set<Long> listMenuContentIds();


    /**
     * 查询指定菜单列表的权限信息
     * @param idList id集合
     * @return 权限列表
     */
    Set<String> listPermissionByMenuId(@Param("idList") Set<Long> idList);
}
