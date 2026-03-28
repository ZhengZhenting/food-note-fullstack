package cn.kmbeast.service.impl;

import cn.kmbeast.mapper.CategoryMapper;
import cn.kmbeast.mapper.GourmetMapper;
import cn.kmbeast.mapper.InteractionMapper;
import cn.kmbeast.pojo.api.ApiResult;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.CategoryQueryDto;
import cn.kmbeast.pojo.dto.query.extend.GourmetQueryDto;
import cn.kmbeast.pojo.entity.Category;
import cn.kmbeast.pojo.entity.Gourmet;
import cn.kmbeast.pojo.entity.Interaction;
import cn.kmbeast.pojo.vo.GourmetVO;
import cn.kmbeast.service.CategoryService;
import cn.kmbeast.service.RecommendService;
import cn.kmbeast.utils.GourmetRecommendUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * category service interface implementation
 */
@Service
public class RecommendServiceImpl implements RecommendService {

    @Resource
    private GourmetMapper gourmetMapper;

    @Override
    public Result<List<GourmetVO>> getRecommendations(Integer gourmetId, int topN) {
        //get current gourmet
        GourmetQueryDto gourmetQueryDto = new GourmetQueryDto();
        gourmetQueryDto.setId(gourmetId);
        List<GourmetVO> gourmetVOList = gourmetMapper.query(gourmetQueryDto);
        GourmetVO target = gourmetVOList.get(0);

        //get all gourmet
        GourmetQueryDto allDto = new GourmetQueryDto();
        allDto.setIsPublish(true);
        allDto.setIsAudit(true);
        List<GourmetVO> candidates = gourmetMapper.query(allDto);

        //get List<GourmetScore>
        List<GourmetRecommendUtil.GourmetScore> scored =
                GourmetRecommendUtil.recommend(target, candidates, topN);

        //get a list of all recommended gourmets' ids
        List<Integer> recommendIds = scored.stream()
                .map(s -> s.getGourmet().getId())
                .collect(Collectors.toList());

        //get a list those id's GourmetVO
        List<GourmetVO> result = gourmetMapper.queryByIds(recommendIds);

        return ApiResult.success(result);
    }
}
