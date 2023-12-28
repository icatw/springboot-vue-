package com.pumpkin.disease.controller.wallpaper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pumpkin.disease.base.result.ResponseResult;
import com.pumpkin.disease.entity.User;
import com.pumpkin.disease.entity.wallpaper.CollectVo;
import com.pumpkin.disease.enums.http.HttpCodeEnum;
import com.pumpkin.disease.service.CollectService;
import com.pumpkin.disease.utils.user.UserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 收藏表(Collect)表控制层
 *
 * @author icatw
 * @since 2022-10-16 01:57:07
 */
@Api(tags = "收藏表(Collect)")
@RestController
@RequestMapping("collect")
public class CollectController {

    /**
     * 服务对象
     */
    @Resource
    private CollectService collectService;

    @GetMapping("/frontend/page")
    @ApiOperation("获取用户收藏列表")
    public ResponseResult<?> pageList(HttpServletRequest request, @RequestParam int current, @RequestParam int size) {
        Page<CollectVo> page = new Page<>(current, size);
        User userInfo = UserUtil.getUserInfo();
        IPage<CollectVo> iPage = collectService.pageList(page, userInfo.getId());
        return ResponseResult.success(iPage);
    }

    @PostMapping("/frontend/love/{imageId}")
    @ApiOperation("收藏/取消收藏图片")
    public ResponseResult<?> love(@PathVariable int imageId) {
        collectService.love(imageId);
        return ResponseResult.success(HttpCodeEnum.OPERATOR_IS_SUCCESS);
    }
}

