package com.pumpkin.disease.utils.image;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONObject;
import com.pumpkin.disease.base.exception.BaseException;
import com.pumpkin.disease.entity.wallpaper.ImageJson;
import com.pumpkin.disease.utils.http.TemplateMappingJackson2HttpMessageConverter;
import com.pumpkin.disease.utils.vaild.AssertUtil;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 * @author icatw
 * @date 2022/10/15
 * @email 762188827@qq.com
 * @apiNote
 */
public class ImageUtil {
    public static String getImageSize(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        URLConnection connection = url.openConnection();
        connection.setDoOutput(true);
        BufferedImage image = ImageIO.read(connection.getInputStream());
        int srcWidth = image.getWidth();      // 源图宽度
        int srcHeight = image.getHeight();    // 源图高度
        System.out.println("srcWidth = " + srcWidth);
        System.out.println("srcHeight = " + srcHeight);
        return srcWidth + "*" + srcHeight;
    }

    public static Map<Object, Object> getImageInfo(String imageUrl) {
        try {
            AssertUtil.assertIsNotPass(AssertUtil.isEmpty(imageUrl), "壁纸url为空！");
            URL url = new URL(imageUrl);
            // 获取图像的尺寸
            BufferedImage image = ImageIO.read(url);
            int width = image.getWidth();      // 图像宽度
            int height = image.getHeight();    // 图像高度
            long fileSizeInBytes = url.openConnection().getContentLengthLong();
            System.out.println("Image Width: " + width);
            System.out.println("Image Height: " + height);

            // 获取图像的文件大小
            System.out.println("File Size: " + fileSizeInBytes / 1024 + " KB");
            return MapUtil.builder()
                    .put("imageSize", width + "*" + height)
                    .put("fileSize", fileSizeInBytes / 1024).build();
        } catch (IOException e) {
            e.printStackTrace();
            throw new BaseException("获取图片信息失败！");
        }
    }

    // 获取图像文件大小
    private static long getFileSize(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            return url.openConnection().getContentLengthLong();
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static ImageJson getRandomImageJson() throws IOException {
        // 我们需要进行请求的地址：
        //String getRandomImageJson = "https://www.dmoe.cc/random.php?return=json";
        String temp = "https://v1.yurikoto.com/wallpaper?encode=json";
        String api = "https://tuapi.eees.cc/api.php?type={type}&category={category}";
        RestTemplate restTemplate = new RestTemplate();
        //随机type
        String[] categoryArray = {"biying", "fengjing", "dongman"};
        // 创建一个随机数生成器
        Random random = new Random();
        String randomCategory = categoryArray[random.nextInt(categoryArray.length)];
        restTemplate.getMessageConverters().add(new TemplateMappingJackson2HttpMessageConverter());
        HashMap<String, String> map = new HashMap<>(3);
        map.put("type", "json");
        map.put("category", randomCategory);
        JSONObject o = restTemplate.getForObject(api, JSONObject.class, map);
        System.out.println(o);
        if (Objects.nonNull(o) && "200".equals(o.getString("result"))) {
            return ImageJson.builder()
                    .height(Objects.requireNonNull(o).getString("height"))
                    .width(Objects.requireNonNull(o).getString("width"))
                    .format(Objects.requireNonNull(o).getString("format"))
                    .category(randomCategory)
                    .imgurl(Objects.requireNonNull(o).getString("img"))
                    .code(Objects.requireNonNull(o).getString("result"))
                    .build();
        } else {
            return null;
        }
    }
}
