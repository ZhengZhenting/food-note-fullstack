package cn.kmbeast.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * Evaluation Entity
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Evaluations {
    /**
     * prime key
     */
    private Integer id;

    /**
     * parent comment ID
     */
    private Integer parentId;

    /**
     * comment ID
     */
    private Integer commenterId;

    /**
     * reply ID
     */
    private Integer replierId;

    /**
     * content type
     */
    private String contentType;

    /**
     * content
     */
    private String content;

    /**
     * content ID
     */
    private Integer contentId;

    /**
     * vote list (seperated by ',')
     */
    private String upvoteList;

    /**
     * create time
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
