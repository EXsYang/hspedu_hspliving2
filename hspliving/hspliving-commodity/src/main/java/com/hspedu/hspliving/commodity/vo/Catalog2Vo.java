package com.hspedu.hspliving.commodity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yangda
 * @create 2024-04-03-16:03
 * @description:
 *
 * Catalog2Vo 是返回给前端的二级分类的信息/数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Catalog2Vo {

    /**
     * 一级父类的id
     */
    private String catalog1Id;


    /**
     * 三级分类的信息-集合
     */
    private List<Category3Vo> catalog3List;


    /**
     * 二级分类本身的信息
     */
    private String id;
    private String name;


    /**
     * 要给前端返回的 三级分类信息的类的 类型 - 公共的静态内部类
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Category3Vo{
        /**
         * 父级分类-二级分类的id
         */
        private String catalog2Id;

        /**
         * 三级分类本身的信息
         */
        private String id;
        private String name;
    }



}
