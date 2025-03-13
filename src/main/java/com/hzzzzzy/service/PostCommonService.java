package com.hzzzzzy.service;

import com.hzzzzzy.model.dto.AddCommentRequest;
import com.hzzzzzy.model.dto.AddPostRequest;
import com.hzzzzzy.model.dto.UpdatePostRequest;
import com.hzzzzzy.model.entity.PageResult;
import com.hzzzzzy.model.vo.CommentVO;
import com.hzzzzzy.model.vo.PostDetailVO;
import com.hzzzzzy.model.vo.PostVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author hzzzzzy
 * @date 2025/1/24
 * @description
 */
public interface PostCommonService {
    void toggleLike(Integer postId, HttpServletRequest request, boolean b);

    void createPost(HttpServletRequest request, AddPostRequest addPostRequest);

    void updatePost(UpdatePostRequest request);

    void deletePost(Integer postId);

    PageResult<PostVO> getAllPost(Integer current, Integer pageSize, String keyword, HttpServletRequest request);

    PostDetailVO getPost(Integer postId, Integer current, Integer pageSize, HttpServletRequest request);

    void commentPost(AddCommentRequest addCommentRequest, HttpServletRequest request);

    void deleteComment(Integer commentId);

    List<Integer> uploadPostImage(MultipartFile[] file);

    PageResult<PostVO> getMyPost(Integer current, Integer pageSize, String keyword, HttpServletRequest request);

    CommentVO getCommentInfo(Integer objectId);

    PostVO getPostInfo(Integer objectId);

	Long getUserPostLikeCount(Integer userId);
}
