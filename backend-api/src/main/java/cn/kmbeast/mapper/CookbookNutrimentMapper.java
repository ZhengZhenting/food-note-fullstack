package cn.kmbeast.mapper;

import cn.kmbeast.pojo.dto.query.extend.CookbookNutrimentQueryDto;
import cn.kmbeast.pojo.entity.CookbookNutriment;
import cn.kmbeast.pojo.vo.CookbookNutrimentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * cookbook nutriment mapper
 */
@Mapper
public interface CookbookNutrimentMapper {
    void save(CookbookNutriment cookbookNutriment);
    void update(CookbookNutriment cookbookNutriment);
    void batchDelete(@Param(value="ids") List<Integer> ids);

//    searching for data
    List<CookbookNutrimentVO> query(CookbookNutrimentQueryDto queryDto);

//    searching for the amount of data (paging)
    Integer queryCount(CookbookNutrimentQueryDto queryDto);

    List<CookbookNutrimentVO> queryByCookbookIds(@Param(value="ids") List<Integer> ids);
}
