package com.hzzzzzy.utils;

import com.hzzzzzy.model.entity.PageResult;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hzzzzzy
 * @create 2023/4/1
 * @description 分页工具类
 */
public class PageUtil<T> {
	public static <T> PageResult<T> getPage(List<T> list, int page, int size) {
		if (page == 0 || size == 0 || list == null || list.size() == 0) {
			return new PageResult<T>((long) (list == null ? 0 : list.size()), list);
		}
		List<T> records = new ArrayList<>();
		for (int i = (page - 1) * size; i < page * size && i < list.size(); i++) {
			records.add(list.get(i));
		}
		return new PageResult<T>((long) list.size(), records);
	}
}
