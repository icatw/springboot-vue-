package com.pumpkin.disease.enums.business;

import com.pumpkin.disease.base.exception.BaseException;
import com.pumpkin.disease.constant.BusinessErrorConstant;
import com.pumpkin.disease.response.system.enums.EnumResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 审核状态枚举
 *
 * @author 王顺
 * @date 2023/11/23
 * @email 762188827@qq.com
 * @apiNote
 */
@Getter
@AllArgsConstructor
public enum AuditStatusEnum {
    /**
     * 未提交
     */
    NOT_SUBMITTED(0, "未提交"),
    /**
     * 待审核
     */
    PENDING(1, "待审核"),
    /**
     * 审核通过
     */
    APPROVED(2, "审核通过"),
    /**
     * 审核不通过
     */
    REFUSE(3, "审核不通过"),
    /**
     * 审核通过后驳回
     */
    REJECT(4, "审核通过后驳回"),
    /**
     * 审核撤回
     */
    WITHDRAW(5, "审核撤回");

    /**
     * code
     */
    private final int code;
    /**
     * 描述
     */
    private final String description;
    /**
     * 初始化map
     */
    private static final Map<Integer, AuditStatusEnum> CODE_NAME_MAP = new HashMap<>((int) ((AuditStatusEnum.values().length / 0.75) + 1));

    /**
     * 设备来源列表
     */
    public static final List<EnumResponse<Integer>> AUDIT_STATUS_ENUM_LIST = new ArrayList<>(AuditStatusEnum.values().length);

    static {
        for (AuditStatusEnum auditStatusEnum : AuditStatusEnum.values()) {
            AUDIT_STATUS_ENUM_LIST.add(new EnumResponse<Integer>(auditStatusEnum.getCode(), auditStatusEnum.description));
            CODE_NAME_MAP.put(auditStatusEnum.getCode(), auditStatusEnum);
        }
    }

    /**
     * 根据code获取审批状态枚举
     *
     * @param code code
     * @return {@link AuditStatusEnum} 审批状态枚举
     */
    public static AuditStatusEnum getAuditStatusByCode(Integer code) {
        if (CODE_NAME_MAP.containsKey(code)) {
            return CODE_NAME_MAP.get(code);
        } else {
            throw new BaseException(BusinessErrorConstant.EnumError.NO_ENUM_FOUNDED_BY_KEY);
        }
    }
}
