package com.hspedu.hspliving.commodity.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

// import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hspedu.hspliving.commodity.entity.CategoryEntity;
import com.hspedu.hspliving.commodity.service.CategoryService;
import com.hspedu.common.utils.PageUtils;
import com.hspedu.common.utils.R;



/**
 * 商品分类表
 *
 * @author yd
 * @email yd@gmail.com
 * @date 2024-02-14 22:55:23
 */

/**
 * 使用 @RestController 注解: 如果控制器类被 @RestController 注解标注
 * ，那么该类中的所有方法都默认加上了 @ResponseBody 功能，无需再单独在
 * 每个方法上使用 @ResponseBody。@RestController 是 @Controller 和
 * @ResponseBody 的结合体。
 */
@RestController
@RequestMapping("commodity/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;


    //提供接口/方法 返回所有分类及其子分类(带有层级关系-即树形!)

    /**
     * {
     *   "msg": "success",
     *   "code": 0,
     *   "data": [
     *     {
     *       "id": 1,
     *       "name": "家用电器",
     *       "parentId": 0,
     *       "catLevel": 1,
     *       "isShow": 1,
     *       "sort": 0,
     *       "icon": null,
     *       "proUnit": null,
     *       "proCount": 0,
     *       "childrenCategories": [
     *         {
     *           "id": 21,
     *           "name": "大 家 电",
     *           "parentId": 1,
     *           "catLevel": 2,
     *           "isShow": 1,
     *           "sort": 0,
     *           "icon": null,
     *           "proUnit": null,
     *           "proCount": 0,
     *           "childrenCategories": [
     *             {
     *               "id": 301,
     *               "name": "平板电视",
     *               "parentId": 21,
     *               "catLevel": 3,
     *               "isShow": 1,
     *               "sort": 0,
     *               "icon": null,
     *               "proUnit": null,
     *               "proCount": 0,
     *               "childrenCategories": []
     *             }
     *           ]
     *         }
     *       ]
     *     }
     *   ]
     * }
     *
     *
     * http://localhost:9090/commodity/category/list/tree
     *
     * 注意该方法经过mybatis-plus整合@TableLogic逻辑删除后，只有条件满足，即满足逻辑未删除isShow=1
     * 的数据才会被返回
     *
     */
    @RequestMapping("/list/tree")
    public R listTree(){

        List<CategoryEntity> entities = categoryService.listTree();

        return R.ok().put("data",entities);

    }




    /**
     * 列表
     * http://localhost:9090/commodity/category/list
     */
    @RequestMapping("/list")
    //@RequiresPermissions 是shiro的权限验证的注解，我们不使用，将其注销掉
    // @RequiresPermissions("commodity:category:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    // @RequiresPermissions("commodity:category:info")
    public R info(@PathVariable("id") Long id){
		CategoryEntity category = categoryService.getById(id);

        return R.ok().put("category", category);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("commodity:category:save")
    public R save(@RequestBody CategoryEntity category){
		categoryService.save(category);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("commodity:category:update")
    public R update(@RequestBody CategoryEntity category){
		categoryService.updateById(category);

        return R.ok();
    }

    /**
     * 删除方法 (在前端向这个 Spring Boot 后端发送数据以调用 /delete 方法时，您应该发送一个 HTTP 请求，这个请求需要包含一个 JSON 格式的请求体（body），其中包含一个长整型数组（Long[]）。)
     * 数组转换为 JSON 字符串的格式说明：
     * 当您将一个数组转换为 JSON 字符串时，结果将直接反映数组的结构。以您提供的数组 `[1, 2, 3, 4, 5]` 为例，转换成 JSON 后的字符串形式是：
     * ```json
     * [1, 2, 3, 4, 5]
     * ```
     * 这与您提到的对象形式的 JSON (`{ "msg": "success", "code": 0, "category": { ... } }`) 是不同的。对象形式的 JSON 是用来表示键值对的，而数组形式的 JSON 直接用方括号 `[]` 包围数组元素，数组中的每个元素由逗号分隔。
     * 因此，如果您的后端期望接收一个数组，您应该确保发送的 JSON 数据是数组形式的，就像上面的示例那样。如果后端期望的是一个对象，那么您需要构造一个符合期望结构的 JSON 对象。
     *
     * http://localhost:9090/commodity/category/delete
     *
     * 该方法配置了Mybatis-Plus逻辑删除(删除: 转变为 更新),通过CategoryEntity的isShow属性进行控制的
     * UPDATE commodity_category SET is_show=0 WHERE id IN ( ? ) AND is_show=1
     * `WHERE id IN ( ? )`这种方式允许 SQL 语句一次性匹配多个 ID 值。
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("commodity:category:delete")
    public R delete(@RequestBody Long[] ids){
		categoryService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
