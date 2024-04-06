package com.hspedu.hspliving.commodity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hspedu.common.utils.PageUtils;
import com.hspedu.hspliving.commodity.entity.SkuSaleAttrValueEntity;

import java.util.Map;

/**
 * sku 的销售属性/值表
 *
 * @author yd
 * @email yd@gmail.com
 * @date 2024-04-01 01:20:07
 */
public interface SkuSaleAttrValueService extends IService<SkuSaleAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

