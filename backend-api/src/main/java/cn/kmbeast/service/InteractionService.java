package cn.kmbeast.service;

import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.CategoryQueryDto;
import cn.kmbeast.pojo.dto.query.extend.InteractionQueryDto;
import cn.kmbeast.pojo.entity.Category;
import cn.kmbeast.pojo.vo.GourmetListVO;
import cn.kmbeast.pojo.vo.InteractionVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * interaction interface
 */

public interface InteractionService {


    Result<List<InteractionVO>> query(InteractionQueryDto InteractionQueryDto);

    Result<Void> viewOperation(Integer contentId);

    Result<Integer> upvoteOperation(Integer contentId);

    Result<Integer> upvoteStatus(Integer contentId);

    Result<Integer> saveOperation(Integer contentId);

    Result<Integer> saveStatus(Integer contentId);

    Result<List<GourmetListVO>> queryCollectionList(InteractionQueryDto interactionQueryDto);
}
