package com.pumpkin.disease.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pumpkin.disease.entity.wallpaper.Image;
import com.pumpkin.disease.entity.wallpaper.ImageVo;
import com.pumpkin.disease.request.security.wallpaper.ImageConditionRequest;
import org.apache.ibatis.annotations.Param;

/**
 * 图像映射器
 * 壁纸表(Image)表数据库访问层
 *
 * @author icatw
 * @date 2023/11/17
 * @since 2022-10-14 19:58:55
 */
public interface ImageMapper extends BaseMapper<Image> {

    /**
     * 查询图像列表
     *
     * @param imagePage 图像页面
     * @param request   要求
     * @return {@link IPage}<{@link Image}>
     */
    IPage<Image> queryImageList(Page<Image> imagePage, ImageConditionRequest request);

    /**
     * 按id恢复
     *
     * @param id id
     */
    void recoveryById(String id);

    /**
     * 查询页面vo列表
     *
     * @param page    页
     * @param request 要求
     * @param userId  用户id
     * @return {@link Page}<{@link ImageVo}>
     */
    Page<ImageVo> queryPageVoList(Page<ImageVo> page, @Param("request") ImageConditionRequest request);
}

