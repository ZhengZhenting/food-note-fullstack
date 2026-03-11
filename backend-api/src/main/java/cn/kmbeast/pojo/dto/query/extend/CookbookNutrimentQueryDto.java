package cn.kmbeast.pojo.dto.query.extend;

import cn.kmbeast.pojo.dto.query.base.QueryDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Cookbook Nutriment Query Dto
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CookbookNutrimentQueryDto extends QueryDto {
    /**
     * cook book id
     */
    private Integer cookbookId;

    /**
     * nutriment id
     */
    private Integer nutrimentId;
}
