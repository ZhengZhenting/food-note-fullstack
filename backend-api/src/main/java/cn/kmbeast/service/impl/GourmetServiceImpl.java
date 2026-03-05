package cn.kmbeast.service.impl;

import cn.kmbeast.context.LocalThreadHolder;
import cn.kmbeast.mapper.GourmetMapper;
import cn.kmbeast.pojo.api.ApiResult;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.GourmetQueryDto;
import cn.kmbeast.pojo.em.AuditEnum;
import cn.kmbeast.pojo.em.PublishEnum;
import cn.kmbeast.pojo.entity.Gourmet;
import cn.kmbeast.pojo.vo.GourmetListVO;
import cn.kmbeast.pojo.vo.GourmetVO;
import cn.kmbeast.service.GourmetService;
import cn.kmbeast.utils.TextUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
        gourmet.setUserId(LocalThreadHolder.getUserId());
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
        List<GourmetVO> categoryList = gourmetMapper.query(gourmetQueryDto);
        Integer totalCount = gourmetMapper.queryCount(gourmetQueryDto);
        return ApiResult.success(categoryList, totalCount);
    }

    /**
     * audit gourmet
     *
     * @param id ids
     * @return Result<String> getting result
     */
    @Override
    public Result<String> audit(Integer id) {
        Gourmet gourmet = new Gourmet();
        gourmet.setId(id);
        gourmet.setIsAudit(AuditEnum.OK_AUDIT.getFlag());
        gourmetMapper.update(gourmet);
        return ApiResult.success();
    }

    /**
     * searching gourmet list
     *
     * @param gourmetQueryDto 查询参数
     * @return Result<List <GourmetListVO>> 响应结果
     */
    @Override
    public Result<List<GourmetListVO>> queryList(GourmetQueryDto gourmetQueryDto) {
        gourmetQueryDto.setIsPublish(PublishEnum.OK_AUDIT.getFlag());
        gourmetQueryDto.setIsAudit(AuditEnum.OK_AUDIT.getFlag());
        List<GourmetVO> categoryList = gourmetMapper.query(gourmetQueryDto);
        Integer totalCount = gourmetMapper.queryCount(gourmetQueryDto);
        List<GourmetListVO> gourmetListVOS= categoryList.stream()
                .map(gourmetVO -> new GourmetListVO(
                        gourmetVO.getId(),
                        gourmetVO.getTitle(),
                        gourmetVO.getCover(),
                        TextUtil.parseText(gourmetVO.getContent(), 200),
                        gourmetVO.getUserName(),
                        gourmetVO.getUserAvatar(),
                        gourmetVO.getViewCount(),
                        gourmetVO.getUpvoteCount(),
                        gourmetVO.getSaveCount(),
                        gourmetVO.getRating(),
                        gourmetVO.getCreateTime()
                )).collect(Collectors.toList());

        return ApiResult.success(gourmetListVOS, totalCount);
    }
}
