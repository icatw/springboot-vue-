package com.pumpkin.disease.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pumpkin.disease.base.request.IDRequest;
import com.pumpkin.disease.base.result.PageResult;
import com.pumpkin.disease.entity.wallpaper.Image;
import com.pumpkin.disease.entity.wallpaper.ImageVo;
import com.pumpkin.disease.request.security.wallpaper.ImageConditionRequest;

import javax.servlet.http.HttpServletResponse;

/**
 * 壁纸表(Image)表服务接口
 *
 * @author icatw
 * @since 2022-10-14 19:58:55
 */
public interface ImageService extends IService<Image> {

    /**
     * 查询图像列表
     *
     * @param imageConditionRequest 图像条件请求
     * @return {@link PageResult}<{@link Image}>
     */
    PageResult<Image> queryImageList(ImageConditionRequest imageConditionRequest);

    /**
     * 保存图像
     *
     * @param image 形象
     */
    void saveImage(Image image);

    /**
     * 更新图像
     *
     * @param image 形象
     */
    void updateImage(Image image);

    /**
     * 批量删除图像
     *
     * @param idRequest id请求
     */
    void batchDeleteImage(IDRequest<String> idRequest);

    /**
     * 批量恢复壁纸
     *
     * @param idRequest id请求
     */
    void batchRecoveryImage(IDRequest<String> idRequest);

    /**
     * 下载
     *
     * @param response 回答
     * @param id       id
     */
    void download(HttpServletResponse response, String id);

    /**
     * 页面列表
     *
     * @param request 要求
     * @return {@link Page}<{@link ImageVo}>
     */
    Page<ImageVo> pageList(ImageConditionRequest request);

    /**
     * 定时上传
     */
    void timedUpload();
}

