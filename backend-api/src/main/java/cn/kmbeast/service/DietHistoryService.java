package cn.kmbeast.service;

import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.DietHistoryQueryDto;
import cn.kmbeast.pojo.dto.query.extend.DietQueryDto;
import cn.kmbeast.pojo.entity.DietHistory;
import cn.kmbeast.pojo.vo.DietHistoryVO;
import cn.kmbeast.pojo.vo.DietNutrimentVO;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * food dietHistory interface
 */

public interface DietHistoryService {

    Result<String> save(DietHistory dietHistory);

    Result<String> update(DietHistory dietHistory);

    Result<String> batchDelete(List<Integer> ids);

    Result<List<DietHistoryVO>> query(DietHistoryQueryDto dietHistoryQueryDto);

    Result<List<DietHistoryVO>> queryUser( DietHistoryQueryDto dietHistoryQueryDto);

    Result<List<DietNutrimentVO>> queryDietNutrimentInfo(DietQueryDto dietQueryDto);
}
