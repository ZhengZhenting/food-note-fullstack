package cn.kmbeast.service.impl;

import cn.kmbeast.context.LocalThreadHolder;
import cn.kmbeast.mapper.CookbookMapper;
import cn.kmbeast.mapper.CookbookNutrimentMapper;
import cn.kmbeast.pojo.api.ApiResult;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.CookbookNutrimentQueryDto;
import cn.kmbeast.pojo.dto.query.extend.CookbookQueryDto;
import cn.kmbeast.pojo.entity.CookbookNutriment;
import cn.kmbeast.pojo.vo.CookbookNutrimentVO;
import cn.kmbeast.pojo.vo.CookbookVO;
import cn.kmbeast.service.CookbookNutrimentService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * cookbookNutriment service interface implementation
 */
@Service
public class CookbookNutrimentServiceImpl implements CookbookNutrimentService {

    @Resource
    private CookbookNutrimentMapper cookbookNutrimentMapper;
    @Resource
    private CookbookMapper cookbookMapper;

    /**
     * save
     * @param cookbookNutriment
     * @return Result<String>
     */
    @Override
    public Result<String> save(CookbookNutriment cookbookNutriment) {
        //检查输入是否为空
        if(!StringUtils.hasText(cookbookNutriment.getG100Value())){
            return ApiResult.error("value cannot be empty");
        }
        //检查该食谱的营养素是否已经存在，已存在不能重复添加
        CookbookNutrimentQueryDto queryDto = new CookbookNutrimentQueryDto();
        BeanUtils.copyProperties(cookbookNutriment, queryDto);
        Integer queryCount = cookbookNutrimentMapper.queryCount(queryDto);
        if(queryCount != 0){ return ApiResult.error("this nutriment already exist"); }

        cookbookNutrimentMapper.save(cookbookNutriment);
        return ApiResult.success();
    }

    /**
     * update
     * @param cookbookNutriment
     * @return Result<String>
     */
    @Override
    public Result<String> update(CookbookNutriment cookbookNutriment) {
        cookbookNutrimentMapper.update(cookbookNutriment);
        return ApiResult.success();
    }

    /**
     * delete
     * @param ids
     * @return Result<String>
     */
    @Override
    public Result<String> batchDelete(List<Integer> ids) {
        cookbookNutrimentMapper.batchDelete(ids);
        return ApiResult.success();
    }

    /**
     * searching
     * @param queryDto
     * @return Result<List<CookbookVO>>
     */
    @Override
    public Result<List<CookbookNutrimentVO>> query(CookbookNutrimentQueryDto queryDto) {
        List<CookbookNutrimentVO> cookbookNutrimentList = cookbookNutrimentMapper.query(queryDto);
        Integer totalCount = cookbookNutrimentMapper.queryCount(queryDto);
        return ApiResult.success(cookbookNutrimentList, totalCount);
    }

    /**
     * searching cookbookNutriment for user
     *
     * @return Result<List < CookbookNutrimentVO>> 响应结果
     */
    @Override
    public Result<List<CookbookNutrimentVO>> queryUser() {
        //searching for cookbook according to userId
        CookbookQueryDto cookbookQueryDto = new CookbookQueryDto();
        cookbookQueryDto.setUserId(LocalThreadHolder.getUserId());
        //my cookbook
        List<CookbookVO> cookbookVOS = cookbookMapper.query(cookbookQueryDto);
        //get a list of ids
        List<Integer> cookbookIds = cookbookVOS.stream()
                .map(CookbookVO::getId).collect(Collectors.toList());
        List<CookbookNutrimentVO> cookbookNutrimentVOS = cookbookNutrimentMapper.queryByCookbookIds(cookbookIds);
        return ApiResult.success(cookbookNutrimentVOS);
    }
}
