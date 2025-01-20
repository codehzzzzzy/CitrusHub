package com.hzzzzzy.controller;

import com.hzzzzzy.model.dto.AddCommentRequest;
import com.hzzzzzy.model.dto.AddPostRequest;
import com.hzzzzzy.model.dto.UpdatePostRequest;
import com.hzzzzzy.model.entity.PageResult;
import com.hzzzzzy.model.entity.Result;
import com.hzzzzzy.model.vo.PostDetailVO;
import com.hzzzzzy.model.vo.PostVO;
import com.hzzzzzy.service.PostCommonService;
import com.hzzzzzy.service.PostRecommendationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author hzzzzzy
 * @date 2025/1/20
 * @description
 */
@Api(value = "评论区管理", tags = "评论区管理")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostCommonService postCommonService;

    private final PostRecommendationService recommendationService;

    @ApiOperation(value = "创建帖子", tags = "帖子管理")
    @PostMapping("/createPost")
    public Result createPost(
            @RequestBody
            AddPostRequest addPostRequest,
            HttpServletRequest request
    ) {
        postCommonService.createPost(request, addPostRequest);
        return new Result<>().success().message("创建帖子成功");
    }

    @ApiOperation(value = "更新帖子", tags = "帖子管理")
    @PostMapping("/updatePost")
    public Result updatePost(
            @RequestBody
            UpdatePostRequest request
    ) {
        postCommonService.updatePost(request);
        return new Result<>().success().message("更新帖子成功");
    }

    @ApiOperation(value = "删除帖子", tags = "帖子管理")
    @PostMapping("/deletePost")
    public Result deletePost(
            @RequestParam("postId")
            @Parameter(description = "帖子id")
            Integer postId
    ) {
        postCommonService.deletePost(postId);
        return new Result<>().success().message("删除帖子成功");
    }

    @ApiOperation(value = "获取多条帖子", tags = "帖子管理")
    @GetMapping("/getAllPost")
    public Result getAllPost(
            @RequestParam("current")
            @Parameter(description = "当前页")
            Integer current,
            @RequestParam("pageSize")
            @Parameter(description = "页容量")
            Integer pageSize,
            @RequestParam(value = "keyword", required = false)
            @Parameter(description = "关键词")
            String keyword
    ){
        PageResult<PostVO> res = postCommonService.getAllPost(current, pageSize, keyword);
        return new Result<>().success().data(res);
    }

    @ApiOperation(value = "获取帖子详情", tags = "帖子管理")
    @GetMapping("/getPost")
    public Result getPost(
            @RequestParam("postId")
            @Parameter(description = "帖子id")
            Integer postId,
            @RequestParam(value = "current", defaultValue = "1")
            @Parameter(description = "当前页")
            Integer current,
            @RequestParam(value = "pageSize", defaultValue = "5")
            @Parameter(description = "页容量")
            Integer pageSize
    ){
        PostDetailVO res = postCommonService.getPost(postId, current, pageSize);
        return new Result<>().success().data(res);
    }

    @ApiOperation(value = "点赞或取消点赞帖子", tags = "帖子管理")
    @PostMapping("/toggleLike/{postId}/{action}")
    public Result toggleLike(
            @PathVariable("postId")
            @Parameter(description = "帖子id")
            Integer postId,
            @PathVariable("action")
            @Parameter(description = "1:点赞;2:取消点赞")
            Integer action,
            HttpServletRequest request
    ) {
        if (action == 1) {
            postCommonService.toggleLike(postId, request, true);
            return new Result<>().success().message("点赞成功");
        } else if (action == 0) {
            postCommonService.toggleLike(postId, request, false);
            return new Result<>().success().message("取消点赞成功");
        } else {
            return new Result<>().error().message("无效的操作类型");
        }
    }

    @ApiOperation(value = "获取推荐帖子", tags = "帖子管理")
    @GetMapping("/getRecommend")
    public Result getRecommend(
            @RequestParam("postId")
            @Parameter(description = "帖子id")
            Integer postId
    ){
        List<PostVO> voList = recommendationService.recommendPosts(postId);
        return new Result<>().success().data(voList);
    }

    @ApiOperation(value = "评论帖子", tags = "帖子管理")
    @PostMapping("/commentPost")
    public Result commentPost(
            @RequestBody
            AddCommentRequest addCommentRequest,
            HttpServletRequest request
    ) {
        postCommonService.commentPost(addCommentRequest, request);
        return new Result<>().success().message("评论成功");
    }

    @ApiOperation(value = "删除评论", tags = "帖子管理")
    @DeleteMapping("/deleteComment")
    public Result deleteComment(
            @RequestParam("commentId")
            @Parameter(description = "评论id")
            Integer commentId
    ){
        postCommonService.deleteComment(commentId);
        return new Result<>().success().message("删除成功");
    }



}
