package cn.kmbeast.pojo.vo;

import cn.kmbeast.pojo.entity.DietHistory;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 *Diet History VO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DietHistoryVO extends DietHistory {
    /**
     * user Name
     */
    private String userName;

    /**
     * cookbook cover
     */
    private String cookbookCover;

    /**
     * cookbook title
     */
    private String cookbookTitle;

    /**
     * nutriment ID
     */
    private Integer nutrimentId;

    /**
     * nutriment per 100g
     */
    private String g100Value;

    /**
     * nutriment name
     */
    private String nutrimentName;

    /**
     * nutriment unit
     */
    private String nutrimentUnit;
}
