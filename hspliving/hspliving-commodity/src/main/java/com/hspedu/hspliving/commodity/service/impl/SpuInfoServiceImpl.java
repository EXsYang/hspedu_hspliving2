package com.hspedu.hspliving.commodity.service.impl;

import com.hspedu.hspliving.commodity.entity.*;
import com.hspedu.hspliving.commodity.service.*;
import com.hspedu.hspliving.commodity.vo.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hspedu.common.utils.PageUtils;
import com.hspedu.common.utils.Query;

import com.hspedu.hspliving.commodity.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    //装配 SpuInfoDescService
    @Resource
    private SpuInfoDescService spuInfoDescService;
    //装配 SpuImagesService
    @Resource
    private SpuImagesService spuImagesService;
    //装配
    @Resource
    private AttrService attrService;
    //装配
    @Resource
    private ProductAttrValueService productAttrValueService;

    //装配
    @Resource
    private SkuInfoService skuInfoService;

    //装配
    @Resource
    private SkuImagesService skuImagesService;

    //装配
    @Resource
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 保存SpuSaveVO对象/数据到表中[会根据业务，将数据分别保存对应表]
     * 保存Spu信息。
     * <p>
     * 此方法接收一个 SpuSaveVO 对象作为参数，该对象包含了前端提交的Spu数据。
     * 主要步骤为：
     * 1. 将 SpuSaveVO 对象中的部分属性拷贝到 SpuInfoEntity 实体类中。
     * SpuSaveVO 和 SpuInfoEntity 之间有相同的属性名称和类型，这使得我们可以通过 BeanUtils.copyProperties
     * 方法直接进行属性复制，以便保存 Spu 的基本信息到数据库表 commodity_spu_info 中。
     * 2. 对于 SpuSaveVO 中的其他属性，如描述、图片等，根据具体业务需求，可能需要保存到其他相关表中。
     * (此处的注释假设之后的代码会处理这些操作，实际代码中应根据实际情况补充。)
     * 3. 因为saveSpuInfo方法 涉及到对多表的操作, 因此需要进行事务管理, 所以我们标识@Transactional
     */
    @Transactional
    @Override
    public void saveSpuInfo(SpuSaveVO spuSaveVO) {

        // 1----------- 保存spu基本信息 start 对应的表-> commodity_spu_info ----------------
        //1. 保存spu基本信息到 表commodity_spu_info
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();

        //2. 将spuSaveVO对象的属性值对拷到 spuInfoEntity对象[属性名需要有对应关系]
        /**
         * spuSaveVO对象的以下六个属性:
         *     private String spuName;
         *     private String spuDescription;
         *     private Long catalogId;
         *     private Long brandId;
         *     private BigDecimal weight;
         *     private int publishStatus;
         *
         * 在SpuInfoEntity对象中也有相同的对应的六个属性，类型和字段名都相同
         * 因此可以使用BeanUtils.copyProperties(spuSaveVO,spuInfoEntity);进行对拷操作
         */
        /**使用 BeanUtils.copyProperties(source, target) 方法时的关键点:

         1. 属性名称和类型匹配:
         - 名称匹配: 源对象和目标对象之间对应的属性名必须完全相同。
         - 类型兼容: 属性的类型也需要相互兼容。基本数据类型和其对应的包装类型之间是兼容的。

         2. 访问权限:
         - 必须确保源对象具有相应属性的公开（public）getter方法，且目标对象具有相应属性的公开（public）setter方法。
         这是因为 BeanUtils.copyProperties 方法依赖于 JavaBean 的属性访问规范。

         3. 浅拷贝:
         - 该方法执行的是浅拷贝操作。如果属性类型是复杂对象，复制的是对象引用而不是对象本身的副本。
         需要深拷贝的场景下要特别注意这一点。

         4. null 值的处理:
         - 默认情况下，如果源对象的某个属性值为 null，相应的目标对象的属性也会被设置为 null。
         这可能会覆盖目标对象中原有的值，需要在这种情况下特别小心。

         应用场景:
         - 数据转换: 当需要将数据从一个层（如 DTO）转换到另一个层（如实体类）时，此方法提供了一种快速转换属性值的方式。
         - 更新对象: 在执行更新操作时，可以使用此方法将更新的字段值从源对象复制到已存在的目标对象中。

         注意事项:
         - 使用此方法时，应该注意可能的数据覆盖问题，特别是对于默认值和 null 值的处理。
         对于需要深拷贝的场景，可能需要寻找其他解决方案或手动编写复制逻辑。*/
        BeanUtils.copyProperties(spuSaveVO, spuInfoEntity);

        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());

        //3. 将spuInfoEntity 保存到表commodity_spu_info，
        //   这里为了可读性和扩展性，我们单独写一个方法saveBaseSpuInfo()
        this.saveBaseSpuInfo(spuInfoEntity);
        // 1----------- 保存spu基本信息 end ----------------

        // 2----------- 保存描述/介绍图片的信息 start 对应的表-> commodity_spu_info_desc ----------------
        List<String> decript = spuSaveVO.getDecript();
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();

        // 这里我们设置给spuInfoDescEntity对象的spuId就是上面添加的spuInfoEntity对应的id
        // 又因为spuInfoEntity对象的id属性有@TableId 注解，
        // @TableId 注解确实会把自增长的值自动 回填 到对应的实体类属性中
        // ,使得在插入操作完成后，你可以直接从实体对象获取到这个值。
        // 所以这里可以直接使用spuInfoEntity.getId() 获取到保存完spuInfoEntity对象后自增长的id字段的值
        spuInfoDescEntity.setSpuId(spuInfoEntity.getId());

        // 判断 decript 中有几个图片路径-元素
        if (decript.size() == 0) {
            //没有spu对应的介绍图片，这里设置一张默认图片
            spuInfoDescEntity.setDecript("default.jpg");
        } else {
            //spu有对应的介绍图片，就进行遍历，如果有多个图片url,就使用英文逗号间隔 集合中的形式如 [1.jpg,2.jpg]
            //String.join(",",decript) 该方法 会遍历 decript 并使用第一个形参作为分隔符进行拼接
            // String.join()方法处理后的形式如:"1.jpg,2.jpg"
            spuInfoDescEntity.setDecript(String.join(",", decript));
        }

        //保存 spuInfoDescEntity
        spuInfoDescService.saveSpuInfoDesc(spuInfoDescEntity);

        // 2----------- 保存描述/介绍图片的信息 end ----------------

        // 3----------- 批量保存spu对应的图片集的信息 start 对应的表-> commodity_spu_images ----------------

        //(1)先得到前端发过来的spu对应的图片集的信息
        List<String> images = spuSaveVO.getImages();
        //(2)批量保存
        spuImagesService.saveImages(spuInfoEntity.getId(), images);

        // 3----------- 批量保存spu对应的图片集的信息 end 对应的表-> commodity_spu_images ----------------

        // 4----------- 批量保存SPU对应的基本属性baseAttrs[一个SPU可以有多个基本属性] start 对应的表-> commodity_product_attr_value ----------------

        //(1) 从spuSaveVO对象取出前端通过json发送的 基本属性数据
        List<BaseAttrs> baseAttrs = spuSaveVO.getBaseAttrs();
        //(2) 遍历baseAttrs 将数据封装到 ProductAttrValueEntity[根据相应的业务要求完成，因为出现了数据不一致需要处理]-并将其收集到集合中
        //    ,通常情况下对有综合业务需求处理的遍历，使用流式计算 stream,因为可以把需要的数据收集起来，进行二次处理
        List<ProductAttrValueEntity> productAttrValueEntities = baseAttrs.stream().map(attr -> {
            //创建ProductAttrValueEntity对象
            ProductAttrValueEntity productAttrValueEntity = new ProductAttrValueEntity();
            productAttrValueEntity.setSpuId(spuInfoEntity.getId());
            productAttrValueEntity.setQuickShow(attr.getShowDesc());
            productAttrValueEntity.setAttrValue(attr.getAttrValues());
            productAttrValueEntity.setAttrSort(0);//给默认值0
            productAttrValueEntity.setAttrId(attr.getAttrId());

            // 这里我们发现前端没有通过json把基本属性名携带到VO对象,需要我们进行二次处理
            // 通过前端传过来的attrId,到数据库查到对应的attrEntity
            // ,再获取到对应的attrName,在设置到productAttrValueEntity中
            AttrEntity attrEntity = attrService.getById(attr.getAttrId());
            productAttrValueEntity.setAttrName(attrEntity.getAttrName());

            return productAttrValueEntity;
        }).collect(Collectors.toList());

        //(3) 将收集到的productAttrValueEntities 批量保存到对应的表 commodity_product_attr_value
        productAttrValueService.saveProductAttr(productAttrValueEntities);

        // 4----------- 批量保存SPU对应的基本属性baseAttrs[一个SPU可以有多个基本属性] end 对应的表-> commodity_product_attr_value ----------------

        // 5-----------  //保存SKU基本信息attr[一个SKU可以有多个基本属性] start 对应的表-> commodity_sku_info ----------------

        //(1) 从spuSaveVO对象取出前端通过json发送的 sku基本信息的数据
        //前端发送的多个sku信息是多个-对应集合
        List<Skus> skus = spuSaveVO.getSkus();

        //(2) 遍历skus, 将sku的基本信息封装到 SkuInfoEntity[根据相应的业务要求处理即可]
        //这里依然涉及到业务处理，我们使用遍历完成


        if (skus != null && skus.size() > 0) {

            skus.forEach(item -> {

                //先创建 SkuInfoEntity 对象
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                skuInfoEntity.setSpuId(spuInfoEntity.getId());

                //处理默认图片
                String defaultImg = "default.jpg";

                for (Images image : item.getImages()) {
                    // 这里要保证前端只有一个图片是默认图片,
                    // 在这里的所有的image对象里只有一个 image.getDefaultImg() == 1,比如这里的图片集有4张图片，但只有一个图片的 DefaultImg = 1
                    if (image.getDefaultImg() == 1) {
                        // 如果设置的有默认图片，就将其作为默认图片
                        defaultImg = image.getImgUrl();
                    }
                }

                skuInfoEntity.setSkuDefaultImg(defaultImg);

                skuInfoEntity.setSaleCount(0L); //销量默认给0
                skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
                skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
                skuInfoEntity.setPrice(item.getPrice());
                // skuInfoEntity.setSkuDesc("");
                skuInfoEntity.setSkuName(item.getSkuName());
                skuInfoEntity.setSkuTitle(item.getSkuTitle());
                skuInfoEntity.setSkuSubtitle(item.getSkuSubtitle());

                //保存sku基本信息到表中 commodity_sku_info
                skuInfoService.saveSkuInfo(skuInfoEntity);

                // 6-----------  //保存SKU 的图片集信息 - images [一个SKU可以对应多个图片 image] start 对应的表-> commodity_sku_images ----------------
                //(1) 从item(sku)取出图片集集合->进行遍历->把数据封装到 SkuImagesEntity
                //    -> 再进行保存即可
                //(2) 在收集skuImagesEntities时，要过滤imgUrl为空的情况
                List<Images> imagesList = item.getImages();
                List<SkuImagesEntity> skuImagesEntities = imagesList.stream().map(img -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    skuImagesEntity.setImgSort(0);
                    skuImagesEntity.setDefaultImg(img.getDefaultImg());
                    skuImagesEntity.setImgUrl(img.getImgUrl());
                    skuImagesEntity.setSkuId(skuInfoEntity.getSkuId());
                    return skuImagesEntity;
                }).filter(img -> {
                    //过滤掉 img(实际上是SkuImagesEntity对象)对象的imgUrl为空,""的对象
                    //filter 中的 img 实际上是 map 方法转换后的 SkuImagesEntity 对象，而不是原始的 Images 对象。

                    //因为前端发过来的sku 的 images 其中有imgUrl="" 的，
                    //而且是按照spu images的数量进行占位，然后发送到后端的
                    //这样导致选择spu图片时，选了几个图片，在sku 的images中就有几个占位
                    //即对应到后端会映射为对应数量的Images对象,因此其中有imgUrl="" 的Images对象
                    //具体可以到对应的前端项目renren-fast-vue的 src/components/upload/singleUpload.vue文件 default.methods.submitSkus()方法中打印json输出查看
                    return StringUtils.isNotBlank(img.getImgUrl());
                }).collect(Collectors.toList());

                //(3) 批量添加
                skuImagesService.saveBatch(skuImagesEntities);

                // 6-----------  //保存SKU 的图片集信息 - images [一个SKU可以对应多个图片 image] end 对应的表-> commodity_sku_images ----------------

                // 7-----------  //保存SKU 的销售属性attr信息 - attr [一个SKU可以对应多个销售属性 attr] start 对应的表-> commodity_sku_sale_attr_value ----------------

                //(1) 得到前端发送的sku的销售属性-集合
                List<Attr> attrs = item.getAttr();

                //(2) 遍历attrs 将数据封装到 SkuSaleAttrValueEntity对象中[根据具体的业务需求完成即可，即前端封装到VO的数据和保存的Entity如果有对应不上的属性，比如新增了或者减少了，按照业务需求处理即可]
                List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = attrs.stream().map(attr -> { //attr就是一个销售属性
                    SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                    skuSaleAttrValueEntity.setSkuId(skuInfoEntity.getSkuId());
                    skuSaleAttrValueEntity.setAttrSort(0); //默认排序给0
                    skuSaleAttrValueEntity.setAttrId(attr.getAttrId());
                    skuSaleAttrValueEntity.setAttrName(attr.getAttrName());
                    skuSaleAttrValueEntity.setAttrValue(attr.getAttrValue());
                    return skuSaleAttrValueEntity;
                }).collect(Collectors.toList());

                //(3)批量保存
                skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntities);

                // 7-----------  //保存SKU 的销售属性attr信息 - attr [一个SKU可以对应多个销售属性 attr] end 对应的表-> commodity_sku_sale_attr_value ----------------
            });

        }

        // 5-----------  //保存SKU基本信息attr[一个SKU可以有多个基本属性] end 对应的表-> commodity_sku_info ----------------

    }

    // 将spuInfoEntity (即SpuSaveVO对象的基本的spu信息的对象) 保存到表commodity_spu_info
    @Override
    public void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity) {
        // this.save(spuInfoEntity);

        this.baseMapper.insert(spuInfoEntity);
    }

    /**
     * 根据前端携带的检索条件，进行分页查询。 Condition:条件
     * 最重要的就是组织好QueryWrapper
     *
     * @param params
     * @return
     */
    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {

        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();

        //检索条件-key
        String key = (String) params.get("key");
        if (StringUtils.isNotBlank(key)) {
            //key 不为null 或者 "" 或者 "  " ...
            //我们定一个业务需求,如果视为 id ,就是等于, 如果视为名称就是模糊查询
            wrapper.and(w -> {
                w.eq("id", key).or().like("spu_name", key);
            });
        }

        //检索条件-状态
        String status = (String) params.get("status");
        if (StringUtils.isNotBlank(status)) {
            wrapper.eq("publish_status", status);
        }

        //检索条件-品牌id
        String brandId = (String) params.get("brandId");
        if (StringUtils.isNotBlank(brandId) && !"0".equalsIgnoreCase(brandId)) {
            wrapper.eq("brand_id", brandId);
        }

        //检索条件-分类id
        String catalogId = (String) params.get("catalogId");
        if (StringUtils.isNotBlank(catalogId) && !"0".equalsIgnoreCase(catalogId)) {
            wrapper.eq("catalog_id", catalogId);
        }


        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    //商品SPU上架
    @Override
    public void up(Long spuId) {
        this.baseMapper.updateSpuStatus(spuId,1);
    }

    //商品SPU下架
    @Override
    public void down(Long spuId) {
        this.baseMapper.updateSpuStatus(spuId,2);
    }


}