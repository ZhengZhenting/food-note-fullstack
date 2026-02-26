package cn.kmbeast.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Content Sharring Entity
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContentNet {

    /**
     * prime key
     */
    private Integer id;

    /**
     * user id
     */
    private Integer userId;

    /**
     * gourmet id
     */
    private Integer gourmetId;

    /**
     * access code
     */
    private String accessCode;

    /**
     * valid day
     */
    private Integer validDay;

    /**
     * access password
     */
    private String accessPassword;

    /**
     * password auth
     */
    private Boolean passwordAuth;

    /**
     * create time
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
