package cn.kmbeast.controller;

import cn.kmbeast.aop.Pager;
import cn.kmbeast.aop.Protector;
import cn.kmbeast.context.LocalThreadHolder;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.CookbookNutrimentQueryDto;
import cn.kmbeast.pojo.em.PublishEnum;
import cn.kmbeast.pojo.entity.CookbookNutriment;
import cn.kmbeast.pojo.vo.CookbookNutrimentVO;
import cn.kmbeast.service.CookbookNutrimentService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cookbookNutriment")
public class CookbookNutrimentController {

    @Resource
    private CookbookNutrimentService cookbookNutrimentService;

    /**
     * add new cookbookNutriment
     *
     * @param cookbookNutriment new entity
     * @return Result<String> getting result
     */
    @PostMapping(value = "/save")
    @ResponseBody
    public Result<String> save(@RequestBody CookbookNutriment cookbookNutriment) {
        return cookbookNutrimentService.save(cookbookNutriment);
    }

    /**
     * modify cookbookNutriment
     *
     * @param cookbookNutriment new entity
     * @return Result<String> getting result
     */
    @PutMapping(value = "/update")
    @ResponseBody
    public Result<String> update(@RequestBody CookbookNutriment cookbookNutriment) {
        return cookbookNutrimentService.update(cookbookNutriment);
    }

    /**
     * delete cookbookNutriment
     *
     * @param ids list of ids
     * @return Result<String> getting result
     */
    @PostMapping(value = "/batchDelete")
    @ResponseBody
    public Result<String> batchDelete(@RequestBody List<Integer> ids) {
        return cookbookNutrimentService.batchDelete(ids);
    }

    /**
     * searching cookbookNutriment
     *
     * @param queryDto 查询参数
     * @return Result<List < CookbookNutrimentVO>> 响应结果
     */
    @Pager
    @PostMapping(value = "/query")
    @ResponseBody
    public Result<List<CookbookNutrimentVO>> query(@RequestBody CookbookNutrimentQueryDto queryDto) {
        return cookbookNutrimentService.query(queryDto);
    }

    /**
     * searching cookbookNutriment for user
     *
     * @return Result<List < CookbookNutrimentVO>> 响应结果
     */
    @Pager
    @PostMapping(value = "/queryUser")
    @ResponseBody
    public Result<List<CookbookNutrimentVO>> queryUser() {
        return cookbookNutrimentService.queryUser();
    }



}

