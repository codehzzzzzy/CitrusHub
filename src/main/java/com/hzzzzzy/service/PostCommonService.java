package com.hzzzzzy.service;

import com.hzzzzzy.model.dto.AddCommentRequest;
import com.hzzzzzy.model.dto.AddPostRequest;
import com.hzzzzzy.model.dto.UpdatePostRequest;
import com.hzzzzzy.model.entity.PageResult;
import com.hzzzzzy.model.vo.PostDetailVO;
import com.hzzzzzy.model.vo.PostVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author hzzzzzy
 * @date 2025/1/20
 * @description
 */
public interface PostCommonService {

    /**
     * 创建帖子
     * @param request
     * @param addPostRequest
     */
    void createPost(HttpServletRequest request, AddPostRequest addPostRequest);

    /**
     * 更新帖子
     * @param request
     */
    void updatePost(UpdatePostRequest request);

    /**
     * 删除帖子
     * @param postId
     */
    void deletePost(Integer postId);

    /**
     * 获取帖子
     * @param current
     * @param pageSize
     * @param keyword
     * @return
     */
    PageResult<PostVO> getAllPost(Integer current, Integer pageSize, String keyword);

    /**
     * 评论帖子
     * @param addCommentRequest
     * @param request
     */
    void commentPost(AddCommentRequest addCommentRequest, HttpServletRequest request);

    /**
     * 删除评论
     * @param commentId
     */
    void deleteComment(Integer commentId);

    /**
     * 点赞或取消点赞帖子
     *
     * @param postId
     * @param request
     * @param like
     */
    void toggleLike(Integer postId, HttpServletRequest request, boolean like);

    /**
     * 获取帖子详情
     *
     * @param postId
     * @param current
     * @param pageSize
     * @return
     */
    PostDetailVO getPost(Integer postId, Integer current, Integer pageSize);
}
