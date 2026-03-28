package cn.kmbeast.service;

import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.CategoryQueryDto;
import cn.kmbeast.pojo.entity.Category;
import cn.kmbeast.pojo.entity.Gourmet;
import cn.kmbeast.pojo.vo.GourmetVO;

import java.util.List;

/**
 * food category interface
 */

public interface RecommendService {

    Result<List<GourmetVO>> getRecommendations(Integer gourmetId, int topN);
}
