<template>

  <!-- 老师解读
1. el-row 是 elementui 的布局控件 参考 elementui 官网 - Layout 布局 -分栏间隔
2. 我们将其分成两栏， 一栏放置分类树形菜单 一栏放置家居分类属性组的表单-->
  <el-row :gutter="20">

    <!--分类树形菜单的列-->
    <el-col :span="5">
      <!--1. 分类树形占 5 个单元宽度, 保留 ref, :data, :props, node-key 基本显示的属性
      2. node-click 实际是 el-tree 中规定好的实际，节点被点击时的回调	共三个参数，依次为：传递给 data 属性的数组中该节点所对应的对象、节点对应的 Node、节点组件本身。
      为什么会默认向 方法池自定义的nodeclick函数 传递三个参数？
      对于自定义事件（如 node-click），参数是由该事件的设计者定义的，并且会自动传递给处理函数。
      参考 elmentui 官网 elementui-Tree 树形控件-Events -->
      <el-tree ref="categoryTree" :data="data" :props="defaultProps"
               node-key="id" @node-click="nodeclick">
        <span class="custom-tree-node" slot-scope="{ node, data }">
          <span>{{ node.label }}</span>
        </span>
      </el-tree>
    </el-col>


    <!--家居分类属性组的表单的列-->
    <el-col :span="19">
      <!-- 说明 原家居分类属性组的表单放在这里 带 div. -->
      <div class="mod-config">
        <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
          <el-form-item>
            <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-button @click="getDataList()">查询</el-button>
            <el-button v-if="isAuth('commodity:attrgroup:save')" type="primary" @click="addOrUpdateHandle()">新增
            </el-button>
            <el-button v-if="isAuth('commodity:attrgroup:delete')" type="danger" @click="deleteHandle()"
                       :disabled="dataListSelections.length <= 0">批量删除
            </el-button>
          </el-form-item>
        </el-form>
        <!-- dataList 绑定的就是从后端返回的属性分组信息 -->
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
            label="组名">
          </el-table-column>
          <el-table-column
            prop="sort"
            header-align="center"
            align="center"
            label="排序">
          </el-table-column>
          <el-table-column
            prop="description"
            header-align="center"
            align="center"
            label="说明">
          </el-table-column>
          <el-table-column
            prop="icon"
            header-align="center"
            align="center"
            label="组图标">
          </el-table-column>
          <el-table-column
            prop="categoryId"
            header-align="center"
            align="center"
            label="所属分类 id">
          </el-table-column>
          <el-table-column
            fixed="right"
            header-align="center"
            align="center"
            width="150"
            label="操作">
            <template slot-scope="scope">

              <el-button type="text" size="small" @click="relationAttrHandle(scope.row.id)">关联</el-button>
              <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">修改</el-button>
              <el-button type="text" size="small" @click="deleteHandle(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          @size-change="sizeChangeHandle"
          @current-change="currentChangeHandle"
          :current-page="pageIndex"
          :page-sizes="[2, 10, 20, 50, 100]"
          :page-size="pageSize"
          :total="totalPage"
          layout="total, sizes, prev, pager, next, jumper">
        </el-pagination>


        <!-- Vue.js 组件声明、引用和命名约定总结 -->

        <!-- Kebab-case (短横线分隔命名): -->
        <!-- 主要用于 HTML 模板中的组件引用 -->
        <!-- 示例: <add-or-update></add-or-update> -->
        <!-- 推荐此格式以避免 HTML 和 XHTML 的大小写敏感问题 -->

        <!-- PascalCase (大驼峰式命名): -->
        <!-- 主要用于 JavaScript/TypeScript 中的导入和 JSX/TSX 中的组件引用 -->
        <!-- 示例: <AddOrUpdate></AddOrUpdate> -->
        <!-- 在 Vue 单文件组件 (SFC) 的模板中也支持此格式 -->

        <!-- 组件导入示例 (在 JavaScript 或 TypeScript 文件中): -->
        <!-- import AddOrUpdate from './AddOrUpdate.vue' -->

        <!-- 组件导出声明 (在 Vue 文件的 script 部分): -->
        <!-- export default { -->
        <!--   components: { -->
        <!--     'add-or-update': AddOrUpdate -->
        <!--   } -->
        <!-- } -->

        <!-- 弹窗, 新增 / 修改
        v-if/v-else 会根据 返回的值,来决定是否动态创建/销毁对应的子组件 <add-or-update>
        -->
        <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
        <!--        <AddOrUpdate v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></AddOrUpdate>-->

        <!-- 维护关联关系的组件 添加到 add-or-update 下面
        v-if/v-else 会根据 返回的值,来决定是否动态创建/销毁对应的子组件 <RelationUpdate>
        -->
        <RelationUpdate v-if="relationVisible" ref="relationUpdate" @refreshData="getDataList"></RelationUpdate>

      </div>
    </el-col>
  </el-row>


