package cn.kmbeast.mapper;

import cn.kmbeast.pojo.dto.query.extend.CategoryQueryDto;
import cn.kmbeast.pojo.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * food category mapper
 */
@Mapper
public interface CategoryMapper {
    void save(Category category);
    void update(Category category);
    void batchDelete(@Param(value="ids") List<Integer> ids);

//    searching for data
    List<Category> query(CategoryQueryDto categoryQueryDto);

//    searching for the amount of data (paging)
    Integer queryCount(CategoryQueryDto categoryQueryDto);
}
