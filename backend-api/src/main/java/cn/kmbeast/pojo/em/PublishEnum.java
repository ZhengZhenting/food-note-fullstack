package cn.kmbeast.pojo.em;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * publish status enum
 */
@Getter
@AllArgsConstructor
public enum PublishEnum {

    NO_AUDIT(false, "private"),
    OK_AUDIT(true, "public");

    /**
     * flag
     */
    private final Boolean flag;
    /**
     * name
     */
    private final String name;

}
