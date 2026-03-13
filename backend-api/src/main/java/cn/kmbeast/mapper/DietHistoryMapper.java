package cn.kmbeast.mapper;

import cn.kmbeast.pojo.dto.query.extend.DietHistoryQueryDto;
import cn.kmbeast.pojo.entity.DietHistory;
import cn.kmbeast.pojo.vo.DietHistoryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * food dietHistory mapper
 */
@Mapper
public interface DietHistoryMapper {
    void save(DietHistory dietHistory);
    void update(DietHistory dietHistory);
    void batchDelete(@Param(value="ids") List<Integer> ids);

//    searching for data
    List<DietHistoryVO> query(DietHistoryQueryDto dietHistoryQueryDto);

//    searching for the amount of data (paging)
    Integer queryCount(DietHistoryQueryDto dietHistoryQueryDto);
}
