package com.hspedu.hspliving.commodity.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 商品 spu 信息
 * 
 * @author yd
 * @email yd@gmail.com
 * @date 2024-03-29 20:44:53
 *
 * SpuInfoEntity类 即是存放SpuSaveVO对象的基本的spu信息的类
 * /* 1. 保存商品 spu 信息
 * /* 2. 1 个 spu 信息可能对于多个 sku
 * /* 3. 1 个 spu+1 个 sku 就是一个商品的组合关系()
 *
 */
@Data
@TableName("commodity_spu_info")
public class SpuInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;


	/**
	 * 商品 id
	 * 在 MyBatis Plus 配置中，当您在 application.yml 或 application.properties 文件
	 * 中配置了全局的主键生成策略为自增（id-type: auto），那么所有实体类中带有
	 * @TableId 注解且没有显式指定主键生成策略（type 属性）的字段，
	 * 都将默认采用这个全局配置的自增策略。
	 *
	 * 说明:
	 * 1. 这里的 @TableId 没有明确的显式指定主键生成策略（type 属性）的字段，
	 * 但是在application.yml指定了`id-type: auto`
	 * ，因此这里遵守全局策略自增长auto
	 *
	 * 2.@TableId 注解确实会把自增长的值自动 回填 到对应的实体类属性中
	 * ，使得在插入操作完成后，你可以直接从实体对象获取到这个值。
	 */
	@TableId
	private Long id;
	/**
	 * 商品名称
	 */
	private String spuName;
	/**
	 * 商品描述
	 */
	private String spuDescription;
	/**
	 * 所属分类 id
	 */
	private Long catalogId;
	/**
	 * 品牌 id
	 */
	private Long brandId;
	/**
	 * 
	 */
	private BigDecimal weight;
	/**
	 * 上架状态[0 - 下架，1 - 上架]
	 */
	private Integer publishStatus;
	/**
	 * 
	 */
	private Date createTime;
	/**
	 * 
	 */
	private Date updateTime;

}
