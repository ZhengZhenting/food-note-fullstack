package cn.kmbeast.mapper;

import cn.kmbeast.pojo.dto.query.extend.GourmetQueryDto;
import cn.kmbeast.pojo.entity.Gourmet;
import cn.kmbeast.pojo.vo.GourmetVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * food gourmet mapper
 */
@Mapper
public interface GourmetMapper {
    void save(Gourmet gourmet);
    void update(Gourmet gourmet);
    void batchDelete(@Param(value="ids") List<Integer> ids);

//    searching for data
    List<GourmetVO> query(GourmetQueryDto gourmetQueryDto);

//    searching for the amount of data (paging)
    Integer queryCount(GourmetQueryDto gourmetQueryDto);
}
