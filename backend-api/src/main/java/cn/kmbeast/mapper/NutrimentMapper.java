package cn.kmbeast.mapper;

import cn.kmbeast.pojo.dto.query.extend.NutrimentQueryDto;
import cn.kmbeast.pojo.entity.Nutriment;
import cn.kmbeast.pojo.vo.NutrimentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * food nutriment mapper
 */
@Mapper
public interface NutrimentMapper {
    void save(Nutriment nutriment);
    void update(Nutriment nutriment);
    void batchDelete(@Param(value="ids") List<Integer> ids);

//    searching for data
    List<NutrimentVO> query(NutrimentQueryDto nutrimentQueryDto);

//    searching for the amount of data (paging)
    Integer queryCount(NutrimentQueryDto nutrimentQueryDto);
}
