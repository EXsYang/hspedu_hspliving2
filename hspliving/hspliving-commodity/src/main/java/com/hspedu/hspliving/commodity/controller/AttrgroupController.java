package com.hspedu.hspliving.commodity.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

// import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.hspedu.hspliving.commodity.entity.AttrAttrgroupRelationEntity;
import com.hspedu.hspliving.commodity.entity.AttrEntity;
import com.hspedu.hspliving.commodity.service.AttrAttrgroupRelationService;
import com.hspedu.hspliving.commodity.service.AttrService;
import com.hspedu.hspliving.commodity.service.CategoryService;
import com.hspedu.hspliving.commodity.service.impl.AttrServiceImpl;
import com.hspedu.hspliving.commodity.vo.AttrGroupWithAttrsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hspedu.hspliving.commodity.entity.AttrgroupEntity;
import com.hspedu.hspliving.commodity.service.AttrgroupService;
import com.hspedu.common.utils.PageUtils;
import com.hspedu.common.utils.R;

import javax.annotation.Resource;


/**
 * 家居商品属性分组
 *
 * @author yd
 * @email yd@gmail.com
 * @date 2024-03-18 14:35:38
 */
@RestController
@RequestMapping("commodity/attrgroup")
public class AttrgroupController {
    @Autowired
    private AttrgroupService attrgroupService;


    //装配AttrAttrgroupRelationService
    @Resource
    private AttrAttrgroupRelationService attrAttrgroupRelationService;

    /**
     * 获取某个分类的所有属性组及其关联的所有基本属性
     * http://localhost:5050/api/commodity/attrgroup/301/withattr
     */
    @RequestMapping("/{catalogId}/withattr")
    public R getAttrGroupWithAttrs(@PathVariable("catalogId") Long categoryId){

        List<AttrGroupWithAttrsVo> attrGroupWithAttrsVos = attrgroupService.getAttrGroupWithAttrsByCategoryId(categoryId);

        return R.ok().put("data",attrGroupWithAttrsVos);
    }



    /**
     * 批量添加属性组和商品属性(基本属性)的关联关系的接口
     * @RequestBody: 将json数组 [{},{}] 封装到对应的 List集合
     */
    @RequestMapping("/attr/relation")
    public R addRelation(@RequestBody List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntities){

        // 批量添加关联关系
        attrAttrgroupRelationService.saveBatch(attrAttrgroupRelationEntities);

        return R.ok();
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("commodity:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrgroupService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 说明:
     * 1. 根据业务需求，增加根据分类(第三级分类) + 查询条件 + 分页 的API接口/方法
     */
    @RequestMapping("/list/{categoryId}")
    // @RequiresPermissions("commodity:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params,@PathVariable Long categoryId){

        PageUtils page = attrgroupService.queryPage(params,categoryId);

        // page.getList() 的数据才是返回来的 属性组 attrgroup 数据

        return R.ok().put("page", page);
    }


    //装配对象
    @Resource
    private CategoryService categoryService;

    /**
     * 信息, 根据id返回对应的属性组的信息
     */
    @RequestMapping("/info/{id}")
    // @RequiresPermissions("commodity:attrgroup:info")
    public R info(@PathVariable("id") Long id){
		AttrgroupEntity attrgroup = attrgroupService.getById(id);

		// 需要在这里将 cascadedCategoryId 数据形如 [1,21,301] 返回给前端进行显示
        // 通过 id 先拿到 attrgroup 对象
        // 然后通过attrgroup 对象 拿到对应的 category_id 的值
        Long categoryId = attrgroup.getCategoryId();
        // 再将 categoryId 传入到 CategoryServiceImpl 的方法 getCascadedCategoryId(Long categoryId) 中
        // 即可使用 attrgroup 的 category_id 得到 对应的应该显示的 cascadedCategoryId 层级关系

        // 获取到categoryId 对应的 cascadedCategoryId 形如 [1,21,301]
        Long[] cascadedCategoryId = categoryService.getCascadedCategoryId(categoryId);

        // 把 cascadedCategoryId 设置到attrgroup对象的 cascadedCategoryId属性
        attrgroup.setCascadedCategoryId(cascadedCategoryId);

        return R.ok().put("attrgroup", attrgroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("commodity:attrgroup:save")
    public R save(@RequestBody AttrgroupEntity attrgroup){
		attrgroupService.save(attrgroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("commodity:attrgroup:update")
    public R update(@RequestBody AttrgroupEntity attrgroup){
		attrgroupService.updateById(attrgroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("commodity:attrgroup:delete")
    public R delete(@RequestBody Long[] ids){
		attrgroupService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }


    //注入
    @Resource
    private AttrService attrService;

    /**
     * 根据属性组id,返回该属性组关联的基本属性
     */
    @RequestMapping("/{attrgroupId}/attr/relation")
    public R attrRelation(@PathVariable("attrgroupId") Long attrGroupId){

        List<AttrEntity> attrEntities =
                attrService.getRelationAttr(attrGroupId);

        // return R.ok().put("data",attrGroupId);
        return R.ok().put("data",attrEntities);
    }

    /**
     * 响应批量删除属性组和商品属性(基本属性)的关联关系
     */
    @RequestMapping("/attr/relation/delete")
    public R deleteRelation(@RequestBody AttrAttrgroupRelationEntity[] attrAttrgroupRelationEntities){

        // 调用attrService中的 批量删除属性组和商品属性(基本属性)的关联关系的方法
        attrService.deleteRelation(attrAttrgroupRelationEntities);

        return R.ok();
    }

    /**
     * 根据 attrgroupId 返回可以关联的基本属性
     */
    @RequestMapping("/{attrgroupId}/attr/allowrelation")
    public R attrAllowRelation(@PathVariable("attrgroupId") Long attrgroupId,
                               @RequestParam Map<String,Object> params){


        PageUtils page =
                attrService.getAllowRelationAttr(params, attrgroupId);

        return R.ok().put("page",page);
    }
}
