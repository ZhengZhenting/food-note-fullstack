package cn.kmbeast.controller;

import cn.kmbeast.aop.Pager;
import cn.kmbeast.aop.Protector;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.NutrimentQueryDto;
import cn.kmbeast.pojo.entity.Nutriment;
import cn.kmbeast.pojo.vo.NutrimentVO;
import cn.kmbeast.service.NutrimentService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/nutriment")
public class NutrimentController {

    @Resource
    private NutrimentService nutrimentService;

    /**
     * add new nutriment
     *
     * @param nutriment new entity
     * @return Result<String> getting result
     */
    @Protector(role = "管理员") //access only for admin
    @PostMapping(value = "/save")
    @ResponseBody
    public Result<String> save(@RequestBody Nutriment nutriment) {
        return nutrimentService.save(nutriment);
    }

    /**
     * modify nutriment
     *
     * @param nutriment new entity
     * @return Result<String> getting result
     */
    @Protector(role = "管理员") //access only for admin
    @PutMapping(value = "/update")
    @ResponseBody
    public Result<String> update(@RequestBody Nutriment nutriment) {
        return nutrimentService.update(nutriment);
    }

    /**
     * delete nutriment
     *
     * @param ids list of ids
     * @return Result<String> getting result
     */
    @Protector(role = "管理员") //access only for admin
    @PostMapping(value = "/batchDelete")
    @ResponseBody
    public Result<String> batchDelete(@RequestBody List<Integer> ids) {
        return nutrimentService.batchDelete(ids);
    }

    /**
     * searching nutriment
     *
     * @param nutrimentQueryDto 查询参数
     * @return Result<List < Nutriment>> 响应结果
     */
    @Pager
    @PostMapping(value = "/query")
    @ResponseBody
    public Result<List<NutrimentVO>> query(@RequestBody NutrimentQueryDto nutrimentQueryDto) {
        return nutrimentService.query(nutrimentQueryDto);
    }



}

