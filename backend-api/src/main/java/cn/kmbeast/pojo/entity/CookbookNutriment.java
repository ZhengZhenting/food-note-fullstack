package cn.kmbeast.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * User Entity
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CookbookNutriment {

    /**
     * prime key
     */
    private Integer id;

    /**
     * cook book id
     */
    private Integer cookbookId;

    /**
     * nutriment id
     */
    private Integer nutrimentId;

    /**
     * value of nutriment every 100g
     */
    private String g100Value;

}
