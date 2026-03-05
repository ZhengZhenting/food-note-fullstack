package cn.kmbeast.controller;

import cn.kmbeast.aop.Pager;
import cn.kmbeast.aop.Protector;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.GourmetQueryDto;
import cn.kmbeast.pojo.em.AuditEnum;
import cn.kmbeast.pojo.em.PublishEnum;
import cn.kmbeast.pojo.entity.Gourmet;
import cn.kmbeast.pojo.vo.GourmetListVO;
import cn.kmbeast.pojo.vo.GourmetVO;
import cn.kmbeast.service.GourmetService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gourmet")
public class GourmetController {

    @Resource
    private GourmetService gourmetService;

    /**
     * add new gourmet
     *
     * @param gourmet new entity
     * @return Result<String> getting result
     */
    @PostMapping(value = "/save")
    @ResponseBody
    public Result<String> save(@RequestBody Gourmet gourmet) {
        return gourmetService.save(gourmet);
    }

    /**
     * modify gourmet
     *
     * @param gourmet new entity
     * @return Result<String> getting result
     */
    @PutMapping(value = "/update")
    @ResponseBody
    public Result<String> update(@RequestBody Gourmet gourmet) {
        return gourmetService.update(gourmet);
    }

    /**
     * audit gourmet
     *
     * @param id ids
     * @return Result<String> getting result
     */
    @Protector(role = "管理员") //access only for admin
    @PutMapping(value = "/audit/{id}")
    @ResponseBody
    public Result<String> audit(@PathVariable Integer id) {
        return gourmetService.audit(id);
    }


    /**
     * delete gourmet
     *
     * @param ids list of ids
     * @return Result<String> getting result
     */
    @PostMapping(value = "/batchDelete")
    @ResponseBody
    public Result<String> batchDelete(@RequestBody List<Integer> ids) {
        return gourmetService.batchDelete(ids);
    }


    /**
     * 查询美食做法
     *
     * @param gourmetQueryDto 查询参数
     * @return Result<List < GourmetVO>> 响应结果
     */
    @Pager
    @Protector(role = "管理员")
    @PostMapping(value = "/query")
    @ResponseBody
    public Result<List<GourmetVO>> query(@RequestBody GourmetQueryDto gourmetQueryDto) {
        return gourmetService.query(gourmetQueryDto);
    }

    /**
     * searching gourmet
     *
     * @param gourmetQueryDto 查询参数
     * @return Result<List <GourmetListVO>> 响应结果
     */
    @Pager //分页查询
    @PostMapping(value = "/queryList")
    @ResponseBody
    public Result<List<GourmetListVO>> queryList(@RequestBody GourmetQueryDto gourmetQueryDto) {
        return gourmetService.queryList(gourmetQueryDto);
    }

    /**
     * searching gourmet by ID
     *
     * @param id id
     * @return Result<List <GourmetVO>> 响应结果
     */
    @Pager //分页查询
    @GetMapping(value = "/{id}")
    @ResponseBody
    public Result<List<GourmetVO>> queryById(@PathVariable Integer id) {
        return gourmetService.queryById(id);
    }



}

