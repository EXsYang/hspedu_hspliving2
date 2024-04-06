<template>

  <!-- brand.vue 是处理品牌的查询和删除的 -->

  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <el-button v-if="isAuth('commodity:brand:save')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
        <el-button v-if="isAuth('commodity:brand:delete')" type="danger" @click="deleteHandle()"
                   :disabled="dataListSelections.length <= 0">批量删除
        </el-button>
      </el-form-item>
    </el-form>
    <el-table
      :data="dataList"
      border
      v-loading="dataListLoading"
      @selection-change="selectionChangeHandle"
      style="width: 100%;">
      <el-table-column
        type="selection"
        header-align="center"
        align="center"
        width="50">
      </el-table-column>
      <el-table-column
        prop="id"
        header-align="center"
        align="center"
        label="id">
      </el-table-column>
      <el-table-column
        prop="name"
        header-align="center"
        align="center"
        label="品牌名">
      </el-table-column>
      <el-table-column
        prop="logo"
        header-align="center"
        align="center"
        label="logo">
        <!--使用插槽机制,给表格的列插入 template，显示logo图片
        style="width: 80px", 这里图片的样式只设置宽度，图片就会按照比例进行缩小展示
        不需要再指定图片的高度，否则图片有可能会变形
        -->
        <template slot-scope="scope">
          <img :src="scope.row.logo" style="width: 80px">
        </template>
      </el-table-column>
      <el-table-column
        prop="description"
        header-align="center"
        align="center"
        label="说明">
      </el-table-column>
      <el-table-column
        prop="isshow"
        header-align="center"
        align="center"
        label="显示">

        <!--引入自定义的内容，使用template和插槽机制
        1. 使用插槽机制，给表格的列加入一个 template , 这可以参考 ElementUI-Table 表格-自定义列模板的代码
        2. 在 template 中替换成 el-switch 控件，参考 ElementUI-Switch 开关-基本用法
        3. v-model="scope.row.isshow" 动态绑定当前行的 isshow 的值
        4. active-color / inactive-color 是激活和未激活的颜色
        5. active-value="1" 激活状态的值，和数据库表isShow字段对应
        6. inactive-value="0" 未激活状态的值，和数据库表isShow字段对应
        7. @change="changeIsShow(scope.row)" 当switch控件状态切换时，会触发changeIsShow(scope.row)
           ，而且我们把 当前行数据scope.row传递到该方法
        -->
        <template slot-scope="scope">
          <!--引入el-switch
          绑定v-model到一个Boolean类型的变量。可以使用active-color属性与inactive-color属性来设置开关的背景色。
          scope.row.isshow 通过插槽机制获取到当前行，然后再获取到isshow

          el-switch 组件使用了 v-model 指令绑定到 scope.row.isshow。这意味着当开关的状态改变时，
          scope.row.isshow 的值会自动更新。

          el-switch 组件有两个重要的属性：active-value 和 inactive-value。
          active-value 定义了当开关处于激活状态时对应的值，在你的代码中设置为 "1"。
          inactive-value 定义了当开关处于非激活状态时对应的值，在你的代码中设置为 "0"。
          当用户点击开关改变它的状态时，如果开关被激活，scope.row.isshow 将被设置为 "1"
          （active-value 的值）。如果开关被关闭，scope.row.isshow 将被设置为 "0"
          （inactive-value 的值）。



          注意:
          :active-value="1"：这里的冒号表示 active-value 的值应该被解释为 JavaScript 表达式，因此传递给 active-value 的是数字 1。
          :inactive-value="0"：同样，这里的冒号表示 inactive-value 的值应该被解释为 JavaScript 表达式，因此传递给 inactive-value 的是数字 0。
          如果不加冒号：

          active-value="1"：没有冒号，Vue 会把这里的 1 解释为字符串 "1" 而不是数字。
          inactive-value="0"：同样，没有冒号，0 会被解释为字符串 "0"。
          -->
          <el-switch
            v-model="scope.row.isshow"
            @change="changeIsShow(scope.row)"
            :active-value="1"
            :inactive-value="0"
            active-color="#13ce66"
            inactive-color="#ff4949">
          </el-switch>

        </template>

      </el-table-column>
      <el-table-column
        prop="firstLetter"
        header-align="center"
        align="center"
        label="检索首字母">
      </el-table-column>
      <el-table-column
        prop="sort"
        header-align="center"
        align="center"
        label="排序">
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <!--增加 关联分类 el-button-->
          <el-button type="text" size="small" @click="relateCategoryHandle(scope.row.id)">关联分类</el-button>

          <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">修改</el-button>
          <!--          <el-button type="text" size="small" @click="deleteHandle(scope.row.id)">删除</el-button>-->
          <el-button type="text" size="small" @click="deleteHandle(scope.row.id,scope.row.name)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      @size-change="sizeChangeHandle"
      @current-change="currentChangeHandle"
      :current-page="pageIndex"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="pageSize"
      :total="totalPage"
      layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>

    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>

    <!-- 品牌和分类关联的对话框 -->
    <el-dialog title="关联分类" :visible.sync="cateRelationDialogVisible" width="30%">
      <el-popover placement="right-end" v-model="popCatelogSelectVisible">
        <!-- <category-cascader :catelogPath.sync="catelogPath"></category-cascader>-->
        <!-- 这里我们加入分类的 Cascader 级联选择器, 前面我们使用过 -->
        <el-cascader
          v-model="cascadedCategoryId"
          :options="categories"
          :props="props"></el-cascader>

        <div style="text-align: right; margin: 0">
          <el-button size="mini" type="text" @click="popCatelogSelectVisible = false">取消</el-button>
          <el-button type="primary" size="mini" @click="addBrandCategoryRelation"> 确定</el-button>
        </div>
        <!-- 点击新增关联 显示 el-popover 控件中的内容 当你点击“新增关联”按钮后，popCatelogSelectVisible 会被设置为 true。
        实际上，<el-button slot="reference">新增关联</el-button> 被用作 el-popover 的触发元素（通过使用 slot="reference"），通常它只负责显示弹窗，而不会在再次点击时自动隐藏弹窗。
        在 Element UI 中，v-model 绑定的 popCatelogSelectVisible 控制着弹出框的显示状态，但这个显示状态的改变通常是由弹出框内部的行为控制的（如点击“取消”按钮或完成一个操作）。默认情况下，点击弹出框外的触发元素（即“新增关联”按钮）通常只会显示弹出框，而不会切换其显示状态。这是因为 el-popover 默认行为是当点击外部时关闭弹出框，但不是点击触发元素。
        如果你想要“新增关联”按钮在每次点击时切换 popCatelogSelectVisible 的值（即点击一次显示，再点击一次隐藏），你需要在该按钮上添加一个点击事件，如：el-button slot="reference" @click="popCatelogSelectVisible = !popCatelogSelectVisible 这样设置后，每次点击“新增关联”按钮都会切换 popCatelogSelectVisible 的值，从而使 el-popover 显示或隐藏。但请注意，这种方法可能会与 el-popover 的默认外部点击行为冲突，因此你可能需要根据实际使用场景调整逻辑。-->
        <el-button slot="reference">新增关联</el-button>
      </el-popover>

      <el-table :data="cateRelationTableData" style="width: 100%">
        <el-table-column prop="id" label="#"></el-table-column>
        <el-table-column prop="brandName" label="品牌名"></el-table-column>
        <el-table-column prop="categoryName" label="分类名"></el-table-column>
        <el-table-column fixed="right" header-align="center" align="center" label="操作">
          <template slot-scope="scope">
            <el-button
              type="text" size="small" @click="deleteCateRelationHandle(scope.row.id,scope.row.brandId)">移除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cateRelationDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="cateRelationDialogVisible = false"> 确 定</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
