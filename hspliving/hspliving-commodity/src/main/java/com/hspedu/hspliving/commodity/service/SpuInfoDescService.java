package com.hspedu.hspliving.commodity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hspedu.common.utils.PageUtils;
import com.hspedu.hspliving.commodity.entity.SpuInfoDescEntity;

import java.util.Map;

/**
 * 商品 spu 信息介绍
 *
 * @author yd
 * @email yd@gmail.com
 * @date 2024-03-30 01:15:01
 */
public interface SpuInfoDescService extends IService<SpuInfoDescEntity> {

    PageUtils queryPage(Map<String, Object> params);

    //增加方法 用于保存SpuInfoDescEntity的信息
    // SpuInfoDescEntity 类 即是存放SpuSaveVO对象的商品描述图片url信息的类
    void saveSpuInfoDesc(SpuInfoDescEntity spuInfoDescEntity);
}

