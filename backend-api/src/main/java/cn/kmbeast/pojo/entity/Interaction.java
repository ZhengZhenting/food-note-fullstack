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
public class Interaction {

    /**
     * prime key
     */
    private Integer id;

    /**
     * type
     */
    private Integer type;

    /**
     * content type
     */
    private String contentType;

    /**
     * content id
     */
    private Integer contentId;

    /**
     * user id
     */
    private Integer userId;

    /**
     * score (rating)
     */
    private Integer score;

    /**
     * create time
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
