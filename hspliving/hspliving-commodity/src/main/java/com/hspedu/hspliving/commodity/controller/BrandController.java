package com.hspedu.hspliving.commodity.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

// import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.hspedu.common.valid.SaveGroup;
import com.hspedu.common.valid.UpdateGroup;
import com.hspedu.common.valid.UpdateIsShowGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hspedu.hspliving.commodity.entity.BrandEntity;
import com.hspedu.hspliving.commodity.service.BrandService;
import com.hspedu.common.utils.PageUtils;
import com.hspedu.common.utils.R;


/**
 * 家居品牌
 *
 * @author yd
 * @email yd@gmail.com
 * @date 2024-03-05 21:20:36
 */
@RestController
@RequestMapping("commodity/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("commodity:brand:list")
    public R list(@RequestParam Map<String, Object> params) {

        System.out.println("params= " + params);
        //params= {t=1709697951824, page=1, limit=10, key=海}


        PageUtils page = brandService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    // @RequiresPermissions("commodity:brand:info")
    public R info(@PathVariable("id") Long id) {
        BrandEntity brand = brandService.getById(id);

        return R.ok().put("brand", brand);
    }

    /**
     * 保存
     * http://localhost:9090/commodity/brand/save
     * <p>
     * 1. @Validated 注解的作用就是启用 BrandEntity 字段上添加的校验
     * 2. 在这里的控制器接口方法上，如果没有写 @Validated 注解 ，这个校验规则不生效
     * 3. BindingResult result: SpringBoot 会将校验的错误放入到 result
     * 4. 程序员可以通过 BindingResult result 将校验得到的错误取出，然后进行相应的操作
     *    比如取出放入map，在返回给前端并显示
     * 5. 因为我们要将数据校验异常/错误 交给全局异常处理器 HsplivingExceptionControllerAdvice 来处理
     *    ,所以这里save()方法,就注销了数据校验相关代码，也就是将如果数据校验,验证失败后的处理交给了全局异常处理器统一处理
     * 6. 但是校验工作还是通过 下面save()方法形参位置的 @Validated注解完成的，只不过验证失败后的处理交给了全局异常处理器统一处理
     * 7. @Validated(SaveGroup.class) 表示调用save方法时，进行参数校验, 使用的是SaveGroup的校验规则
     *
     * http://localhost:9090/commodity/brand/save
     */
    @RequestMapping("/save")
    // @RequiresPermissions("commodity:brand:save")
    public R save(@Validated(SaveGroup.class) @RequestBody BrandEntity brand/*,BindingResult result*/) {

        // //先创建一个map,用于收集校验的错误
        // HashMap<String, String> map = new HashMap<>();
        // if (result.hasErrors()) {
        //     //如果有校验错误，就返回错误信息
        //     //遍历result, 将错误信息收集到map
        //     // result.getFieldErrors() 返回一个List集合
        //     // System.out.println("-------result.getFieldErrors().forEach(System.out::println);----------");
        //     // result.getFieldErrors().forEach(System.out::println);
        //     // System.out.println("-----------------");
        //
        //     result.getFieldErrors().forEach((item) -> {
        //         // 得到field
        //         String field = item.getField();
        //         // 得到校验的错误信息
        //         String message = item.getDefaultMessage();
        //         // 放入到map
        //         map.put(field, message);
        //     });
        //     // 返回收集到的错误信息
        //     return R.error(400, "品牌表单数据不合法").put("data", map);
        // } else {
        //     //如果没有校验错误，就入库
        //     brandService.save(brand);
        //     return R.ok();
        // }

            // 入库
            brandService.save(brand);
            return R.ok();
    }

    /**
     * 这里抛出了一个算数异常
     * http://localhost:9090/commodity/brand/save2
     *
     * postman 请求 http://localhost:9090/commodity/brand/save2 后 返回下面的信息
     *
     * {
     *     "timestamp": "2024-03-16T10:31:36.640+0000",
     *     "status": 500,
     *     "error": "Internal Server Error",
     *     "message": "/ by zero",
     *     "path": "/commodity/brand/save2"
     * }
     *
     * 这里发生的情况是，当你的Spring Boot应用中发生了异常（在这个案例中是算术异常，由`int i = 10 / 0;`产生），且没有被应用内的任何异常处理器（如`@ExceptionHandler`注解的方法）捕获时，Spring Boot的默认异常处理机制会接管。
     *
     * Spring Boot为了方便开发者，默认集成了一个基本的异常处理机制，这个机制是通过`BasicErrorController`实现的。当应用中抛出一个异常，而且这个异常没有在应用层面得到处理，它会被Spring Boot的默认错误处理控制器捕获。然后，这个控制器会根据请求的类型（是HTML页面请求还是API调用）来决定返回一个错误页面还是一个错误信息的JSON。
     *
     * 因为你是通过浏览器地址栏直接访问的，这通常被当作一个API调用（特别是在REST API中），所以Spring Boot返回了一个标准的错误响应的JSON，其中包含了错误信息。这就是你看到的那个包含时间戳、状态码、错误类型、具体错误信息（在这里是`/ by zero`）和请求路径的JSON对象。
     *
     * 如果你想自定义这些错误信息，或者想提供不同类型的响应，你可以自定义异常处理逻辑，比如使用`@ControllerAdvice`和`@ExceptionHandler`注解来捕获特定类型的异常，并返回你想要的响应。这样，你就可以完全控制异常的处理方式和返回给客户端的信息了。
     *
     * 简而言之，你没有直接编写返回这个特定格式的代码，是因为这是Spring Boot的默认错误处理行为。如果你想要修改这个行为，你需要自定义你的异常处理策略。
     */
    @RequestMapping("/save2")
    // @RequiresPermissions("commodity:brand:update")
    public R save2(@RequestBody BrandEntity brand) {

        // 注意要产生一个算数异常，即方法要走到这里来，需要先满足前端要给一个BrandEntity json数据
        // 如果在全局异常处理类使用
        // @ExceptionHandler({Throwable.class})
        // public R handleException(Throwable throwable){}进行了全局处理
        // 则前端即使不满足前端要给一个BrandEntity json数据，也会由handleException()进行处理
        System.out.println("产生算数异常 10/0");
        int i = 10 / 0;
        return R.ok();
    }
    /**
     * 修改
     * 1. @Validated(UpdateGroup.class) 表示进行修改/调用update方法时,会对即将要
     *    封装到brand对象的属性 进行参数校验,使用的时UpdateGroup组的校验规则
     *
     */
    @RequestMapping("/update")
    // @RequiresPermissions("commodity:brand:update")
    public R update(@Validated(UpdateGroup.class) @RequestBody BrandEntity brand) {
        brandService.updateById(brand);

        return R.ok();
    }

    @RequestMapping("/update/isshow")
    // @RequiresPermissions("commodity:brand:update")
    public R updateIsShow(@Validated(UpdateIsShowGroup.class) @RequestBody BrandEntity brand) {
        brandService.updateById(brand);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("commodity:brand:delete")
    public R delete(@RequestBody Long[] ids) {
        brandService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
