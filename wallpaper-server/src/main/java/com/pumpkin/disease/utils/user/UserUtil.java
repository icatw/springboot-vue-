package com.pumpkin.disease.utils.user;

import com.pumpkin.disease.base.exception.BaseException;
import com.pumpkin.disease.entity.User;
import com.pumpkin.disease.enums.http.HttpCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Random;

import static com.pumpkin.disease.constant.SystemConstant.Permission.*;

/**
 * @author: Pumpkin
 * @description: 用户信息工具类
 * @since : 2022/12/29
 */
@Slf4j
public class UserUtil {
    public static void main(String[] args) {
        String s = generateNickname();
        System.out.println(s);
    }

    public static boolean hasLogin() {
        try {
            return SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User;
        } catch (Exception e) {
            log.error("----用户未登录----");
            return false;
        }
    }


    public static User getUserInfo() {
        Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (object instanceof User) {
            return (User) object;
        } else {
                throw new BaseException(HttpCodeEnum.USER_IDENTITY_LOAD_FAIL);
        }
    }


    /**
     * 检测当前用户是否是管理员或超级管理员
     *
     * @return true 为管理员或超级管理员  false 普通用户或其他身份用户
     */
    public static boolean userIsAdmin() {
        return SUPER_ADMIN_LABEL.equals(getUserInfo().getRole().getRoleLabel()) || ADMIN_LABEL.equals(getUserInfo().getRole().getRoleLabel());
    }

    /**
     * 检测当前用户是否是超级管理员
     *
     * @return true 超级管理员  false 普通用户或其他身份用户
     */
    public static boolean userIsSuperAdmin() {
        return SUPER_ADMIN_LABEL.equals(getUserInfo().getRole().getRoleLabel());
    }
    /**
     * 检测当前用户是否是前台用户
     *
     * @return true 前台用户  false 其他身份用户
     */
    public static boolean userIsUser() {
        return USER_LABEL.equals(getUserInfo().getRole().getRoleLabel());
    }


    private static final String[] ADJECTIVES = {
            "快乐的", "聪明的", "勇敢的", "美丽的", "迷人的", "可爱的", "幸运的", "魅力的", "活力的", "热情的"
    };

    private static final String[] NOUNS = {
            "猫咪", "小熊", "小鸟", "小兔", "小狐", "小猴", "小狗", "小虎", "小龙", "小鱼"
    };

    private static final Random RANDOM = new Random();

    public static String generateNickname() {
        String adjective = getRandomElement(ADJECTIVES);
        String noun = getRandomElement(NOUNS);
        // 生成一个随机数字，范围为 0-999
        int randomNumber = RANDOM.nextInt(1000);
        return adjective + noun + randomNumber;
    }

    private static String getRandomElement(String[] array) {
        int index = RANDOM.nextInt(array.length);
        return array[index];
    }
}
