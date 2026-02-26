package cn.kmbeast.pojo.vo;

import cn.kmbeast.pojo.entity.Gourmet;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Gourmet VO
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GourmetVO extends Gourmet {
    /**
     * user name
     */
    private String userName;

    /**
     * category name
     */
    private String categoryName;

    /**
     * user avatar
     */
    private String userAvatar;
}
