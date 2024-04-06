package com.hspedu.hspliving.commodity.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author yangda
 * @create 2024-03-19-17:14
 * @description:
 */
@Configuration
@EnableTransactionManagement //开启事务
@MapperScan("com.hspedu.hspliving.commodity.dao")
public class MyBatisConfig {

    //引入分页插件
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        PaginationInterceptor paginationInterceptor =
                new PaginationInterceptor();

        //做基本设置
        // 这个设置是指当当前页码超出总页数时，是否自动跳转回第一页。设置为 true 表示允许这样做。

        //当你指定的页数为 `page=2` 但查询回来的数据只有一条时，会出现以下情况：
        //
        // 1. **分页信息处理**：MyBatis Plus 的分页查询会根据提供的页码（`page`）和每页显示数量（`limit`）来查询数据。如果指定的页码超出了实际的数据页数（例如，请求第2页，但总数据量只足够填充到第1页），MyBatis Plus 会根据具体的分页插件配置处理这种情况。
        //
        // 2. **返回结果**：在你的例子中，假设总数据只有3条，每页显示2条，那么第2页实际上只有1条数据。分页查询会正常返回这1条数据作为第2页的内容。如果请求的页码没有数据（例如，总数据只有1条，请求第2页），则返回的列表将为空。
        //
        // 3. **前端展示**：前端接收到的分页数据包括当前页的数据列表、总数据量、总页数等信息。前端需要根据这些信息渲染分页组件。如果当前页没有数据，前端可以根据具体需求展示相关信息（如“当前页没有数据”等提示）。
        //
        // 4. **分页组件逻辑**：一般的分页组件会根据总数据量和每页显示的数量动态计算并展示可供选择的页码。如果实际数据量变化导致原选中的页码超出新的总页数，分页组件应相应调整，避免用户选择不存在的页码。
        //
        // **关于 `Query<T>` 类和 `getDataList()` 方法**：
        //
        // - `Query<T>` 类是一个工具类，封装了分页查询的逻辑，包括从请求参数中提取分页信息（页码和每页显示数量）、构造分页对象等。
        // - `getDataList()` 方法是前端发起分页查询请求的方法。它通过 HTTP GET 请求传递分页参数（页码、每页显示数量等）给后端。后端根据这些参数执行分页查询，并返回分页结果。
        //
        // **总结**：在分页查询中，无论指定的页码是否超出实际页数，MyBatis Plus 和前端逻辑都会合理处理，确保用户界面上的分页显示与实际数据相匹配，从而提供良好的用户体验。

        // 是的，`paginationInterceptor.setOverflow(true);` 这个设置和你提到的问题确实有关。
        //
        // 当设置 `paginationInterceptor.setOverflow(true);` 时，它告诉 MyBatis Plus 的分页插件在当前页码超出总页数时自动跳转回第一页。这就是“溢出”行为的处理：如果请求的页码超过了实际存在的最大页码，查询将不会返回空结果，而是返回第一页的内容。
        //
        // 这与你提出的问题直接相关：
        //
        // - 如果没有设置 `.setOverflow(true)`，在请求的页码超过实际最大页数时，你将得到一个空的分页结果，即页面上不会显示任何数据。
        // - 如果设置了 `.setOverflow(true)`，并且请求的页码超出了实际的总页数，那么系统会将请求“重定向”到第一页，并返回第一页的数据。
        //
        // 这个设置对于提升用户体验特别有帮助，因为它可以防止用户看到一个空的页面，特别是在他们通过手动输入页码或因为数据变更导致页码变化时。然而，这种行为是否被启用应根据实际的应用需求和用户体验设计来决定。
        paginationInterceptor.setOverflow(true);
        /*
        上面这个设置会改变 响应体中currPage的值，如果超出 就返回第一页 会将"currPage"设置为1
        比如请求到的数据只有一条记录，但是要显示的页码却为 page=2 ,此时按理说currPage=2，但是却被修改为"currPage":1

        你的理解几乎是正确的，但有一点需要澄清。设置 `paginationInterceptor.setOverflow(true);` 主要影响的是请求页码超出实际页数时的行为。具体到 `currPage` 或类似的分页响应参数，这取决于分页插件的具体实现和如何构建分页响应。

        1. 如果设置了 `.setOverflow(true)`，并且请求的页码超过了实际的总页数，那么系统行为是这样的：
            - **查询行为**：分页查询会返回第一页的数据而不是一个空页。
            - **分页响应**：通常，响应体中的当前页 (`currPage` 或类似命名) 应反映实际返回的页码。在这种情况下，如果请求的页码超过了总页数，并且由于 `overflow` 设置为 `true`，返回了第一页的数据，`currPage` 应该会被设置为 `1`。

        2. 在没有设置 `.setOverflow(true)` 的情况下：
            - **查询行为**：如果请求的页码超过总页数，分页查询通常会返回一个空的结果集。
            - **分页响应**：在这种情况下，响应体中的 `currPage` 通常会保持为请求的页码，即使那一页实际上没有数据。

        因此，你的描述是正确的，但需要注意的是，具体的 `currPage` 值和是否修改它取决于你的分页响应构建逻辑。如果你使用的是 MyBatis Plus 的分页插件并且启用了 `overflow`，且实际业务逻辑在请求页码超出总页数时返回第一页的数据，那么在这种情况下 `currPage` 确实应该反映为 `1`。这样做是为了保持响应体中的数据和状态与实际返回的分页数据一致。


         响应体: {"msg":"success","code":0,"page":{"totalCount":1,"pageSize":2,"totalPage":1,"currPage":1,"list":[{"id":3,"name":"功能","sort":0,"description":"功能说明","icon":"","categoryId":301}]}}
         */


        //单页限制 100 条，小于 0 如 -1 不受限制
        paginationInterceptor.setLimit(100);

        return paginationInterceptor;
    }
}
