package cn.kmbeast.service;

import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.CategoryQueryDto;
import cn.kmbeast.pojo.dto.query.extend.CookbookNutrimentQueryDto;
import cn.kmbeast.pojo.entity.Category;
import cn.kmbeast.pojo.entity.Cookbook;
import cn.kmbeast.pojo.entity.CookbookNutriment;
import cn.kmbeast.pojo.vo.CookbookNutrimentVO;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * cookbook nutriment interface
 */

public interface CookbookNutrimentService {

    Result<String> save(CookbookNutriment cookbookNutriment);

    Result<String> update(CookbookNutriment cookbookNutriment);

    Result<String> batchDelete(List<Integer> ids);

    Result<List<CookbookNutrimentVO>> query(CookbookNutrimentQueryDto cookbookNutrimentQueryDto);

    Result<List<CookbookNutrimentVO>> queryUser();
}
