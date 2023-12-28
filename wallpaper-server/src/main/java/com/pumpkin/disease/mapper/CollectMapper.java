package com.pumpkin.disease.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pumpkin.disease.entity.wallpaper.Collect;
import com.pumpkin.disease.entity.wallpaper.CollectVo;
import org.apache.ibatis.annotations.Param;

/**
 * 收藏表(Collect)表数据库访问层
 *
 * @author icatw
 * @since 2022-10-16 01:57:07
 */
public interface CollectMapper extends BaseMapper<Collect> {
    /**
     * 分页查询用户收藏壁纸
     *
     * @param page   页面
     * @param userId 用户id
     * @return {@link IPage}<{@link CollectVo}>
     */
    IPage<CollectVo> pageListByUserId(@Param("page") Page<CollectVo> page, @Param("userId") String userId);
}

