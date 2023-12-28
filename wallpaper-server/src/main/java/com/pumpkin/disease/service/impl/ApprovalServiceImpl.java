package com.pumpkin.disease.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pumpkin.disease.base.request.BaseRequest;
import com.pumpkin.disease.base.request.IDRequest;
import com.pumpkin.disease.base.result.PageResult;
import com.pumpkin.disease.entity.wallpaper.ApprovalHistory;
import com.pumpkin.disease.entity.wallpaper.Image;
import com.pumpkin.disease.enums.business.AuditStatusEnum;
import com.pumpkin.disease.mapper.ApprovalMapper;
import com.pumpkin.disease.service.ApprovalService;
import com.pumpkin.disease.service.ImageService;
import com.pumpkin.disease.utils.user.UserUtil;
import com.pumpkin.disease.utils.vaild.AssertUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * 审批服务实现类
 *
 * @author icatw
 * @date 2023/12/11
 */
@Service("approvalService")
public class ApprovalServiceImpl extends ServiceImpl<ApprovalMapper, ApprovalHistory> implements ApprovalService {

    @Resource
    ImageService imageService;

    /**
     * 数据库表名常量
     */
    private static final String APPROVAL_HISTORY_TABLE = "approval_history";
    private static final String WALLPAPER_IMAGE_TABLE = "wallpaper_image";

    /**
     * 查询审批列表，并返回分页结果
     *
     * @param request 请求参数
     * @return 分页结果
     */
    @Override
    public PageResult<ApprovalHistory> queryApprovalList(BaseRequest request) {
        IPage<ApprovalHistory> iPage = baseMapper.queryApprovalList(new Page<>(request.getPageNow(), request.getPageSize()), request);
        return new PageResult<>(iPage.getTotal(), iPage.getRecords());
    }

    /**
     * 保存审批历史记录
     *
     * @param approval 审批历史记录
     */
    @Override
    public void saveApprovalHistory(ApprovalHistory approval) {
        // 查询是否存在相同模块的审批记录
        ApprovalHistory existingApproval = lambdaQuery()
                .eq(ApprovalHistory::getModuleId, approval.getModuleId())
                .one();

        // 设置公共的审批字段
        setCommonApprovalFields(approval);

        if (existingApproval != null) {
            // 如果存在，则更新审批记录
            approval.setId(existingApproval.getId());
            baseMapper.updateById(approval);
        } else {
            // 如果不存在，则插入新审批记录
            baseMapper.insert(approval);
        }

        // 更新壁纸表的审核信息
        updateImageApproval(approval.getModuleId(), approval.getId(), AuditStatusEnum.PENDING.getCode());
    }

    /**
     * 更新审批历史记录
     *
     * @param approval 审批历史记录
     */
    @Override
    public void updateApprovalHistory(ApprovalHistory approval) {
        // 实现审核通过是驳回，审核中是撤回，撤回需要重新提交，之后是审核中的才显示同意按钮
        // 具体逻辑待实现
    }

    /**
     * 批量删除审批历史记录
     *
     * @param idRequest ID请求
     */
    @Override
    public void batchDeleteApprovalHistory(IDRequest<String> idRequest) {
        // 待实现
    }

    /**
     * 批量同意审批历史记录
     *
     * @param idRequest ID请求
     */
    @Override
    public void batchAgree(IDRequest<String> idRequest) {
        // 批量同意，更新审批时间，状态字段
        if (AssertUtil.isNotNull(idRequest) && !idRequest.getIds().isEmpty()) {
            idRequest.getIds().forEach(
                    id -> {
                        ApprovalHistory approval = baseMapper.selectById(id);
                        if (approval.getApprovalStatus().equals(AuditStatusEnum.PENDING.getCode())) {
                            updateApprovalStatus(id, AuditStatusEnum.APPROVED.getCode());
                        }
                    }
            );
        }
    }

    /**
     * 批量审核通过后驳回审批历史记录
     *
     * @param idRequest ID请求
     */
    @Override
    public void batchReject(IDRequest<String> idRequest) {
        // 批量审核通过后驳回
        if (AssertUtil.isNotNull(idRequest) && !idRequest.getIds().isEmpty()) {
            idRequest.getIds().forEach(
                    id -> {
                        ApprovalHistory approval = baseMapper.selectById(id);
                        if (approval.getApprovalStatus().equals(AuditStatusEnum.APPROVED.getCode())) {
                            updateApprovalStatus(id, AuditStatusEnum.REJECT.getCode());
                        }
                    }
            );
        }
    }

    /**
     * 批量拒绝审批历史记录
     *
     * @param idRequest ID请求
     */
    @Override
    public void batchRefuse(IDRequest<String> idRequest) {
        // 批量拒绝
        if (AssertUtil.isNotNull(idRequest) && !idRequest.getIds().isEmpty()) {
            idRequest.getIds().forEach(
                    id -> {
                        ApprovalHistory approval = baseMapper.selectById(id);
                        if (approval.getApprovalStatus().equals(AuditStatusEnum.PENDING.getCode())) {
                            updateApprovalStatus(id, AuditStatusEnum.REFUSE.getCode());
                        }
                    }
            );
        }
    }

    /**
     * 批量撤回审批历史记录
     *
     * @param idRequest ID请求
     */
    @Override
    public void batchWithdraw(IDRequest<String> idRequest) {
        // 批量撤回
        if (AssertUtil.isNotNull(idRequest) && !idRequest.getIds().isEmpty()) {
            idRequest.getIds().forEach(
                    id -> {
                        ApprovalHistory approval = baseMapper.selectById(id);
                        if (approval.getApprovalStatus().equals(AuditStatusEnum.PENDING.getCode())) {
                            updateApprovalStatus(id, AuditStatusEnum.WITHDRAW.getCode());
                        }
                    }
            );
        }
    }

    /**
     * 设置公共的审批字段
     *
     * @param approval 审批历史记录
     */
    private void setCommonApprovalFields(ApprovalHistory approval) {
        approval.setSubmitterId(UserUtil.getUserInfo().getId());
        approval.setApprovalStatus(AuditStatusEnum.PENDING.getCode());
    }

    /**
     * 更新壁纸表的审核信息
     *
     * @param imageId    壁纸ID
     * @param approvalId 审批ID
     * @param status     状态码
     */
    private void updateImageApproval(Long imageId, Long approvalId, Integer status) {
        LambdaUpdateWrapper<Image> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Image::getId, imageId)
                .set(Image::getApprovalId, approvalId)
                .set(Image::getStatus, status);
        imageService.update(lambdaUpdateWrapper);
    }

    /**
     * 更新审批记录的状态
     *
     * @param id     记录ID
     * @param status 状态码
     */
    private void updateApprovalStatus(String id, Integer status) {
        baseMapper.updateApprovalTime(id, LocalDateTime.now());
        baseMapper.updateStatus(APPROVAL_HISTORY_TABLE, "approval_status", "id", id, status);
        baseMapper.updateStatus(WALLPAPER_IMAGE_TABLE, "status", "approval_id", id, status);
    }
}
