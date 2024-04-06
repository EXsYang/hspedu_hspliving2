package com.hspedu.hspliving.commodity.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * sku 信息
 * 
 * @author yd
 * @email yd@gmail.com
 * @date 2024-03-31 17:32:49
 */
@Data
@TableName("commodity_sku_info")
public class SkuInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * skuId
	 */
	@TableId
	private Long skuId;
	/**
	 * spuId
	 */
	private Long spuId;
	/**
	 * sku 名称
	 */
	private String skuName;
	/**
	 * sku 介绍描述
	 */
	private String skuDesc;
	/**
	 * 所属分类 id
	 */
	private Long catalogId;
	/**
	 * 品牌 id
	 */
	private Long brandId;
	/**
	 * 默认图片
	 */
	private String skuDefaultImg;
	/**
	 * 标题
	 */
	private String skuTitle;
	/**
	 * 副标题
	 */
	private String skuSubtitle;
	/**
	 * 价格
	 */
	private BigDecimal price;
	/**
	 * 销量
	 */
	private Long saleCount;

}
