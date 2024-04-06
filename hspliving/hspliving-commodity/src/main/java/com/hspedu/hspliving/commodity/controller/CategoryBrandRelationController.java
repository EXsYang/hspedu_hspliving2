package com.hspedu.hspliving.commodity.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

// import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hspedu.hspliving.commodity.entity.BrandEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hspedu.hspliving.commodity.entity.CategoryBrandRelationEntity;
import com.hspedu.hspliving.commodity.service.CategoryBrandRelationService;
import com.hspedu.common.utils.PageUtils;
import com.hspedu.common.utils.R;


/**
 * 品牌分类关联表
 *
 * @author yd
 * @email yd@gmail.com
 * @date 2024-03-21 18:22:46
 */
@RestController
@RequestMapping("commodity/categorybrandrelation")
public class CategoryBrandRelationController {
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;


    /**
     * 根据categoryId返回关联的品牌信息
     * http://localhost:5050/api/commodity/categorybrandrelation/brands/list?catId=301
     */
    @RequestMapping("/brands/list")
    public R relationBrandsList(@RequestParam(value = "catId",required = true) Long categoryId){

        List<BrandEntity> brandEntities =
                categoryBrandRelationService.getBrandsByCategoryId(categoryId);

        return R.ok().put("data",brandEntities);
    }


    /**
     * 列表
     * http://localhost:9090/commodity/categorybrandrelation/list
     */
    @RequestMapping("/list")
    // @RequiresPermissions("commodity:categorybrandrelation:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = categoryBrandRelationService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 列表-根据brandId 返回 品牌和分类关联的记录
     * http://localhost:9090/commodity/categorybrandrelation/brand/list?brandId=1
     *
     */
    @RequestMapping("/brand/list")
    // @RequiresPermissions("commodity:categorybrandrelation:list")
    public R categoryBrandListByBrandId(@RequestParam("brandId") Long brandId) {

        List<CategoryBrandRelationEntity> data = categoryBrandRelationService.list(
                new QueryWrapper<CategoryBrandRelationEntity>().eq("brand_id", brandId));

        return R.ok().put("data", data);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    // @RequiresPermissions("commodity:categorybrandrelation:info")
    public R info(@PathVariable("id") Long id) {
        CategoryBrandRelationEntity categoryBrandRelation = categoryBrandRelationService.getById(id);

        return R.ok().put("categoryBrandRelation", categoryBrandRelation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("commodity:categorybrandrelation:save")
    public R save(@RequestBody CategoryBrandRelationEntity categoryBrandRelation) {
        // 代码生成器自动生成的保存逻辑
        // categoryBrandRelationService.save(categoryBrandRelation);

        // 自定义的保存逻辑
        // 因为我们要保存brandName和categoryName,所以我们使用自定义的方法saveAll
        categoryBrandRelationService.saveAll(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("commodity:categorybrandrelation:update")
    public R update(@RequestBody CategoryBrandRelationEntity categoryBrandRelation) {
        categoryBrandRelationService.updateById(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 删除 注意这里前端需要放到数组中再传过来！！
     * 当你使用 @RequestBody 注解时，Spring会期待接收的是一个完整的请求体，这通常意味着你需要在前端发送一个JSON格式的请求体。在这个例子中，前端应该发送一个如 [1, 2, 3] 的JSON数组，每个元素都是一个要删除的资源的ID。
     * 如果你尝试直接发送单个ID（不是数组），如直接发送数字 1，后端会尝试将这个单个值映射到一个 Long[] 类型，这会导致类型不匹配的错误，因为后端期待的是一个数组而不是单个数字。
     * 因此，在前端调用这个API时，即使你只需要删除一个条目，也应该将这个ID放入一个数组中，如 [1]，然后发送这个数组作为请求体。这样做符合后端期待的数据格式，可以确保后端正确处理请求。
     *
     * 注意这里使用的是 @RequestBody, 前端需要在请求体中发送数据！
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("commodity:categorybrandrelation:delete")
    public R delete(@RequestBody Long[] ids) {
        categoryBrandRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
