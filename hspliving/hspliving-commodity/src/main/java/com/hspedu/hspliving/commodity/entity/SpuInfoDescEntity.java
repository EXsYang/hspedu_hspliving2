package com.hspedu.hspliving.commodity.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 商品 spu 信息介绍
 * 
 * @author yd
 * @email yd@gmail.com
 * @date 2024-03-30 01:15:01
 *
 * SpuInfoDescEntity 类 即是存放SpuSaveVO对象的商品描述图片url信息的类
 *  1. 保存商品 spu 的介绍图片,单独创建一张表, 可能有多张图片路径(使用,隔开)，也可以没有
 *  2. 这里的主键 商品 id 即spu_id 和前面的表commodity_spu_info的id关联，因此这里没有设置为自增长的
 */
@Data
@TableName("commodity_spu_info_desc")
public class SpuInfoDescEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 在 MyBatis Plus 中，@TableId 注解是用来标识实体类中的属性对应数据库表的主键字段。@TableId 注解主要有两个作用：
	 *
	 * 标识主键字段：告诉 MyBatis Plus，这个实体类的哪个属性是数据库表的主键列。
	 * 指定主键生成策略：通过注解的 type 属性来指定如何生成主键的值。
	 * @TableId 注解的使用
	 * 没有指定 type 属性：当你只写 @TableId 而没有指定 type 属性时，MyBatis Plus 会使用全局的主键生成策略。这个策略默认是 IdType.NONE（即不自动增长），但可以在配置文件中被覆盖。
	 *
	 * 指定 type = IdType.INPUT：这表明你打算“手动”输入主键的值，而不是依赖数据库的自增长特性或者 MyBatis Plus 的其他主键生成策略来自动生成这个值。在某些情况下，比如你希望使用另一个表的 ID 作为当前表的主键，或者你有自己的主键生成逻辑，这种方式非常有用。
	 */
	/**
	 * 商品 id
	 * 注解 @TableId(type = IdType.INPUT)表示，
	 * 用户输入ID <p>该类型可以通过自己注册自动填充插件进行填充</p>
	 *
	 * 说明:
	 * 1. 因为 commodity_spu_info_desc表的spu_id 字段不是自增长的
	 * 2. 而是我们添加SpuInfoDescEntity对象时配置的id,即与表commodity_spu_info的id字段关联
	 */
	// @TableId
	@TableId(type = IdType.INPUT)
	private Long spuId;
	/**
	 * 商品介绍图片
	 */
	private String decript;

}
