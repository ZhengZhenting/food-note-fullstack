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
}
