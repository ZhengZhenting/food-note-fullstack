package cn.kmbeast.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * comment parentclass
 */
@Data
public class CommentParentVO {

    /**
     * comment ID
     */
    private Integer id;
    /**
     * user ID
     */
    private Integer userId;
    /**
     * user name
     */
    private String userName;
    /**
     * user avatar
     */
    private String userAvatar;
    /**
     * comment content
     */
    private String content;
    /**
     * 回复框显示状态
     */
    private Boolean showReplyInput;
    /**
     * 一共拥有的子级评论数
     */
    private Integer childTotal;
    /**
     * user是否已经点赞
     */
    private Boolean upvoteFlag;
    /**
     * 点赞列表
     */
    private String upvoteList;
    /**
     * 点赞量
     */
    private Integer upvoteCount;
    /**
     * 评论时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 子级评论
     */
    private List<CommentChildVO> commentChildVOS;

}
