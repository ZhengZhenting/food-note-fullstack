package cn.kmbeast.controller;

import cn.kmbeast.aop.Pager;
import cn.kmbeast.aop.Protector;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.DietHistoryQueryDto;
import cn.kmbeast.pojo.dto.query.extend.DietQueryDto;
import cn.kmbeast.pojo.entity.DietHistory;
import cn.kmbeast.pojo.vo.DietHistoryVO;
import cn.kmbeast.pojo.vo.DietNutrimentVO;
import cn.kmbeast.service.DietHistoryService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dietHistory")
public class DietHistoryController {

    @Resource
    private DietHistoryService dietHistoryService;

    /**
     * add new dietHistory
     *
     * @param dietHistory new entity
     * @return Result<String> getting result
     */
    @PostMapping(value = "/save")
    @ResponseBody
    public Result<String> save(@RequestBody DietHistory dietHistory) {
        return dietHistoryService.save(dietHistory);
    }

    /**
     * modify dietHistory
     *
     * @param dietHistory new entity
     * @return Result<String> getting result
     */
    @PutMapping(value = "/update")
    @ResponseBody
    public Result<String> update(@RequestBody DietHistory dietHistory) {
        return dietHistoryService.update(dietHistory);
    }

    /**
     * delete dietHistory
     *
     * @param ids list of ids
     * @return Result<String> getting result
     */
    @PostMapping(value = "/batchDelete")
    @ResponseBody
    public Result<String> batchDelete(@RequestBody List<Integer> ids) {
        return dietHistoryService.batchDelete(ids);
    }

    /**
     * searching dietHistory
     *
     * @param dietHistoryQueryDto 查询参数
     * @return Result<List < DietHistoryVO>> 响应结果
     */
    @Pager
    @PostMapping(value = "/query")
    @ResponseBody
    public Result<List<DietHistoryVO>> query(@RequestBody DietHistoryQueryDto dietHistoryQueryDto) {
        return dietHistoryService.query(dietHistoryQueryDto);
    }

    /**
     * searching dietHistory for user
     *
     * @param dietHistoryQueryDto 查询参数
     * @return Result<List < DietHistoryVO>> 响应结果
     */
    @Pager
    @PostMapping(value = "/queryUser")
    @ResponseBody
    public Result<List<DietHistoryVO>> queryUser(@RequestBody DietHistoryQueryDto dietHistoryQueryDto) {
        return dietHistoryService.queryUser(dietHistoryQueryDto);
    }

    /**
     * searching dietHistory for user
     *
     * @param dietHistoryQueryDto 查询参数
     * @return Result<List < DietHistoryVO>> 响应结果
     */
    @Pager
    @PostMapping(value = "/queryUserDiet")
    @ResponseBody
    public Result<List<DietHistoryVO>> queryUserDiet(@RequestBody DietHistoryQueryDto dietHistoryQueryDto) {
        return dietHistoryService.queryUserDiet(dietHistoryQueryDto);
    }

    /**
     * searching nutriment
     *
     * @param dietQueryDto 查询参数
     * @return Result<List < DietNutrimentVO>> 响应结果
     */
    @PostMapping(value = "/queryDietNutrimentInfo")
    @ResponseBody
    public Result<List<DietNutrimentVO>> queryDietNutrimentInfo(@RequestBody DietQueryDto dietQueryDto) {
        return dietHistoryService.queryDietNutrimentInfo(dietQueryDto);
    }

}

