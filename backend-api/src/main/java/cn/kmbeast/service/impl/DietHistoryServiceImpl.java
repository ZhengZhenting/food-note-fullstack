package cn.kmbeast.service.impl;

import cn.kmbeast.context.LocalThreadHolder;
import cn.kmbeast.mapper.DietHistoryMapper;
import cn.kmbeast.pojo.api.ApiResult;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.base.QueryDto;
import cn.kmbeast.pojo.dto.query.extend.DietHistoryQueryDto;
import cn.kmbeast.pojo.dto.query.extend.DietQueryDto;
import cn.kmbeast.pojo.entity.DietHistory;
import cn.kmbeast.pojo.vo.DietHistoryVO;
import cn.kmbeast.pojo.vo.DietNutrimentVO;
import cn.kmbeast.service.DietHistoryService;
import cn.kmbeast.utils.DateUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * dietHistory service interface implementation
 */
@Service
public class DietHistoryServiceImpl implements DietHistoryService {

    @Resource
    private DietHistoryMapper dietHistoryMapper;

    /**
     * save
     * @param dietHistory
     * @return Result<String>
     */
    @Override
    public Result<String> save(DietHistory dietHistory) {
        dietHistory.setUserId(LocalThreadHolder.getUserId());
        dietHistory.setCreateTime(LocalDateTime.now());
        dietHistoryMapper.save(dietHistory);
        return ApiResult.success();
    }

    /**
     * update
     * @param dietHistory
     * @return Result<String>
     */
    @Override
    public Result<String> update(DietHistory dietHistory) {
        dietHistoryMapper.update(dietHistory);
        return ApiResult.success();
    }

    /**
     * delete
     * @param ids
     * @return Result<String>
     */
    @Override
    public Result<String> batchDelete(List<Integer> ids) {
        dietHistoryMapper.batchDelete(ids);
        return ApiResult.success();
    }

    /**
     * searching
     * @param dietHistoryQueryDto
     * @return Result<List<DietHistoryVO>>
     */
    @Override
    public Result<List<DietHistoryVO>> query(DietHistoryQueryDto dietHistoryQueryDto) {
        List<DietHistoryVO> dietHistoryList = dietHistoryMapper.query(dietHistoryQueryDto);
        Integer totalCount = dietHistoryMapper.queryCount(dietHistoryQueryDto);
        return ApiResult.success(dietHistoryList, totalCount);
    }

    /**
     * searching dietHistory for user
     *
     * @param dietHistoryQueryDto 查询参数
     * @return Result<List < DietHistoryVO>> 响应结果
     */
    @Override
    public Result<List<DietHistoryVO>> queryUser(DietHistoryQueryDto dietHistoryQueryDto) {
        dietHistoryQueryDto.setUserId(LocalThreadHolder.getUserId()); //get user ID
        List<DietHistoryVO> dietHistoryVOS = dietHistoryMapper.query(dietHistoryQueryDto);
        Integer totalCount = dietHistoryMapper.queryCount(dietHistoryQueryDto);
        return ApiResult.success(dietHistoryVOS, totalCount);
    }

    /**
     * searching dietHistory for user
     *
     * @param dietHistoryQueryDto 查询参数
     * @return Result<List < DietHistoryVO>> 响应结果
     */
    @Override
    public Result<List<DietHistoryVO>> queryUserDiet(DietHistoryQueryDto dietHistoryQueryDto) {
        dietHistoryQueryDto.setUserId(LocalThreadHolder.getUserId()); //get user ID
        List<DietHistoryVO> dietHistoryVOS = dietHistoryMapper.queryUserDiet(dietHistoryQueryDto);
        Integer totalCount = dietHistoryVOS.size();
        return ApiResult.success(dietHistoryVOS, totalCount);
    }

    /**
     * searching nutriment
     *
     * @param dietQueryDto 查询参数
     * @return Result<List < DietNutrimentVO>> 响应结果
     */
    @Override
    public Result<List<DietNutrimentVO>> queryDietNutrimentInfo(DietQueryDto dietQueryDto) {
        DietHistoryQueryDto dietHistoryQueryDto = createDietHistoryQueryDto(dietQueryDto);
        List<DietHistoryVO> historyVOS = dietHistoryMapper.query(dietHistoryQueryDto);
        List<DietNutrimentVO> dietNutrimentVOS = parseNutriment(historyVOS, dietQueryDto);
        Collections.reverse(dietNutrimentVOS);
        return ApiResult.success(dietNutrimentVOS);
    }

