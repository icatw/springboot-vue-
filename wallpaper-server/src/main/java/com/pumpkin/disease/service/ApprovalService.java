package com.pumpkin.disease.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pumpkin.disease.base.request.BaseRequest;
import com.pumpkin.disease.base.request.IDRequest;
import com.pumpkin.disease.base.result.PageResult;
import com.pumpkin.disease.entity.wallpaper.ApprovalHistory;

/**
 * 审批服务
 *
 * @author icatw
 * @date 2023/12/11
 */
public interface ApprovalService extends IService<ApprovalHistory> {

    /**
     * 查询审批列表
     *
     * @param approvalConditionRequest 批准条件请求
     * @return {@link PageResult}<{@link ApprovalHistory}>
     */
    PageResult<ApprovalHistory> queryApprovalList(BaseRequest approvalConditionRequest);

    /**
     * 保存审批历史记录
     *
     * @param approval 批准
     */
    void saveApprovalHistory(ApprovalHistory approval);

    /**
     * 更新审批历史记录
     *
     * @param approval 批准
     */
    void updateApprovalHistory(ApprovalHistory approval);

    /**
     * 批量删除审批历史记录
     *
     * @param idRequest id请求
     */
    void batchDeleteApprovalHistory(IDRequest<String> idRequest);

    /**
     * 批量同意
     *
     * @param idRequest id请求
     */
    void batchAgree(IDRequest<String> idRequest);

    /**
     * 审核通过后驳回
     *
     * @param idRequest id请求
     */
    void batchReject(IDRequest<String> idRequest);

    /**
     * 批量拒绝
     *
     * @param idRequest id请求
     */
    void batchRefuse(IDRequest<String> idRequest);

    /**
     * 批量撤回
     *
     * @param idRequest id请求
     */
    void batchWithdraw(IDRequest<String> idRequest);
}

