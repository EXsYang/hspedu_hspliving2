package com.hspedu.hspliving.commodity.dao;

import com.hspedu.hspliving.commodity.entity.AttrAttrgroupRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品属性和商品属性组的关联表
 *
 * @author yd
 * @email yd@gmail.com
 * @date 2024-03-24 02:50:43
 */
@Mapper
public interface AttrAttrgroupRelationDao extends BaseMapper<AttrAttrgroupRelationEntity> {

    /**
     * 批量删除属性组和属性的关联关系。
     *
     * @param entities 一个包含多个 AttrAttrgroupRelationEntity 实例的列表，
     *                 每个实例代表一个要删除的属性组和属性的关联关系。
     *                 通过 @Param("entities") 注解，我们可以在 XML 映射文件中
     *                 以 'entities' 作为参数引用这个列表。
     */
    void deleteBatchRelation(@Param("entities") List<AttrAttrgroupRelationEntity> entities);
}
