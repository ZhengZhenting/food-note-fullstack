package cn.kmbeast.pojo.vo;

import cn.kmbeast.pojo.entity.Gourmet;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Gourmet List VO
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GourmetListVO {
    /**
     * prime key
     */
    private Integer id;

    /**
     * title
     */
    private String title;

    /**
     * cover
     */
    private String cover;

    /**
     * detail
     */
    private String detail;

    /**
     * user name
     */
    private String userName;

    /**
     * user avatar
     */
    private String userAvatar;

    /**
     * create time
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
