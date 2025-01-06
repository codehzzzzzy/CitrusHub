package com.hzzzzzy.utils;

import cn.hutool.core.bean.BeanUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hzzzzzy
 * @create 2023/4/1
 * @description 将EntityList转为VOList
 */
public class ListUtil {
    public static <Entity,VO> List<VO> entity2VO(List<Entity> entityList, Class<VO> vo){
        return entityList.stream().map(item -> BeanUtil.copyProperties(item, vo)).collect(Collectors.toList());
    }
}
