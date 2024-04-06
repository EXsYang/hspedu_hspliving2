package com.hspedu.common.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author yangda
 * @create 2024-03-17-18:50
 * @description: 1. 创建自定义的注解 @EnumValidate 参考@NotNull源码来开发
 * 2. @Constraint(validatedBy = {EnumConstraintValidator.class}) ,  可以指定该自定义注解可以和EnumConstraintValidator.class校验器关联
 * 3. String message() default "{?}"; 可以指定校验时返回的信息
 * 4.  Class<?>[] groups() default { }; 指定以后的自定义注解 @EnumValidate 在哪些校验组生效
 * 5. 可以在@Constraint validatedBy 属性指定多个校验器，因为validatedBy接收的是一个数组类型
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {EnumConstraintValidator.class})
public @interface EnumValidate {

    // String message() default "{javax.validation.constraints.NotNull.message}";
    // "{javax.validation.constraints.NotNull.message}" 就是默认的message是啥
    // ，这里指定的是message的key javax.validation.constraints包下的注解的 这个默认的消息是在  ValidationMessages_zh_CN.properties 文件配置如@NotBlank的 javax.validation.constraints.NotBlank.message        = \u4e0d\u80fd\u4e3a\u7a7a
    String message() default "{com.hspedu.common.valid.EnumValidate.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    // 增加values属性
    int[] values() default {};

    /**
     * @return the regular expression to match
     */
    String regexp() default "";

    // 如果没有给默认值 default "" 那么在使用注解EnumValidate时就必须指定regexp,否则报错
    /**
     * 在Java自定义注解中，如果您为一个注解属性提供了默认值，
     * 比如`String regexp() default "";`，那么在使用该注解时，
     * 指定这个属性变成可选的。如果没有提供默认值（比如只声明`String regexp();`），
     * 那么在使用这个注解时就必须显式提供该属性的值，否则编译器会报错。
     */
    // String regexp();
}
