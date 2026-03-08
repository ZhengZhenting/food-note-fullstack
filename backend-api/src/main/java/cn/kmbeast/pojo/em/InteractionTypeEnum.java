package cn.kmbeast.pojo.em;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * interaction types
 */
@Getter
@AllArgsConstructor
public enum InteractionTypeEnum {

    UPVOTE(1, "like"),
    COLLECTION(2, "save"),
    VIEW(3, "view"),
    RATING(4, "rating");

    /**
     * type
     */
    private final Integer type;
    /**
     * detail
     */
    private final String detail;

}
