package com.pumpkin.disease.service.impl;

import cn.hutool.core.text.StrJoiner;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pumpkin.disease.base.exception.BaseException;
import com.pumpkin.disease.base.request.IDRequest;
import com.pumpkin.disease.base.result.PageResult;
import com.pumpkin.disease.entity.wallpaper.Image;
import com.pumpkin.disease.entity.wallpaper.ImageJson;
import com.pumpkin.disease.entity.wallpaper.ImageVo;
import com.pumpkin.disease.entity.website.OtherConfig;
import com.pumpkin.disease.enums.business.AuditStatusEnum;
import com.pumpkin.disease.enums.business.YesNoEnum;
import com.pumpkin.disease.enums.file.UploadModeEnum;
import com.pumpkin.disease.enums.website.WebsiteConfigEnum;
import com.pumpkin.disease.mapper.ImageMapper;
import com.pumpkin.disease.request.security.wallpaper.ImageConditionRequest;
import com.pumpkin.disease.service.ImageService;
import com.pumpkin.disease.service.WebsiteConfigService;
import com.pumpkin.disease.strategy.context.UploadStrategyContext;
import com.pumpkin.disease.utils.image.ImageUtil;
import com.pumpkin.disease.utils.redis.RedisCache;
import com.pumpkin.disease.utils.user.UserUtil;
import com.pumpkin.disease.utils.vaild.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Objects;

/**
 * 壁纸表(Image)表服务实现类
 *
 * @author icatw
 * @since 2022-10-14 19:58:55
 */
@Service("imageService")
@Slf4j
public class ImageServiceImpl extends ServiceImpl<ImageMapper, Image> implements ImageService {
    @Resource
    WebsiteConfigService websiteConfigService;
    @Resource
    UploadStrategyContext uploadStrategyContext;
    @Resource
    RedisCache redisCache;

    @Override
    public Page<ImageVo> pageList(ImageConditionRequest request) {

        Page<ImageVo> page = new Page<>(request.getPageNow(), request.getPageSize());
        //region 是否开启壁纸审核
        OtherConfig otherConfig = websiteConfigService.getWebsiteConfig(WebsiteConfigEnum.OTHER_MODULE.getWebsiteModuleCode());
        if (otherConfig.getWallpaperVerify() == YesNoEnum.YES.getValue()) {
            request.setStatus(AuditStatusEnum.APPROVED.getCode());
        }
        //endregion
        if (UserUtil.hasLogin()) {
            request.setCurrentUserId(UserUtil.getUserInfo().getId());
        }
       // redis缓存 线上取消
       /* Page<ImageVo> cachedResult = redisCache.getCacheObject(buildCacheKey(request));
        if (cachedResult != null) {
            log.info("---使用了缓存---");
            return cachedResult;
        }*/
        Page<ImageVo> result = baseMapper.queryPageVoList(page, request);
        log.info("---查询了数据库---");
        //redisCache.setCacheObject(buildCacheKey(request), result, 10, TimeUnit.MINUTES);
        return result;
    }

    private String buildCacheKey(ImageConditionRequest request) {
        StrJoiner strJoiner = StrJoiner.of(":");
        strJoiner.setPrefix("user:").append(request.getCurrentUserId())
                .append("image")
                .append("page")
                .append(request.getPageNow())
                .append("size")
                .append(request.getPageSize())
                .append("status").append(request.getStatus());
        return strJoiner.toString();
        //return "user:" + request.getCurrentUserId()
        //        + "image:"
        //        + "page:" + request.getPageNow()
        //        + "size:" + request.getPageSize()
        //        + "status" + request.getStatus();
    }

