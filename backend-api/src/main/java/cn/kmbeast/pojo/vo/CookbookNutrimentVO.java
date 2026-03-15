package cn.kmbeast.pojo.vo;


import cn.kmbeast.pojo.entity.CookbookNutriment;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Cookbook Nutriment VO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CookbookNutrimentVO extends CookbookNutriment {
    /**
     * cook book name
     */
    private String cookbookName;

    /**
     * nutriment name
     */
    private String nutrimentName;

    /**
     * nutriment unit
     */
    private String nutrimentUnit;

}
