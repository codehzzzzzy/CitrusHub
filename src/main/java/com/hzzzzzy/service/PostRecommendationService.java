package com.hzzzzzy.service;

import com.hzzzzzy.model.vo.PostVO;

import java.util.List;

/**
 * @author hzzzzzy
 * @date 2025/1/20
 * @description
 */
public interface PostRecommendationService {

    List<PostVO> recommendPosts(Integer targetPostId);
}
