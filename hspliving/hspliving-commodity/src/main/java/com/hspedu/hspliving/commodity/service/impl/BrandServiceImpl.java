package com.hspedu.hspliving.commodity.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hspedu.common.utils.PageUtils;
import com.hspedu.common.utils.Query;

import com.hspedu.hspliving.commodity.dao.BrandDao;
import com.hspedu.hspliving.commodity.entity.BrandEntity;
import com.hspedu.hspliving.commodity.service.BrandService;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {

    /**
     * 在这个代码段中，如果你直接传入一个新的 QueryWrapper<BrandEntity>()（没有设置任何查询条件）
     * ，并与分页参数一起使用，MyBatis Plus 会执行一个没有任何过滤条件的分页查询。这意味着它会返回
     * 数据库中符合分页参数的所有 BrandEntity 记录。
     *
     * new Query<BrandEntity>().getPage(params) 是根据传入的参数（通常包括分页信息如页码和页面大小）
     * 创建一个分页请求对象。而 new QueryWrapper<BrandEntity>() 则是创建了一个没有任何条件的查询包装器，
     * 因此不会对返回的数据进行任何过滤。
     *
     * 所以，总结来说，如果你传入一个空的 QueryWrapper，那么这个查询就会返回表中所有的记录，但是因为
     * 你使用了分页参数（通过 new Query<BrandEntity>().getPage(params)），所以返回的数据将是分页后
     * 的结果，只包含请求的那一页数据，而不是所有数据。这对于实现具有大量数据的表的分页显示是非常有用的，
     * 可以有效减少一次性加载的数据量，从而提高应用的性能和响应速度。
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        //`QueryWrapper` 类是 MyBatis Plus 框架中的一个功能类，它并不是直接与分页插件 `PaginationInnerInterceptor` 关联的。`QueryWrapper` 主要用于构建数据库查询条件，它提供了一种链式的方式来构建查询语句，使得操作数据库时的代码更加简洁易读。
        // `PaginationInnerInterceptor` 是 MyBatis Plus 中用于分页的拦截器。它的主要作用是拦截查询操作，自动解析分页参数，并对 SQL 语句进行修改，以实现分页功能。虽然 `QueryWrapper` 可以与 `PaginationInnerInterceptor` 一起使用来实现分页查询的需求，但它们属于 MyBatis Plus 中不同的功能模块。
        // 简而言之，`QueryWrapper` 不是 `PaginationInnerInterceptor` 带的，而是作为构建查询条件的工具，它可以单独使用或与分页拦截器等其他组件一起使用以满足不同的业务需求。

        // 1.通过分析后端的代码，我们指定如果需要进行带条件查询，就给QueryWrapper 设置查询条件和参数
        // 2.这里简单的进行实现，后面还有更好的实现方式
        // QueryWrapper<BrandEntity> brandEntityQueryWrapper = new QueryWrapper<>();

        //形参第一个位置"name" 是和数据库表的字段匹配 第二个位置是检索条件 即分别对应 WHERE `name` LIKE '%name2%';
        // brandEntityQueryWrapper.like("name",params.get("name2"));

        // ==>  Preparing: SELECT id,isshow,name,description,logo,sort,first_letter FROM commodity_brand WHERE (name LIKE ?)
        // ==> Parameters: %方%(String) 这里即为name2检索条件

        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params),
                new QueryWrapper<BrandEntity>()
                // brandEntityQueryWrapper
        );

        return new PageUtils(page);
    }

}