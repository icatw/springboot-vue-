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
 * yes no枚举
 * 是或者否枚举
 *
 * @author 76218
 * @date 2023/11/23
 */
@Getter
@AllArgsConstructor
public enum YesNoEnum {
    /**
     * 是
     */
    YES(1, "是"),
    /**
     * 否
     */
    NO(0, "否");

    /**
     * 值
     */
    private final int value;
    /**
     * 描述
     */
    private final String description;

    /**
     * 初始化map
     */
    private static final Map<Integer, YesNoEnum> CODE_NAME_MAP = new HashMap<>((int) ((YesNoEnum.values().length / 0.75) + 1));

    /**
     * 设备来源列表
     */
    public static final List<EnumResponse<Integer>> YES_NO_ENUM_LIST = new ArrayList<>(YesNoEnum.values().length);

    static {
        for (YesNoEnum yesNoEnum : YesNoEnum.values()) {
            YES_NO_ENUM_LIST.add(new EnumResponse<Integer>(yesNoEnum.getValue(), yesNoEnum.description));
            CODE_NAME_MAP.put(yesNoEnum.getValue(), yesNoEnum);
        }
    }

    /**
     * 根据value获取是否枚举
     *
     * @param value value
     * @return {@link YesNoEnum} 审批状态枚举
     */
    public static YesNoEnum getYesNoByValue(Integer value) {
        if (CODE_NAME_MAP.containsKey(value)) {
            return CODE_NAME_MAP.get(value);
        } else {
            throw new BaseException(BusinessErrorConstant.EnumError.NO_ENUM_FOUNDED_BY_KEY);
        }
    }
}
