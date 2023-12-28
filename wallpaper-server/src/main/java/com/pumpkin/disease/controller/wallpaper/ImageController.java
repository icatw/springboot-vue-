package com.pumpkin.disease.controller.wallpaper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pumpkin.disease.annotation.NormalOperateLog;
import com.pumpkin.disease.base.request.IDRequest;
import com.pumpkin.disease.base.result.PageResult;
import com.pumpkin.disease.base.result.ResponseResult;
import com.pumpkin.disease.entity.wallpaper.Image;
import com.pumpkin.disease.entity.wallpaper.ImageVo;
import com.pumpkin.disease.request.security.wallpaper.ImageConditionRequest;
import com.pumpkin.disease.response.security.user.UserResponse;
import com.pumpkin.disease.service.ImageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static com.pumpkin.disease.constant.SystemConstant.LogType.*;

/**
 * 壁纸表(Image)表控制层
 *
 * @author icatw
 * @since 2022-10-14 19:58:55
 */

/**
 * @author icatw
 * @date 2022/10/16
 * @email 762188827@qq.com
 * @apiNote
 */
@Api(tags = "前台图片接口")
@RestController
@RequestMapping("/image")
public class ImageController {
    @Resource
    ImageService imageService;

    /**
     * 前台分页查询所有数据
     */
    @ApiOperation(value = "分页查询所有数据 壁纸表（带条件和分页）")
    @PostMapping("frontend/pageList")
    public ResponseResult<?> page(@RequestBody ImageConditionRequest request) {
        Page<ImageVo> result = imageService.pageList(request);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "下载图片", produces = "application/octet-stream")
    @GetMapping(value = "/frontend/download/{id}", produces = "application/octet-stream")
    public void download(HttpServletResponse response, @PathVariable String id) {
        imageService.download(response, id);
    }

    /**
     * 查询壁纸列表
     *
     * @param imageConditionRequest 壁纸条件请求入参
     * @return {@link  UserResponse} 壁纸响应信息
     */
    @ApiOperation(value = "[后台]查询壁纸列表")
    @PreAuthorize("@mznPerms.checkCurrentUserIsHasPerm('wallpaper:image:query')")
    @PostMapping("/list")
    public ResponseResult<PageResult<Image>> queryImageList(@RequestBody @Valid ImageConditionRequest imageConditionRequest) {
        return ResponseResult.success("查询壁纸列表信息成功！", imageService.queryImageList(imageConditionRequest));
    }

    /**
     * 新增壁纸信息
     *
     * @param image 请求入参
     * @return 是否新增成功
     */
    @ApiOperation(value = "[后台]新增壁纸信息")
    @NormalOperateLog(operateType = SAVE_OPERATE)
    @PreAuthorize("@mznPerms.checkCurrentUserIsHasPerm('wallpaper:image:save')")
    @PostMapping("/save")
    public ResponseResult<?> saveUser(@RequestBody @Valid Image image) {
        imageService.saveImage(image);
        return ResponseResult.success("新增壁纸成功！");
    }

    /**
     * 更新壁纸信息
     *
     * @param image 请求入参
     * @return 是否更新成功
     */
    @ApiOperation(value = "[后台]更新壁纸信息")
    @NormalOperateLog(operateType = UPDATE_OPERATE)
    @PreAuthorize("@mznPerms.checkCurrentUserIsHasPerm('wallpaper:image:update')")
    @PostMapping("/update")
    public ResponseResult<?> updateImage(@RequestBody Image image) {
        imageService.updateImage(image);
        return ResponseResult.success("更新壁纸信息成功！");
    }

    /**
     * 批量删除壁纸信息
     *
     * @param idRequest id请求入参
     * @return 是否删除成功
     */
    @ApiOperation(value = "[后台]删除壁纸信息")
    @NormalOperateLog(operateType = BATCH_DELETE_OPERATE)
    @PreAuthorize("@mznPerms.checkCurrentUserIsHasPerm('wallpaper:image:delete')")
    @DeleteMapping("/batch/delete")
    public ResponseResult<?> batchDeleteImage(@RequestBody IDRequest<String> idRequest) {
        imageService.batchDeleteImage(idRequest);
        return ResponseResult.success("删除壁纸成功！");
    }

    /**
     * 恢复
     *
     * @param idRequest id请求入参
     * @return 是否删除成功
     */
    @ApiOperation(value = "[后台]删除壁纸信息")
    @NormalOperateLog(operateType = BATCH_RECOVERY_OPERATE)
    @PreAuthorize("@mznPerms.checkCurrentUserIsHasPerm('wallpaper:image:recovery')")
    @PutMapping("/recovery")
    public ResponseResult<?> recoveryImage(@RequestBody IDRequest<String> idRequest) {
        imageService.batchRecoveryImage(idRequest);
        return ResponseResult.success("恢复壁纸成功！");
    }
}
