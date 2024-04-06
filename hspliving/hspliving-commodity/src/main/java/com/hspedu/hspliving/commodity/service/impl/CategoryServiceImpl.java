package com.hspedu.hspliving.commodity.service.impl;

import com.hspedu.hspliving.commodity.vo.Catalog2Vo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hspedu.common.utils.PageUtils;
import com.hspedu.common.utils.Query;

import com.hspedu.hspliving.commodity.dao.CategoryDao;
import com.hspedu.hspliving.commodity.entity.CategoryEntity;
import com.hspedu.hspliving.commodity.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    //返回所有分类及其子分类(带有层级关系-即树形!)
    //这里会使用到java8的流式计算(stream api)+递归操作
    @Override
    public List<CategoryEntity> listTree() {

        /*
            baseMapper,实际上就是CategoryDao对象
            这里使用的`baseMapper` 在这里指的是 `CategoryDao` 对象。
            在使用 MyBatis Plus 的 `ServiceImpl` 类时，`baseMapper`
            是自动注入的 DAO（或者叫 Mapper）接口。当你继承了
            `ServiceImpl<CategoryDao, CategoryEntity>` 类时
            ，MyBatis Plus 框架会自动注入一个类型为 `CategoryDao`
            的实例到 `baseMapper` 属性中。
            这样，你就可以直接在你的服务实现类中使用 `baseMapper`
            来执行数据库操作，而不需要自己手动注入。`baseMapper`
            提供了大量的常用数据库操作方法，例如 `selectList`、
            `selectById`、`insert`、`delete` 等，这些都是直接可以使用的
            ，大大简化了 DAO 层的代码量。
         */
        //1. 查出所有的分类数据
        List<CategoryEntity> entities = baseMapper.selectList(null);

        //2.组装成层级树形结构[使用到java8的stream api+递归操作]
        //思路


        List<CategoryEntity> categoryTree = entities.stream().filter(categoryEntity -> {
            //2.1 过滤filter，返回1级分类
            return categoryEntity.getParentId() == 0;
        }).map(category -> {
            //2.2 进行mαp映射操作，给每个一级分类设置对应的子分类（这个过程会使用到递归）
            category.setChildrenCategories(getChildrenCategories(category, entities));
            // category.getChildrenCategories();
            return category;
        }).sorted((category1, category2) -> {
            //2.3 进行排序sorted操作,按照sort升序排列
            return (category1.getSort() == null ? 0 : category1.getSort()) -
                    (category2.getSort() == null ? 0 : category2.getSort());
        }).collect(Collectors.toList());//2.4 将处理好的数据收集collect/转换到集合


        //3.返回 带有层级关系数据-即树形
        return categoryTree;
    }

    //递归查询所有分类的子分类
    //1. 该方法的任务就是把 root 下的所有子分类的层级关系组织好(有多少级，就处理多少级)，并返回
    //2. all 就是所有的分类信息[即上个方法的entities]
    private List<CategoryEntity> getChildrenCategories(CategoryEntity root, List<CategoryEntity> all) {

        List<CategoryEntity> children = all.stream().filter(categoryEntity -> {
            //这里有问题, categoryEntity.getParentId() root.getId()
            //返回的是 Long 包装类型 == 表示是对象比较
            //return categoryEntity.getParentId() == root.getId();
            //解决方法1
            //return categoryEntity.getParentId().longValue() == root.getId().longValue();
            /**
             *  public boolean equals(Object obj) {
             *         if (obj instanceof Long) {
             *             return value == ((Long)obj).longValue();
             *         }
             *         return false;
             *  }
             */
            //解决方案2 转成对数值的比较
            return categoryEntity.getParentId().equals(root.getId());
        }).map(categoryEntity -> {
            //1. 找到子分类, 并设置,递归
            categoryEntity.setChildrenCategories(getChildrenCategories(categoryEntity, all));
            return categoryEntity;
        }).sorted((category1, category2) -> {
            //按照sort排序-升序
            return (category1.getSort() == null ? 0 : category1.getSort()) -
                    (category2.getSort() == null ? 0 : category2.getSort());
        }).collect(Collectors.toList());


        return children;
    }


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 分析
     * 1. 该方法返回 cascadedCategoryId ,数据形如 [1,21,301]
     * 2. 这里我们需要使用递归，会递归的查找parentId
     *
     * @param categoryId
     * @return
     */
    @Override
    public Long[] getCascadedCategoryId(Long categoryId) {

        // 1. 先创建一个集合，把层级关系收集到集合中
        List<Long> cascadedCategoryId = new ArrayList<>();

        // 2. 调用方法进行处理-递归方法
        // cascadedCategoryId是引用传递, 只要下面的方法顺利执行完,
        // 上面本方法声明的集合cascadedCategoryId的值已经变化了 所以这里不接收也没关系
        // 返回的数据形如 [301,21,1]
        getParentCategoryId(categoryId, cascadedCategoryId);

        // 3. 将cascadedCategoryId集合进行反转 [301,21,1] => [1,21,301]
        //注意reverse()方法会影响到集合原本的值
        Collections.reverse(cascadedCategoryId);

        // 注意这里如果传入的是一个空数组，底层会自动判断集合长度并生成合适大小的数组进行存放，不会报错
        // return cascadedCategoryId.toArray(new Long[0]);

        // 将集合转为数组并返回
        return cascadedCategoryId.toArray(new Long[cascadedCategoryId.size()]);
    }

    /**
     * 返回当前所有的一级分类
     *
     * @return
     */
    @Override
    public List<CategoryEntity> getLevel1Categories() {

        QueryWrapper<CategoryEntity> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("parent_id", 0);

        List<CategoryEntity> categoryEntities = this.baseMapper.selectList(queryWrapper);

        return categoryEntities;

        //上面其实一条语句就可以完成
        // return this.baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_id",0));
    }


    /**
     * @param selectList 就是所有的分类数据
     * @param parentCId  根据父级的分类id, 返回对应的分类数据
     * @return
     */
    private List<CategoryEntity> getParent_cid
    (List<CategoryEntity> selectList, Long parentCId) {

        List<CategoryEntity> categoryEntities = selectList.stream().filter(item -> {
            return item.getParentId().equals(parentCId);
        }).collect(Collectors.toList());

        return categoryEntities;

    }


    /**
     * 这里涉及到一个解决方案
     * 返回二级分类(包括三级分类)的数据-按照规定的格式 Map<String, List<Catalog2Vo>>
     * 这里会使用到流式计算 将List集合 转换为 我们需要的Map格式
     * 有一定难度-有层级关系
     * 老师分析: 我们需要一个辅助方法getParent_cid()，就是通过parentId获取对应的下一级的分类数据
     */
    @Override
    public Map<String, List<Catalog2Vo>> getCatalogJson() {

        //先得到所有的分类数据，然后在程序中进行业务处理(到数据表只查询一次，得到所有的数据然后过滤，对数据库的压力小一些)
        // ，得到最终要的结果-> Map<String, List<Catalog2Vo>>

        // 得到所有的分类数据[到数据表查询一次],
        // 这里直接传一个null会把数据表中对应的所有的数据返回,
        // 但是不包含已经被逻辑删除的数据,进行过逻辑删除的数据不会返回 is_show=0的不返回
        // List<CategoryEntity> categoryEntities =
        //         this.baseMapper.selectList(null);

        //使用循环打印 查看获取到的数据
        // categoryEntities.forEach(item -> {
        //     System.out.println("item-> " + item);
        // });

        // System.out.println("categoryEntities=> " + categoryEntities);


        // Map<String, List<Catalog2Vo>> map =
        // categoryEntities.stream().collect(Collectors.toMap(k -> {

        // Map<String, List<Catalog2Vo>> map =
        //         categoryEntities.stream().filter(categoryEntity -> {
        //             // 如果是一级分类,就接着处理
        //             return categoryEntity.getParentId().toString().equalsIgnoreCase("0");
        //         }).collect(Collectors.toMap(k -> {

        // 得到所有的分类数据[到数据表查询一次]
        List<CategoryEntity> selectList =
                this.baseMapper.selectList(null);

        //从一级分类开始 -》 二级分类 -》 三级分类 -》Map<String, List<Catalog2Vo>> map

        //得到所有的一级分类
        List<CategoryEntity> level1Categories = getParent_cid(selectList, 0L);

        //使用Collectors.toMap 构建 Map<String, List<Catalog2Vo>> map
        Map<String, List<Catalog2Vo>> categoryMap =
                level1Categories.stream().collect(Collectors.toMap(k -> {

                    //这里拿到的 k 就是遍历categoryEntities集合后的一个一个的 CategoryEntity对象
                    //先处理一级分类 一级分类的id作为返回的map的key
                    //当k的parentId=0 就说明当前遍历的k 就是需要的一级分类
                    // if (k.getParentId().toString().equalsIgnoreCase("0")){
                    // 说明当前的k就是一级分类的CategoryEntity对象
                    // return k.getId().toString();
                    // }
                    //
                    // return null;

                    // 前面加了 filter过滤,之后，只要是到了这里的k,说明就是一级分类,就不需要if处理和必须返回一个null作为返回条件了!
                    return k.getId().toString();
                }, v -> {

                    //这里拿到的 v 就是遍历categoryEntities集合后的一个一个的 CategoryEntity对象
                    //这里是对应 一级分类下的二级分类的集合 List<Catalog2Vo>
                    //当k的parentId=0 就说明当前遍历的k 就是需要的一级分类
                    // if (v.getParentId().toString().equalsIgnoreCase("0")){
                    //说明当前的v就是一级分类的CategoryEntity对象


                    //获取当前一级分类对应的所有二级分类的信息
                    List<CategoryEntity> level2categories =
                            getParent_cid(selectList, v.getId());

                    // 创建二级分类的集合 catalog2Vos 用于存放要返回的二级分类 Catalog2Vo 的数据
                    // 一个一级分类对应多个二级分类 一次使用List集合存放
                    // 这里就是需要进行业务处理的 List<Catalog2Vo> catalog2Vos
                    List<Catalog2Vo> catalog2Vos = new ArrayList<>();

                    //遍历二级分类的信息集合 l2categoryEntityList
                    // , 根据对应的二级分类的信息 生成/创建对应的 Catalog2Vo 对象
                    // ,并放入集合中返回

                    // 如果 二级分类level2categories有数据,才进行遍历-流式计算
                    if (level2categories != null && level2categories.size() > 0) {
                        catalog2Vos = level2categories.stream().map(l2 -> {
                            //l2 即是遍历出来的单个 二级分类的对象 CategoryEntity
                            //根据遍历出来的单个 二级分类的对象的信息，构建Catalog2Vo对象，并将其放入到catalog2Vos集合
                            Catalog2Vo catalog2Vo =
                                    new Catalog2Vo(v.getId().toString(), null, l2.getId().toString(), l2.getName());

                            // //将其放入到catalog2Vos集合
                            // catalog2Vos.add(catalog2Vo);

                            //这里可以拿到每个二级分类的数据信息,根据每个二级分类的id,作为parentId 获取对应的三级分类的信息
                            List<CategoryEntity> l3categoryEntityList =
                                    getParent_cid(selectList, l2.getId());

                            //遍历三级分类的集合 l3categoryEntityList
                            // , 再获取对应的 Category3Vo 三级分类的集合-> List<Category3Vo> catalog3List

                            // 创建用于保存 返回给前端的三级分类的集合 catalog3List
                            List<Catalog2Vo.Category3Vo> category3Vos = new ArrayList<>();

                            //非空校验
                            if (l3categoryEntityList != null && l3categoryEntityList.size() > 0) {

                                // 遍历 l3categoryEntityList,生成/构建对应的 Catalog2Vo.Category3Vo 对象
                                // 并放入到catalog3List集合中
                                category3Vos = l3categoryEntityList.stream().map(l3 -> {

                                    //构建当前二级分类对应的三级分类CategoryEntity对象 l3 的 Category3Vo 对象
                                    Catalog2Vo.Category3Vo category3Vo = new Catalog2Vo.Category3Vo(l2.getId().toString(), l3.getId().toString(), l3.getName());

                                    return category3Vo;

                                    // return new Catalog2Vo.Category3Vo(l2.getId().toString(), l3.getId().toString(), l3.getName());
                                }).collect(Collectors.toList());

                            }

                            catalog2Vo.setCatalog3List(category3Vos);


                            //直接返回 catalog2Vo对象, 并使用对应的集合进行收集
                            return catalog2Vo;

                        }).collect(Collectors.toList());
                    }

                    // 前面加了 filter过滤,之后，只要是到了这里的k,说明就是一级分类,就不需要if处理(即判断是否为一级分类) 和必须返回一个null作为返回条件了!
                    return catalog2Vos;
                    // }
                    //
                    // return null;

                }));


        return categoryMap;
    }

    /**
     * 编写方法，根据categoryId 递归的查找层级关系
     * ，比如我们接收到 categoryId 301 -> parentId -> parentId -> 直到parentId=0 结束
     * <p>
     * 分析
     */
    private List<Long> getParentCategoryId(Long categoryId,
                                           List<Long> cascadedCategoryId) {

        //1. 先把categoryId 放入 cascadedCategoryId 集合, 第一次调用该方法 此时 [301]
        cascadedCategoryId.add(categoryId);
        //2. 根据 categoryId 得到 CategoryEntity
        CategoryEntity categoryEntity = this.getById(categoryId);
        // 再得到 CategoryEntity 的 parentId。 才可以判断其parentId是否为0
        //3. 判断 CategoryEntity 的parentId是否为0, 如果不为0,说明还有上一级分类
        if (!categoryEntity.getParentId().equals(0L)) {
            //递归操作，将categoryEntity的ParentId当作查询条件categoryId传入
            //同时 cascadedCategoryId 是一个引用传递，最终放入到的都是同一个引用的集合
            // 放入21,cascadedCategoryId=> cascadedCategoryId[301,21,1]
            getParentCategoryId(categoryEntity.getParentId(), cascadedCategoryId);
        }


        // 这里不设置为有返回值的方法也可以，即不需要返回值，因为这里是一个引用传递!!原本的集合中的数据已经改变了
        return cascadedCategoryId;
    }

}