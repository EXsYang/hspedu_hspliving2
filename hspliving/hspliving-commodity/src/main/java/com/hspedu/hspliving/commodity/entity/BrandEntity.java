package com.hspedu.hspliving.commodity.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.hspedu.common.valid.EnumValidate;
import com.hspedu.common.valid.SaveGroup;
import com.hspedu.common.valid.UpdateGroup;
import com.hspedu.common.valid.UpdateIsShowGroup;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;

/**
 * 家居品牌
 *
 * @author yd
 * @email yd@gmail.com
 * @date 2024-03-05 21:20:36
 */
@Data
@TableName("commodity_brand")
public class BrandEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 类说明: BrandEntity
     *
     * 使用Spring Validation进行数据校验时，可以通过指定校验组（Validation Groups）
     * 来对不同操作（如创建、更新等）下的数据进行不同的校验规则。
     *
     * 校验组允许我们定义不同的接口（如SaveGroup.class, UpdateGroup.class），
     * 并将这些接口作为校验注解的参数，来指明某个字段属于哪个校验组。
     * 这样，在不同的操作场景中，我们可以选择性地应用不同的校验规则。
     *
     * 例如，在更新操作中，我们可能需要校验id的非空性，但在创建操作中，id应该为空。
     *
     * 校验规则示例:
     * - 当在更新（Update）操作中，只有被标记为UpdateGroup的字段才会进行校验。
     * - 当在创建（Save）操作中，只有被标记为SaveGroup的字段才会进行校验。
     *
     * 使用@Validated或@Valid注解触发校验时，如果指定了校验组，
     * 则只有标记为该校验组的字段校验规则才会被执行。
     *
     * 例如，以下字段在更新时会被校验是否为空，因为它被指定为UpdateGroup的一部分。
     * 注意：未指定校验组的字段在没有明确指定校验组的校验中都不会被校验。
     */
    /**
     * id
     * 解读：
     * 1. @NotNull(message = "修改要求指定id",groups = {UpdateGroup.class})
     * 表示@NotNull 注解在 UpdateGroup 校验组生效,也可以理解为UpdateGroup校验组有一个校验规则就是@NotNull
     * ,在有注解 @Validated(UpdateGroup.class) 标识的方法上,下面的@NotNull(message = "修改要求指定id",groups = {UpdateGroup.class})注解非空校验生效
     * 2. @Null(message = "添加不能指定id",groups = {SaveGroup.class})
     * 表示@Null注解 在SaveGroup校验组生效
     */
    @TableId
    @NotNull(message = "修改要求指定id", groups = {UpdateGroup.class,UpdateIsShowGroup.class})
    @Null(message = "添加不能指定id", groups = {SaveGroup.class})
    private Long id;

    /**
     * 以下字段在没有指定特定校验组时，默认不属于任何校验组，
     * 它将不会在指定校验组的校验操作中被校验。
     * 如果需要在特定操作（如更新）中被校验，需要添加相应的校验组。
     */
    // @NotBlank(message = "品牌名不能为空")
    // private String name;

    /**
     * 品牌名
     * 老韩说明
     * 1. @NotBlank 表示name必须包括一个非空字符
     * 2. message = "品牌名不能为空" 是老师指定的一个校验消息
     * 3. 如果没有指定 message = "品牌名不能为空" ,就会返回默认的校验消息  key = javax.validation.constraints.NotBlank.message
     * 4. 这个默认的消息是在  ValidationMessages_zh_CN.properties 文件配置 javax.validation.constraints.NotBlank.message        = \u4e0d\u80fd\u4e3a\u7a7a
     * 5. @NotBlank 可以用于  CharSequence，即支持字符串
     * 6. groups = {SaveGroup.class, UpdateGroup.class}
     * 就是@NotBlank 在 SaveGroup 和 UpdateGroup校验组都生效 groups可以指定多个校验组都生效
     * <p>
     * 注解 @NotBlank: 带注释的元素不能为｛@code null｝，并且必须至少包含一个
     * 非空白字符(空格等)。接受｛@code CharSequence｝，即支持字符串
     */
    @NotBlank(message = "品牌名不能为空", groups = {SaveGroup.class, UpdateGroup.class})
    private String name;
    /**
     * logo 因为这个logo对应的是图片的url
     * 1. @NotBlank 和 @URL 组合去校验  logo
     * 2. 在修改时可以不带logo, 生成update sql时不会生成logo,logo就会保留原值
     * 3. 保存时必须携带logo值,修改时如果没有携带就保留原值，如果携带了必须满足@URL
     * 因为如果携带了logo值,@URL生效进行URL校验
     */
    @NotBlank(message = "logo不能为空", groups = {SaveGroup.class})
    @URL(message = "logo不是一个合法的URL", groups = {SaveGroup.class, UpdateGroup.class})
    private String logo;
    /**
     * 说明
     */
    private String description;
    /**
     * 显示 isshow 注意 这里的 show的首字母是小写
     * 1. 这里使用的注解是 @NotNull , 它可以接收任意类型
     * 2. 如果这里使用的注解是 @NotBlank, 会报错
     * ，@NotBlank可以在String类型上标注,@NotBlank不支持 Integer类型
     * ,但是这里的 isshow 是 Integer类型, 因此不能使用 @NotBlank
     * 3. 同学们在开发时，需要知道注解可以用在哪些类型上，可以查看注解源码
     * 4. 老师说明，假如 isshow 规定是 0/1 , 这时我们后面通过自定义校验器来解决..
     * 5. 假如 isshow 规定是 0/1，如果是String 类型，可以直接使用@Pattern来 进一步校验
     */
    @NotNull(message = "显示状态不能为空", groups = {SaveGroup.class, UpdateGroup.class, UpdateIsShowGroup.class})
    // @Pattern(regexp = "^[0-1]$",message = "显示状态必须是0或者1",groups = {SaveGroup.class,UpdateGroup.class})
    @EnumValidate(values = {0,1},message = "显示状态的值需要是0或者1~",groups = {SaveGroup.class,UpdateGroup.class,UpdateIsShowGroup.class})
    // @EnumValidate(values = {0, 1}, groups = {SaveGroup.class, UpdateGroup.class})
    // @EnumValidate(regexp = "^[0-1]$",message = "显示状态的值需要是0或者1~~",groups = {SaveGroup.class,UpdateGroup.class})
    private Integer isshow;

    /**
     * 这里有个问题， 如果前端http://localhost:9090/commodity/brand/save发送的是
     * {
     *     "firstLetter": ""
     * }
     * 返回的结果有时是  "firstLetter": "检索首字母必须是a-z或者A-Z" 有时是  "firstLetter": "检索首字母不能为空"
     * GPT:
     * 这种情况出现的原因与Java Bean Validation（JSR 380）的工作方式有关。在您的例子中，`firstLetter`字段同时使用了`@NotBlank`和`@Pattern`两个注解，并且都指定了校验组`SaveGroup.class`。
     *
     * 当传入的`firstLetter`为一个空字符串时（例如`"firstLetter": ""`），Spring Validation会对`firstLetter`字段应用所有相关的校验注解。然而，`@NotBlank`和`@Pattern`可能会导致不同的验证结果：
     *
     * 1. **@NotBlank** 校验确保字段不是`null`且至少包含一个非空白字符。当`firstLetter`为""（空字符串）时，这个校验会失败，因为空字符串不包含任何非空白字符。
     *
     * 2. **@Pattern** 校验确保字段匹配给定的正则表达式（在这个例子中是`"^[a-zA-Z]$"`，意味着必须是一个a到z或A到Z的单个字母）。当`firstLetter`为""时，这个校验也会失败，但原因不同：空字符串不匹配正则表达式。
     *
     * 因为两个校验注解都指定了相同的校验组`SaveGroup.class`，它们都会在执行保存操作（即调用包含`@Validated(SaveGroup.class)`的方法时）时被触发。现象出现的“有时”差异，可能是由于校验框架内部处理多个违反情况的顺序问题。校验框架可能不保证违反的顺序，因此有时你可能先看到由`@NotBlank`引起的错误消息，有时可能先看到由`@Pattern`引起的错误消息。
     *
     * 解决方法是理解，这两个错误实际上都是有效的：如果`firstLetter`为空字符串，那么它既违反了`@NotBlank`（因为它是空的），也违反了`@Pattern`（因为空字符串不匹配指定的模式）。因此，从用户的角度来看，应该同时满足这两个条件：`firstLetter`不应该为空，且应该符合特定的模式。从实践的角度来看，返回哪个错误首先可能取决于校验实现的内部逻辑，但实际上两种情况都应该被视为用户需要解决的问题。
     *
     * 对于您遇到的校验消息不一致的问题，以下是一些解决建议：
     *
     * 1. **明确错误消息**：
     *    - 如果`firstLetter`字段同时不满足`@NotBlank`和`@Pattern`的条件，您可以调整前端的校验逻辑或错误消息处理逻辑，使其能够更清晰地通知用户所有不满足的条件。比如，在用户提交表单时，前端可以先检查是否为空，然后检查格式是否正确。
     *
     * 2. **前端校验**：
     *    - 强化前端校验：在前端实施相同的校验逻辑。确保在数据发送到后端之前，用户输入已经符合所有要求。这可以减少后端校验失败的情况，提高用户体验。
     *
     * 3. **后端校验顺序**：
     *    - 虽然后端校验顺序可能不固定，但您可以在设计API响应时合并相关的校验错误。例如，如果`firstLetter`字段违反了多个校验规则，您可以合并这些错误信息，告诉用户“检索首字母不能为空且必须是a-z或者A-Z之间的一个字母”。
     *
     * 4. **自定义校验**：
     *    - 创建自定义校验注解，将`@NotBlank`和`@Pattern`的校验逻辑组合在一起，并提供一个综合的错误消息。这样，无论是因为空字符串还是格式不匹配，用户都会收到一个明确的指示，指出他们需要提供一个非空的、格式正确的首字母。
     *
     * 5. **错误消息聚合**：
     *    - 在后端，如果使用了全局异常处理（如`@ControllerAdvice`），可以设计逻辑来聚合和格式化来自同一字段的所有错误消息。例如，如果`firstLetter`字段有多个校验错误，您可以将它们聚合为一个消息数组或一个合并的消息字符串。
     *
     * 6. **文档和说明**：
     *    - 在API文档和用户界面上清楚地说明每个字段的要求。确保用户了解`firstLetter`需要符合哪些规则，这样他们在输入时可以避免错误。
     *
     * 通过实施以上建议中的一项或多项，您可以改善用户体验，减少因校验失败导致的混淆，并确保数据的准确性和一致性。
     */
    /**
     * 检索首字母
     * 因为 firstLetter 是 String类型，所以可以直接使用 @Pattern
     * 如果前端发送的是 "firstLetter": ""
     */
    @NotBlank(message = "检索首字母不能为空", groups = {SaveGroup.class})
    @Pattern(regexp = "^[a-zA-Z]$", message = "检索首字母必须是a-z或者A-Z", groups = {SaveGroup.class, UpdateGroup.class})
    private String firstLetter;


    /**
     * 排序
     */
    @NotNull(message = "排序值不能为空", groups = {SaveGroup.class})
    @Min(value = 0, message = "排序值要求大于等于0", groups = {SaveGroup.class, UpdateGroup.class})
    private Integer sort;

}
