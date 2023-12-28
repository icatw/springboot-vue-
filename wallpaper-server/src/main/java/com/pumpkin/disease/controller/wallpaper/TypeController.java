package com.pumpkin.disease.controller.wallpaper;

import com.pumpkin.disease.annotation.NormalOperateLog;
import com.pumpkin.disease.base.request.IDRequest;
import com.pumpkin.disease.base.result.PageResult;
import com.pumpkin.disease.base.result.ResponseResult;
import com.pumpkin.disease.entity.wallpaper.Type;
import com.pumpkin.disease.request.security.wallpaper.TypeConditionRequest;
import com.pumpkin.disease.response.security.user.UserResponse;
import com.pumpkin.disease.service.TypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static com.pumpkin.disease.constant.SystemConstant.LogType.*;

/**
 * 分类分类表(Type)表控制层
 *
 * @author icatw
 * @since 2022-10-14 19:58:56
 */
@Api(tags = "分类分类表(Type)")
@RestController
@RequestMapping("type")
public class TypeController {

    /**
     * 服务对象
     */
    @Resource
    private TypeService typeService;

    /**
     * 查询分类基础信息列表
     *
     * @return {@link Type} 分类信息
     */
    @ApiOperation(value = "[后台]查询分类基础信息列表")
    @GetMapping("/list/basic")
    public ResponseResult<List<Type>> listBasicType() {
        return ResponseResult.success("查询分类基础信息成功！", typeService.listBasicType());
    }

    /**
     * 查询分类列表
     *
     * @param typeConditionRequest 分类条件请求入参
     * @return {@link  UserResponse} 分类响应信息
     */
    @ApiOperation(value = "[后台]查询分类列表")
    @PreAuthorize("@mznPerms.checkCurrentUserIsHasPerm('wallpaper:type:query')")
    @PostMapping("/list")
    public ResponseResult<PageResult<Type>> queryTypeList(@RequestBody @Valid TypeConditionRequest typeConditionRequest) {
        return ResponseResult.success("查询分类列表信息成功！", typeService.queryTypeList(typeConditionRequest));
    }

    /**
     * 新增分类信息
     *
     * @param type 请求入参
     * @return 是否新增成功
     */
    @ApiOperation(value = "[后台]新增分类信息")
    @NormalOperateLog(operateType = SAVE_OPERATE)
    @PreAuthorize("@mznPerms.checkCurrentUserIsHasPerm('wallpaper:type:save')")
    @PostMapping("/save")
    public ResponseResult<?> saveUser(@RequestBody @Valid Type type) {
        typeService.saveType(type);
        return ResponseResult.success("新增分类成功！");
    }

    /**
     * 更新分类信息
     *
     * @param type 请求入参
     * @return 是否更新成功
     */
    @ApiOperation(value = "[后台]更新分类信息")
    @NormalOperateLog(operateType = UPDATE_OPERATE)
    @PreAuthorize("@mznPerms.checkCurrentUserIsHasPerm('wallpaper:type:update')")
    @PostMapping("/update")
    public ResponseResult<?> updateType(@RequestBody Type type) {
        typeService.updateType(type);
        return ResponseResult.success("更新分类信息成功！");
    }

    /**
     * 批量删除分类信息
     *
     * @param idRequest id请求入参
     * @return 是否删除成功
     */
    @ApiOperation(value = "[后台]删除分类信息")
    @NormalOperateLog(operateType = BATCH_DELETE_OPERATE)
    @PreAuthorize("@mznPerms.checkCurrentUserIsHasPerm('wallpaper:type:delete')")
    @DeleteMapping("/batch/delete")
    public ResponseResult<?> batchDeleteType(@RequestBody IDRequest<String> idRequest) {
        typeService.batchDeleteType(idRequest);
        return ResponseResult.success("删除分类成功！");
    }

    @ApiOperation(value = "[前台] 获取所有分类")
    @GetMapping("/frontend/list/basic")
    public ResponseResult<?> list() {
        return ResponseResult.success("查询分类基础信息成功！", typeService.listBasicType());
    }
}

