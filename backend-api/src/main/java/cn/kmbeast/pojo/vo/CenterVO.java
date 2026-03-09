package cn.kmbeast.pojo.vo;

import lombok.Data;

/**
 * center vo for Dashboard
 */

@Data
public class CenterVO {
    /**
     * user name
     */
    private String userName;

    /**
     * user avatar
     */
    private String userAvatar;

    /**
     * upvote count
     */
    private Integer upvoteCount;

    /**
     * save count
     */
    private Integer saveCount;

    /**
     * view count
     */
    private Integer viewCount;
}
