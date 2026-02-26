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
public class Nutriment {

    /**
     * prime key
     */
    private Integer id;

    /**
     * user id
     */
    private Integer userId;

    /**
     * name
     */
    private String name;

    /**
     * detail
     */
    private String detail;

    /**
     * unit
     */
    private String unit;

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
