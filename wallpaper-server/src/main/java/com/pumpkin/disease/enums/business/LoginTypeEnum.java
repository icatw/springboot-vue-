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
public enum LoginTypeEnum {
    /**
     * 是
     */
    QQ(1, "qq"),
    GITEE(2, "gitee"),
    GITHUB(3, "github"),
    /**
     * 否
     */
    WEIBO(4, "微博登录");

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
    private static final Map<Integer, LoginTypeEnum> CODE_NAME_MAP = new HashMap<>((int) ((LoginTypeEnum.values().length / 0.75) + 1));

    /**
     * 设备来源列表
     */
    public static final List<EnumResponse<Integer>> YES_NO_ENUM_LIST = new ArrayList<>(LoginTypeEnum.values().length);

    static {
        for (LoginTypeEnum yesNoEnum : LoginTypeEnum.values()) {
            YES_NO_ENUM_LIST.add(new EnumResponse<Integer>(yesNoEnum.getValue(), yesNoEnum.description));
            CODE_NAME_MAP.put(yesNoEnum.getValue(), yesNoEnum);
        }
    }

    /**
     * 根据value获取是否枚举
     *
     * @param value value
     * @return {@link LoginTypeEnum} 审批状态枚举
     */
    public static LoginTypeEnum getYesNoByValue(Integer value) {
        if (CODE_NAME_MAP.containsKey(value)) {
            return CODE_NAME_MAP.get(value);
        } else {
            throw new BaseException(BusinessErrorConstant.EnumError.NO_ENUM_FOUNDED_BY_KEY);
        }
    }

    //    根据desc获取value
    public static Integer getValueByDesc(String desc) {
        for (LoginTypeEnum loginTypeEnum : LoginTypeEnum.values()) {
            if (loginTypeEnum.getDescription().equals(desc)) {
                return loginTypeEnum.getValue();
            }
        }
        return null;
    }
}
