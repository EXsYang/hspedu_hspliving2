package com.hspedu.hspliving.commodity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hspedu.common.utils.PageUtils;
import com.hspedu.hspliving.commodity.entity.BrandEntity;
import com.hspedu.hspliving.commodity.entity.CategoryBrandRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * 品牌分类关联表
 *
 * @author yd
 * @email yd@gmail.com
 * @date 2024-03-21 18:22:46
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    // 增加方法saveAll 可以保存 commodity_category_brand_relation 表的所有字段
    // 包括 brand_name 和 category_name
    void saveAll(CategoryBrandRelationEntity categoryBrandRelationEntity);

    // 根据categoryId 返回该分类关联的品牌信息-集合
    List<BrandEntity> getBrandsByCategoryId(Long categoryId);
}

