package cn.kmbeast.controller;

import cn.kmbeast.aop.Pager;
import cn.kmbeast.aop.Protector;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.CookbookQueryDto;
import cn.kmbeast.pojo.entity.Cookbook;
import cn.kmbeast.pojo.vo.CookbookVO;
import cn.kmbeast.service.CookbookService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cookbook")
public class CookbookController {

    @Resource
    private CookbookService cookbookService;

    /**
     * add new cookbook
     *
     * @param cookbook new entity
     * @return Result<String> getting result
     */
    @Protector(role = "管理员") //access only for admin
    @PostMapping(value = "/save")
    @ResponseBody
    public Result<String> save(@RequestBody Cookbook cookbook) {
        return cookbookService.save(cookbook);
    }

    /**
     * modify cookbook
     *
     * @param cookbook new entity
     * @return Result<String> getting result
     */
    @Protector(role = "管理员") //access only for admin
    @PutMapping(value = "/update")
    @ResponseBody
    public Result<String> update(@RequestBody Cookbook cookbook) {
        return cookbookService.update(cookbook);
    }

    /**
     * delete cookbook
     *
     * @param ids list of ids
     * @return Result<String> getting result
     */
    @Protector(role = "管理员") //access only for admin
    @PostMapping(value = "/batchDelete")
    @ResponseBody
    public Result<String> batchDelete(@RequestBody List<Integer> ids) {
        return cookbookService.batchDelete(ids);
    }

    /**
     * searching cookbook
     *
     * @param cookbookQueryDto 查询参数
     * @return Result<List < Cookbook>> 响应结果
     */
    @Pager
    @PostMapping(value = "/query")
    @ResponseBody
    public Result<List<CookbookVO>> query(@RequestBody CookbookQueryDto cookbookQueryDto) {
        return cookbookService.query(cookbookQueryDto);
    }



}

