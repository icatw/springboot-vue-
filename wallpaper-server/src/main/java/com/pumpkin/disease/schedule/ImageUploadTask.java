package com.pumpkin.disease.schedule;

import com.pumpkin.disease.service.ImageService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 王顺
 * @date 2023/12/2
 * @email 762188827@qq.com
 * @apiNote
 */
@Component
public class ImageUploadTask {
    @Resource
    ImageService imageService;
    private static final SimpleDateFormat f=new SimpleDateFormat("HH:mm:ss");

    /**
     * 上传oss
     * 60秒执行一次
     */
    @Scheduled(fixedRate = 60000)
    public void uploadOss(){
        imageService.timedUpload();
        System.out.println("timedUpload方式开启定时任务：现在的时间是"+f.format(new Date()));
    }
}
