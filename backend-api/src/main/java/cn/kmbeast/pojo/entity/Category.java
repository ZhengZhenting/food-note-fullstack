package cn.kmbeast.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Food Category Entity
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    /**
     * prime key
     */
    private Integer id;

    /**
     * category name
     */
    private String name;

}
