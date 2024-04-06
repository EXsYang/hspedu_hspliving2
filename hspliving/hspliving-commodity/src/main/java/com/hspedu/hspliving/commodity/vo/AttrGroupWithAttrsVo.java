package com.hspedu.hspliving.commodity.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.hspedu.hspliving.commodity.entity.AttrEntity;
import lombok.Data;

import java.util.List;

/**
 * @author yangda
 * @create 2024-03-28-14:18
 * @description:
 * 1. 如果返回的数据是单独的实体类/对象 不能满足需求
 * 2. 通常的解决方案 可以增加VO类/对象
 * 3. 这个VO类/对象，可以根据前端的需求，进行组合，也可以增加属性(字段)，或者删除属性(字段)
 */
@Data
public class AttrGroupWithAttrsVo {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    // @TableId
    private Long id;
    /**
     * 组名
     */
    private String name;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 说明
     */
    private String description;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类 id
     */
    private Long categoryId;

    /**
     * 跟当前属性组关联的基本属性的数据-集合
     */
    private List<AttrEntity> attrs;

}
