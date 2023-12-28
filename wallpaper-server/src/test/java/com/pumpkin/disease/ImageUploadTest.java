package com.pumpkin.disease;

import com.pumpkin.disease.service.ImageService;
import com.pumpkin.disease.strategy.context.UploadStrategyContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author 王顺
 * @date 2023/12/2
 * @email 762188827@qq.com
 * @apiNote
 */
@SpringBootTest
public class ImageUploadTest {
    @Resource
    ImageService imageService;
    @Resource
    UploadStrategyContext uploadStrategyContext;

    @Test
    public void upload(){
        imageService.timedUpload();
    }
}
