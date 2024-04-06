package com.hspedu.hspliving.commodity.exception;

import com.hspedu.common.exception.HsplivingCodeEnum;
import com.hspedu.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

/**
 * @author yangda
 * @create 2024-03-16-17:30
 * @description: 全局异常处理类
 * 全局异常处理类 在 springmvc时讲过
 * 异常处理的优先级
 * 局部异常 > 全局异常 > SimpleMappingExceptionResolver > tomcat 默认机制
 * <p>
 * 如果类上标注了@ControllerAdvice,就是一个全局异常处理类
 * advice:建议;意见;忠告;劝告
 * <p>
 * 注解@ControllerAdvice(basePackages = "")的basePackages属性
 * 可以指定对那个包下抛出的异常进行全局处理，
 * 如果没有指定，则默认处理所有的包下抛出的异常
 * <p>
 * 1. @ResponseBody: 表示该全局异常处理器，如果要返回数据，全都以json格式进行返回
 * 2. @Slf4j: 可以输出日志，便于观察
 * 3. @ControllerAdvice(basePackages = "com.hspedu.hspliving.commodity.controller")
 * 表示统一接管 com.hspedu.hspliving.commodity.controller 包下抛出的异常
 */
@Slf4j
@ResponseBody
@ControllerAdvice(basePackages = "com.hspedu.hspliving.commodity.controller")
public class HsplivingExceptionControllerAdvice {

    /**
     * 1. @ExceptionHandler({MethodArgumentNotValidException.class})
     * 表示如果 com.hspedu.hspliving.commodity.controller 包下抛出了数据检验异常
     * ,就由handleValidException()处理
     * 2. 数据校验异常就是 MethodArgumentNotValidException.class
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public R handleValidException(MethodArgumentNotValidException e) {
        //输出日志观察异常的信息
        // log.error("数据校验出现问题{} 异常类型是{}",e.getMessage(),e.getClass());
        // 异常类型是class org.springframework.web.bind.MethodArgumentNotValidException

        // 可以进入到该方法，就说明BindingResult已经有错误了, 即 result.hasErrors() 肯定为true
        BindingResult bindingResult = e.getBindingResult();
        HashMap<String, String> errorMap = new HashMap<>();
        //遍历bindingResult，将校验错误信息，收集/封装到errorMap
        bindingResult.getFieldErrors().forEach(fieldError -> {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        // return R.error(400,"方法参数异常").put("data",errorMap);
        return R.error(HsplivingCodeEnum.INVALID_EXCEPTION.getCode(),
                HsplivingCodeEnum.INVALID_EXCEPTION.getMsg()).put("data", errorMap);
    }

    // 说明:如果按照业务逻辑，需要去精确匹配处理异常/错误，可以在该类中再单独写对应的处理异常的方法...


    /**
     * 编写方法，处理没有精确匹配到的异常/错误
     * 返回一个统一的信息，方便前端处理
     * <p>
     * // 上面的精准异常匹配如果没有匹配到，就会走这里顶级异常处理
     * // 注意优先走上面的精准异常匹配 globalException ！
     * // “如果抛出的异常不匹配上面定义的具体异常类型，则会被此方法（处理Throwable.class）捕获。
     * // ”这是因为Throwable是所有错误和异常的超类，所以此方法可以捕获所有未被上面方法捕获的异常和错误。
     */
    @ExceptionHandler({Throwable.class})
    public R handleException(Throwable throwable) {
        // return R.error(40000,"系统未知错误");
        return R.error(HsplivingCodeEnum.UNKNOWN_EXCEPTION.getCode(),
                HsplivingCodeEnum.UNKNOWN_EXCEPTION.getMsg());
    }

    /**
     * 解释“如果抛出的异常不匹配上面定义的具体异常类型，则会被此方法（处理Throwable.class）捕获。
     * 这是因为Throwable是所有错误和异常的超类，所以此方法可以捕获所有未被上面方法捕获的异常和错误。”，为什么会这样呢？和方法的声明顺序有关还是其他别的什么原因？？
     *
     * GPT:
     * 在Java中，`Throwable`类是所有错误和异常的最顶层父类。这意味着，Java中的所有异常和错误类型都是`Throwable`的子类。在Spring框架的全局异常处理中，当你使用`@ExceptionHandler`注解来标注方法时，你告诉Spring当发生特定类型的异常时，应该调用这个方法。
     *
     * 这里的行为和方法的声明顺序无关，而是与异常的类型匹配有关：
     *
     * 1. **类型匹配**：当一个异常被抛出时，Spring会查找用`@ExceptionHandler`注解的方法，并检查这些方法所指定的异常类型。Spring将选择与抛出的异常类型最匹配的方法。"最匹配"意味着异常类型直接相同或者最接近的父类。
     *
     * 2. **多个处理器**：如果你有多个`@ExceptionHandler`方法，每个处理不同类型的异常，Spring会选择与抛出的异常类型最为匹配的那个处理器。例如，如果一个方法标注为处理`NumberFormatException`，另一个标注为处理`Exception`，那么当抛出`NumberFormatException`时，会调用第一个方法。
     *
     * 3. **使用Throwable作为异常类型**：如果你有一个`@ExceptionHandler(Throwable.class)`，这意味着它能够捕获所有类型的异常和错误，因为所有的异常和错误都是`Throwable`的子类。如果没有更具体的异常处理器匹配抛出的异常类型，这个处理器就会被使用。这不是因为方法的声明顺序，而是因为没有其他更具体的异常类型与抛出的异常相匹配。
     *
     * 因此，当一个异常抛出时，Spring会首先尝试找到最具体的异常匹配。如果没有找到具体的匹配，它会继续查找直到找到`Throwable`的处理器。这就是为什么`@ExceptionHandler(Throwable.class)`能够作为一个“后备”来捕获所有未被其他处理器捕获的异常和错误。这种机制确保了异常总能被适当地处理，即使没有为它们配置具体的处理器。
     */


}
