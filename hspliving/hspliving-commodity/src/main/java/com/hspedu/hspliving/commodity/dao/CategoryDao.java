package com.hspedu.hspliving.commodity.dao;

import com.hspedu.hspliving.commodity.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品分类表
 * 
 * @author yd
 * @email yd@gmail.com
 * @date 2024-02-14 22:55:23
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
