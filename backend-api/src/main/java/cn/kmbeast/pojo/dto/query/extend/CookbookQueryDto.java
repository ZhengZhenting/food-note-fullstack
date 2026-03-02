package cn.kmbeast.pojo.dto.query.extend;

import cn.kmbeast.pojo.dto.query.base.QueryDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * searching for food cookbook
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CookbookQueryDto extends QueryDto {
    /**
     * user id
     */
    private Integer userId;

    /**
     * title
     */
    private String title;

    /**
     * category id
     */
    private Integer categoryId;

    /**
     * is published
     */
    private Boolean isPublish;

}
