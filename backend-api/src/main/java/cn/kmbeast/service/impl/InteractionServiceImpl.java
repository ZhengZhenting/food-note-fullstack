package cn.kmbeast.service.impl;

import cn.kmbeast.context.LocalThreadHolder;
import cn.kmbeast.mapper.InteractionMapper;
import cn.kmbeast.pojo.api.ApiResult;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.InteractionQueryDto;
import cn.kmbeast.pojo.em.InteractionTypeEnum;
import cn.kmbeast.pojo.entity.Interaction;
import cn.kmbeast.pojo.vo.InteractionVO;
import cn.kmbeast.service.InteractionService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * interactionservice interface implementation
 */
@Service
public class InteractionServiceImpl implements InteractionService {

    @Resource
    private InteractionMapper interactionMapper;


    /**
     * searching
     * @param interactionQueryDto
     * @return Result<List<Interaction>>
     */
    @Override
    public Result<List<InteractionVO>> query(InteractionQueryDto interactionQueryDto) {
        List<InteractionVO> interactionList = interactionMapper.query(interactionQueryDto);
        Integer totalCount = interactionMapper.queryCount(interactionQueryDto);
        return ApiResult.success(interactionList, totalCount);
    }

    /**
     * view operation
     * input content_id, if user has not already viewed this content, then view +1, otherwise no operation.
     * @param contentId
     * @return Result<Void>
     */
    @Override
    public Result<Void> viewOperation(Integer contentId) {
        Integer operationCount = getOperationCount(contentId, InteractionTypeEnum.VIEW.getType());
        // proof if the content is already viewed, if yes then success
        if(operationCount > 0) {
            return ApiResult.success();
        }
        saveOperation(contentId, InteractionTypeEnum.VIEW.getType(),"VIEW");
        return ApiResult.success();
    }

    /**
     * like operation (like, unlike)
     *
     * @return Result<Integer> 响应结果
     */
    @Override
    public Result<Integer> upvoteOperation(Integer contentId) {
        Integer operationCount = getOperationCount(contentId, InteractionTypeEnum.UPVOTE.getType());
        // unlike
        if(operationCount > 0) {
            interactionMapper.deleteByUserInfo(
                    InteractionTypeEnum.UPVOTE.getType(),
                    contentId,
                    LocalThreadHolder.getUserId());
            return ApiResult.success(0);
        }
        saveOperation(contentId, InteractionTypeEnum.UPVOTE.getType(), "UPVOTE");
        return ApiResult.success(1);
    }

    /**
     * get operation count
     * @param contentId content ID
     * @param type interaction type
     * @return Integer count
     */
    public Integer getOperationCount(Integer contentId, Integer type) {
        InteractionQueryDto queryDto = new InteractionQueryDto();
        queryDto.setContentId(contentId);
        queryDto.setUserId(LocalThreadHolder.getUserId());
        queryDto.setType(type);
        return interactionMapper.queryCount(queryDto);
    }

    /**
     * save operation count
     * @param contentId content ID
     * @param type interaction type
     */
    public void saveOperation(Integer contentId, Integer type, String contentType) {
        Interaction interaction = new Interaction();
        interaction.setType(type);
        interaction.setContentId(contentId);
        interaction.setContentType(contentType);
        interaction.setUserId(LocalThreadHolder.getUserId());
        interaction.setCreateTime(LocalDateTime.now());
        interactionMapper.save(interaction);
    }

    /**
     * upvote status
     *
     * @param contentId 查询参数
     * @return Result<Integer> 响应结果
     */
    @Override
    public Result<Integer> upvoteStatus(Integer contentId) {
        Integer operationCount = getOperationCount(contentId, InteractionTypeEnum.UPVOTE.getType());
        return ApiResult.success(operationCount);
    }

    /**
     * save operation (save, cancle)
     *
     * @return Result<Integer> 响应结果
     */
    @Override
    public Result<Integer> saveOperation(Integer contentId) {
        Integer operationCount = getOperationCount(contentId, InteractionTypeEnum.COLLECTION.getType());
        // cancle save
        if(operationCount > 0) {
            interactionMapper.deleteByUserInfo(
                    InteractionTypeEnum.COLLECTION.getType(),
                    contentId,
                    LocalThreadHolder.getUserId());
            return ApiResult.success(0);
        }
        saveOperation(contentId, InteractionTypeEnum.COLLECTION.getType(), "COLLECTION");
        return ApiResult.success(1);
    }

    /**
     * save status
     *
     * @param contentId 查询参数
     * @return Result<Integer> 响应结果
     */
    @Override
    public Result<Integer> saveStatus(Integer contentId) {
        Integer operationCount = getOperationCount(contentId, InteractionTypeEnum.COLLECTION.getType());
        return ApiResult.success(operationCount);
    }
}
