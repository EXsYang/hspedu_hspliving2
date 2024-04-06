package com.hspedu.hspliving.commodity.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hspedu.common.utils.PageUtils;
import com.hspedu.hspliving.commodity.entity.AttrAttrgroupRelationEntity;
import com.hspedu.hspliving.commodity.entity.AttrEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品属性表
 *
 * @author yd
 * @email yd@gmail.com
 * @date 2024-03-23 04:06:40
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 增加接口，完成保存商品属性(基本属性) 的同时，
     * 还需要保存该基本属性和属性组的关联关系
     */
    void saveAttrAndRelation(AttrEntity attrEntity);

    /**
     * 增加接口，完成更新/修改商品属性(基本属性) 的同时，
     * 还需要保存该基本属性和属性组的关联关系
     */
    void updateAttrAndRelation(AttrEntity attrEntity);

    // 增加根据分类categoryId+查询条件[key] 进行分页检索[基本属性]的API接口
    // 根据自己的业务逻辑，进行定制
    PageUtils queryBaseAttrPage(Map<String, Object> params, Long categoryId);

    // 增加根据分类categoryId+查询条件[key] 进行分页检索[销售属性]的API接口
    // 根据自己的业务逻辑，进行定制
    PageUtils querySaleAttrPage(Map<String, Object> params, Long categoryId);

    /**
     * 根据属性组id,返回该属性关联的商品属性(基本属性)
     * 一个属性组id,可以关联多个商品属性(基本属性)，因此返回的是一个集合
     */
    List<AttrEntity> getRelationAttr(Long attrgroupId);


    /**
     * 批量删除属性组和商品属性(基本属性)的关联关系
     */
    void deleteRelation(AttrAttrgroupRelationEntity[] attrAttrgroupRelationEntities);

    /**
     * 获取某个属性组可以关联的基本属性
     * 1. 如果某个基本属属性组关联了，就不能再关联了
     * 2. 某个属性组可以关联的基本属性，必须是同一个分类
     */
    PageUtils getAllowRelationAttr(Map<String, Object> params, Long attrgroupId);
}

