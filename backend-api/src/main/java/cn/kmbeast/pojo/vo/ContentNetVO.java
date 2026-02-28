package cn.kmbeast.pojo.vo;

import cn.kmbeast.pojo.entity.ContentNet;
import cn.kmbeast.pojo.entity.Gourmet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * content VO
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentNetVO extends ContentNet {

    /**
     * gourmet title
     */
    private String gourmetTitle;

    /**
     * user name
     */
    private String userName;

    /**
     * user avatar
     */
    private String userAvatar;
}
