package cn.kmbeast.service.impl;

import cn.kmbeast.context.LocalThreadHolder;
import cn.kmbeast.mapper.GourmetMapper;
import cn.kmbeast.mapper.InteractionMapper;
import cn.kmbeast.pojo.api.ApiResult;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.base.QueryDto;
import cn.kmbeast.pojo.dto.query.extend.GourmetQueryDto;
import cn.kmbeast.pojo.dto.query.extend.InteractionQueryDto;
import cn.kmbeast.pojo.dto.query.extend.InteractionStaticQueryDto;
import cn.kmbeast.pojo.dto.query.extend.UserQueryDto;
import cn.kmbeast.pojo.em.AuditEnum;
import cn.kmbeast.pojo.em.InteractionTypeEnum;
import cn.kmbeast.pojo.em.PublishEnum;
import cn.kmbeast.pojo.entity.Interaction;
import cn.kmbeast.pojo.entity.User;
import cn.kmbeast.pojo.vo.ChartVO;
import cn.kmbeast.pojo.vo.GourmetListVO;
import cn.kmbeast.pojo.vo.GourmetVO;
import cn.kmbeast.pojo.vo.InteractionVO;
import cn.kmbeast.service.InteractionService;
import cn.kmbeast.utils.DateUtil;
import cn.kmbeast.utils.TextUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * interactionservice interface implementation
 */
@Service
public class InteractionServiceImpl implements InteractionService {

    @Resource
    private InteractionMapper interactionMapper;
    @Resource
    private GourmetMapper gourmetMapper;


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

    /**
     * searching for collection list
     *
     * @param interactionQueryDto 查询参数
     * @return Result<List < GourmetListVO>> 响应结果
     */
    @Override
    public Result<List<GourmetListVO>> queryCollectionList(InteractionQueryDto interactionQueryDto) {
        //search for my collections
        interactionQueryDto.setUserId(LocalThreadHolder.getUserId()); //get user id
        interactionQueryDto.setType(InteractionTypeEnum.COLLECTION.getType()); //get interaction type
        List<InteractionVO> interactionVOS = interactionMapper.query(interactionQueryDto);
        //get content id list
        List<Integer> gourmetIds = interactionVOS.stream()
                .map(InteractionVO::getContentId)
                .collect(Collectors.toList());
        //get corresponding content according to the gourmet ids
        List<GourmetVO> gourmetVOS = gourmetMapper.queryByIds(gourmetIds);
        //convert to listVO
        return getListResult(gourmetVOS);
    }

    /**
     * searching for collection list
     *
     * @param gourmetVOS 查询参数
     * @return Result<List < GourmetListVO>> 响应结果
     */
    private static Result<List<GourmetListVO>> getListResult(List<GourmetVO> gourmetVOS) {
        List<GourmetListVO> gourmetListVOS= gourmetVOS.stream()
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
        return ApiResult.success(gourmetListVOS);
    }

    /**
     * rating status
     *
     * @param contentId 查询参数
     * @return Result<Integer> 响应结果
     */
    @Override
    public Result<Integer> ratingStatus(Integer contentId) {
        Integer operationCount = getOperationCount(contentId, InteractionTypeEnum.RATING.getType());
        return ApiResult.success(operationCount);
    }

    /**
     * rating operation
     *
     * @param contentId
     * @param score
     * @return Result<List<GourmetVO>> 响应结果
     */
    @Override
    public Result<List<GourmetVO>> ratingOperation(Integer contentId,Integer score) {
        Integer operationCount = getOperationCount(contentId, InteractionTypeEnum.RATING.getType());
        if(operationCount != 0) {
            return ApiResult.error("you have already rated this article");
        }
        Interaction interaction = new Interaction();
        interaction.setType(InteractionTypeEnum.RATING.getType());
        interaction.setContentId(contentId);
        interaction.setContentType("RATING");
        interaction.setUserId(LocalThreadHolder.getUserId());
        interaction.setScore(score);
        interaction.setCreateTime(LocalDateTime.now());
        interactionMapper.save(interaction);

        GourmetQueryDto gourmetQueryDto = new GourmetQueryDto();
        gourmetQueryDto.setId(contentId);
        gourmetQueryDto.setIsPublish(PublishEnum.OK_AUDIT.getFlag());
        gourmetQueryDto.setIsAudit(AuditEnum.OK_AUDIT.getFlag());
        List<GourmetVO> gourmetVOList = gourmetMapper.query(gourmetQueryDto);
        return ApiResult.success(gourmetVOList);
    }

    /**
     * statistics
     *
     * @return Result<List < ChartVO>> 响应结果
     */
    @Override
    public Result<List<ChartVO>> daysQuery(InteractionQueryDto interactionQueryDto) {
        //find teh gourmet of a specific user's id
        GourmetQueryDto gourmetQueryDto = new GourmetQueryDto();
        gourmetQueryDto.setUserId(LocalThreadHolder.getUserId());
        List<GourmetVO> gourmetVOS = gourmetMapper.query(gourmetQueryDto);
        List<Integer> gourmetIds = gourmetVOS.stream().map(GourmetVO::getId).collect(Collectors.toList());

        if (gourmetIds.isEmpty()) {
            List<ChartVO> chartVOS = DateUtil.countDatesWithinRange(
                    interactionQueryDto.getDay(), List.of());
            return ApiResult.success(chartVOS);
        }
        QueryDto queryDto = DateUtil.startAndEndTime(interactionQueryDto.getDay());
        interactionQueryDto.setStartTime(queryDto.getStartTime());
        interactionQueryDto.setEndTime(queryDto.getEndTime());

        InteractionStaticQueryDto interactionStaticQueryDto = new InteractionStaticQueryDto();
        interactionStaticQueryDto.setInteractionQueryDto(interactionQueryDto);
        interactionStaticQueryDto.setGourmetIds(gourmetIds);

        List<InteractionVO> interactionVOList = interactionMapper.queryDays(interactionStaticQueryDto);
        List<LocalDateTime> localDateTimes = interactionVOList.stream().map(InteractionVO::getCreateTime).collect(Collectors.toList());
        List<ChartVO> chartVOS = DateUtil.countDatesWithinRange(interactionQueryDto.getDay(), localDateTimes);
        return ApiResult.success(chartVOS);
    }

    /**
     * delete interaction
     *
     * @param ids list of ids
     * @return Result<String> getting result
     */
    @Override
    public Result<String> batchDelete(List<Integer> ids) {
        interactionMapper.batchDelete(ids);
        return ApiResult.success();
    }
}
