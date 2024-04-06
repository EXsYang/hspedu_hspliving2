/**
  * Copyright 2021 json.cn
  */
package com.hspedu.hspliving.commodity.vo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Auto-generated: 2021-10-13 23:25:51
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
@Data
public class SpuSaveVO {

    private String spuName;
    private String spuDescription;
    private Long catalogId;
    private Long brandId;
    private BigDecimal weight;
    private int publishStatus;
    private List<String> decript;
    private List<String> images;
    private Bounds bounds;
    private List<BaseAttrs> baseAttrs;

    //如果前端发送的 JSON 中包含 "skus": []（一个空数组），那么 skus 会被初始化为一个空的 List。在这种情况下，skus.size() 为 0，表明列表中没有元素。
    //
    // 如果前端发送的 JSON 中根本没有 skus 字段，那么 skus 属性将保持为 null，除非你在某处（比如在类的构造函数中或者在声明时）显式地对它进行了初始化。

    private List<Skus> skus;


}
