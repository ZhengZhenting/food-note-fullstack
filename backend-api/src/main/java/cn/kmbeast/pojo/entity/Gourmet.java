package cn.kmbeast.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * User Entity
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Gourmet {

    /**
     * prime key
     */
    private Integer id;

    /**
     * user id
     */
    private Integer userId;

    /**
     * category id
     */
    private Integer categoryId;

    /**
     * title
     */
    private String title;

    /**
     * content
     */
    private String content;

    /**
     * cover
     */
    private String cover;

    /**
     * is audit
     */
    private Boolean isAudit;

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
