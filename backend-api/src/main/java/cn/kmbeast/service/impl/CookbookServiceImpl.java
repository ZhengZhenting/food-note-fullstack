package cn.kmbeast.service.impl;

import cn.kmbeast.context.LocalThreadHolder;
import cn.kmbeast.mapper.CookbookMapper;
import cn.kmbeast.pojo.api.ApiResult;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.CookbookQueryDto;
import cn.kmbeast.pojo.em.PublishEnum;
import cn.kmbeast.pojo.entity.Cookbook;
import cn.kmbeast.pojo.vo.CookbookVO;
import cn.kmbeast.pojo.vo.SelectedVO;
import cn.kmbeast.service.CookbookService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * cookbook service interface implementation
 */
@Service
public class CookbookServiceImpl implements CookbookService {

    @Resource
    private CookbookMapper cookbookMapper;

    /**
     * save
     * @param cookbook
     * @return Result<String>
     */
    @Override
    public Result<String> save(Cookbook cookbook) {
        cookbook.setUserId(LocalThreadHolder.getUserId());
        cookbook.setCreateTime(LocalDateTime.now());
        cookbookMapper.save(cookbook);
        return ApiResult.success();
    }

    /**
     * update
     * @param cookbook
     * @return Result<String>
     */
    @Override
    public Result<String> update(Cookbook cookbook) {
        cookbookMapper.update(cookbook);
        return ApiResult.success();
    }

    /**
     * delete
     * @param ids
     * @return Result<String>
     */
    @Override
    public Result<String> batchDelete(List<Integer> ids) {
        cookbookMapper.batchDelete(ids);
        return ApiResult.success();
    }

    /**
     * searching
     * @param cookbookQueryDto
     * @return Result<List<Cookbook>>
     */
    @Override
    public Result<List<CookbookVO>> query(CookbookQueryDto cookbookQueryDto) {
        List<CookbookVO> cookbookList = cookbookMapper.query(cookbookQueryDto);
        Integer totalCount = cookbookMapper.queryCount(cookbookQueryDto);
        System.out.println("查询结果条数：" + cookbookList.size());
        return ApiResult.success(cookbookList, totalCount);
    }

    /**
     * searching selected items
     * @return Result<List<SelectedVO>>
     */
    @Override
    public Result<List<SelectedVO>> querySelectedItems() {
        CookbookQueryDto cookbookQueryDto = new CookbookQueryDto();
        List<CookbookVO> cookbookList = cookbookMapper.query(cookbookQueryDto);
        List<SelectedVO> selectedVOS = cookbookList.stream().map(cookbookVO -> new SelectedVO(
                cookbookVO.getId(),
                cookbookVO.getTitle()
        )).collect(Collectors.toList());
        return ApiResult.success(selectedVOS);
    }

    /**
     * searching selected items for user
     * @return Result<List<SelectedVO>>
     */
    @Override
    public Result<List<SelectedVO>> querySelectedItemsUser() {
        CookbookQueryDto cookbookQueryDto = new CookbookQueryDto();
        cookbookQueryDto.setUserId(LocalThreadHolder.getUserId());
        List<CookbookVO> cookbookList = cookbookMapper.query(cookbookQueryDto);
        List<SelectedVO> selectedVOS = cookbookList.stream().map(cookbookVO -> new SelectedVO(
                cookbookVO.getId(),
                cookbookVO.getTitle()
        )).collect(Collectors.toList());
        return ApiResult.success(selectedVOS);
    }

    @Override
    public Result<List<SelectedVO>> querySelectedItemsAll() {
        CookbookQueryDto cookbookQueryDto = new CookbookQueryDto();
        cookbookQueryDto.setUserId(LocalThreadHolder.getUserId());
        List<CookbookVO> cookbookList = cookbookMapper.query(cookbookQueryDto);

        //get my own cookbook
        List<SelectedVO> selectedVOS = cookbookList.stream().map(cookbookVO -> new SelectedVO(
                cookbookVO.getId(),
                "Private Cookbook: "+cookbookVO.getTitle()
        )).collect(Collectors.toList());

        //get public cookbook
        cookbookQueryDto.setUserId(null);
        cookbookQueryDto.setIsPublish(PublishEnum.OK_AUDIT.getFlag());
        List<CookbookVO> publishCookbookList = cookbookMapper.query(cookbookQueryDto);
        List<SelectedVO> publishAll = publishCookbookList.stream().map(cookbookVO -> new SelectedVO(
                cookbookVO.getId(),
                "Public Cookbook: "+cookbookVO.getTitle()
        )).collect(Collectors.toList());


        //add two list together
        List<SelectedVO> selectedVOList = new ArrayList<>();
        selectedVOList.addAll(selectedVOS);
        selectedVOList.addAll(publishAll);

        return ApiResult.success(selectedVOList);
    }

    /**
     * searching for public cookbook and user's own cookbook
     *
     * @param cookbookQueryDto
     * @return Result<List < SelectedVO>> 响应结果
     */
    @Override
    public Result<List<CookbookVO>> queryVisible(CookbookQueryDto cookbookQueryDto) {
        List<CookbookVO> cookbookVOList = cookbookMapper.queryVisible(cookbookQueryDto);
        Integer count = cookbookVOList.size();
        return ApiResult.success(cookbookVOList,count);
    }
}
