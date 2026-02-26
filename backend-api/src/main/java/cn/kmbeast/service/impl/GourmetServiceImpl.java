package cn.kmbeast.service.impl;

import cn.kmbeast.mapper.CategoryMapper;
import cn.kmbeast.mapper.GourmetMapper;
import cn.kmbeast.pojo.api.ApiResult;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.CategoryQueryDto;
import cn.kmbeast.pojo.dto.query.extend.GourmetQueryDto;
import cn.kmbeast.pojo.entity.Category;
import cn.kmbeast.pojo.entity.Gourmet;
import cn.kmbeast.pojo.vo.GourmetVO;
import cn.kmbeast.service.CategoryService;
import cn.kmbeast.service.GourmetService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * gourmet service interface implementation
 */
@Service
public class GourmetServiceImpl implements GourmetService {

    @Resource
    private GourmetMapper gourmetMapper;

    /**
     * save
     * @param gourmet
     * @return Result<String>
     */
    @Override
    public Result<String> save(Gourmet gourmet) {
        gourmet.setCreateTime(LocalDateTime.now());
        gourmet.setIsAudit(false);
        gourmetMapper.save(gourmet);
        return ApiResult.success();
    }

    /**
     * update
     * @param gourmet
     * @return Result<String>
     */
    @Override
    public Result<String> update(Gourmet gourmet) {
        gourmetMapper.update(gourmet);
        return ApiResult.success();
    }

    /**
     * delete
     * @param ids
     * @return Result<String>
     */
    @Override
    public Result<String> batchDelete(List<Integer> ids) {
        gourmetMapper.batchDelete(ids);
        return ApiResult.success();
    }

    /**
     * searching
     * @param gourmetQueryDto
     * @return Result<List<Gourmet>>
     */
    @Override
    public Result<List<GourmetVO>> query(GourmetQueryDto gourmetQueryDto) {
        List<GourmetVO> gourmetList = gourmetMapper.query(gourmetQueryDto);
        Integer totalCount = gourmetMapper.queryCount(gourmetQueryDto);
        return ApiResult.success(gourmetList, totalCount);
    }
}
