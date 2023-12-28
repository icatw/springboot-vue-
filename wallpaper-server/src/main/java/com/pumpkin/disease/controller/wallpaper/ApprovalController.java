package com.pumpkin.disease.controller.wallpaper;

import com.pumpkin.disease.annotation.NormalOperateLog;
import com.pumpkin.disease.base.request.BaseRequest;
import com.pumpkin.disease.base.request.IDRequest;
import com.pumpkin.disease.base.result.PageResult;
import com.pumpkin.disease.base.result.ResponseResult;
import com.pumpkin.disease.entity.wallpaper.ApprovalHistory;
import com.pumpkin.disease.response.security.user.UserResponse;
import com.pumpkin.disease.service.ApprovalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static com.pumpkin.disease.constant.SystemConstant.LogType.*;

/**
 * 审核(approval)表控制层
 *
 * @author icatw
 * @since 2022-10-14 19:58:56
 */
@Api(tags = "审核表(approval)")
@RestController
@RequestMapping("approval")
public class ApprovalController {

    /**
     * 服务对象
     */
    @Resource
    private ApprovalService approvalService;


    /**
     * 查询审核列表
     *
     * @param approvalConditionRequest 审核条件请求入参
     * @return {@link  UserResponse} 审核响应信息
     */
    @ApiOperation(value = "[后台]查询审核列表")
    @PreAuthorize("@mznPerms.checkCurrentUserIsHasPerm('wallpaper:approval:query')")
    @PostMapping("/list")
    public ResponseResult<PageResult<ApprovalHistory>> queryApprovalHistoryList(@RequestBody @Valid BaseRequest approvalConditionRequest) {
        return ResponseResult.success("查询审核列表信息成功！", approvalService.queryApprovalList(approvalConditionRequest));
    }

    /**
     * 提交审核
     *
     * @param approval 请求入参
     * @return 是否成功
     */
    @ApiOperation(value = "[后台]新增审核信息")
    @NormalOperateLog(operateType = SAVE_OPERATE)
    @PreAuthorize("@mznPerms.checkCurrentUserIsHasPerm('wallpaper:approval:save')")
    @PostMapping("/submit")
    public ResponseResult<?> saveUser(@RequestBody @Valid ApprovalHistory approval) {
        approvalService.saveApprovalHistory(approval);
        return ResponseResult.success("提交审核成功！");
    }

    /**
     * 同意审核信息
     *
     * @param idRequest 请求入参
     * @return 是否成功
     */
    @ApiOperation(value = "[后台]同意审核信息")
    @NormalOperateLog(operateType = SAVE_OPERATE)
    @PreAuthorize("@mznPerms.checkCurrentUserIsHasPerm('wallpaper:approval:agree')")
    @PostMapping("/batch/agree")
    public ResponseResult<?> agree(@RequestBody IDRequest<String> idRequest) {
        approvalService.batchAgree(idRequest);
        return ResponseResult.success("同意审核成功！");
    }

    /**
     * 审核通过后驳回
     *
     * @param idRequest 请求入参
     * @return 是否成功
     */
    @ApiOperation(value = "[后台]驳回审核信息")
    @NormalOperateLog(operateType = SAVE_OPERATE)
    @PreAuthorize("@mznPerms.checkCurrentUserIsHasPerm('wallpaper:approval:agree')")
    @PostMapping("/batch/reject")
    public ResponseResult<?> reject(@RequestBody IDRequest<String> idRequest) {
        approvalService.batchReject(idRequest);
        return ResponseResult.success("驳回成功！");
    }

    /**
     * 拒绝
     *
     * @param idRequest 请求入参
     * @return 是否成功
     */
    @ApiOperation(value = "[后台]拒绝审核信息")
    @NormalOperateLog(operateType = SAVE_OPERATE)
    @PreAuthorize("@mznPerms.checkCurrentUserIsHasPerm('wallpaper:approval:agree')")
    @PostMapping("/batch/refuse")
    public ResponseResult<?> refuse(@RequestBody IDRequest<String> idRequest) {
        approvalService.batchRefuse(idRequest);
        return ResponseResult.success("拒绝成功！");
    }
    /**
     * 撤回
     *
     * @param idRequest 请求入参
     * @return 是否成功
     */
    @ApiOperation(value = "[后台]撤回审核信息")
    @NormalOperateLog(operateType = SAVE_OPERATE)
    @PreAuthorize("@mznPerms.checkCurrentUserIsHasPerm('wallpaper:approval:agree')")
    @PostMapping("/batch/withdraw")
    public ResponseResult<?> withdraw(@RequestBody IDRequest<String> idRequest) {
        approvalService.batchWithdraw(idRequest);
        return ResponseResult.success("撤回成功！");
    }
    /**
     * 更新审核信息
     *
     * @param approval 请求入参
     * @return 是否更新成功
     */
    @ApiOperation(value = "[后台]更新审核信息")
    @NormalOperateLog(operateType = UPDATE_OPERATE)
    @PreAuthorize("@mznPerms.checkCurrentUserIsHasPerm('wallpaper:approval:update')")
    @PostMapping("/update")
    public ResponseResult<?> updateApprovalHistory(@RequestBody ApprovalHistory approval) {
        approvalService.updateApprovalHistory(approval);
        return ResponseResult.success("更新审核信息成功！");
    }

    /**
     * 批量删除审核信息
     *
     * @param idRequest id请求入参
     * @return 是否删除成功
     */
    @ApiOperation(value = "[后台]删除审核信息")
    @NormalOperateLog(operateType = BATCH_DELETE_OPERATE)
    @PreAuthorize("@mznPerms.checkCurrentUserIsHasPerm('wallpaper:approval:delete')")
    @DeleteMapping("/batch/delete")
    public ResponseResult<?> batchDeleteApprovalHistory(@RequestBody IDRequest<String> idRequest) {
        approvalService.batchDeleteApprovalHistory(idRequest);
        return ResponseResult.success("删除审核成功！");
    }

}

