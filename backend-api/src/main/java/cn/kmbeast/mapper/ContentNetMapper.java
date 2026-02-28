package cn.kmbeast.mapper;

import cn.kmbeast.pojo.dto.query.extend.ContentNetQueryDto;
import cn.kmbeast.pojo.entity.ContentNet;
import cn.kmbeast.pojo.vo.ContentNetVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * content mapper
 */
@Mapper
public interface ContentNetMapper {
    void save(ContentNet contentNet);
    void update(ContentNet contentNet);
    void batchDelete(@Param(value="ids") List<Integer> ids);

//    searching for data
    List<ContentNetVO> query(ContentNetQueryDto contentNetQueryDto);

//    searching for the amount of data (paging)
    Integer queryCount(ContentNetQueryDto contentNetQueryDto);
}
