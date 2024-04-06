package com.hspedu.hspliving.commodity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hspedu.common.utils.PageUtils;
import com.hspedu.hspliving.commodity.entity.SpuInfoEntity;
import com.hspedu.hspliving.commodity.vo.SpuSaveVO;

import java.util.Map;

/**
 * 商品 spu 信息
 *
 * @author yd
 * @email yd@gmail.com
 * @date 2024-03-29 20:44:53
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    //保存SpuSaveVO对象/数据到表中[会根据业务，将数据分别保存对应表]
    void saveSpuInfo(SpuSaveVO spuSaveVO);

    // 将spuInfoEntity 保存到表commodity_spu_info，
    // 这里为了可读性和扩展性，我们单独写一个方法saveBaseSpuInfo()
    void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity);


    //通过携带的检索条件，进行分页查询。 Condition:条件
    PageUtils queryPageByCondition(Map<String, Object> params);

    //商品SPU上架
    void up(Long spuId);

    //商品SPU下架
    void down(Long spuId);

}

