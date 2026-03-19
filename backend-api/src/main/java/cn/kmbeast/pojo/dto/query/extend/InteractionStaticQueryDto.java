package cn.kmbeast.pojo.dto.query.extend;

import cn.kmbeast.pojo.dto.query.base.QueryDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * searching for food categories
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InteractionStaticQueryDto extends QueryDto {
    /**
     * interaction dto
     */
    private InteractionQueryDto interactionQueryDto;

    /**
     * gourmet list
     */
    private List<Integer> gourmetIds;
}
