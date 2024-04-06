package com.hspedu.hspliving.commodity.controller;

import com.hspedu.common.utils.PageUtils;
import com.hspedu.hspliving.commodity.entity.SkuInfoEntity;
import com.hspedu.hspliving.commodity.service.SkuInfoService;
import com.hspedu.hspliving.commodity.vo.SearchResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author yangda
 * @create 2024-04-03-23:44
 * @description:
 */
@Controller
public class SearchController {

    //装配
    @Resource
    private SkuInfoService skuInfoService;

    /**
     * 1. 家居网前台(购买用户看到的页面)-检索页
     * 2. 如果将来检索的时候有检索条件，还需要进行处理...
     * 3. 用户提交的检索条件，我们封装到 params中
     *
     * http://localhost:9090/list.html?keyword=海信&catalog3Id=301&brandId=1
     */
    @RequestMapping("/list.html")
    public String searchList(@RequestParam Map<String,Object> params, Model model){

        //分页查询
        SearchResult searchResult = skuInfoService.querySearchPageByCondition(params);

        //因为我们得到的searchResult 是需要给thymeleaf(html)模板使用，将 searchResult 放入 model
        model.addAttribute("result",searchResult);

        //说明, 默认找的就是 "classpath:templates\" + "list" + ".html"
        // thymeleaf 默认进行请求转发
        return "list";
    }
    
}
