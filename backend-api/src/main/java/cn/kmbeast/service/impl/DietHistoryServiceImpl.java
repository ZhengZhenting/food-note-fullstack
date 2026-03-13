package cn.kmbeast.service.impl;

import cn.kmbeast.context.LocalThreadHolder;
import cn.kmbeast.mapper.DietHistoryMapper;
import cn.kmbeast.pojo.api.ApiResult;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.DietHistoryQueryDto;
import cn.kmbeast.pojo.entity.DietHistory;
import cn.kmbeast.pojo.vo.DietHistoryVO;
import cn.kmbeast.service.DietHistoryService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
        dietHistory.setId(LocalThreadHolder.getUserId());
        dietHistory.setCreateTime(LocalDateTime.now());
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
}
