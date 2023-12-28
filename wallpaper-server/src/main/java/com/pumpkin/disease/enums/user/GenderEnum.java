package com.pumpkin.disease.enums.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 性别枚举
 *
 * @author wangshun
 * @date 2023/11/18
 */
@Getter
@AllArgsConstructor
public enum GenderEnum {

    /**
     * 未知
     */
    UNKNOWN(-1, "未知"),
    /**
     * 女
     */
    FEMALE(0, "仙女"),
    /**
     * 男性
     */
    MALE(1, "帅哥");

    /**
     * value
     */
    private final int value;
    /**
     * name
     */
    private final String name;
}
