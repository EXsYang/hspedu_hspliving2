package com.hspedu.hspliving.commodity.vo;

import com.hspedu.hspliving.commodity.entity.SkuInfoEntity;
import lombok.Data;

import java.util.List;

/**
 * @author yangda
 * @create 2024-04-04-19:47
 * @description:
 * SearchResult: 就是用户在前端页面检索sku商品时需要返回的VO对象
 * 属性的名称和类型，需要按照业务需求设计
 * url-> http://localhost:9090/list.html
 */
@Data
public class SearchResult {

    //查询到所有的商品信息
    private List<SkuInfoEntity> commodity;

    //当前页码
    private Integer pageNum;

    //总的记录数
    private Long total;

    //共多少页-总的页码数
    private Integer totalPages;

    //分页导航条 是根据总的页码数得来的
    private List<Integer> pageNavs;

    //keyword 用于回显搜索框中进行搜索的关键字
    private String keyword;

}
