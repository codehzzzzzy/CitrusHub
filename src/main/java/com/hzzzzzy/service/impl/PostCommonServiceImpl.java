package com.hzzzzzy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.PutObjectRequest;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzzzzzy.constant.BusinessFailCode;
import com.hzzzzzy.constant.RedisConstant;
import com.hzzzzzy.exception.GlobalException;
import com.hzzzzzy.model.dto.AddCommentRequest;
import com.hzzzzzy.model.dto.AddPostRequest;
import com.hzzzzzy.model.dto.UpdatePostRequest;
import com.hzzzzzy.model.entity.*;
import com.hzzzzzy.model.vo.CommentVO;
import com.hzzzzzy.model.vo.PostDetailVO;
import com.hzzzzzy.model.vo.PostVO;
import com.hzzzzzy.sensitive.ACFilter;
import com.hzzzzzy.service.*;
import com.hzzzzzy.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.hzzzzzy.config.OSSConfiguration.*;
import static com.hzzzzzy.constant.CommonConstant.HEADER_TOKEN;
import static com.hzzzzzy.constant.RedisConstant.*;

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
    @Autowired
    private ImageService imageService;
    @Autowired
    private ACFilter acFilter;

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
                redisTemplate.opsForValue().increment(TOTAL_LIKE_COUNT_KEY, 1);
            }
        } else {
            // 取消点赞：从帖子的点赞集合中移除用户ID
            redisTemplate.opsForSet().remove(POST_DEDUPLICATION_KEY_PREFIX + postId, userIdStr);
            // 同时递减点赞计数
            redisTemplate.opsForValue().increment(postLikeKey, -1);
            redisTemplate.opsForValue().increment(TOTAL_LIKE_COUNT_KEY, -1);
        }
    }

    @Override
    public void createPost(HttpServletRequest request, AddPostRequest addPostRequest) {
        User user = getUser(request);
        Integer userId = user.getId();
        Post post = new Post();
        post.setUserId(userId);
        String context = addPostRequest.getContext();
        String title = addPostRequest.getTitle();
        String filterContext = acFilter.filter(context);
        String filterTitle = acFilter.filter(title);
        post.setContext(filterContext);
        post.setTitle(filterTitle);
        if (!addPostRequest.getImageIdList().isEmpty()){
            post.setImageId(JSONUtil.toJsonStr(addPostRequest.getImageIdList()));
        }
        postService.save(post);
    }

    @Override
    public void updatePost(UpdatePostRequest request) {
        String filterContext = acFilter.filter(request.getContext());
        String filterTitle = acFilter.filter(request.getTitle());
        Post post = new Post();
        post.setId(request.getId());
        post.setTitle(filterTitle);
        post.setContext(filterContext);
        if (!request.getImageIdList().isEmpty()){
            post.setImageId(JSONUtil.toJsonStr(request.getImageIdList()));
        }
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
    public PageResult<PostVO> getAllPost(Integer current, Integer pageSize, String keyword, HttpServletRequest request) {
        // 从请求中获取用户 ID
        Integer userId = getUser(request).getId();

        LambdaQueryWrapper<Post> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(keyword), Post::getTitle, keyword).eq(Post::getStatus, 1);
        List<Post> postList = postService.list(queryWrapper);
        if (postList.isEmpty()) {
            return null;
        }
        // 对帖子列表进行随机排序
        Collections.shuffle(postList);
        List<PostVO> voList = postList.stream().map(post -> {
            Integer authorId = post.getUserId();
            User user = userService.getById(authorId);
            PostVO vo = BeanUtil.copyProperties(post, PostVO.class);
            vo.setLikeCount(getLikeCount(post.getId()));
            vo.setCommentCount(getCommentCount(post.getId()));
            vo.setAvatar(user.getAvatar());

            // 检查用户是否对该帖子点赞
            String userIdStr = userId.toString();
            String postDeduplicationKey = POST_DEDUPLICATION_KEY_PREFIX + post.getId();
            Boolean isLiked = redisTemplate.opsForSet().isMember(postDeduplicationKey, userIdStr);
            vo.setIsLiked(isLiked != null && isLiked);

            String imageId = post.getImageId();
            if (!StringUtils.isEmpty(imageId)) {
                List<Integer> idList = JSONUtil.toBean(imageId, new TypeReference<List<Integer>>() {}, false);
                List<Image> images = imageService.listByIds(idList);
                List<String> urlList = images.stream().map(Image::getUrl).collect(Collectors.toList());
                vo.setUrlList(urlList);
            }
            vo.setAuthorId(authorId);

            return vo;
        }).collect(Collectors.toList());
        return PageUtil.getPage(voList, current, pageSize);
    }

    @Override
    public PostDetailVO getPost(Integer postId, Integer current, Integer pageSize, HttpServletRequest request) {
        // 从请求中获取用户 ID
        Integer userId = getUser(request).getId();

        Post post = postService.getById(postId);
        Integer authorId = post.getUserId();
        PostDetailVO vo = BeanUtil.copyProperties(post, PostDetailVO.class);
        vo.setCommentCount(getCommentCount(postId));
        vo.setLikeCount(getLikeCount(postId));
        User user = userService.getById(authorId);
        vo.setAvatar(user.getAvatar());
        vo.setAuthorId(authorId);

        // 检查用户是否对该帖子点赞
        String userIdStr = userId.toString();
        String postDeduplicationKey = POST_DEDUPLICATION_KEY_PREFIX + postId;
        Boolean isLiked = redisTemplate.opsForSet().isMember(postDeduplicationKey, userIdStr);
        vo.setIsLiked(isLiked != null && isLiked);

        // 帖子图片url列表
        String imageId = post.getImageId();
        if (!StringUtils.isEmpty(imageId)) {
            List<Integer> idList = JSONUtil.toBean(imageId, new TypeReference<List<Integer>>() {}, false);
            List<Image> images = imageService.listByIds(idList);
            List<String> urlList = images.stream().map(Image::getUrl).collect(Collectors.toList());
            vo.setUrlList(urlList);
        }

        Page<Comment> commentPage = commentService.page(new Page<>(current, pageSize),
                new LambdaQueryWrapper<Comment>().eq(Comment::getPostId, postId).eq(Comment::getStatus, 1));
        if (commentPage.getRecords().isEmpty()) {
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
                commentVO.setAvatar(commentUser.getAvatar());
            }
            return commentVO;
        }).collect(Collectors.toList());

        // 设置分页结果
        vo.setCommentVOList(new PageResult<>(commentPage.getTotal(), commentVOList));
        return vo;
    }

    @Override
    public void commentPost(AddCommentRequest addCommentRequest, HttpServletRequest request) {
        User user = getUser(request);
        Integer postId = addCommentRequest.getPostId();
        Integer userId = user.getId();
        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setUserId(userId);
        String context = addCommentRequest.getContext();
        String filterContext = acFilter.filter(context);
        comment.setContext(filterContext);
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
    public List<Integer> uploadPostImage(MultipartFile[] files) {
        List<Integer> imageIds = new ArrayList<>();
        OSS ossClient = new OSSClientBuilder().build(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        try {
            for (MultipartFile file : files) {
                if (file.isEmpty()) {
                    continue;
                }
                // 获取文件名称
                String originalFileName = file.getOriginalFilename();
                String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".")); // 提取文件后缀名
                String fileName = originalFileName.substring(0, originalFileName.lastIndexOf(".")) + new Date().getTime() + fileExtension;
                // 创建上传请求
                PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, fileName, file.getInputStream());
                ossClient.putObject(putObjectRequest);
                ossClient.setObjectAcl(BUCKET_NAME, fileName, CannedAccessControlList.PublicRead);
                // 构造URL
                String url = "https://" + BUCKET_NAME + "." + ENDPOINT + "/" + fileName;
                Image image = new Image();
                image.setUrl(url);
                imageService.save(image);
                imageIds.add(image.getId()); // 将图片 ID 添加到列表中
            }
        } catch (IOException e) {
            throw new GlobalException(new Result<>().error(BusinessFailCode.FILE_UPLOAD_ERROR).message("文件上传失败"));
        } finally {
            ossClient.shutdown();
        }
        return imageIds;
    }

    @Override
    public PageResult<PostVO> getMyPost(Integer current, Integer pageSize, String keyword, HttpServletRequest request) {
        User user = getUser(request);
        Integer userId = user.getId();
        LambdaQueryWrapper<Post> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(keyword), Post::getTitle, keyword);
        queryWrapper.eq(Post::getUserId, userId);
        List<Post> postList = postService.list(queryWrapper);
        if (postList.isEmpty()) {
            return null;
        }
        List<PostVO> voList = postList.stream().map(post -> {
            PostVO vo = BeanUtil.copyProperties(post, PostVO.class);
            vo.setLikeCount(getLikeCount(post.getId()));
            vo.setCommentCount(getCommentCount(post.getId()));
            vo.setAvatar(user.getAvatar());

            // 检查用户是否对该帖子点赞
            String userIdStr = userId.toString();
            String postDeduplicationKey = POST_DEDUPLICATION_KEY_PREFIX + post.getId();
            Boolean isLiked = redisTemplate.opsForSet().isMember(postDeduplicationKey, userIdStr);
            vo.setIsLiked(isLiked != null && isLiked);

            String imageId = post.getImageId();
            if (!StringUtils.isEmpty(imageId)) {
                List<Integer> idList = JSONUtil.toBean(imageId, new TypeReference<List<Integer>>() {}, false);
                List<Image> images = imageService.listByIds(idList);
                List<String> urlList = images.stream().map(Image::getUrl).collect(Collectors.toList());
                vo.setUrlList(urlList);
            }
            return vo;
        }).collect(Collectors.toList());
        return PageUtil.getPage(voList, current, pageSize);
    }

    @Override
    public CommentVO getCommentInfo(Integer id) {
        Comment comment = commentService.getById(id);
        CommentVO vo = BeanUtil.copyProperties(comment, CommentVO.class);
        User user = userService.getById(comment.getUserId());
        vo.setAvatar(user.getAvatar());
        return vo;
    }

    @Override
    public PostVO getPostInfo(Integer objectId) {
        Post post = postService.getById(objectId);
        User user = userService.getById(post.getUserId());
        PostVO vo = BeanUtil.copyProperties(post, PostVO.class);
        vo.setLikeCount(getLikeCount(post.getId()));
        vo.setCommentCount(getCommentCount(post.getId()));
        vo.setAvatar(user.getAvatar());
        String imageId = post.getImageId();
        if (!StringUtils.isEmpty(imageId)){
            List<Integer> idList = JSONUtil.toBean(imageId, new TypeReference<List<Integer>>() {}, false);
            List<Image> images = imageService.listByIds(idList);
            List<String> urlList = images.stream().map(Image::getUrl).collect(Collectors.toList());
            vo.setUrlList(urlList);
        }
        return vo;
    }

    @Override
    public Long getUserPostLikeCount(Integer userId) {
        // 获取用户发布的所有帖子id
        List<Post> postList = postService.list(new LambdaQueryWrapper<Post>().eq(Post::getUserId, userId));
        // 计算总点赞数
        return postList.stream()
                .mapToLong(post -> getLikeCount(post.getId()))
                .sum();
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
