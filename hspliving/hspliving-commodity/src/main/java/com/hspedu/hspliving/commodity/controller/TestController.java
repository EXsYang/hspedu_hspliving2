package com.hspedu.hspliving.commodity.controller;

import com.aliyun.oss.*;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.hspedu.common.utils.R;
import com.hspedu.hspliving.commodity.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author yangda
 * @create 2024-03-08-15:09
 * @description: 阿里云对象存储OSS（Object Storage Service） 测试
 */
@RestController
@Slf4j
public class TestController {


    //编写方法-上传指定的文件 [原生SDK 完成文件上传到阿里云OSS对象存储服务的bucket]
    // localhost:9090/test
    // @RequestMapping("/test")
    // public R testUpload() throws FileNotFoundException {
    //     //参考文档 https://help.aliyun.com/zh/oss/developer-reference/simple-upload-11?spm=a2c4g.11186623.0.0.1fb056e5DTFLH0
    //
    //     // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
    //     // String endpoint = "https://oss-cnxxxxxxxuncs.com";
    //     String endpoint = "oss-xxxxx.aliyuncs.com";
    //     // 从环境变量中获取访问凭证。运行本代码示例之前，请确保已设置环境变量OSS_ACCESS_KEY_ID和OSS_ACCESS_KEY_SECRET。
    //     // EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
    //
    //     // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录
    //     String accessKeyId = "xxxxxxxPqxxxxx5";
    //     String accessKeySecret = "xxxxxxx7JxxG";
    //
    //     // 填写Bucket名称，例如examplebucket。
    //     String bucketName = "ydxxxx10000";
    //     // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
    //     // 就是文件上传后的文件名
    //     // 上传的文件的文件名如果有相同的,比如同一个文件上传了两次,就会发生覆盖，文件名相同就会覆盖
    //     // String objectName = "1(副本).jpeg";
    //     // 文件名中如果有斜杆`/`就会生成一级文件夹
    //     // String objectName = "2024-3-8/1(副本2).jpeg";
    //
    //     // 下面这种不会生成一个文件夹，而是当作文件名的一部分 '2024-3-8\1(副本3).jpeg'
    //     String objectName = "2024-3-8\\1(副本3).jpeg";
    //     // 填写本地文件的完整路径，例如D:\\localpath\\examplefile.txt。
    //     // 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
    //     // 这里指定你要上传的文件完整路径
    //     String filePath= "C:\\Users\\yangd\\Desktop\\新建文件夹 (2)\\1.jpeg";
    //
    //     // 创建OSSClient实例。
    //     // OSS ossClient = new OSSClientBuilder().build(endpoint, credentialsProvider);
    //     OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId,accessKeySecret);
    //
    //     try {
    //         InputStream inputStream = new FileInputStream(filePath);
    //         // 创建PutObjectRequest对象。
    //         // PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, inputStream);
    //         // 创建PutObject请求。
    //         // PutObjectResult result = ossClient.putObject(putObjectRequest);
    //
    //         // 创建PutObject请求。
    //         ossClient.putObject(bucketName, objectName, inputStream);
    //     } catch (OSSException oe) {
    //         System.out.println("Caught an OSSException, which means your request made it to OSS, "
    //                 + "but was rejected with an error response for some reason.");
    //         System.out.println("Error Message:" + oe.getErrorMessage());
    //         System.out.println("Error Code:" + oe.getErrorCode());
    //         System.out.println("Request ID:" + oe.getRequestId());
    //         System.out.println("Host ID:" + oe.getHostId());
    //     } catch (ClientException ce) {
    //         System.out.println("Caught an ClientException, which means the client encountered "
    //                 + "a serious internal problem while trying to communicate with OSS, "
    //                 + "such as not being able to access the network.");
    //         System.out.println("Error Message:" + ce.getMessage());
    //     } finally {
    //         if (ossClient != null) {
    //             ossClient.shutdown();
    //         }
    //     }
    //
    //
    //     return null;
    // }



    //装配OSSClient
    @Resource
    private OSSClient ossClient;

    //编写方法-上传指定的文件 [springcloud alibaba 上传文件阿里云OSS对象存储服务的bucket]
    // localhost:9090/test2
    @RequestMapping("/test2")
    public R testUpload2() throws FileNotFoundException {

        // 下面这种不会生成一个文件夹，而是当作文件名的一部分 '2024-3-8\1(副本3).jpeg'
            String objectName = "助手.jpg";
            // 填写本地文件的完整路径，例如D:\\localpath\\examplefile.txt。
            // 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
            // 这里指定你要上传的文件完整路径
            String filePath= "C:\\Users\\yangd\\Desktop\\新建文件夹 (2)\\1.jpg";

        InputStream inputStream = new FileInputStream(filePath);

        ossClient.putObject("ydliving-10000",objectName,inputStream);
        ossClient.shutdown();

        return R.ok("上传文件OK");
    }


    // 编写方法测试 getCascadedCategoryId
    // 传入 categoryId=301 => 应该返回 [1,21,301]
    // 装配
    @Resource
    private CategoryService categoryService;

    @RequestMapping("/test3")
    public R testGetCascadedCategoryId(){

        Long[] cascadedCategoryId = categoryService.getCascadedCategoryId(301L);

        // System.out.println("cascadedCategoryId= " + cascadedCategoryId); //cascadedCategoryId= [Ljava.lang.Long;@316952c4
        for (Long categoryId : cascadedCategoryId) {
            System.out.println("categoryId= " + categoryId);
        }

        return R.ok().put("data",cascadedCategoryId);

    }
}
