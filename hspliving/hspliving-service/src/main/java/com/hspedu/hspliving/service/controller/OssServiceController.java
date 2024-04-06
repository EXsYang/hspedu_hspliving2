package com.hspedu.hspliving.service.controller;

import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.hspedu.common.utils.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * @author yangda
 * @create 2024-03-08-22:01
 * @description:  给客户端/用户 返回 上传Policy和签名 的服务
 *
 * 应用服务器核心代码解析
 * 应用服务器源码包含了签名直传服务以及上传回调服务两个功能。
 * 签名直传服务
 * 签名直传服务响应客户端发送给应用服务器的GET消息，代码片段如下。
 */
@RestController
public class OssServiceController {

    //装配属性
    @Resource
    OSS ossClient;

    // 加上@Value("${spring.cloud.alicloud.access-key}")注解后
    // OssServiceController 注入ioc容器时，该属性的值
    // 会按照配置文件中配置的k-v值进行注入
    @Value("${spring.cloud.alicloud.access-key}")
    private String accessId;
    @Value("${spring.cloud.alicloud.secret-key}")
    private String secret;
    @Value("${spring.cloud.alicloud.oss.endpoint}")
    private String endpoint;
    // Bucket名称
    @Value("${spring.cloud.alicloud.oss.bucket}")
    private String bucket = "ydliving-10000";


    @RequestMapping("/oss/policy")
    public R policy() {

        System.out.println("service第三方模块 policy方法 拿签名");
        // 填写Host名称，格式为https://bucketname.endpoint。
        String host = "https://" + bucket + "." + endpoint;

        // 如果只是一个斜杠`/`,则上传的文件，默认放在阿里云OSS Bucket文件列表的根目录下
        // String dir = "/";
        // 如果我们按照当前日期，来分目录存放文件 2024-3-8
        // Java基础 SimpleDateFormat
        String format =
                new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String dir = format + "/";

        Map<String, String> respMap = null;

        try {
            long expireTime = 30; //默认超时时间30s
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000; //这里得到的是毫秒
            Date expiration = new Date(expireEndTime);
            // PostObject请求最大可支持的文件大小为5 GB，即CONTENT_LENGTH_RANGE为5*1024*1024*1024。
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            // String accessId = credentialsProvider.getCredentials().getAccessKeyId();
            // 得到编码策略 encodedPolicy
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            // 得到后置签名 postSignature
            String postSignature = ossClient.calculatePostSignature(postPolicy);

            respMap = new LinkedHashMap<String, String>();
            // 注意是`accessid` 否则上传失败
            // respMap.put("accessId", accessId);
            respMap.put("accessid", accessId);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", dir);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000)); //这里除以1000后得到的是秒
            // respMap.put("expire", formatISO8601Date(expiration));


        } catch (Exception e) {
            // Assert.fail(e.getMessage());
            System.out.println(e.getMessage());
        } finally {
            ossClient.shutdown();
        }

        return R.ok().put("data", respMap);
    }


}