import AddOrUpdate from './brand-add-or-update'

export default {
  data() {
    return {
      //数据池加入相关数据变量
      cateRelationDialogVisible: false, // 控制dialog对话框是否显示,默认不显示
      cateRelationTableData: [], // 绑定品牌和分类的表格数据
      cascadedCategoryId: [], // 和 Cascader 级联选择器 显示分类层级数据绑定 形式如 [1,21,301]
      popCatelogSelectVisible: false, // 控制el-popover 控件是否显示  popover:空心松饼，膨松饼(将蛋、奶、面调和后烘焙而成)
      props: { //显示返回的家居分类的哪些字段/信息 ,和 Cascader 级联选择器关联的
        value: "id",
        label: "name",
        children: "childrenCategories"
      },
      categories: [], //所有的家居分类
      brandId: 0, // 记录你选择的品牌id

      dataForm: {
        key: ''
      },
      dataList: [], // 表格需要展示的数据
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      addOrUpdateVisible: false
    }
  },
  components: {
    AddOrUpdate
  },
  activated() {
    this.getDataList()
  },
  created() {
    this.getCategories()
  },
  methods: {
    // 增加方法，响应删除 品牌和分类关联的处理
    deleteCateRelationHandle(id) {

      // 获取删除的分类 id ,这里之所以将id放到数组中，是因为后端是通过数组进行接收数据的
      // 当你使用 @RequestBody 注解时，Spring会期待接收的是一个完整的请求体，这通常意味着你需要在前端发送一个JSON格式的请求体。在这个例子中，前端应该发送一个如 [1, 2, 3] 的JSON数组，每个元素都是一个要删除的资源的ID。
      // 如果你尝试直接发送单个ID（不是数组），如直接发送数字 1，后端会尝试将这个单个值映射到一个 Long[] 类型，这会导致类型不匹配的错误，因为后端期待的是一个数组而不是单个数字。
      // 因此，在前端调用这个API时，即使你只需要删除一个条目，也应该将这个ID放入一个数组中，如 [1]，然后发送这个数组作为请求体。这样做符合后端期待的数据格式，可以确保后端正确处理请求。
      // var ids = id
      var ids = [id]

      this.$confirm(`是否删除该记录?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        //如果点击`确定` 进行删除操作
        this.$http({
          url: this.$http.adornUrl(`/commodity/categorybrandrelation/delete`),
          // method: 'get',
          //携带的参数 注意这里的 `params:` 是url参数
          // params: this.$http.adornParams({
          //   id: ids
          // }),
          // url: this.$http.adornUrl('/commodity/category/delete'),
          method: 'post',

          //下面这里`data:`是在请求体中携带数据的！
          // 发出请求时携带的数据
          data: this.$http.adornData(ids, false)
        }).then(({data}) => { //这里的{data} 是对象解构，解构了上面url请求后端返回来的数据，

          this.$message({
            message: '品牌分类关联删除成功',
            type: 'success'
          })
          // 刷新 品牌-分类 表格数据
          this.getCategoryBrandRelation()

        }).catch(() => {
          //如果点击`取消`，不删除
        })

      })
    },
    //增加方法，请求后端对应的API，根据brandId返回，品牌和分类对应的记录
    getCategoryBrandRelation() {
      this.$http({
        // url: this.$http.adornUrl(`/commodity/categorybrandrelation/brand/list?brandId=${this.brandId}`),

        // 下面这段代码会将 brandId 添加到请求的查询字符串中，这与直接在URL中指定查询参数是等效的。在实际发送请求时，这些参数会附加到URL之后，形式也是 /brand/list?brandId=1。
        // 下面这个请求的请求头为:
        // GET /api/commodity/categorybrandrelation/brand/list?t=1711129443854&brandId=1 HTTP/1.1
        url: this.$http.adornUrl(`/commodity/categorybrandrelation/brand/list`),
        method: 'get',
        //携带的参数
        params: this.$http.adornParams({
          brandId: this.brandId
        })
      }).then(({data}) => {
        // 测试得到的数据
        console.log("data=> ", data)

        //在Vue中，数组类型的数据属性非常适合用来存储和展示表格类型的数据。
        // 当您从后端API接收到一个List类型的数据时，这些数据通常以JSON数组
        // 的形式出现,使用 @RestController 注解意味着你的 Spring Boot
        // 控制器会自动将返回的对象序列化为JSON格式。
        // 在JavaScript（以及在Vue组件的上下文中）中，JSON数组可以
        // 直接赋值给Vue组件的数组类型的数据属性。
        // 请求成功后，将返回的数据赋值给cateRelationTableData，用于前端表格展示
        this.cateRelationTableData = data.data
      })

    },
    //增加关联关系【品牌-分类】 保存到数据表中
    addBrandCategoryRelation() {

      //点击el-popover的确定后，隐藏el-popover对话框
      this.popCatelogSelectVisible = false

      // 发送请求，添加 品牌-分类 的关联关系
      this.$http({
        url: this.$http.adornUrl(`/commodity/categorybrandrelation/save`),
        method: 'post',
        //发出请求时携带的数据
        data: this.$http.adornData({
          brandId: this.brandId,
          categoryId: this.cascadedCategoryId[this.cascadedCategoryId.length - 1]
        }, false)
      }).then(({data}) => { //这里的{data} 是对象解构，解构了上面url请求后端返回来的数据，
        // 添加成功后，可以刷新显示 品牌和分类的列表
        this.getCategoryBrandRelation()
      })

    },
    // 获取树形菜单分类列表，带层级
    getCategories() {
      //这里的`$http`实际上就是axios,只是在main.js中起了一个别名
      this.$http({
        url: this.$http.adornUrl('/commodity/category/list/tree'),
        method: 'get'
      }).then(({data}) => { //这里的{data} 是对象解构，解构了上面url请求后端返回来的数据，
        //输出
        // console.log("getCategories()方法中 .then(({data}) => {..) 返回的data= ", data);
        //返回需要展示的数据 data.data.data
        //如果使用{data}, 得到的要展示的数据使用 data.data 即可，因为解构了一层

        //将后端返回来的数据，绑定到数据池的data属性 注意绑定数据后，页面就会相应地变化！
        // this.data = data.data;
        this.categories = data.data;
      })
    },
    //响应用户操作 关联分类
    relateCategoryHandle(id) {
      // console.log("id -> ", id)
      this.cateRelationDialogVisible = true
      // 将当前选择的品牌id 赋给 数据池 this.brandId
      this.brandId = id;

      // 调用方法 刷新/获取 返回当前brandId 对应的 品牌分类关联关系的数据
      this.getCategoryBrandRelation()

    },

    //相应用户切换switch控件的函数
    changeIsShow(row) {
      console.log("row.id=> ", row.id, "  row.isshow=> ", row.isshow)
      // console.log("row.id=> ",row.id, "  typeof row.isshow=> ", typeof row.isshow) // row.id=>  1   typeof row.isshow=>  string

      // var id = row.id;
      // var isshow = row.isshow;

      // 因为我们是使用 id 修改 isShow , 所以这里我们发送时，只需要携带 id 和 isshow即可
      // 使用对象解构
      let {id, isshow} = row;

      // 发送请求，修改对应品牌记录的isShow字段值
      this.$http({
        // 解读这里的url是获取后端分级菜单列表的url地址，即获取带层级的商品分类表`commodity_category`所有数据
        // url: `http://localhost:9090/commodity/brand/update`,
        url: this.$http.adornUrl(`/commodity/brand/update/isshow`),
        method: 'post',
        //发出请求时携带的数据，参考 src/views/modules/sys/role.vue:158
        // {id, isshow} , 是json数据，对象的简写形式，完整写法是{id: id, isshow: isshow},
        // 在下面的{id, isshow} 是一个 JavaScript对象,发送到后端时会被转换为json格式:{"id": id, "isshow": isshow}
        // 是的，你的理解是正确的。在这里的 `{id, isshow}` 是一个 JavaScript 对象。当你发送这个对象到后端时，通常会使用 JSON 格式进行数据交换。在发送过程中（通常是通过 AJAX 请求），这个 JavaScript 对象会被转换为 JSON 字符串，转换后的格式将会是 `{"id": id, "isshow": isshow}`。
        // 这个转换过程通常是由 JavaScript 的 `JSON.stringify()` 函数自动处理的，或者由前端框架/库（如 Axios、Fetch API 等）内部处理。因此，尽管在前端代码中你是用对象字面量的简写形式 `{id, isshow}`，一旦它被发送到服务器，它就会以完整的 JSON 格式出现，即属性名（键）会被包含在双引号中。

        // 当属性名/key 和 值/值名一致时，可以简写成 {id, isshow}
        data: this.$http.adornData({id, isshow}, false)
      }).then(({data}) => { //这里的{data} 是对象解构，解构了上面url请求后端返回来的数据，
        // responseData对象的data属性
        //输出
        console.log("changeIsShow(row)方法 .then(({data}) 返回的data= ", data);

        this.$message({
          message: '修改品牌显示状态成功OK',
          type: 'success'
        })

        //刷新分类列表 绑定数据，这里不需要，因为没有添加新的行或减少旧的行
        // this.getDataList();
      })


    },
    // 获取数据列表-查询
    getDataList() {
      this.dataListLoading = true
      this.$http({
        // url: 'http://localhost:9090/commodity/brand/list',
        url: this.$http.adornUrl('/commodity/brand/list'),
        method: 'get',
        params: this.$http.adornParams({
          'page': this.pageIndex,
          'limit': this.pageSize,
          'key': this.dataForm.key,
          // 'name2': this.dataForm.key
        })
      }).then(({data}) => {
        if (data && data.code === 0) {
          //this.dataList 用来绑定表格需要展示的数据，定义在数据池
          this.dataList = data.page.list
          this.totalPage = data.page.totalCount

          console.log("this.dataList=> ", this.dataList)
        } else {
          this.dataList = []
          this.totalPage = 0
        }
        this.dataListLoading = false
      })
    },
    // 每页数
    sizeChangeHandle(val) {
      this.pageSize = val
      this.pageIndex = 1
      this.getDataList()
    },
    // 当前页
    currentChangeHandle(val) {
      this.pageIndex = val
      this.getDataList()
    },
    // 多选
    selectionChangeHandle(val) {
      this.dataListSelections = val
    },
    // 新增 / 修改
    addOrUpdateHandle(id) {
      this.addOrUpdateVisible = true
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(id)
      })
    },

    // 删除 [批量删除、指定删除都是走这个函数]
    // 在 Vue.js 中，方法的对应关系是基于方法名而不是方法签名（即方法的参数）。
    // 因此 deleteHandle() 和 deleteHandle(scope.row.id,scope.row.name)
    // 都和方法池中的  deleteHandle (id,name) {} 方法对应

    // deleteHandle (id) {
    deleteHandle(id, name) {
      /*
      收集要删除的id
      `id ? [id] : this.dataListSelections.map(item => {
        return item.id
      })` 是一个整体,是一个使用三元运算符的表达式

      如果 id 存在（即 id 是非空、非零等，被判断为“真”），那么 ids 将被赋值为一个只包含此 id 的数组（即 [id]）。
      如果 id 不存在（即 id 是 null、undefined、0、空字符串等，被判断为“假”），那么 ids 将被赋值为另一个数组，该数组由 this.dataListSelections 中所有元素的 id 组成，此过程通过调用 .map(item => item.id) 来实现。
      因此，整体上这是一个基于 id 的存在与否来对 ids 进行条件赋值的逻辑表达式。
       */
      // .map() 是一个数组方法，用于遍历数组并对每个元素执行一个转换函数（在这里，是取出每个元素的 id 属性），然后返回结果的新数组。
      // this.dataListSelections 表示列表中被用户选择（或选中）的条目集合。this.dataListSelections 是一个数组
      var ids = id ? [id] : this.dataListSelections.map(item => {
        return item.id
      })

      // 模仿ids的获取方式，来获取要删除的brand的name,收集到names
      var names = id ? [name] : this.dataListSelections.map(item => {
        return item.name
      })

      // console.log("this.dataListSelections=> ", this.dataListSelections)
      // console.log("this.dataList=> ", this.dataList)

      // if (id){
      //   var names = this.dataList.map(item => {
      //     if (id == item.id){
      //       return item.name
      //     }
      //   })
      // }else {
      //   var names = this.dataListSelections.map(item => {
      //     return item.name
      //   })
      // }

      // this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
      this.$confirm(`确定对[name=${names.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          // url: 'http://localhost:9090/commodity/brand/delete',
          url: this.$http.adornUrl('/commodity/brand/delete'),
          method: 'post',
          data: this.$http.adornData(ids, false)
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.getDataList()
              }
            })
          } else {
            this.$message.error(data.msg)
          }
        })
      })
    }
  }
}
</script>
