package cn.kmbeast.pojo.dto.query.extend;

import cn.kmbeast.pojo.dto.query.base.QueryDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * user nutriment query dto
 */
@Data
public class DietQueryDto{
    /**
     * user ID
     */
    private Integer userId;

    /**
     * nutriment ID
     */
    private Integer nutrimentId;

    /**
     * user ID
     */
    private Integer day;
}
