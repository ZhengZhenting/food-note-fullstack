package cn.kmbeast.pojo.vo;

import cn.kmbeast.pojo.dto.query.extend.NutrimentQueryDto;
import cn.kmbeast.pojo.entity.Cookbook;
import cn.kmbeast.pojo.entity.Nutriment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * nutriment VO
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NutrimentVO extends Nutriment {

    /**
     * user name
     */
    private String userName;

    /**
     * user avatar
     */
    private String userAvatar;

}


