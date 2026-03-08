package cn.kmbeast.mapper;

import cn.kmbeast.pojo.dto.query.extend.InteractionQueryDto;
import cn.kmbeast.pojo.entity.Interaction;
import cn.kmbeast.pojo.vo.InteractionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * food interaction mapper
 */
@Mapper
public interface InteractionMapper {
    void save(Interaction interaction);

    void batchDelete(@Param(value="ids") List<Integer> ids);

    void deleteByUserInfo(@Param(value="type") Integer type,
                          @Param(value="contentId") Integer contentId,
                          @Param(value= "userId") Integer userId);

//    searching for data
    List<InteractionVO> query(InteractionQueryDto interactionQueryDto);

//    searching for the amount of data (paging)
    Integer queryCount(InteractionQueryDto interactionQueryDto);
}
