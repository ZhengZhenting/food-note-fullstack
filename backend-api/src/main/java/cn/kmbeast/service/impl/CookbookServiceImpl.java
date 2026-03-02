package cn.kmbeast.service.impl;

import cn.kmbeast.context.LocalThreadHolder;
import cn.kmbeast.mapper.CookbookMapper;
import cn.kmbeast.pojo.api.ApiResult;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.CookbookQueryDto;
import cn.kmbeast.pojo.entity.Cookbook;
import cn.kmbeast.pojo.vo.CookbookVO;
import cn.kmbeast.service.CookbookService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * cookbook service interface implementation
 */
@Service
public class CookbookServiceImpl implements CookbookService {

    @Resource
    private CookbookMapper cookbookMapper;

    /**
     * save
     * @param cookbook
     * @return Result<String>
     */
    @Override
    public Result<String> save(Cookbook cookbook) {
        cookbook.setUserId(LocalThreadHolder.getUserId());
        cookbook.setCreateTime(LocalDateTime.now());
        cookbookMapper.save(cookbook);
        return ApiResult.success();
    }

    /**
     * update
     * @param cookbook
     * @return Result<String>
     */
    @Override
    public Result<String> update(Cookbook cookbook) {
        cookbookMapper.update(cookbook);
        return ApiResult.success();
    }

    /**
     * delete
     * @param ids
     * @return Result<String>
     */
    @Override
    public Result<String> batchDelete(List<Integer> ids) {
        cookbookMapper.batchDelete(ids);
        return ApiResult.success();
    }

    /**
     * searching
     * @param cookbookQueryDto
     * @return Result<List<Cookbook>>
     */
    @Override
    public Result<List<CookbookVO>> query(CookbookQueryDto cookbookQueryDto) {
        List<CookbookVO> cookbookList = cookbookMapper.query(cookbookQueryDto);
        Integer totalCount = cookbookMapper.queryCount(cookbookQueryDto);
        return ApiResult.success(cookbookList, totalCount);
    }
}
