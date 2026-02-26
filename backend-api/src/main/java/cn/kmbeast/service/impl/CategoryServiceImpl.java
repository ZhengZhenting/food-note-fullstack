package cn.kmbeast.service.impl;

import cn.kmbeast.mapper.CategoryMapper;
import cn.kmbeast.pojo.api.ApiResult;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.CategoryQueryDto;
import cn.kmbeast.pojo.entity.Category;
import cn.kmbeast.service.CategoryService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * category service interface implementation
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    /**
     * save
     * @param category
     * @return Result<String>
     */
    @Override
    public Result<String> save(Category category) {
        categoryMapper.save(category);
        return ApiResult.success();
    }

    /**
     * update
     * @param category
     * @return Result<String>
     */
    @Override
    public Result<String> update(Category category) {
        categoryMapper.update(category);
        return ApiResult.success();
    }

    /**
     * save
     * @param ids
     * @return Result<String>
     */
    @Override
    public Result<String> batchDelete(List<Integer> ids) {
        categoryMapper.batchDelete(ids);
        return ApiResult.success();
    }

    /**
     * save
     * @param categoryQueryDto
     * @return Result<List<Category>>
     */
    @Override
    public Result<List<Category>> query(CategoryQueryDto categoryQueryDto) {
        List<Category> categoryList = categoryMapper.query(categoryQueryDto);
        Integer totalCount = categoryMapper.queryCount(categoryQueryDto);
        return ApiResult.success(categoryList, totalCount);
    }
}
