package cn.kmbeast.service.impl;

import cn.kmbeast.context.LocalThreadHolder;
import cn.kmbeast.mapper.ContentNetMapper;
import cn.kmbeast.pojo.api.ApiResult;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.ContentNetQueryDto;
import cn.kmbeast.pojo.em.AuditEnum;
import cn.kmbeast.pojo.entity.ContentNet;
import cn.kmbeast.pojo.vo.ContentNetVO;
import cn.kmbeast.service.ContentNetService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * contentNet service interface implementation
 */
@Service
public class ContentNetServiceImpl implements ContentNetService {

    @Resource
    private ContentNetMapper contentNetMapper;

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
        List<ContentNetVO> contentNetList = contentNetMapper.query(contentNetQueryDto);
        Integer totalCount = contentNetMapper.queryCount(contentNetQueryDto);
        return ApiResult.success(contentNetList, totalCount);
    }

}
