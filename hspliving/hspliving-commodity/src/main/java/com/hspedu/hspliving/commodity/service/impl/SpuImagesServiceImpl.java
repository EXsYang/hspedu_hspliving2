package com.hspedu.hspliving.commodity.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hspedu.common.utils.PageUtils;
import com.hspedu.common.utils.Query;

import com.hspedu.hspliving.commodity.dao.SpuImagesDao;
import com.hspedu.hspliving.commodity.entity.SpuImagesEntity;
import com.hspedu.hspliving.commodity.service.SpuImagesService;


@Service("spuImagesService")
public class SpuImagesServiceImpl extends ServiceImpl<SpuImagesDao, SpuImagesEntity> implements SpuImagesService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuImagesEntity> page = this.page(
                new Query<SpuImagesEntity>().getPage(params),
                new QueryWrapper<SpuImagesEntity>()
        );

        return new PageUtils(page);
    }

    //批量保存spu对应的图片集
    @Override
    public void saveImages(Long id, List<String> images) {

        if (images == null || images.size() == 0){
            //保存一个默认图片集
            SpuImagesEntity spuImagesEntity = new SpuImagesEntity();
            spuImagesEntity.setSpuId(id);
            spuImagesEntity.setImgUrl("default1.jpg");
            spuImagesEntity.setDefaultImg(1);
            this.save(spuImagesEntity);
        }else {
            //images 不为空,即有值
            //使用流式计算遍历images,进行批量保存
            List<SpuImagesEntity> spuImagesEntities = images.stream().map((imageUrl) -> {
                SpuImagesEntity spuImagesEntity = new SpuImagesEntity();
                spuImagesEntity.setSpuId(id);
                spuImagesEntity.setImgUrl(imageUrl);
                spuImagesEntity.setDefaultImg(0);
                return spuImagesEntity;
            }).collect(Collectors.toList());

            //批量添加
            this.saveBatch(spuImagesEntities);
        }

    }

}