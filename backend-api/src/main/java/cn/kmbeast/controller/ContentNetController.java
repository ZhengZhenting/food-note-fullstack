package cn.kmbeast.controller;

import cn.kmbeast.aop.Pager;
import cn.kmbeast.aop.Protector;
import cn.kmbeast.context.LocalThreadHolder;
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
     * get data through access password
     *
     * @param contentNetQueryDto new entity
     * @return Result<Object> getting result
     */
    @PostMapping(value = "/findContent")
    @ResponseBody
    public Result<Object> findContent(@RequestBody ContentNetQueryDto contentNetQueryDto) {
        return contentNetService.findContent(contentNetQueryDto);
    }

    /**
     * if authentification necessary
     *
     * @param contentNetQueryDto new entity
     * @return Result<Boolean> getting result
     */
    @PostMapping(value = "/authStatus")
    @ResponseBody
    public Result<Boolean> authStatus(@RequestBody ContentNetQueryDto contentNetQueryDto) {
        return contentNetService.authStatus(contentNetQueryDto);
    }

    /**
     * add new contentNet
     *
     * @param contentNet new entity
     * @return Result<String> getting result
     */
    @PostMapping(value = "/save")
    @ResponseBody
    public Result<String> save(@RequestBody ContentNet contentNet) {
        return contentNetService.save(contentNet);
    }

    /**
     * modify contentNet
     *
     * @param contentNet new entity
     * @return Result<String> getting result
     */
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

    /**
     * searching contentNet of user
     *
     * @param contentNetQueryDto 查询参数
     * @return Result<List < ContentNet>> 响应结果
     */
    @Pager
    @PostMapping(value = "/queryUser")
    @ResponseBody
    public Result<List<ContentNetVO>> queryUser(@RequestBody ContentNetQueryDto contentNetQueryDto) {
        contentNetQueryDto.setUserId(LocalThreadHolder.getUserId());
        return contentNetService.query(contentNetQueryDto);
    }

}

