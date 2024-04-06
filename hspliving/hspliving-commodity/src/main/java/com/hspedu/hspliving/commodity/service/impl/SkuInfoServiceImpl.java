package com.hspedu.hspliving.commodity.service.impl;

import com.hspedu.hspliving.commodity.dao.SpuInfoDao;
import com.hspedu.hspliving.commodity.entity.SpuInfoEntity;
import com.hspedu.hspliving.commodity.vo.SearchResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hspedu.common.utils.PageUtils;
import com.hspedu.common.utils.Query;

import com.hspedu.hspliving.commodity.dao.SkuInfoDao;
import com.hspedu.hspliving.commodity.entity.SkuInfoEntity;
import com.hspedu.hspliving.commodity.service.SkuInfoService;

import javax.annotation.Resource;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    //装配SpuInfoDao
    @Resource
    private SpuInfoDao spuInfoDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    //保存SKU基本信息
    @Override
    public void saveSkuInfo(SkuInfoEntity skuInfoEntity) {
        this.baseMapper.insert(skuInfoEntity);
    }

    /**
     * 进行分页查询-携带相应的检索条件
     */
    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {

        QueryWrapper<SkuInfoEntity> queryWrapper = new QueryWrapper<>();

        //检索条件-key
        String key = (String) params.get("key");
        if (StringUtils.isNotBlank(key)) {
            queryWrapper.and(wrapper -> {
                wrapper.eq("sku_id", key).or().like("sku_name", key);
            });
        }

        //检索条件-分类
        String catalogId = (String) params.get("catalogId");
        if (StringUtils.isNotBlank(catalogId) && !"0".equalsIgnoreCase(catalogId)) {
            queryWrapper.eq("catalog_id", catalogId);
        }

        //检索条件-品牌
        String brandId = (String) params.get("brandId");
        if (StringUtils.isNotBlank(brandId) && !"0".equalsIgnoreCase(brandId)) {
            queryWrapper.eq("brand_id", brandId);
        }

        //检索条件-价格 范围 - 可以加入合理性判断，即前后端校验
        String min = (String) params.get("min");
        if (StringUtils.isNotBlank(min)) {
            //大于等于 ge
            queryWrapper.ge("price", min);
        }

        String max = (String) params.get("max");
        if (StringUtils.isNotBlank(max)) {
            //小于等于 le
            queryWrapper.le("price", max);
        }

        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    /**
     * 返回购买 用户 检索的结果
     * http://localhost:9090/list.html?keyword=海信&catalog3Id=301&brandId=1
     * 代码重构 返回PageUtils 改为返回 SearchResult 对象
     */
    @Override
    public SearchResult querySearchPageByCondition(
            Map<String, Object> params) {

        //先查询到已经上架的 spuInfoEntity,即publish_status=1的
        List<SpuInfoEntity> spuInfoEntities =
                spuInfoDao.selectList(new QueryWrapper<SpuInfoEntity>().eq("publish_status", 1));

        //收集上架的spuInfoEntity的id到集合-使用stream API 流式计算
        List<Long> spuInfoEntitiesIds = spuInfoEntities.stream().map(spuInfoEntity -> {
            return spuInfoEntity.getId();
        }).collect(Collectors.toList());


        //构建 QueryWrapper , 根据检索条件, 返回结果
        QueryWrapper<SkuInfoEntity> queryWrapper = new QueryWrapper<>();

        //将spuInfoEntitiesIds 加入到分页查询条件
        if (spuInfoEntitiesIds != null && spuInfoEntitiesIds.size() > 0) {
            //收集的有 已经上架了的spu
            queryWrapper.in("spu_id", spuInfoEntitiesIds);
        } else {
            //如果没有上架的spu商品，就直接返回,即构建一个SearchResult对象返回
            SearchResult searchResult = new SearchResult();
            //设置当前页码
            searchResult.setPageNum(1);
            searchResult.setTotal(0L);
            searchResult.setTotalPages(0);
            // searchResult.setCommodity(new ArrayList<SkuInfoEntity>());
            // searchResult.setPageNavs(new ArrayList<Integer>());
            searchResult.setCommodity(new ArrayList<>());
            searchResult.setPageNavs(new ArrayList<>());
            //设置回显检索关键字 keyword
            searchResult.setKeyword(params.get("keyword") == null ? "" : params.get("keyword").toString());

            //直接返回
            return searchResult;
        }

        //检索条件-keyword
        String keyword = (String) params.get("keyword");

        if (StringUtils.isNotBlank(keyword)) { //keyword 不是 null,"","     "
            //如果keyword视为sku_id 使用等于eq判断,如果keyword视为sku_name 使用模糊查询like判断
            queryWrapper.and(wrapper -> {
                wrapper.eq("sku_id", keyword).or().like("sku_name", keyword);
            });
        }

        //检索条件-三级分类id
        String catalog3Id = (String) params.get("catalog3Id");
        if (StringUtils.isNotBlank(catalog3Id) && !"0".equalsIgnoreCase(catalog3Id)) {
            queryWrapper.eq("catalog_id", catalog3Id);
        }

        //检索条件-品牌id
        String brandId = (String) params.get("brandId");
        if (StringUtils.isNotBlank(brandId) && !"0".equalsIgnoreCase(brandId)) {
            queryWrapper.eq("brand_id", brandId);
        }

        //如果还有其他的检索条件，这里继续处理即可...
        //为了看到分页效果,将每一页显示的记录数调整为2
        params.put("limit", "2");
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                queryWrapper
        );

        PageUtils pageUtils = new PageUtils(page);

        //将page(PageUtils) 相关数据取出，根据业务需求进行二次封装，封装成SearchResult对象
        SearchResult searchResult = new SearchResult();

        searchResult.setCommodity((List<SkuInfoEntity>) pageUtils.getList());
        searchResult.setPageNum(pageUtils.getCurrPage());
        // searchResult.setTotal(((Integer)page.getTotalCount()).longValue()); //这里太绕远了
        // searchResult.setTotal((Long) page.getTotalCount()); //这里会报错 因为这里是包装类 不可以自动类型提升
        searchResult.setTotal((long) pageUtils.getTotalCount()); //自动拆箱和自动类型提升
        int totalPage = pageUtils.getTotalPage();
        searchResult.setTotalPages(totalPage);

        List<Integer> pageNavs = new ArrayList<>();
        //遍历总的页数，放入 pageNavs
        for (int i = 1; i <= totalPage; i++) {
            pageNavs.add(i);
        }

        searchResult.setPageNavs(pageNavs);
        //设置回显检索关键字 keyword
        searchResult.setKeyword(params.get("keyword") == null ? "" : params.get("keyword").toString());

        return searchResult;
    }

}