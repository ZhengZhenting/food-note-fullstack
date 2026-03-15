package cn.kmbeast.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * diet nutriment vo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DietNutrimentVO {
    /**
     * nutriment ID
     */
    private Integer nutrimentId;

    /**
     * nutriment name
     */
    private String nutrimentName;

    /**
     * nutriment value
     */
    private Integer value;

    /**
     * create time
     */
    private String time;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DietNutrimentVO that = (DietNutrimentVO) o;
        return nutrimentId == that.nutrimentId && Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nutrimentId, time);
    }
}
