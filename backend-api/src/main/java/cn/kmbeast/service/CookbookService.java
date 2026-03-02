package cn.kmbeast.service;

import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.CookbookQueryDto;
import cn.kmbeast.pojo.entity.Cookbook;
import cn.kmbeast.pojo.vo.CookbookVO;

import java.util.List;

/**
 * food category interface
 */

public interface CookbookService {

    Result<String> save(Cookbook category);

    Result<String> update(Cookbook category);

    Result<String> batchDelete(List<Integer> ids);

    Result<List<CookbookVO>> query(CookbookQueryDto categoryQueryDto);
}
