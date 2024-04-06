package com.hspedu.hspliving.commodity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hspedu.common.utils.PageUtils;
import com.hspedu.hspliving.commodity.entity.AttrAttrgroupRelationEntity;

import java.util.Map;

/**
 * 商品属性和商品属性组的关联表
 *
 * @author yd
 * @email yd@gmail.com
 * @date 2024-03-24 02:50:43
 */
public interface AttrAttrgroupRelationService extends IService<AttrAttrgroupRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);


}

