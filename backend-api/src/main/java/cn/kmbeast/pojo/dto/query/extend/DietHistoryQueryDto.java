package cn.kmbeast.pojo.dto.query.extend;

import cn.kmbeast.pojo.dto.query.base.QueryDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Diet History Query Dto
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DietHistoryQueryDto extends QueryDto {
    /**
     * cook book id
     */
    private Integer cookbookId;

    /**
     * user id
     */
    private Integer userId;
}
