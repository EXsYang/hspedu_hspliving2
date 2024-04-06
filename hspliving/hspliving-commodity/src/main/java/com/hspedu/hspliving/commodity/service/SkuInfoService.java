package com.hspedu.hspliving.commodity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hspedu.common.utils.PageUtils;
import com.hspedu.hspliving.commodity.entity.SkuInfoEntity;
import com.hspedu.hspliving.commodity.vo.SearchResult;

import java.util.Map;

/**
 * sku 信息
 *
 * @author yd
 * @email yd@gmail.com
 * @date 2024-03-31 17:32:49
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
    //保存SKU基本信息

    void saveSkuInfo(SkuInfoEntity skuInfoEntity);

    //进行分页查询-携带相应的检索条件
    PageUtils queryPageByCondition(Map<String, Object> params);


    //返回购买 用户 检索的结果 http://localhost:9090/list.html?keyword=海信&catalog3Id=301&brandId=1
    // PageUtils querySearchPageByCondition(Map<String, Object> params);
    SearchResult querySearchPageByCondition(Map<String, Object> params);

}

