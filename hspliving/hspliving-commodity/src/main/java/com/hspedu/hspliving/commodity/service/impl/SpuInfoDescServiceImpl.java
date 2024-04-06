package com.hspedu.hspliving.commodity.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hspedu.common.utils.PageUtils;
import com.hspedu.common.utils.Query;

import com.hspedu.hspliving.commodity.dao.SpuInfoDescDao;
import com.hspedu.hspliving.commodity.entity.SpuInfoDescEntity;
import com.hspedu.hspliving.commodity.service.SpuInfoDescService;


@Service("spuInfoDescService")
public class SpuInfoDescServiceImpl extends ServiceImpl<SpuInfoDescDao, SpuInfoDescEntity> implements SpuInfoDescService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoDescEntity> page = this.page(
                new Query<SpuInfoDescEntity>().getPage(params),
                new QueryWrapper<SpuInfoDescEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 该方法 保存SPU的介绍图片(的url)，  即用于保存SpuInfoDescEntity的信息
     * SpuInfoDescEntity 类 即是存放SpuSaveVO对象的商品描述图片url信息的类
     */
    @Override
    public void saveSpuInfoDesc(SpuInfoDescEntity spuInfoDescEntity) {
        // this.save(spuInfoDescEntity);
        this.baseMapper.insert(spuInfoDescEntity);
    }

}