package com.hspedu.hspliving.commodity.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hspedu.common.utils.PageUtils;
import com.hspedu.common.utils.Query;

import com.hspedu.hspliving.commodity.dao.ProductAttrValueDao;
import com.hspedu.hspliving.commodity.entity.ProductAttrValueEntity;
import com.hspedu.hspliving.commodity.service.ProductAttrValueService;


@Service("productAttrValueService")
public class ProductAttrValueServiceImpl extends ServiceImpl<ProductAttrValueDao, ProductAttrValueEntity> implements ProductAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProductAttrValueEntity> page = this.page(
                new Query<ProductAttrValueEntity>().getPage(params),
                new QueryWrapper<ProductAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 支持批量保存spu对应的 商品基本属性
     */
    @Override
    public void saveProductAttr(List<ProductAttrValueEntity> productAttrValueEntities) {

        this.saveBatch(productAttrValueEntities);
    }

}