    @Override
    public void timedUpload() {
        try {
            //最大520张
            long count = this.count();
            if (count > 520) {
                return;
            }
            ImageJson imageJson = ImageUtil.getRandomImageJson();
            AssertUtil.assertIsNotPass(AssertUtil.isNull(imageJson), "壁纸接口异常！");
            //把地址转换成URL对象
            URL url = new URL(Objects.requireNonNull(imageJson).getImgurl());
            //创建http链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置超时间为3秒
            conn.setConnectTimeout(3 * 1000);
            //防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            //得到输入流
            InputStream inputStream = conn.getInputStream();
            /**
             * filename:文件名
             * file.getName()：原文件名称
             * file.getContentType()：原文件ContentType
             * fileStream：文件输入字节流
             */
            MultipartFile file = new MockMultipartFile("filename", "", imageJson.getFormat(), inputStream);
            String ossUrl = uploadStrategyContext.executeUploadFile(file, UploadModeEnum.OSS_UPLOAD.getMode(), "randomWallpaper");
            Image image = new Image();
            image.setImageUrl(ossUrl);
            image.setImageName("随机测试");
            image.setDescription("这是壁纸接口拿来的随机图片");
            image.setTypeId(getTypeIdByCategory(imageJson.getCategory()));
            saveImage(image);
            log.info("图片保存成功！");
            System.out.println(ossUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Long getTypeIdByCategory(String category) {
        switch (category) {
            case "biying":
                return 4L;
            case "dongman":
                return 5L;
            case "fengjing":
                return 6L;
            default:
                return 3L;
        }
    }

    @Override
    public PageResult<Image> queryImageList(ImageConditionRequest request) {
        //region 如果登陆用户是前台用户角色，则只能查看自己上传的图片
        if (UserUtil.userIsUser()) {
            request.setCurrentUserId(UserUtil.getUserInfo().getId());
        }
        //endregion
        IPage<Image> iPage = baseMapper.queryImageList(new Page<>(request.getPageNow(), request.getPageSize()), request);
        return new PageResult<>(iPage.getTotal(), iPage.getRecords());
    }

    @Override
    public void saveImage(Image image) {
        String imageUrl = image.getImageUrl();
        Map<Object, Object> imageInfo = ImageUtil.getImageInfo(imageUrl);
        image.setImageSize(String.valueOf(imageInfo.get("imageSize")));
        image.setFileSize(String.valueOf(imageInfo.get("fileSize")));
        image.setUploadBy(UserUtil.hasLogin() ? UserUtil.getUserInfo().getId() : null);
        baseMapper.insert(image);
    }

    @Override
    public void updateImage(Image image) {
        String imageUrl = image.getImageUrl();
        Map<Object, Object> imageInfo = ImageUtil.getImageInfo(imageUrl);
        image.setImageSize(String.valueOf(imageInfo.get("imageSize")));
        image.setFileSize(String.valueOf(imageInfo.get("fileSize")));
        baseMapper.updateById(image);
    }

    @Override
    public void batchDeleteImage(IDRequest<String> idRequest) {
        if (AssertUtil.isNotNull(idRequest) && idRequest.getIds().size() > 0) {
            idRequest.getIds().forEach(baseMapper::deleteById);
        }
    }

    @Override
    public void batchRecoveryImage(IDRequest<String> idRequest) {
        if (AssertUtil.isNotNull(idRequest) && idRequest.getIds().size() > 0) {
            idRequest.getIds().forEach(id -> baseMapper.recoveryById(id));
        }
    }

    @Override
    public void download(HttpServletResponse response, String id) {
        ServletOutputStream out = null;
        InputStream inputStream = null;
        Image image = this.getById(id);
        AssertUtil.assertIsNotPass(AssertUtil.isNull(image), "当前壁纸已被删除或已失效！");
        try {
            //文件名
            String pdfName = "壁纸.jpg";
            // 获取外部文件流
            URL url = new URL(image.getImageUrl());
            image.setDownloads(image.getDownloads() + 1);
            this.updateById(image);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(3 * 1000);
            //防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            inputStream = conn.getInputStream();
            /**
             * 输出文件到浏览器
             */
            int len = 0;
            // 输出 下载的响应头，如果下载的文件是中文名，文件名需要经过url编码
            //response.setContentType("application/x-download");
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(pdfName, "UTF-8"));
            response.setHeader("Cache-Control", "no-cache");
            out = response.getOutputStream();
            byte[] buffer = new byte[1024];
            while ((len = inputStream.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(401, "壁纸下载失败！");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception ignored) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (Exception ignored) {
                }
            }
        }
    }


}

