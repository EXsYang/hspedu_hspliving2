package com.hspedu.hspliving.commodity.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 商品分类表
 * 
 * @author yd
 * @email yd@gmail.com
 * @date 2024-02-14 22:55:23
 */
@Data
@TableName("commodity_category")
public class CategoryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id 自增的，默认给null即可
	 */
	@TableId
	private Long id;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 父分类 id
	 */
	private Long parentId;
	/**
	 * 层级
	 */
	private Integer catLevel;
	/**
	 * 0 不显示，1 显示
	 * 1.如果我们没有在application.yml文件配置逻辑删除和逻辑未删除的值
	 * 	 也可以通过@TableLogic注解来配置，如下  delval:默认逻辑删除值
	 * @TableLogic(value = "1",delval = "0")
	 */
	@TableLogic
	// @TableLogic(value = "1",delval = "0")
	private Integer isShow; //设置的逻辑删除的属性的类型有要求，这里是Integer,可以匹配上在application.yml文件配置逻辑删除和逻辑未删除的值
	/**
	 * 排序
	 */
	private Integer sort;
	/**
	 * 图标
	 */
	private String icon;
	/**
	 * 统计单位
	 */
	private String proUnit;
	/**
	 * 商品数量
	 */
	private Integer proCount;

	/**
	 * 增加属性 childrenCategories
	 * 1. childrenCategories 表示某个分类的子分类集合
	 * 2. childrenCategories 没有对应表 commodity_category 的字段
	 *    ，需要使用注解，排除掉该属性和表中字段的映射
	 * 3. @TableField(exist = false),表示childrenCategories不对应表的字段
	 * 4. @JsonInclude(JsonInclude.Include.NON_EMPTY)
	 * 	   表示如果childrenCategories的结果为空数组/集合就不需要返回
	 *
	 * 5. `@JsonInclude(JsonInclude.Include.NON_EMPTY)` 是一个来自 Jackson 库的注解，用在 Java 对象的属性上。在这个上下文中，它被应用到 `childrenCategories` 属性上。
	 *  这个注解的作用是在将 Java 对象序列化为 JSON 时，对该属性的处理进行特定的设定。具体来说，`JsonInclude.Include.NON_EMPTY` 参数指定如果 `childrenCategories` 属性是空（空集合或空数组），则在生成的 JSON 中不包含这个属性。
	 *  这意味着，如果 `childrenCategories` 是一个空列表（没有子类别），那么在序列化包含这个属性的对象时，`childrenCategories` 字段将被省略，不会出现在结果 JSON 字符串中。这样做可以减少 JSON 的大小，使输出更加简洁。
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@TableField(exist = false)
	private List<CategoryEntity> childrenCategories;

}