    /**
     * change Data type for diagrams
     *
     * @param historyVOS original Data from Databank
     * @return Result<List < DietNutrimentVO>> result
     */
    private List<DietNutrimentVO> parseNutriment(List<DietHistoryVO> historyVOS, DietQueryDto dietQueryDto) {
        List<DietNutrimentVO> dietNutrimentVOS = new ArrayList<>();
        for(DietHistoryVO historyVO : historyVOS){
            if (!StringUtils.hasText(historyVO.getNutrimentName())) {
                continue;
            }
            DietNutrimentVO dietNutrimentVO = ceateDietNutrimentVO(historyVO);
            dietNutrimentVOS.add(dietNutrimentVO);
        }

        selectedNutriment(dietNutrimentVOS, dietQueryDto);
        return diffSum(dietNutrimentVOS);
    }

    /**
     * combining the record of the same nutriment according to nutriment ID and createTime
     * get the sum value of the same nutriment before combining
     *
     * @param dietNutrimentVOS
     * @return List<DietNutrimentVO>
     */
    public List<DietNutrimentVO> diffSum(List<DietNutrimentVO>  dietNutrimentVOS) {
        Map<DietNutrimentVO, Integer> intermediateMap = dietNutrimentVOS.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        DietNutrimentVO::getValue, //get value
                        Integer::sum    //add value
                ));
        // 由于DietNutrimentVO是不可变的，并且重写了equals和hashCode，
        // 中间Map的键实际上是唯一的DietNutrimentVO对象，其value字段已经被累加。
        // 由Map转回List
        return intermediateMap.entrySet().stream()
                .map(entry -> new DietNutrimentVO(
                        entry.getKey().getNutrimentId(),
                        entry.getKey().getNutrimentName(),
                        entry.getValue(),
                        entry.getKey().getTime()))
                .collect(Collectors.toList());
    }

    /**
     * new nutriment input
     *
     * @param dietHistoryVO
     * @return DietNutrimentVO
     */
    private DietNutrimentVO ceateDietNutrimentVO(DietHistoryVO dietHistoryVO) {
        DietNutrimentVO dietNutrimentVO = new DietNutrimentVO();
        String g100Value = dietHistoryVO.getG100Value();
        String value = dietHistoryVO.getValue();
        double inputValue = Double.parseDouble(value);
        double sumInput = (inputValue/100)*Double.parseDouble(g100Value);
        String recordTime = dietHistoryVO.getCreateTime().toString().substring(5,10);
        dietNutrimentVO.setNutrimentId(dietHistoryVO.getNutrimentId());
        dietNutrimentVO.setValue((int)sumInput);
        dietNutrimentVO.setNutrimentName(dietHistoryVO.getNutrimentName());
        dietNutrimentVO.setTime(recordTime);

        return dietNutrimentVO;
    }

    /**
     * according to query, get data for data bank searching
     *
     * @param dietQueryDto
     * @return DietHistoryQueryDto
     */
    private DietHistoryQueryDto createDietHistoryQueryDto(DietQueryDto dietQueryDto) {
        Integer day = dietQueryDto.getDay();
        QueryDto queryDto = DateUtil.startAndEndTime(day);
        LocalDateTime startTime = queryDto.getStartTime();
        LocalDateTime endTime = queryDto.getEndTime();
        DietHistoryQueryDto dietHistoryQueryDto = new DietHistoryQueryDto();
        BeanUtils.copyProperties(dietQueryDto, dietHistoryQueryDto);
        dietHistoryQueryDto.setStartTime(startTime);
        dietHistoryQueryDto.setEndTime(endTime);
        dietHistoryQueryDto.setUserId(LocalThreadHolder.getUserId());

        return dietHistoryQueryDto;
    }

    /**
     * filter data throught nutriment ID
     *
     * @param dietNutrimentVOS
     * @param dietQueryDto
     */
    private void selectedNutriment(List<DietNutrimentVO> dietNutrimentVOS,DietQueryDto dietQueryDto) {
        if(dietQueryDto.getNutrimentId() != null){
            List<DietNutrimentVO> nutrimentVOS = dietNutrimentVOS.stream().filter(dietNutrimentVO ->
                            Objects.equals(dietNutrimentVO.getNutrimentId(),dietQueryDto.getNutrimentId()))
                    .collect(Collectors.toList());
            dietNutrimentVOS.clear();
            dietNutrimentVOS.addAll(nutrimentVOS);
        }
    }
}
