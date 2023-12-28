package com.pumpkin.disease.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pumpkin.disease.entity.wallpaper.Type;
import com.pumpkin.disease.request.security.wallpaper.TypeConditionRequest;

import java.util.List;

/**
 * 壁纸分类表(Type)表数据库访问层
 *
 * @author icatw
 * @since 2022-10-14 19:58:56
 */
public interface TypeMapper extends BaseMapper<Type> {
    /**
     * 列出基本类型
     *
     * @return {@link List}<{@link Type}>
     */
    List<Type> listBasicType();

    /**
     * 查询类型列表
     *
     * @param typePage 对象页
     * @param request  要求
     * @return {@link IPage}<{@link Type}>
     */
    IPage<Type> queryTypeList(Page<Type> typePage, TypeConditionRequest request);
}

