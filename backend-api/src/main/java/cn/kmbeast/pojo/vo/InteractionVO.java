package cn.kmbeast.pojo.vo;

import cn.kmbeast.pojo.entity.Cookbook;
import cn.kmbeast.pojo.entity.Interaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * interaction VO
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InteractionVO extends Interaction {


    /**
     * user name
     */
    private String userName;

    /**
     * user avatar
     */
    private String userAvatar;

    /**
     * title
     */
    private String contentTitle;
}
