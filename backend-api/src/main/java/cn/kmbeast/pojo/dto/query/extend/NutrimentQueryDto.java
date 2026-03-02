package cn.kmbeast.pojo.dto.query.extend;

import cn.kmbeast.pojo.dto.query.base.QueryDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * searching for food cookbook
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NutrimentQueryDto extends QueryDto {
    /**
     * user id
     */
    private Integer userId;

    /**
     * name
     */
    private String name;


    /**
     * is published
     */
    private Boolean isPublish;

    /**
     * detail
     */
    private String detail;

    /**
     * unit
     */
    private String unit;

}
