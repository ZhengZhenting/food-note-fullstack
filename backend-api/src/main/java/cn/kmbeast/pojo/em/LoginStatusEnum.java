package cn.kmbeast.pojo.em;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 登录状态枚举
 */
@Getter
@AllArgsConstructor
public enum LoginStatusEnum {

    USE(false, "可登录"),
    BANK_USE(true, "登录状态异常");

    /**
     * flag
     */
    private final Boolean flag;
    /**
     * name
     */
    private final String name;

}
