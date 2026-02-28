package cn.kmbeast.pojo.dto.query.extend;

import cn.kmbeast.pojo.dto.query.base.QueryDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * searching for content
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ContentNetQueryDto extends QueryDto {
    /**
     * access code
     */
    private String accessCode;


    /**
     * user id
     */
    private Integer userId;


    /**
     * gourmet id
     */
    private Integer gourmetId;
}
