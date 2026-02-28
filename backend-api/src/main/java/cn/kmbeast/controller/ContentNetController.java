package cn.kmbeast.controller;

import cn.kmbeast.aop.Pager;
import cn.kmbeast.aop.Protector;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.ContentNetQueryDto;
import cn.kmbeast.pojo.entity.ContentNet;
import cn.kmbeast.pojo.vo.ContentNetVO;
import cn.kmbeast.service.ContentNetService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contentNet")
public class ContentNetController {

    @Resource
    private ContentNetService contentNetService;

    /**
     * add new contentNet
     *
     * @param contentNet new entity
     * @return Result<String> getting result
     */
    @Protector(role = "管理员") //access only for admin
    @PostMapping(value = "/save")
    @ResponseBody
    public Result<ContentNet> save(@RequestBody ContentNet contentNet) {
        return contentNetService.save(contentNet);
    }

    /**
     * modify contentNet
     *
     * @param contentNet new entity
     * @return Result<String> getting result
     */
    @Protector(role = "管理员") //access only for admin
    @PutMapping(value = "/update")
    @ResponseBody
    public Result<String> update(@RequestBody ContentNet contentNet) {
        return contentNetService.update(contentNet);
    }

    /**
     * delete contentNet
     *
     * @param ids list of ids
     * @return Result<String> getting result
     */
    @Protector(role = "管理员") //access only for admin
    @PostMapping(value = "/batchDelete")
    @ResponseBody
    public Result<String> batchDelete(@RequestBody List<Integer> ids) {
        return contentNetService.batchDelete(ids);
    }

    /**
     * searching contentNet
     *
     * @param contentNetQueryDto 查询参数
     * @return Result<List < ContentNet>> 响应结果
     */
    @Pager
    @PostMapping(value = "/query")
    @ResponseBody
    public Result<List<ContentNetVO>> query(@RequestBody ContentNetQueryDto contentNetQueryDto) {
        return contentNetService.query(contentNetQueryDto);
    }



}

