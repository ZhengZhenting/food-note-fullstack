package cn.kmbeast.service.impl;

import cn.kmbeast.mapper.*;
import cn.kmbeast.pojo.api.ApiResult;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.*;
import cn.kmbeast.pojo.vo.ChartVO;
import cn.kmbeast.service.ViewsService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 首页可视化
 */
@Service
public class ViewsServiceImpl implements ViewsService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private GourmetMapper gourmetMapper;
    @Resource
    private CookbookMapper cookbookMapper;
    @Resource
    private NutrimentMapper nutrimentMapper;
    @Resource
    private ContentNetMapper contentNetMapper;
    @Resource
    private InteractionMapper interactionMapper;

    /**
     * 统计一些系统的基础数据
     *
     * @return Result<List < ChartVO>>
     */
    @Override
    public Result<List<ChartVO>> staticControls() {
        List<ChartVO> chartVOS = new ArrayList<>();
        // 1. number of users
        UserQueryDto userQueryDto = new UserQueryDto();
        int userCount = userMapper.queryCount(userQueryDto);
        change(userCount, "Users", chartVOS);


        // 2. number of gourmets
        GourmetQueryDto gourmetQueryDto = new GourmetQueryDto();
        int gourmetCount = gourmetMapper.queryCount(gourmetQueryDto);
        change(gourmetCount,"Gourmet",chartVOS);


        // 3. number of cookbook
        CookbookQueryDto cookbookQueryDto = new CookbookQueryDto();
        int cookbookCount = cookbookMapper.queryCount(cookbookQueryDto);
        change(cookbookCount,"Cookbook",chartVOS);


        // 4. number of nutriment
        NutrimentQueryDto nutrimentQueryDto = new NutrimentQueryDto();
        int nutrimentCount = nutrimentMapper.queryCount(nutrimentQueryDto);
        change(nutrimentCount,"Nutriment",chartVOS);


        // 5. number of content sharing
        ContentNetQueryDto contentNetQueryDto = new ContentNetQueryDto();
        int contentNetCount = contentNetMapper.queryCount(contentNetQueryDto);
        change(contentNetCount,"Content",chartVOS);


        // 6. number of interaction
        InteractionQueryDto interactionQueryDto = new InteractionQueryDto();
        int interactionCount = interactionMapper.queryCount(interactionQueryDto);
        change(interactionCount,"Interaction",chartVOS);
        return ApiResult.success(chartVOS);
    }

    /**
     * 参数处理
     *
     * @param count    总数目
     * @param name     统计项
     * @param chartVOS 装它们的集合
     */
    private void change(Integer count, String name, List<ChartVO> chartVOS) {
        ChartVO chartVO = new ChartVO(name, count);
        chartVOS.add(chartVO);
    }


}
