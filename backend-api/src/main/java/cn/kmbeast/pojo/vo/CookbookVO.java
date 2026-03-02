package cn.kmbeast.pojo.vo;

import cn.kmbeast.pojo.entity.ContentNet;
import cn.kmbeast.pojo.entity.Cookbook;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * cookbook VO
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CookbookVO extends Cookbook {

    /**
     * category name
     */
    private String categoryName;

    /**
     * user name
     */
    private String userName;

    /**
     * user avatar
     */
    private String userAvatar;
}
