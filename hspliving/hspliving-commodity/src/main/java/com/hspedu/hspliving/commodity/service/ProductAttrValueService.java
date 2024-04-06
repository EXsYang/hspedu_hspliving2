package com.hspedu.hspliving.commodity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hspedu.common.utils.PageUtils;
import com.hspedu.hspliving.commodity.entity.ProductAttrValueEntity;

import java.util.List;
import java.util.Map;

/**
 * spu 基本属性值
 *
 * @author yd
 * @email yd@gmail.com
 * @date 2024-03-30 18:27:49
 */
public interface ProductAttrValueService extends IService<ProductAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    //支持批量保存spu对应的 商品基本属性
    void saveProductAttr(List<ProductAttrValueEntity> productAttrValueEntities);
}

