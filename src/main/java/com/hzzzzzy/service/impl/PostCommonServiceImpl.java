package com.hzzzzzy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzzzzzy.constant.RedisConstant;
import com.hzzzzzy.model.dto.AddCommentRequest;
import com.hzzzzzy.model.dto.AddPostRequest;
import com.hzzzzzy.model.dto.UpdatePostRequest;
import com.hzzzzzy.model.entity.Comment;
import com.hzzzzzy.model.entity.PageResult;
import com.hzzzzzy.model.entity.Post;
import com.hzzzzzy.model.entity.User;
import com.hzzzzzy.model.vo.CommentVO;
import com.hzzzzzy.model.vo.PostDetailVO;
import com.hzzzzzy.model.vo.PostVO;
import com.hzzzzzy.service.CommentService;
import com.hzzzzzy.service.PostCommonService;
import com.hzzzzzy.service.PostService;
import com.hzzzzzy.service.UserService;
import com.hzzzzzy.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static com.hzzzzzy.constant.CommonConstant.HEADER_TOKEN;
import static com.hzzzzzy.constant.RedisConstant.*;

/**
 * @author hzzzzzy
 * @date 2025/1/20
 * @description
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PostCommonServiceImpl implements PostCommonService {

    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private UserService userService;

    @Override
    public void createPost(HttpServletRequest request, AddPostRequest addPostRequest) {
        User user = getUser(request);
        Integer userId = user.getId();
        Post post = new Post();
        post.setUserId(userId);
        post.setContext(addPostRequest.getContext());
        post.setTitle(addPostRequest.getTitle());
        postService.save(post);
    }

    @Override
    public void updatePost(UpdatePostRequest request) {
        Post post = new Post();
        post.setId(request.getId());
        post.setTitle(request.getTitle());
        post.setContext(request.getContext());
        postService.updateById(post);
    }

    @Override
    public void deletePost(Integer postId) {
        // 删除帖子点赞緩存
        String postLikeKey = POST_LIKE_KEY_PREFIX + postId;
        redisTemplate.delete(postLikeKey);
        // 删除帖子评论緩存
        String postCommentKey = POST_COMMENT_KEY_PREFIX + postId;
        redisTemplate.delete(postCommentKey);
        // 删除该帖子下的所有评论
        commentService.remove(new LambdaQueryWrapper<Comment>().eq(Comment::getPostId, postId));
        // 删除帖子
        postService.removeById(postId);
    }

    @Override
    public PageResult<PostVO> getAllPost(Integer current, Integer pageSize, String keyword) {
        LambdaQueryWrapper<Post> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(keyword), Post::getTitle, keyword);
        List<Post> postList = postService.list(queryWrapper);
        if (postList.isEmpty()){
            return null;
        }
        List<PostVO> voList = postList.stream().map(post -> {
            User user = userService.getById(post.getUserId());
            PostVO vo = BeanUtil.copyProperties(post, PostVO.class);
            vo.setLikeCount(getLikeCount(post.getId()));
            vo.setCommentCount(getCommentCount(post.getId()));
            vo.setAccount(user.getAccount());
            vo.setAvatar(user.getAvatar());
            return vo;
        }).collect(Collectors.toList());
        return PageUtil.getPage(voList, current, pageSize);
    }

    @Override
    public void commentPost(AddCommentRequest addCommentRequest, HttpServletRequest request) {
        User user = getUser(request);
        Integer postId = addCommentRequest.getPostId();
        Integer userId = user.getId();
        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setUserId(userId);
        comment.setContext(addCommentRequest.getContext());
        commentService.save(comment);
        // 增加评论数量
        String postCommentKey = POST_COMMENT_KEY_PREFIX + postId;
        redisTemplate.opsForValue().increment(postCommentKey, 1);
    }

    @Override
    public void deleteComment(Integer commentId) {
        Integer postId = commentService.getById(commentId).getPostId();
        // 删除评论
        commentService.removeById(commentId);
        // 减少评论数量
        String postCommentKey = POST_COMMENT_KEY_PREFIX + postId;
        redisTemplate.opsForValue().increment(postCommentKey, -1);
    }

    @Override
    public void toggleLike(Integer postId, HttpServletRequest request, boolean like) {
        Integer userId = getUser(request).getId();
        String postLikeKey = POST_LIKE_KEY_PREFIX + postId;
        String userIdStr = userId.toString();
        if (like) {
            // 检查用户是否已经点赞
            Boolean isLiked = redisTemplate.opsForSet().isMember(POST_DEDUPLICATION_KEY_PREFIX + postId, userIdStr);
            if (isLiked == null || !isLiked) {
                // 点赞：将用户ID添加到帖子的点赞集合中
                redisTemplate.opsForSet().add(POST_DEDUPLICATION_KEY_PREFIX + postId, userIdStr);
                // 同时递增点赞计数
                redisTemplate.opsForValue().increment(postLikeKey, 1);
            }
        } else {
            // 取消点赞：从帖子的点赞集合中移除用户ID
            redisTemplate.opsForSet().remove(POST_DEDUPLICATION_KEY_PREFIX + postId, userIdStr);
            // 同时递减点赞计数
            redisTemplate.opsForValue().increment(postLikeKey, -1);
        }
    }

    @Override
    public PostDetailVO getPost(Integer postId, Integer current, Integer pageSize) {
        Post post = postService.getById(postId);
        PostDetailVO vo = BeanUtil.copyProperties(post, PostDetailVO.class);
        vo.setCommentCount(getCommentCount(postId));
        vo.setLikeCount(getLikeCount(postId));
        User user = userService.getById(post.getUserId());
        vo.setAccount(user.getAccount());
        vo.setAvatar(user.getAvatar());
        Page<Comment> commentPage = commentService.page(new Page<>(current, pageSize),
                new LambdaQueryWrapper<Comment>().eq(Comment::getPostId, postId));
        if (commentPage.getRecords().isEmpty()){
            vo.setCommentVOList(null);
            return vo;
        }
        List<Integer> userIds = commentPage.getRecords().stream()
                .map(Comment::getUserId)
                .distinct()
                .collect(Collectors.toList());
        Map<Integer, User> userMap = userService.listByIds(userIds).stream()
                .collect(Collectors.toMap(User::getId, user1 -> user1));
        List<CommentVO> commentVOList = commentPage.getRecords().stream().map(comment -> {
            CommentVO commentVO = BeanUtil.copyProperties(comment, CommentVO.class);
            User commentUser = userMap.get(comment.getUserId());
            if (commentUser != null) {
                commentVO.setAccount(commentUser.getAccount());
                commentVO.setAvatar(commentUser.getAvatar());
            }
            return commentVO;
        }).collect(Collectors.toList());
        // 设置分页结果
        vo.setCommentVOList(new PageResult<>(commentPage.getTotal(), commentVOList));
        return vo;
    }

    private Long getLikeCount(Integer postId) {
        String postLikeKey = POST_LIKE_KEY_PREFIX + postId;
        String value = redisTemplate.opsForValue().get(postLikeKey);
        return value == null ? 0 : Long.parseLong(value);
    }

    private Long getCommentCount(Integer postId) {
        String postCommentKey = POST_COMMENT_KEY_PREFIX + postId;
        String value = redisTemplate.opsForValue().get(postCommentKey);
        return value == null ? 0 : Long.parseLong(value);
    }

    private User getUser(HttpServletRequest request) {
        String token = request.getHeader(HEADER_TOKEN);
        String jsonUser = redisTemplate.opsForValue().get(RedisConstant.USER_LOGIN_TOKEN + token);
        return JSONUtil.toBean(jsonUser, User.class);
    }
}