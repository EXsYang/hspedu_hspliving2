package com.hspedu.hspliving.commodity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hspedu.common.utils.PageUtils;
import com.hspedu.hspliving.commodity.entity.SkuImagesEntity;

import java.util.Map;

/**
 * sku 图片
 *
 * @author yd
 * @email yd@gmail.com
 * @date 2024-03-31 21:29:14
 */
public interface SkuImagesService extends IService<SkuImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

