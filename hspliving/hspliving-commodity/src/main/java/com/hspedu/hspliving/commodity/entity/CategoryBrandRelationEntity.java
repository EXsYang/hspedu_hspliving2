package com.hspedu.hspliving.commodity.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 品牌分类关联表
 * 
 * @author yd
 * @email yd@gmail.com
 * @date 2024-03-21 18:22:46
 */
@Data
@TableName("commodity_category_brand_relation")
public class CategoryBrandRelationEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 品牌 id
	 */
	private Long brandId;

	/**
	 * import com.fasterxml.jackson.annotation.JsonProperty;
	 *
	 * 在进行前后端分离开发时，确保前端发送的JSON数据属性名与后端JavaBean中的属性名一致是非常重要的。
	 * 这样可以保证后端正确解析前端发送的数据，并将其正确映射到相应的实体类属性上。
	 *
	 * 如果前后端属性名不一致，后端将无法正确识别前端发送的数据字段，导致数据无法被正确存储或处理。
	 *
	 * 在不使用映射注解的正常情况下，前后端属性名必须保持一致。如果需要解决属性名不一致的问题，可以在JavaBean的属性上使用 @JsonProperty 注解。
	 * 这个注解可以帮助Spring框架中的Jackson库等解析工具，将JSON中的属性名映射到不同名称的JavaBean属性上。
	 *
	 * 例如，如果前端的JSON数据中包含一个字段名为 "brand_id"，而后端JavaBean的属性名为 "brandId"，可以通过在JavaBean属性上使用 @JsonProperty("brand_id") 来实现映射。
	 *
	 * 示例：
	 * @JsonProperty("brand_id") // 映射JSON中的brand_id到JavaBean的brandId属性
	 * private Long brandId;
	 *
	 * 这样的映射确保了数据的正确传输和处理，即使前后端的命名不直接对应。
	 */
	/**
	 * 分类 id
	 */
	private Long categoryId;
	/**
	 * 品牌名称
	 */
	private String brandName;
	/**
	 * 分类名称
	 */
	private String categoryName;

}
