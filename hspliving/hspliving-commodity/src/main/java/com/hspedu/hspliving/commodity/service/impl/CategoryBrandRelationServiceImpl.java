package com.hspedu.hspliving.commodity.service.impl;

import com.hspedu.hspliving.commodity.dao.BrandDao;
import com.hspedu.hspliving.commodity.dao.CategoryDao;
import com.hspedu.hspliving.commodity.entity.BrandEntity;
import com.hspedu.hspliving.commodity.entity.CategoryEntity;
import com.hspedu.hspliving.commodity.service.BrandService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hspedu.common.utils.PageUtils;
import com.hspedu.common.utils.Query;

import com.hspedu.hspliving.commodity.dao.CategoryBrandRelationDao;
import com.hspedu.hspliving.commodity.entity.CategoryBrandRelationEntity;
import com.hspedu.hspliving.commodity.service.CategoryBrandRelationService;

import javax.annotation.Resource;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    //装配DAO
    @Resource
    private BrandDao brandDao;

    @Resource
    private CategoryDao categoryDao;
    @Override
    public void saveAll(CategoryBrandRelationEntity categoryBrandRelationEntity) {

        //1. 获取到前端提交的 brandId 和 categoryId
        Long brandId = categoryBrandRelationEntity.getBrandId();
        Long categoryId = categoryBrandRelationEntity.getCategoryId();

        //2. 通过获取到的brandId 和 categoryId 得到对应的 brandName和 categoryName
        BrandEntity brandEntity = brandDao.selectById(brandId);
        CategoryEntity categoryEntity = categoryDao.selectById(categoryId);

        //3. 将 brandName和 categoryName 设置到 categoryBrandRelationEntity 中
        categoryBrandRelationEntity.setBrandName(brandEntity.getName());
        categoryBrandRelationEntity.setCategoryName(categoryEntity.getName());

        //调用CategoryBrandRelationServiceImpl的保存方法
        this.save(categoryBrandRelationEntity);

    }

    //装配
    @Resource
    private CategoryBrandRelationDao categoryBrandRelationDao;

    @Resource
    private BrandService brandService;

    // 根据categoryId 返回该分类关联的品牌信息-集合
    @Override
    public List<BrandEntity> getBrandsByCategoryId(Long categoryId) {

        //1. 先得到categoryId 关联的所有品牌
        List<CategoryBrandRelationEntity> categoryBrandRelationEntities =
                categoryBrandRelationDao.selectList(new QueryWrapper<CategoryBrandRelationEntity>().eq("category_id", categoryId));

        //2. (改进方案1)根据前面得到的 categoryBrandRelationEntities 收集到对应的 brand_id, 并直接查询到对应的BrandEntity对象-使用stream-API 流式计算
        // 详细解释: 只要收集到categoryId对应的brandId,马上就到数据库查询对应的品牌 BrandEntity对象 结果并收集到集合brandEntities中,
        // 如果没有收集到categoryId对应的brandId,就不会进入到stream().map((item)->{})
        // 的遍历中，也就不会发出sql语句(select或者IN的 sql语句)
        List<BrandEntity> brandEntities = categoryBrandRelationEntities.stream().map((item) -> {
            Long brandId = item.getBrandId();
            // return brandDao.selectById(2);
            // return brandDao.selectById(new QueryWrapper<BrandEntity>().eq("id",brandId));
            return brandService.getById(brandId);
        }).collect(Collectors.toList());


        // //2. 根据前面得到的 categoryBrandRelationEntities 收集到对应的 brand_id, 并放入到一个集合中, 使用stream-API 流式计算
        // List<Long> brandIds = categoryBrandRelationEntities.stream().map((item) -> {
        //     return item.getBrandId();
        // }).collect(Collectors.toList());

        // (改进方案2) 如果 brandIds 为[]或者null 就执行下面的操作，即不会发出空的IN语句

        //3. 根据收集得到的 brandIds 到 commodity_brand 表获取品牌的信息并收集到集合中，依然使用 stream API
        /**
         * # sql语句查询是，IN语句 中如果没有指定任何值，查询会报错，语法错误You have an error in your SQL syntax;
         * SELECT id,isshow,NAME,description,logo,sort,first_letter FROM commodity_brand WHERE (id IN ())
         * # sql语句查询是，IN语句 中如果指定值不存在，查询不会报错，只是查询结果为空
         * SELECT id,isshow,NAME,description,logo,sort,first_letter FROM commodity_brand WHERE (id IN (1000,2000))
         *
         * 因此对IN语句进行优化，不使用这里的IN语句，而是从Stream API 中进行解决
         * 只要收集到categoryId对应的brandId,就到数据库查询结果并返回,
         * 如果没有收集到categoryId对应的brandId,就不会进入到stream().map((item)->{})
         * 的遍历中，也就不会发出sql语句(select或者IN的 sql语句)
         */
        // List<BrandEntity> brandEntities =
        //         brandDao.selectList(new QueryWrapper<BrandEntity>().in("id", brandIds));


        return brandEntities;
    }

}