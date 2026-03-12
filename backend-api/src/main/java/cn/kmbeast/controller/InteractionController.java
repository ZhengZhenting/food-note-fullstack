package cn.kmbeast.controller;

import cn.kmbeast.aop.Pager;
import cn.kmbeast.aop.Protector;
import cn.kmbeast.context.LocalThreadHolder;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.InteractionQueryDto;
import cn.kmbeast.pojo.em.InteractionTypeEnum;
import cn.kmbeast.pojo.entity.Interaction;
import cn.kmbeast.pojo.vo.GourmetListVO;
import cn.kmbeast.pojo.vo.GourmetVO;
import cn.kmbeast.pojo.vo.InteractionVO;
import cn.kmbeast.service.InteractionService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/interaction")
public class InteractionController {

    @Resource
    private InteractionService interactionService;

    /**
     * save operation (save, cancle)
     *
     * @return Result<Integer> 响应结果
     */
    @PostMapping(value = "/saveOperation/{contentId}")
    @ResponseBody
    public Result<Integer> saveOperation(@PathVariable Integer contentId) {
        return interactionService.saveOperation(contentId);
    }

    /**
     * rating operation
     *
     * @param contentId
     * @return Result<List<GourmetVO>> 响应结果
     */
    @PostMapping(value = "/ratingOperation/{contentId}/{score}")
    @ResponseBody
    public Result<List<GourmetVO>> ratingOperation(@PathVariable Integer contentId,
                                                   @PathVariable Integer score) {
        return interactionService.ratingOperation(contentId,score);
    }


    /**
     * like operation (like, unlike)
     *
     * @return Result<Integer> 响应结果
     */
    @PostMapping(value = "/upvoteOperation/{contentId}")
    @ResponseBody
    public Result<Integer> upvoteOperation(@PathVariable Integer contentId) {
        return interactionService.upvoteOperation(contentId);
    }

    /**
     * view operation
     *
     * @return Result<Void> 响应结果
     */
    @PostMapping(value = "/viewOperation/{contentId}")
    @ResponseBody
    public Result<Void> viewOperation(@PathVariable Integer contentId) {
        return interactionService.viewOperation(contentId);
    }

    /**
     * upvote status
     *
     * @param contentId 查询参数
     * @return Result<Integer> 响应结果
     */
    @Pager
    @GetMapping(value = "/upvoteStatus/{contentId}")
    @ResponseBody
    public Result<Integer> upvoteStatus(@PathVariable Integer contentId) {
        return interactionService.upvoteStatus(contentId);
    }

    /**
     * save status
     *
     * @param contentId 查询参数
     * @return Result<Integer> 响应结果
     */
    @Pager
    @GetMapping(value = "/saveStatus/{contentId}")
    @ResponseBody
    public Result<Integer> saveStatus(@PathVariable Integer contentId) {
        return interactionService.saveStatus(contentId);
    }

    /**
     * rating status
     *
     * @param contentId 查询参数
     * @return Result<Integer> 响应结果
     */
    @Pager
    @GetMapping(value = "/ratingStatus/{contentId}")
    @ResponseBody
    public Result<Integer> ratingStatus(@PathVariable Integer contentId) {
        return interactionService.ratingStatus(contentId);
    }

    /**
     * searching interaction
     *
     * @param interactionQueryDto 查询参数
     * @return Result<List < InteractionVO>> 响应结果
     */
    @Pager
    @PostMapping(value = "/query")
    @ResponseBody
    public Result<List<InteractionVO>> query(@RequestBody InteractionQueryDto interactionQueryDto) {
        return interactionService.query(interactionQueryDto);
    }

    /**
     * searching for collection list
     *
     * @param interactionQueryDto 查询参数
     * @return Result<List < GourmetListVO>> 响应结果
     */
    @Pager
    @PostMapping(value = "/queryCollectionList")
    @ResponseBody
    public Result<List<GourmetListVO>> queryCollectionList(@RequestBody InteractionQueryDto interactionQueryDto) {
        return interactionService.queryCollectionList(interactionQueryDto);
    }
}

