package com.pumpkin.disease.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pumpkin.disease.entity.Role;
import com.pumpkin.disease.request.security.role.RolePageRequest;
import com.pumpkin.disease.response.security.role.RoleBasicResponse;
import com.pumpkin.disease.response.security.role.RoleMenuResponse;
import com.pumpkin.disease.response.security.role.RoleResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色表信息 Mapper 接口
 * </p>
 *
 * @author Pumpkin
 * @since 2022-11-30 10:18:24
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

      /**
     * 查询角色列表
     *
     * @param rolePage 角色分页对象
     * @param request  请求入参
     * @return {@link RoleResponse} 角色信息
     */
    IPage<RoleResponse> listRoleByPage(Page<Role> rolePage, @Param("request") RolePageRequest request);

    /**
     * 获取指定角色详细信息
     * @param roleId 角色id
     * @return {@link RoleMenuResponse} 角色信息
     */
    RoleMenuResponse getRoleByRoleId(@Param("roleId") String roleId);

    /**
     * 查询角色基础信息列表
     * @return {@link RoleBasicResponse} 角色信息
     */
    List<RoleBasicResponse> listBasicRole();

    /**
     * 获取前端用户角色id
     *
     * @return {@link String}
     */
    String getFrontendUserRoleID();
}
