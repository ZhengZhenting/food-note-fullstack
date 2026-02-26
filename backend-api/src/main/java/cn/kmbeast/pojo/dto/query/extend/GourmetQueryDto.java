package cn.kmbeast.pojo.dto.query.extend;

import cn.kmbeast.pojo.dto.query.base.QueryDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * searching for food categories
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GourmetQueryDto extends QueryDto {
    /**
     * user id
     */
    private Integer userId;

    /**
     * category id
     */
    private Integer categoryId;

    /**
     * title
     */
    private String title;

    /**
     * content
     */
    private String content;

    /**
     * cover
     */
    private String cover;

    /**
     * is audit
     */
    private Boolean isAudit;

    /**
     * is published
     */
    private Boolean isPublish;
}
