package cn.kmbeast.service.impl;

import cn.kmbeast.context.LocalThreadHolder;
import cn.kmbeast.mapper.ContentNetMapper;
import cn.kmbeast.mapper.GourmetMapper;
import cn.kmbeast.pojo.api.ApiResult;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.base.QueryDto;
import cn.kmbeast.pojo.dto.query.extend.ContentNetQueryDto;
import cn.kmbeast.pojo.dto.query.extend.GourmetQueryDto;
import cn.kmbeast.pojo.dto.query.extend.InteractionQueryDto;
import cn.kmbeast.pojo.dto.query.extend.InteractionStaticQueryDto;
import cn.kmbeast.pojo.em.AuditEnum;
import cn.kmbeast.pojo.entity.ContentNet;
import cn.kmbeast.pojo.vo.ChartVO;
import cn.kmbeast.pojo.vo.ContentNetVO;
import cn.kmbeast.pojo.vo.GourmetVO;
import cn.kmbeast.pojo.vo.InteractionVO;
import cn.kmbeast.service.ContentNetService;
import cn.kmbeast.utils.DateUtil;
import jakarta.annotation.Resource;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * contentNet service interface implementation
 */
@Service
public class ContentNetServiceImpl implements ContentNetService {

    @Resource
    private ContentNetMapper contentNetMapper;
    @Resource
    private GourmetMapper gourmetMapper;

    /**
     * save
     * @param contentNet
     * @return Result<String>
     */
    @Override
    public Result<String> save(ContentNet contentNet) {
        // set up user info
        contentNet.setUserId(LocalThreadHolder.getUserId());
        // set up access code, unique
        String accessCode = UUID.randomUUID().toString().toUpperCase(Locale.ROOT);
        contentNet.setAccessCode(accessCode);
        // set up time
        contentNet.setCreateTime(LocalDateTime.now());
        contentNetMapper.save(contentNet);
        //return link
        String url="http://localhost:21091/shareDetail?accessCode=" + accessCode;
        return ApiResult.success(url);
    }

    /**
     * update
     * @param contentNet
     * @return Result<String>
     */
    @Override
    public Result<String> update(ContentNet contentNet) {
        contentNetMapper.update(contentNet);
        return ApiResult.success();
    }

    /**
     * delete
     * @param ids
     * @return Result<String>
     */
    @Override
    public Result<String> batchDelete(List<Integer> ids) {
        contentNetMapper.batchDelete(ids);
        return ApiResult.success();
    }

    /**
     * searching
     * @param contentNetQueryDto
     * @return Result<List<ContentNet>>
     */
    @Override
    public Result<List<ContentNetVO>> query(ContentNetQueryDto contentNetQueryDto) {
        List<ContentNetVO> contentNetVOS = contentNetMapper.query(contentNetQueryDto);
        Integer totalCount = contentNetMapper.queryCount(contentNetQueryDto);
        return ApiResult.success(contentNetVOS, totalCount);
    }

    /**
     * get data through access password
     *
     * @param contentNetQueryDto new entity
     * @return Result<Object> getting result
     */
    @Override
    public Result<Object> findContent(ContentNetQueryDto contentNetQueryDto) {
        List<ContentNetVO> contentNetVOS = contentNetMapper.query(contentNetQueryDto);
        if(CollectionUtils.isEmpty(contentNetVOS)){
            return ApiResult.success("Error");
        }
        ContentNetVO contentNetVO = contentNetVOS.get(0);

        //valid days
        LocalDateTime createTime = contentNetVO.getCreateTime();
        LocalDateTime now = LocalDateTime.now();
        long timeInbetween = ChronoUnit.DAYS.between(now, createTime);
        if(contentNetVO.getValidDay() != -1 && timeInbetween>contentNetVO.getValidDay()){
            return ApiResult.success("Link expired");
        }

        GourmetQueryDto gourmetQueryDto = new GourmetQueryDto();
        gourmetQueryDto.setId(contentNetVO.getGourmetId());
        List<GourmetVO> gourmetVOS = gourmetMapper.query(gourmetQueryDto);

        //authentification
        if(contentNetVO.getPasswordAuth() == null || !contentNetVO.getPasswordAuth()){
            return ApiResult.success(gourmetVOS);
        }

        //valid password
        if(!Objects.equals(contentNetVO.getAccessPassword(),contentNetQueryDto.getAccessPassword())){
            return ApiResult.success("wrong access password");
        }

        return ApiResult.success(gourmetVOS);
    }

    /**
     * if authentification necessary
     *
     * @param contentNetQueryDto new entity
     * @return Result<Boolean> getting result
     */
    @Override
    public Result<Boolean> authStatus(ContentNetQueryDto contentNetQueryDto) {
        List<ContentNetVO> contentNetVOS = contentNetMapper.query(contentNetQueryDto);
        if(CollectionUtils.isEmpty(contentNetVOS)){
            return ApiResult.success("Error");
        }
        return ApiResult.success(contentNetVOS.get(0).getPasswordAuth());
    }

    /**
     * statistics according to time
     *
     * @return Result<List < ChartVO>> 响应结果
     */
    @Override
    public Result<List<ChartVO>> daysQuery(ContentNetQueryDto contentNetQueryDto) {
        QueryDto queryDto = DateUtil.startAndEndTime(contentNetQueryDto.getDay());
        contentNetQueryDto.setStartTime(queryDto.getStartTime());
        contentNetQueryDto.setEndTime(queryDto.getEndTime());

        List<ContentNetVO> contentNetVOList = contentNetMapper.query(contentNetQueryDto);
        List<LocalDateTime> localDateTimes = contentNetVOList.stream().map(ContentNetVO::getCreateTime).collect(Collectors.toList());
        List<ChartVO> chartVOS = DateUtil.countDatesWithinRange(contentNetQueryDto.getDay(), localDateTimes);
        return ApiResult.success(chartVOS);
    }
}
