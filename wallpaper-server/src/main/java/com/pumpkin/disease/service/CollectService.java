package com.pumpkin.disease.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pumpkin.disease.entity.wallpaper.Collect;
import com.pumpkin.disease.entity.wallpaper.CollectVo;

/**
 * 收藏表(Collect)表服务接口
 *
 * @author icatw
 * @since 2022-10-16 01:57:07
 */
public interface CollectService extends IService<Collect> {
    /**
     * 分页多表查询收藏
     *
     * @param page   页面
     * @param userId 用户id
     * @return {@link IPage}<{@link Collect}>
     */
    IPage<CollectVo> pageList(Page<CollectVo> page, String userId);

    /**
     * 收藏
     *
     * @param imageId 图像id
     */
    void love(int imageId);
}

