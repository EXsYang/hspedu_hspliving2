package com.hspedu.hspliving.commodity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hspedu.common.utils.PageUtils;
import com.hspedu.hspliving.commodity.entity.BrandEntity;

import java.util.Map;

/**
 * 家居品牌
 *
 * @author yd
 * @email yd@gmail.com
 * @date 2024-03-05 21:20:36
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

