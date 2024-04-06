package com.hspedu.hspliving.commodity.service.impl;

import com.hspedu.hspliving.commodity.dao.AttrAttrgroupRelationDao;
import com.hspedu.hspliving.commodity.dao.AttrgroupDao;
import com.hspedu.hspliving.commodity.entity.AttrAttrgroupRelationEntity;
import com.hspedu.hspliving.commodity.entity.AttrgroupEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hspedu.common.utils.PageUtils;
import com.hspedu.common.utils.Query;

import com.hspedu.hspliving.commodity.dao.AttrDao;
import com.hspedu.hspliving.commodity.entity.AttrEntity;
import com.hspedu.hspliving.commodity.service.AttrService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    // 装配 AttrAttrgroupRelationDao
    @Resource
    private AttrAttrgroupRelationDao attrAttrgroupRelationDao;
    //装配AttrgroupDao
    @Resource
    private AttrgroupDao attrgroupDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 增加实现方法，完成保存商品属性(基本属性) 的同时，
     * 还需要保存该基本属性和属性组的关联关系
     * <p>
     * 因为该方法涉及到对多表操作[insert],因此需要进行事务控制
     * ，即使用@Transactional注解进行事务控制就OK
     */
    @Transactional
    @Override
    public void saveAttrAndRelation(AttrEntity attrEntity) {

        //1. 先保存基本属性的数据
        this.save(attrEntity); // insert 保存数据到数据库中，对数据库表1的操作

        //2. 保存商品属性(基本属性)和关联的属性组的关联关系
        // attrEntity.getAttrType() == 1 表示是基本属性
        // attrEntity.getAttrGroupId() != null 用户选的有该属性所属属性组的值
        if (attrEntity.getAttrType() == 1 && attrEntity.getAttrGroupId() != null) {
            // 确实有 基本属性 和关联的 属性组 的关联关系
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity =
                    new AttrAttrgroupRelationEntity();

            attrAttrgroupRelationEntity.setAttrGroupId(attrEntity.getAttrGroupId());
            attrAttrgroupRelationEntity.setAttrId(attrEntity.getAttrId());

            // insert 保存数据到数据库中，对数据库表2的操作
            attrAttrgroupRelationDao.insert(attrAttrgroupRelationEntity);
        }


    }

    /**
     * 增加实现方法，完成修改商品属性(基本属性) 的同时，
     * 还需要修改该基本属性和属性组的关联关系
     * <p>
     * 因为该方法涉及到对多表操作[insert],因此需要进行事务控制
     * ，即使用@Transactional注解进行事务控制就OK
     */
    @Transactional
    @Override
    public void updateAttrAndRelation(AttrEntity attrEntity) {
        //1. 先更新基本属性的数据
        this.updateById(attrEntity); // update语句 修改数据到数据库中，对数据库表1的操作

        //2. 修改商品属性(基本属性)和关联的属性组的关联关系
        // attrEntity.getAttrType() == 1 表示是基本属性
        // attrEntity.getAttrGroupId() != null 用户选的有该属性所属属性组的值
        if (attrEntity.getAttrType() == 1 && attrEntity.getAttrGroupId() != null) {
            // 确实有 基本属性 和关联的 属性组 的关联关系
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity =
                    new AttrAttrgroupRelationEntity();

            attrAttrgroupRelationEntity.setAttrGroupId(attrEntity.getAttrGroupId());
            attrAttrgroupRelationEntity.setAttrId(attrEntity.getAttrId());


            // update语句 保存数据到数据库中，对数据库表2的操作
            /**
             * 但前提是假设每个 attr_id 在 commodity_attr_attrgroup_relation 表中只有一条记录。
             * 如果有多条记录（虽然根据你的业务逻辑，一个基本属性只属于一个属性组，但在数据层面
             * 没有强制约束），这会导致多条记录都被更新。因此，在执行更新操作之前，确认数据模型
             * 的假设是很重要的。
             */
            attrAttrgroupRelationDao.update(attrAttrgroupRelationEntity,
                    new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrAttrgroupRelationEntity.getAttrId()));

            //UPDATE commodity_attr_attrgroup_relation
            // SET column1 = value1, column2 = value2, ..., columnN = valueN
            // WHERE attr_id = {attrAttrgroupRelationEntity.getAttrId()};

        }
    }

    /**
     * 1. 编写根据分类categoryId+查询条件[key] 进行分页检索[基本属性]的API接口
     * 2. 根据自己的业务逻辑，进行定制实现
     */
    @Override
    public PageUtils queryBaseAttrPage(Map<String, Object> params, Long categoryId) {

        //1. 先创建QueryWrapper
        //2. 这里先判断/要求查询的BaseAttr 是一个基本属性 即attr_type=1
        QueryWrapper<AttrEntity> queryWrapper =
                new QueryWrapper<AttrEntity>().eq("attr_type", 1);

        //2. 考虑categoryId
        if (categoryId != 0) {
            queryWrapper.eq("category_id", categoryId);
        }

        //3. 考虑用户是否携带key
        String key = (String) params.get("key");
        if (StringUtils.isNotBlank(key)) {
            // 我们规定[业务逻辑] 查询条件是针对 attr_id 或者 attr_name, attr_id 就是相等条件,
            // attr_name 就是模糊查询
            queryWrapper.and(wrapper -> {
                wrapper.eq("attr_id", key).or().like("attr_name", key);
            });
        }

        //
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    /**
     * 增加根据分类categoryId+查询条件[key] 进行分页检索[销售属性]的API接口
     * 根据自己的业务逻辑，进行定制
     */
    @Override
    public PageUtils querySaleAttrPage(Map<String, Object> params, Long categoryId) {
        //1. 先创建QueryWrapper
        //2. 这里先判断/要求查询的attr 是一个销售属性 即attr_type=0
        QueryWrapper<AttrEntity> queryWrapper =
                new QueryWrapper<AttrEntity>().eq("attr_type", 0);

        //2. 考虑categoryId
        if (categoryId != 0) {
            queryWrapper.eq("category_id", categoryId);
        }

        //3. 考虑用户是否携带key
        String key = (String) params.get("key");
        if (StringUtils.isNotBlank(key)) {
            // 我们规定[业务逻辑] 查询条件是针对 attr_id 或者 attr_name, attr_id 就是相等条件,
            // attr_name 就是模糊查询
            queryWrapper.and(wrapper -> {
                wrapper.eq("attr_id", key).or().like("attr_name", key);
            });
        }

        //
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    /**
     * 根据属性组id,返回该属性关联的商品属性(基本属性)
     * 一个属性组id,可以关联多个商品属性(基本属性)，因此返回的是一个集合
     */
    @Override
    public List<AttrEntity> getRelationAttr(Long attrgroupId) {

        //1. 根据attrgroupId属性组id,到commodity_attr_attrgroup_relation表 查询关联的商品属性(基本属性)
        List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntities =
                attrAttrgroupRelationDao.selectList(
                        new QueryWrapper<AttrAttrgroupRelationEntity>()
                                .eq("attr_group_id", attrgroupId));

        //2. 再使用 将 attrAttrgroupRelationEntities 中
        // 该属性组id(即groupId) 关联/对应的所有的基本属性的id(即attrId) 收集到List集合中
        // 使用流式计算 Stream API进行收集
        List<Long> attrIds = attrAttrgroupRelationEntities.stream().map((item) -> {
            return item.getAttrId();
        }).collect(Collectors.toList());

        // 3. 说明:如果当前的属性组id(即attrgroupId)没有关联任何商品属性(基本属性)
        // ，前面的 attrIds 返回的就是 []
        // 如果没有关联任何的基本属性，返回null !!!
        if (attrIds == null || attrIds.size() == 0) {
            return null;
        }

        //4. 根据attrIds到 commodity_attr表中 获取/查询对应的 AttrEntity对象集合 (可能有多条)
        //   比如 attrIds = [1,2,3] => 对应的AttrEntity对象集合
        Collection<AttrEntity> attrEntities = this.listByIds(attrIds);

        //5. 强转为List集合 (这里之所以可以强转是因为底层返回的实际就是一个 List类型)
        // List<AttrEntity> attrEntityList = (List<AttrEntity>) attrEntities;
        // List<AttrEntity> attrEntityList = (List<AttrEntity>) attrEntities;

        //6. 返回数据
        // return attrEntityList;

        //6. 强转为List集合并返回数据
        // 强转为List集合 (这里之所以可以强转是因为底层返回的实际就是一个 List类型) 并返回
        return (List<AttrEntity>) attrEntities;
    }

    /**
     * 批量删除属性组和商品属性(基本属性)的关联关系
     */
    @Override
    public void deleteRelation(AttrAttrgroupRelationEntity[] attrAttrgroupRelationEntities) {
        attrAttrgroupRelationDao.deleteBatchRelation(Arrays.asList(attrAttrgroupRelationEntities));
    }

    /**
     * 获取某个属性组可以关联的基本属性
     * 1. 如果某个基本属属性组关联了，就不能再关联了
     * 2. 某个属性组可以关联的基本属性，必须是同一个分类
     */
    @Override
    public PageUtils getAllowRelationAttr(Map<String, Object> params, Long attrgroupId) {

        // 注意：这里就是涉及到多表检索的解决方案.. 通过流式计算stream API 完成的

        //1. 通过接收的 属性组id attrGroupId, 得到对应的 categoryId
        AttrgroupEntity attrgroupEntity = attrgroupDao.selectById(attrgroupId);
        Long categoryId = attrgroupEntity.getCategoryId();

        //------ 增加业务需求，排除已经关联的基本属性 --------

        //(1) 先得到当前categoryId 的所有分组(即属性组) - 到commodity_attrgroup表查询
        List<AttrgroupEntity> group =
                attrgroupDao.selectList(new QueryWrapper<AttrgroupEntity>().eq("category_id", categoryId));

        // 收集到上面得到group的对应的属性组id->jdk8 流式计算 stream API
        List<Long> attrGroupIds = group.stream().map((item) -> {
            return item.getId();
        }).collect(Collectors.toList());

        //(2) 再到commodity_category_brand_relation中，去检索有哪些基本属性已经和上一步得到的属性组关联上了

        List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntities =
                attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_group_id", attrGroupIds));

        //从上面得到的 attrAttrgroupRelationEntities 中 收集对应的 attr_id , 放入到集合->使用 jdk8 的流式计算
        // 收集到的attrIds,就是已经和属性组 关联过的基本属性的id (属性组可能是多个，同一个分类(category_id)
        // 下可能有多个属性组 ，只要一个基本属性，和其中的任意一个属性组关联过，就将该基本属性排除)
        List<Long> attrIds = attrAttrgroupRelationEntities.stream().map((item) -> {
            return item.getAttrId();
        }).collect(Collectors.toList());


        //2. 通过得到的categoryId, 获取到对应的商品属性(基本属性, 即attr_type=1)
        QueryWrapper<AttrEntity> wrapper =
                new QueryWrapper<AttrEntity>().eq("category_id", categoryId).eq("attr_type", 1);


        //(3) 增加一个排除条件，将已经和上面检索得到的属性组关联过的商品属性(基本属性)排除
        if (attrIds != null && attrIds.size() != 0) {
            //将上面收集到的，关联过属性组的基本属性排除
            wrapper.notIn("attr_id", attrIds);
        }

        //3. 因为还要支持条件查询，所以考虑携带的检索条件 key
        String key = (String) params.get("key");
        if (StringUtils.isNotBlank(key)) {
            //有内容
            wrapper.and((obj) -> {
                obj.eq("attr_id", key).or().like("attr_name", key);
            });
        }

        //
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

}