</template>

<script>
import AddOrUpdate from './attrgroup-add-or-update'
//引入组件
import RelationUpdate from "./attrgroup-attr-relation";

export default {
  data() {
    return {
      //显示某个属性分组下关联的所有商品属性(基本属性)情况
      relationVisible: false,
      dataForm: {
        key: '' // 查询条件 el-input 输入框双向绑定的值
      },
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      // pageSize: 2,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      addOrUpdateVisible: false,

      data: [], //树形菜单的数据
      defaultProps: { //property:属性  树形菜单默认的支持数据 需要和返回的数据格式和属性名对应
        children: 'childrenCategories', //这里是返回的子分类集合节点名,不能乱写,否则，只会显示一级分类
        label: 'name' //取出 name 显示, 可以参考 elementUI 树形控件规则
      },
      catId: 0 // catId 默认为0,表示在查询属性分组信息时，SQL的查询条件不带category_id字段

    }
  },
  components: {
    //导出RelationUpdate 先导入再导出才可以在本页面使用
    AddOrUpdate, RelationUpdate
  },
  activated() {
    /*
    Vue的`activated`方法是与`keep-alive`组件一起使用时特有的生命周期钩子。当一个被`keep-alive`缓存的组件被激活时，`activated`钩子就会被触发。这通常发生在以下两种情况下：

    1. 当组件第一次被缓存显示时，`activated`钩子在组件的`mounted`钩子之后被调用。
    2. 当缓存的组件再次被重新插入DOM时，`activated`钩子会被触发，但此时不会再次执行`mounted`钩子。

    因此，`activated`钩子在组件的生命周期中大致的位置是在`created`之后，但在两种情况下有所不同：

    - 对于初次渲染，`activated`是在`mounted`之后被调用。
    - 对于由`keep-alive`缓存的组件，当它们重新被激活时，`activated`是在重新插入DOM后被调用，但不再经过`mounted`过程。

    这意味着`activated`钩子适用于需要在组件每次激活时运行的任务，如拉取数据或重置状态。这与`created`和`mounted`钩子不同，这两个钩子只在组件实例被创建和初次插入DOM时分别被调用一次。
    */
    this.getDataList()
    // this.getCategories()
  },
  created() { //这个方法是 Vue生命周期方法
    this.getCategories()
  },
  methods: {
    //添加方法 relationAttrHandle
    //处理分组与属性的关联
    relationAttrHandle(groupId) {
      this.relationVisible = true;
      // console.log("groupId=> ",groupId)

      // 这里调用了子组件RelationUpdate 方法池中的init()方法
      //  this.$nextTick() 来确保我们调用子组件的 init() 方法是在 Vue 更新 DOM 之后。
      this.$nextTick(() => {
        // 现在DOM已经更新完成
        this.$refs.relationUpdate.init(groupId);
      });
    },
    // 响应用户点击树形控件的第三级菜单
    // nodeclick(data,Node,nodecom){
    // console.log("data=> ", data)
    // console.log("Node=> ", Node)
    // console.log("nodecom=> ", nodecom)
    /*
      node-click 实际是 el-tree 中规定好的实际，节点被点击时的回调	共三个参数，依次为：传递给 data 属性的数组中该节点所对应的对象、节点对应的 Node、节点组件本身。
      为什么会默认向 方法池自定义的nodeclick函数 传递三个参数？
      对于自定义事件（如 node-click），参数是由该事件的设计者定义的，并且会自动传递给处理函数。
     */
    nodeclick(data) {
      // console.log("data=> ", data)
      if (data.catLevel == 3) { //点击的是商品分类树形菜单的 第三级菜单节点

        // 将被点击的三级分类的id赋给catId
        // ,就可以通过下面的this.getDataList() ajax将categoryId当作查询条件发送给后端
        this.catId = data.id

        //刷新数据列表/根据条件查询数据列表
        this.getDataList()


      }


    },
    // 获取树形菜单分类列表，带层级-这里没有做抽取，而是直接拿来使用，因为前端知道怎么使用即可
    getCategories() {
      //这里的`$http`实际上就是axios,只是在main.js中起了一个别名
      this.$http({
        // 解读这里的url是获取后端分级菜单列表的url地址，即获取带层级的商品分类表`commodity_category`所有数据
        // url: 'http://localhost:9090/commodity/category/list/tree',

        // 请求的url
        // 就是 http://localhost:5050/api/commodity/category/list/tree
        url: this.$http.adornUrl('/commodity/category/list/tree'),
        method: 'get'
      }).then(({data}) => { //这里的{data} 是对象解构，解构了上面url请求后端返回来的数据，
        // responseData对象的data属性
        //输出
        // console.log("getCategories()方法中 .then(({data}) => {..) 返回的data= ", data);
        //返回需要展示的数据 data.data.data
        //如果使用{data}, 得到的要展示的数据使用 data.data 即可，因为解构了一层

        //将后端返回来的数据，绑定到数据池的data属性 注意绑定数据后，页面就会相应地变化！
        this.data = data.data;
      })
    },
    // 获取数据列表
    getDataList() {
      this.dataListLoading = true
      this.$http({
        // 完整的url是 http://localhost:5050/api/commodity/attrgroup/list
        // url: this.$http.adornUrl('/commodity/attrgroup/list'),
        url: this.$http.adornUrl(`/commodity/attrgroup/list/${this.catId}`),
        method: 'get',
        params: this.$http.adornParams({
          'page': this.pageIndex,
          'limit': this.pageSize,
          'key': this.dataForm.key
        })
      }).then(({data}) => {
        if (data && data.code === 0) {
          // 在这里给属性组列表赋值
          this.dataList = data.page.list
          this.totalPage = data.page.totalCount

          // 重要的设置:根据后端的分页逻辑 将返回来的 第currPage页 进行展示！
          // currPage页说明: **后端分页响应**：通常，响应体中的当前页 (`currPage` 或类似命名) 应反映实际返回的页码。在这种情况下，如果请求的页码超过了总页数，并且由于 `overflow` 设置为 `true`，返回了第一页的数据，`currPage` 应该会被设置为 `1`。
          this.pageIndex = data.page.currPage
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
      // 这里调用了子组件addOrUpdate 方法池中的init()方法
      //  this.$nextTick() 来确保我们调用子组件的 init() 方法是在 Vue 更新 DOM 之后。
      this.$nextTick(() => {
        // 现在DOM已经更新完成
        this.$refs.addOrUpdate.init(id)
      })
    },
    // 删除
    deleteHandle(id) {
      var ids = id ? [id] : this.dataListSelections.map(item => {
        return item.id
      })
      this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: this.$http.adornUrl('/commodity/attrgroup/delete'),
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
