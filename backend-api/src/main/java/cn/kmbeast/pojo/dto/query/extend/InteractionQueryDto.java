package cn.kmbeast.pojo.dto.query.extend;

import cn.kmbeast.pojo.dto.query.base.QueryDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * searching for interactions
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InteractionQueryDto extends QueryDto {
    /**
     * user ID
     */
    private Integer userId;

    /**
     * content ID
     */
    private Integer contentId;

    /**
     * query days
     */
    private Integer day;

    /**
     * interaction type
     */
    private Integer type;
}
