package cn.kmbeast.pojo.em;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 登录状态枚举
 */
@Getter
@AllArgsConstructor
public enum AuditEnum {

    NO_AUDIT(false, "not audited"),
    YES_AUDIT(true, "audited");

    /**
     * flag
     */
    private final Boolean flag;
    /**
     * name
     */
    private final String name;

}
