package cn.kmbeast.pojo.em;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * audit status
 */
@Getter
@AllArgsConstructor
public enum AuditEnum {

    NO_AUDIT(false, "not audited"),
    OK_AUDIT(true, "audited");

    /**
     * flag
     */
    private final Boolean flag;
    /**
     * name
     */
    private final String name;

}
