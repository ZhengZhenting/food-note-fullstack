package cn.kmbeast.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Cook Book Entity
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cookbook {

    /**
     * prime key
     */
    private Integer id;

    /**
     * user id
     */
    private Integer userId;

    /**
     * title
     */
    private String title;

    /**
     * category id
     */
    private Integer categoryId;

    /**
     * content
     */
    private String content;

    /**
     * is published
     */
    private Boolean isPublish;

    /**
     * create time
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
