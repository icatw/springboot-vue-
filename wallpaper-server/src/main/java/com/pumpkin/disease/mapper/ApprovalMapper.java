package com.pumpkin.disease.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pumpkin.disease.base.request.BaseRequest;
import com.pumpkin.disease.entity.wallpaper.ApprovalHistory;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * 审核表(Approval)表数据库访问层
 *
 * @author icatw
 * @date 2023/12/11
 * @since 2022-10-16 01:57:07
 */
public interface ApprovalMapper extends BaseMapper<ApprovalHistory> {
    IPage<ApprovalHistory> queryApprovalList(Page<ApprovalHistory> page, @Param("request") BaseRequest request);


    /**
     * 更新状态
     *
     * @param tableName   表名称
     * @param statusField 状态字段
     * @param idField     id字段
     * @param id          id
     * @param status      状态值
     */
    void updateStatus(@Param("tableName") String tableName, @Param("statusField") String statusField, @Param("idField") String idField, @Param("id") String id, @Param("status") int status);

    /**
     * 更新审批时间
     *
     * @param id   id
     * @param time 时间
     */
    void updateApprovalTime(@Param("id") String id, @Param("time") LocalDateTime time);
}

