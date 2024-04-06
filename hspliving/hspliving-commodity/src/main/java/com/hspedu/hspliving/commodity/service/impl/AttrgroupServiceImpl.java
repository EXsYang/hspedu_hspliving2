package com.hspedu.hspliving.commodity.service.impl;

import com.hspedu.hspliving.commodity.entity.AttrEntity;
import com.hspedu.hspliving.commodity.service.AttrService;
import com.hspedu.hspliving.commodity.vo.AttrGroupWithAttrsVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hspedu.common.utils.PageUtils;
import com.hspedu.common.utils.Query;

import com.hspedu.hspliving.commodity.dao.AttrgroupDao;
import com.hspedu.hspliving.commodity.entity.AttrgroupEntity;
import com.hspedu.hspliving.commodity.service.AttrgroupService;

import javax.annotation.Resource;


@Service("attrgroupService")
public class AttrgroupServiceImpl extends ServiceImpl<AttrgroupDao, AttrgroupEntity> implements AttrgroupService {

    /**
     * 这里的QueryWrapper没有带检索条件，因此默认是返回所有的属性组的信息
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrgroupEntity> page = this.page(
                new Query<AttrgroupEntity>().getPage(params),
                new QueryWrapper<AttrgroupEntity>()
        );

        return new PageUtils(page);
    }


    /**
     * 实现增加根据分类(第三级分类) + 查询条件[key和categoryId] + 分页的API接口
     *
     * @param params
     * @param categoryId
     * @return
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params, Long categoryId) {

        //先获取检索输入的关键字key[Map通过 get 方法，传入 key ,会返回对应的 value]
        String key = (String) params.get("key");

        //根据实际情况，封装查询条件到 QueryWrapper对象
        QueryWrapper<AttrgroupEntity> wrapper = new QueryWrapper<>();

        // StringUtils.isNotBlank() 判断传入的 key 为非空或者不是全部为空格/空白字符
        //判断key是否携带的有查询条件-希望是一组独立的检索条件(使用lambda表达式的形式即可)
        if (StringUtils.isNotBlank(key)) {
            /*
             queryWrapper.like("name",search); 的参考解释如下:
             形参第一个位置"name" 是和数据库表的字段匹配 第二个位置是检索条件
             第一个参数 "name" 是用于指定数据库中用于匹配的字段，而第二个参数
             search 是用户提供的用于模糊匹配的文本值。这行代码将生成一个 SQL WHERE 子句，
             类似于 WHERE name LIKE '%search%'，用于在数据库查询中执行模糊匹配。
             */

            // 这里把上面获取到的key 即看作id 又看作name字段, 因为这里使用的是OR 只要有一个条件满足即可！
            // id就是相等条件，name就是模糊查询
            // wrapper.eq("id", key).or().like("name", key);
            // 上面这条语句生成的sql代码如下:
            //SELECT id,icon,name,description,sort,category_id FROM commodity_attrgroup WHERE (id = ? OR name LIKE ? AND category_id = ?)

