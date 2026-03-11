package cn.kmbeast.service.impl;

import cn.kmbeast.context.LocalThreadHolder;
import cn.kmbeast.mapper.NutrimentMapper;
import cn.kmbeast.pojo.api.ApiResult;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.CookbookQueryDto;
import cn.kmbeast.pojo.dto.query.extend.NutrimentQueryDto;
import cn.kmbeast.pojo.entity.Nutriment;
import cn.kmbeast.pojo.vo.CookbookVO;
import cn.kmbeast.pojo.vo.NutrimentVO;
import cn.kmbeast.pojo.vo.SelectedVO;
import cn.kmbeast.service.NutrimentService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * nutriment service interface implementation
 */
@Service
public class NutrimentServiceImpl implements NutrimentService {

    @Resource
    private NutrimentMapper nutrimentMapper;

    /**
     * save
     * @param nutriment
     * @return Result<String>
     */
    @Override
    public Result<String> save(Nutriment nutriment) {
        nutriment.setUserId(LocalThreadHolder.getUserId());
        nutriment.setCreateTime(LocalDateTime.now());
        nutrimentMapper.save(nutriment);
        return ApiResult.success();
    }

    /**
     * update
     * @param nutriment
     * @return Result<String>
     */
    @Override
    public Result<String> update(Nutriment nutriment) {
        nutrimentMapper.update(nutriment);
        return ApiResult.success();
    }

    /**
     * delete
     * @param ids
     * @return Result<String>
     */
    @Override
    public Result<String> batchDelete(List<Integer> ids) {
        nutrimentMapper.batchDelete(ids);
        return ApiResult.success();
    }

    /**
     * searching
     * @param nutrimentQueryDto
     * @return Result<List<Nutriment>>
     */
    @Override
    public Result<List<NutrimentVO>> query(NutrimentQueryDto nutrimentQueryDto) {
        List<NutrimentVO> nutrimentList = nutrimentMapper.query(nutrimentQueryDto);
        Integer totalCount = nutrimentMapper.queryCount(nutrimentQueryDto);
        return ApiResult.success(nutrimentList, totalCount);
    }

    /**
     * searching nutriment list
     *
     * @return Result<List < SelectedVO>> 响应结果
     */
    @Override
    public Result<List<SelectedVO>> querySelectedItems() {
        NutrimentQueryDto nutrimentQueryDto = new NutrimentQueryDto();
        List<NutrimentVO> nutrimentList = nutrimentMapper.query(nutrimentQueryDto);
        List<SelectedVO> selectedVOS = nutrimentList.stream().map(nutrimentVO -> new SelectedVO(
                nutrimentVO.getId(),
                nutrimentVO.getName()
        )).collect(Collectors.toList());
        return ApiResult.success(selectedVOS);
    }
}
