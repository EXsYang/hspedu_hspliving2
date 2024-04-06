package com.hspedu.hspliving.commodity.controller;

import java.util.Arrays;
import java.util.Map;

// import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.hspedu.hspliving.commodity.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hspedu.hspliving.commodity.entity.AttrEntity;
import com.hspedu.hspliving.commodity.service.AttrService;
import com.hspedu.common.utils.PageUtils;
import com.hspedu.common.utils.R;

import javax.annotation.Resource;


/**
 * 商品属性表
 *
 * @author yd
 * @email yd@gmail.com
 * @date 2024-03-23 04:06:40
 */
@RestController
@RequestMapping("commodity/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("commodity:attr:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 1. 提供一个新的api接口/方法
     * 2. 列表- 根据categoryId + 查询条件key完成分页检索基本属性功能
     *
     */
    @RequestMapping("/base/list/{categoryId}")
    // @RequiresPermissions("commodity:attr:list")
    public R baseAttrList(@RequestParam Map<String, Object> params,@PathVariable("categoryId") Long categoryId){
        // PageUtils page = attrService.queryPage(params);

        PageUtils page = attrService.queryBaseAttrPage(params, categoryId);

        return R.ok().put("page", page);
    }

    /**
     * 1. 提供一个新的api接口/方法
     * 2. 列表- 根据categoryId + 查询条件key完成分页检索销售属性功能
     *
     */
    @RequestMapping("/sale/list/{categoryId}")
    // @RequiresPermissions("commodity:attr:list")
    public R saleAttrList(@RequestParam Map<String, Object> params,
                          @PathVariable("categoryId") Long categoryId){
        // PageUtils page = attrService.queryPage(params);

        PageUtils page = attrService.querySaleAttrPage(params, categoryId);

        return R.ok().put("page", page);
    }


    // 注入CategoryServiceImpl
    @Resource
    private CategoryService categoryService;

    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    // @RequiresPermissions("commodity:attr:info")
    public R info(@PathVariable("attrId") Long attrId){

		AttrEntity attr = attrService.getById(attrId);

        // 需要在这里将 cascadedCategoryId 数据形如 [1,21,301] 返回给前端进行显示
        // 通过 id 先拿到 attrgroup 对象
        // 然后通过attrgroup 对象 拿到对应的 category_id 的值
        Long categoryId = attr.getCategoryId();
        // 再将 categoryId 传入到 CategoryServiceImpl 的方法 getCascadedCategoryId(Long categoryId) 中
        // 即可使用 attrgroup 的 category_id 得到 对应的应该显示的 cascadedCategoryId 层级关系

        // 获取到categoryId 对应的 cascadedCategoryId 形如 [1,21,301]
        Long[] cascadedCategoryId = categoryService.getCascadedCategoryId(categoryId);

        // 把 cascadedCategoryId 设置到attrgroup对象的 cascadedCategoryId属性
        attr.setCascadedCategoryId(cascadedCategoryId);


        return R.ok().put("attr", attr);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("commodity:attr:save")
    public R save(@RequestBody AttrEntity attr){
        // System.out.println("attr-> " + attr);

        //下面这个方法不适用了，需要增加自定义方法saveAttrAndRelation(attr)
        // ，可以保存基本属性和属性组的关联关系
		// attrService.save(attr);

        attrService.saveAttrAndRelation(attr);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("commodity:attr:update")
    public R update(@RequestBody AttrEntity attr){

        //下面这个方法不适用了，需要增加自定义方法updateAttrAndRelation(attr)
        // ，可以保存基本属性和属性组的关联关系
        // attrService.updateById(attr);

        attrService.updateAttrAndRelation(attr);


        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("commodity:attr:delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