            // wrapper.and((obj) -> {
            wrapper.and(obj -> {
                obj.eq("id", key).or().like("name", key);
            });
            // 上面这样使用lambda表达式的形式写的 最终生成的sql语句生成的sql代码如下:
            //SELECT id,icon,name,description,sort,category_id FROM commodity_attrgroup WHERE (( (id = ? OR name LIKE ?) ) AND category_id = ?)
            // 好处是可以独立的当作一个条件


        }


        // 下面我们需要处理是否需要封装categoryId检索条件
        // 这里先设置一个业务规则: categoryId==0 表示在查询属性组的时候，不加入categoryId检索条件
        // ，否则就加入 AND categoryId=xxx , 这里的逻辑需要和前端代码配合
        // if (categoryId == 0) {
        //     //使用renren提供的业务代码
        //     IPage<AttrgroupEntity> page = this.page(
        //             new Query<AttrgroupEntity>().getPage(params),
        //             wrapper
        //     );
        //
        //     return new PageUtils(page);
        // }else { //这时需要带上 category_id 检索条件
        //     //注意这里的.eq("category_id" 中的"category_id"指的是数据库表的字段"category_id"
        //     wrapper.eq("category_id",categoryId);
        //     //使用renren提供的业务代码
        //     IPage<AttrgroupEntity> page = this.page(
        //             new Query<AttrgroupEntity>().getPage(params),
        //             wrapper
        //     );
        //
        //     return new PageUtils(page);
        // }

        //简化后
        if (categoryId != 0) {
            //注意这里的.eq("category_id" 中的"category_id"指的是数据库表的字段"category_id"
            wrapper.eq("category_id", categoryId);

            // Unreachable statement:无法访问的语句
        }

        /*
            在 MyBatis Plus 中进行分页操作的那部分代码是这一行：

            ```java
            IPage<AttrgroupEntity> page = this.page(
                new Query<AttrgroupEntity>().getPage(params),
                wrapper
            );
            ```

            在这段代码中：

            1. `this.page(...)` 方法是 MyBatis Plus 提供的，用于执行分页查询。`this` 在这里指的是当前的服务类 `AttrgroupServiceImpl` 的实例，它继承自 `ServiceImpl<AttrgroupDao, AttrgroupEntity>`，而 `ServiceImpl` 类提供了 `page` 方法，用于执行分页查询。因此，这里的 `this` 实际上就是指当前的服务实例，它继承了 `ServiceImpl` 从而具有执行分页查询的能力。

            2. `new Query<AttrgroupEntity>().getPage(params)` 是创建一个分页查询条件，其中 `Query` 是一个工具类，它根据传入的参数 `params`（这些参数一般包括分页的信息，如当前页和每页大小等），构造出一个 `IPage<AttrgroupEntity>` 类型的对象，这个对象表示分页请求的信息。

            3. `wrapper` 是一个 `QueryWrapper<AttrgroupEntity>` 类的实例，它定义了查询的条件。如果 `wrapper` 没有指定任何条件，那么 `page` 方法将会返回所有记录的分页结果。

            因此，`this.page(...)` 这行代码是执行分页操作的核心，它结合了分页信息和查询条件，进行数据库查询，并返回查询结果。这里的 `page` 方法是通过 MyBatis Plus 实现自动分页的核心方法，通过它可以非常方便地实现复杂的分页逻辑。
         */
        //使用renren提供的业务代码,这里实现了分页+条件查询
        //this.page 调用的是当前对象(AttrgroupServiceImpl类的对象)的page方法
        // , 因为AttrgroupServiceImpl类继承父类 ServiceImpl<M extends BaseMapper<T>, T>
        //而父类中有page()方法，因此可以通过this直接调用！

        IPage<AttrgroupEntity> page = this.page(
                new Query<AttrgroupEntity>().getPage(params),
                wrapper
        );



        return new PageUtils(page);


    }

    //装配AttrService
    @Resource
    private AttrService attrService;

    /**
     * 根据分类categoryId返回该分类关联的属性组和这些属性组关联的基本属性
     */
    @Override
    public List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCategoryId(Long categoryId) {

        //1. 根据categoryId 得到该分类关联的所有属性组
        List<AttrgroupEntity> attrgroupEntities =
                this.list(new QueryWrapper<AttrgroupEntity>().eq("category_id", categoryId));

        //2. 根据前面查询到的 attrgroupEntities，对其进行遍历，将各个属性组关联的
        //   基本属性查询到，并封装到 AttrGroupWithAttrsVo 的集合中，返回-> 依然使用流式计算，stream API
        List<AttrGroupWithAttrsVo> attrGroupWithAttrsVos = attrgroupEntities.stream().map((attrgroupEntity) -> {

            //(1) 先创建一个AttrGroupWithAttrsVo对象
            AttrGroupWithAttrsVo attrGroupWithAttrsVo = new AttrGroupWithAttrsVo();
            //(2) 将attrgroupEntity 对象属性拷贝到 attrGroupWithAttrsVo
            //注意是 import org.springframework.beans.BeanUtils;
            BeanUtils.copyProperties(attrgroupEntity, attrGroupWithAttrsVo);

            //(3) 通过属性组id,获取到该属性组关联的所有基本属性
            Long attrgroupId = attrgroupEntity.getId();
            /**
             * attrService.getRelationAttr(attrgroupId)的说明:
             * 根据属性组id,返回该属性关联的商品属性(基本属性)
             * 一个属性组id,可以关联多个商品属性(基本属性)，因此返回的是一个集合
             */
            List<AttrEntity> attrEntities = attrService.getRelationAttr(attrgroupId);

            //(4) 将前面得到的attrEntities设置到 attrGroupWithAttrsVo的attrs属性
            attrGroupWithAttrsVo.setAttrs(attrEntities);
            // stream.map映射操作完成，返回attrGroupWithAttrsVo对象，
            // 之后使用.collect(Collectors.toList()); 进行收集
            return attrGroupWithAttrsVo;

        }).collect(Collectors.toList());

        return attrGroupWithAttrsVos;


    }

}