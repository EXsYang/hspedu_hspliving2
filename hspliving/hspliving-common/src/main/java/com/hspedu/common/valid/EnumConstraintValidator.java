package com.hspedu.common.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yangda
 * @create 2024-03-17-19:00
 * @description: 自定义校验器EnumConstraintValidator
 * ConstraintValidator<> 自定义校验器需要实现的接口
 * Constraint: 限制;约束;限定;严管
 * <p>
 * 1. EnumConstraintValidator 是真正的校验器，即校验的逻辑是写在这里的
 * 2. EnumConstraintValidator需要实现接口 ConstraintValidator<A extends Annotation, T>
 * 3. <EnumValidate,Integer> 表示该校验器是针对 @EnumValidate注解 传入的 Integer类型的数据进行校验
 */
public class EnumConstraintValidator implements ConstraintValidator<EnumValidate, Integer> {


    //定义一个Set集合，用于收集EnumValidate传入的values
    private Set<Integer> set = new HashSet<>();
    // private String regStr = "";

    /*
        constraintAnnotation 就是标注在对象属性上的@EnumValidate注解
        通过该注解可以获取到EnumValidate注解的各个属性传入的值
    */
    @Override
    public void initialize(EnumValidate constraintAnnotation) {
        // 测试，看是否能够得到注解传入的values
        // 通过注解获取values,在Java基础的反射讲过
        int[] values = constraintAnnotation.values();
        for (int value : values) {
            // System.out.println("EnumValidate注解指定的values= " + value);
            set.add(value);
        }

        // regStr = constraintAnnotation.regexp();

    }

    // 这里是判断最终校验结果的方法
    /**
     * @param value   就是将来在前端的表单中传入的数据，即要校验的字段 前端表单字段值 封装到 有EnumValidate注解标注 的对象属性的值
     * @param context
     * @return 如果返回true校验成功-通过，如果返回false就是校验失败！-没有通过
     */
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        // System.out.println("前端表单传入的value= " + value);

        return set.contains(value);

        // String strValue = String.valueOf(value);

        // compile:编译
        // Pattern pattern = Pattern.compile(regStr);
        // Matcher matcher = pattern.matcher(strValue);
            // return matcher.find();

        // return value.toString().matches(regStr);
    }
}
