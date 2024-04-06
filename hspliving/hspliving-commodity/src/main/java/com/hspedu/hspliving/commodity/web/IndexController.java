package com.hspedu.hspliving.commodity.web;

import com.hspedu.hspliving.commodity.entity.CategoryEntity;
import com.hspedu.hspliving.commodity.service.CategoryService;
import com.hspedu.hspliving.commodity.vo.Catalog2Vo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author yangda
 * @create 2024-04-02-20:45
 * @description:
 * 注意该 IndexController 需要使用thymeleaf模板引擎进行视图解析
 * 所以这里使用的是 @Controller 注解，不要写错 不要写@RestController
 */
@Controller
public class IndexController {


    //装配
    @Resource
    private CategoryService categoryService;

    //响应用户请求首页 http://localhost:9090 或者
    // http://localhost:9090/index.html 都能请求到indexPage
    @GetMapping(value = {"/","/index.html"})
    // private String indexPage(Model model){
    public String indexPage(Model model){

        //调用方法,获取到一级分类信息[集合]
        List<CategoryEntity> categoryEntities =
                categoryService.getLevel1Categories();

        // System.out.println("categoryEntities= " + categoryEntities);

        //将categoryEntities放入到 model, 即放入到数据层 request域中
        model.addAttribute("categories",categoryEntities);

        //说明, 默认找的就是 "classpath:templates\" + "index" + ".html"
        return "index";
    }


    /**
     * 注解 @ResponseBody 返回的是json格式数据
     * http://localhost:9090/index/catalog.json
     */
    @GetMapping("/index/catalog.json")
    @ResponseBody
    public Map<String,List<Catalog2Vo>> getCatalogJson(){

        Map<String, List<Catalog2Vo>> catalogJson =
                categoryService.getCatalogJson();

        // System.out.println("catalogJson=> " + catalogJson);

        return catalogJson;
    }


}
