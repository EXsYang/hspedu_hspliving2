<template>

  <div>
    <!--引入批量删除的按钮-->
    <el-button type="danger" @click="batchDelete">
      批量删除
    </el-button>

    <!--树形菜单 `:data="data"` 这里绑定的是数据池里的data属性

    在 el-tree 增加 ref="categoryTree",
    通过该值可以得到 el-tree
    ref="categoryTree" 这一行的意思是，你正在为 <el-tree> 组件创建一个引
    用（reference），并将这个引用命名为 "categoryTree"。在 Vue 组件内部，
    你可以通过 this.$refs.categoryTree 来访问这个 <el-tree> 组件的实例。
    这意味着你可以直接调用组件的方法，访问其数据和属性，甚至操作其子 DOM。
    -->
    <el-tree :data="data"
             :props="defaultProps"
             ref="categoryTree"
             show-checkbox
             node-key="id"
             :expand-on-click-node="false"
             :default-expanded-keys="expandedKey"
    >
      <!-- 老师解读 (增加的属性，方法等，都可以参考 elementUI Tree 树形控件 的文档说明)
        1. 使用 vue 的插槽机制在每个菜单后加入 Append 和 Delete 链接
        2. slot-scope="{ node, data } : 是一种解构写法, node 表示当前节点, data
        就是从数据库取出的分类对象信息, slot-scope="{ node, data }" 是 Vue.js 中的一个插槽（slot）使用方式
        3. @node-click="handleNodeClick" 没有什么用，删除，注意把对应的 method 也删除
        4. 增加相应的 append(data) 和 remove(node, data) 方法
        5. :expand-on-click-node="false": 设置 false 表示点击 Append 和 Delete 链接
        不展开下级分类
        6. show-checkbox 分类项显示成复选框，在删除时，可以支持多选
        7. node-key="id" 使用分类信息的 id 来唯一标识一个 node,node-key 应该对应您的数据模型中用于唯一标识每个项的字段名，而不是由 Vue 自动生成的。这通常是数据库中的一个字段，如每个项的主键 id。
        8. 通过default-expanded-keys和default-checked-keys设置默认展开和默认选中的节点。
           需要注意的是，此时必须设置node-key，其值为节点数据中的一个字段名，该字段在整棵树中是唯一的。
           设置需要展开的菜单 删除的节点的父节点分类对应的id 这里是根据el-tree
           的node-key属性进行指定的，要根据什么属性来当作展开树形菜单的依据
      -->
      <span class="custom-tree-node" slot-scope="{ node, data }">
        <span>{{ node.label }}</span>
        <span>
          <el-button
            v-if="node.level <= 2"
            type="text"
            size="mini"
            @click="() => append(data)"
          >
            Append
          </el-button>
          <!--引入Edit按钮-->
          <el-button
            type="text"
            size="mini"
            @click="() => edit(data)"
          >
            Edit
          </el-button>
          <el-button
            v-if="node.childNodes.length == 0"
            type="text"
            size="mini"
            @click="() => remove(node, data)">
            Delete
          </el-button>
        </span>
      </span>
    </el-tree>

    <!--引入对话框
    1. :visible.sync="dialogVisible" 动态绑定dialogVisible
       如果dialogVisible为true，就显示对话框，否则不显示
    2. :model="category" 单向绑定数据池的数据 category
    -->
    <el-dialog title="添加/修改 分类" :visible.sync="dialogVisible" width="30%">
      <el-form :model="category">
        <el-form-item label="分类名">
          <!--注意这里 v-model="category.name" 中的category是数据池里定义的对象/属性-->
          <el-input v-model="category.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="图标">
          <!--注意这里 v-model="category.icon" 中的category是数据池里定义的对象/属性-->
          <el-input v-model="category.icon" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="单位">
          <!--注意这里 v-model="category.name" 中的category是数据池里定义的对象/属性-->
          <el-input v-model="category.proUnit" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addOrUpdate">确 定</el-button>
      </div>
    </el-dialog>


  </div>
</template>

<script>
export default {
  data() {
    return {
      data: [], //树形菜单的数据
      dialogType: "", //标识对话框的类型 "add" , "edit"
      dialogVisible: false, // 控制是否显示append添加分类信息对话框
      // 在数据池里定义好category属性后，在方法池进行使用更加方便，直接this.category. 有属性提示，不用自己一个个敲了！！！
      category: { //初始化添加分类表单对象的数据，注意category的属性名和CategoryEntity的属性进行对应，否则会封装失败
        id: null,
        name: "",
        parentId: 0,
        catLevel: 0,
        isShow: 1,
        sort: 0,
        icon: "",
        proUnit: "",
        proCount: 0
      },// 添加的分类数据
      expandedKey: [], //要展开的树形菜单的key
      defaultProps: { //property:属性  树形菜单默认的支持数据 需要和返回的数据格式和属性名对应
        children: 'childrenCategories', //这里是返回的子分类集合节点名,不能乱写,否则，只会显示一级分类
        label: 'name' //取出 name 显示, 可以参考 elementUI 树形控件规则
      }
    };
  },
  methods: {
    //处理批量删除
    batchDelete(){
      // alert("批量删除")

      /**
       * 老韩解读 this.$refs.categoryTree.getCheckedNodes(leafOnly, includeHalfChecked)
       * 1. this.$refs.categoryTree : 代表 el-tree 控件
       * 2. getCheckedNodes(leafOnly, includeHalfChecked)
       * 2.1 若节点可被选择（即 show-checkbox 为 true），则返回目前被选中的节点所组成的数组
       * 2.2 (leafOnly, includeHalfChecked) 接收两个 boolean 类型的参数，
       * (1) 是否只是叶子节点，默认值为 false (2) 是否包含半选节点，默认值为 false
       * 2.3 老师没有传入参数 , 等价getCheckedNodes(false, false), 即返回选中的分类节点(不包
            括半选的)
       * 2.4 半选就是某个分类的子分类, 只选择了部分，没有全选，是 - 符号
       */
      //通过ref="categoryTree", 来获取勾选的分类信息
      var checkedNodes = this.$refs.categoryTree.getCheckedNodes();
      console.log("checkedNodes=> ", checkedNodes)

      //先收集选中的分类的ids 和 分类名
      // 前端发送的是一个 JSON 格式的数组，而后端通过 @RequestBody Long[] ids 接收这个数组，并将其解析为 Java 中的一个 Long 类型数组。
      var ids = [] //注意这里是一个json数组，后端delete接口使用数组进行接收的
      var categoryNames = []
      for (var i = 0; i < checkedNodes.length; i++) {
        ids.push(checkedNodes[i].id)
        categoryNames.push(checkedNodes[i].name)
      }

      //可以给出提示，如果用户选择确定，就调用后端程序接口，完成批量删除(逻辑删除)
      /*
     this.$confirm 在这段代码中并不是一个 axios 对象，而是一个由 Element UI 提供的方法，用于显示确认对话框。这是 Element UI 的一部分，一个基于 Vue 的桌面端组件库。$confirm 方法用于弹出一个模态对话框，询问用户是否确定执行某个操作，并且它返回一个 Promise 对象。
     在 JavaScript 中，Promise 对象用于表示未来某个将要完成（或失败）的异步操作的结果。当 this.$confirm 被调用时，它会显示一个带有确定和取消按钮的对话框。如果用户点击“确定”，Promise 将会被“解决”（resolve），执行 .then() 里的回调函数；如果用户点击“取消”，Promise 将会被“拒绝”（reject），执行 .catch() 里的回调函数。
     因此，当你看到 this.$confirm(...).then(...).catch(...) 这样的代码，它并不是说 $confirm 是一个 axios 请求，而是说 $confirm 返回的是一个可以链式调用 .then() 和 .catch() 方法的 Promise 对象。这样的设计让你可以编写代码来处理用户点击对话框中按钮后的逻辑。
      */
      this.$confirm(`是否批量删除【${categoryNames}】菜单?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        //如果点击`确定` 进行批量删除操作
        this.$http({
          // 解读这里的url是获取后端分级菜单列表的url地址，即获取带层级的商品分类表`commodity_category`所有数据
          // url: 'http://localhost:9090/commodity/category/delete',
          url: this.$http.adornUrl('/commodity/category/delete'),
          method: 'post',
          //发出请求时携带的数据，参考 src/views/modules/sys/role.vue:158
          data: this.$http.adornData(ids, false)
        }).then(({data}) => { //这里的{data} 是对象解构，解构了上面url请求后端返回来的数据，
          // responseData对象的data属性
          //输出
          console.log("remove(node, data)方法 .then(({data}) 返回的data= ", data);
          //返回需要展示的数据 data.data.data
          //如果使用{data}, 得到的要展示的数据使用 data.data 即可，因为解构了一层



          this.$message({
            message: '批量删除成功OK',
            type: 'success'
          })

          //将后端返回来的数据，绑定到数据池的data属性
          // this.data = data.data;
          //刷新分类列表 绑定数据
          this.getCategories();

        }).catch(() => {
          //如果点击`取消`，不删除
        })

      })



    },
    //增加方法addOrUpdate - 可以处理添加分类和修改分类
    addOrUpdate() {
      if ("add" == this.dialogType) {
        // 当前的业务是添加
        this.addCategory();
      }
      if ("edit" == this.dialogType) {
        // 当前的业务是修改
        this.updateCategory();
      }
    },
    //真正修改分类信息(调用后端程序接口)
    updateCategory() {
      //1. 注意: 这里我们只是提交需要修改的字段信息，没有提交的字段，在
      //   数据库中不会被修改(保持原来的值.. 我们可以观察sql语句)
      //2. var {id, name, icon, proUnit} = this.category 进行对象解构
      //   这里取得是回显时从数据库中绑定到this.category的数据，
      //3. 当该方法updateCategory()被调用时，添加/修改对话框是显示的状态，
      //   当调用此方法时，category的数据修改已经确定下来了
      //   ，是回显绑定到数据池的+手动修改的数据()，
      //   对话框中的数据是双向绑定的，在显示的对话框点击确定后调用了
      //   updateCategory()方法时，就是最新的数据，即对话框中的数据
      var {id, name, icon, proUnit} = this.category
      //调用后端程序的接口/API, 发出修改请求
      //发送请求,到后端程序去获取分类信息(实时数据,DB获取)
      this.$http({
        // 解读这里的url是获取后端分级菜单列表的url地址，即获取带层级的商品分类表`commodity_category`所有数据
        url: `http://localhost:9090/commodity/category/update`,
        method: 'post',
        //发出请求时携带的数据，参考 src/views/modules/sys/role.vue:158
        data: this.$http.adornData({id, name, icon, proUnit}, false)
      }).then(({data}) => { //这里的{data} 是对象解构，解构了上面url请求后端返回来的数据，
        // responseData对象的data属性
        //输出
        console.log("updateCategory()方法 .then(({data}) 返回的data= ", data);
        //返回需要展示的数据 data.data.data
        //如果使用{data}, 得到的要展示的数据使用 data.data 即可，因为解构了一层

        //取消显示对话框
        this.dialogVisible = false

        //将后端返回来的数据，绑定到数据池的data属性
        // this.data = data.data;

        this.$message({
          message: '分类信息修改成功OK',
          type: 'success'
        })

        //刷新分类列表 绑定数据
        this.getCategories();

        // console.log(" 删除的节点的父节点分类对应的id",node.parent.data.id)
        // console.log(" 删除的节点的父节点node.parent",node.parent)
        //设置需要展开的菜单 删除的节点的父节点分类对应的id
        // node 即为当前点击的节点
        this.expandedKey = [this.category.parentId]

      })


    },
    //点击Edit按钮,回显分类信息
    edit(data) {
      // console.log("edit(data)方法 data=> ",data)

      //显示回显对话框(和添加分类信息共用同一个)-准备修改
      //设置对话框的类型
      this.dialogType = "edit"
      this.dialogVisible = true

      //发送请求,到后端程序去获取分类信息(实时数据,DB获取)
      this.$http({
        // 解读这里的url是获取后端分级菜单列表的url地址，即获取带层级的商品分类表`commodity_category`所有数据
        url: `http://localhost:9090/commodity/category/info/${data.id}`,
        method: 'get'
      }).then(({data}) => { //这里的{data} 是对象解构，解构了上面url请求后端返回来的数据，
        // responseData对象的data属性
        //输出
        console.log("edit(data)方法 .then(({data}) 返回的data= ", data);
        //返回需要展示的数据 data.data.data
        //如果使用{data}, 得到的要展示的数据使用 data.data 即可，因为解构了一层

        //取消显示对话框
        // this.dialogVisible = false

        //将后端返回来的数据，分别绑定到数据池的category对象的属性上[可以有选择的绑定]
        this.category.name = data.category.name
        this.category.icon = data.category.icon
        this.category.proUnit = data.category.proUnit
        this.category.id = data.category.id

        //为了配合显示展开的菜单，把this.category.parentId也绑定
        this.category.parentId = data.category.parentId

        //刷新分类列表 绑定数据
        this.getCategories();
        // console.log(" 删除的节点的父节点分类对应的id",node.parent.data.id)
        // console.log(" 删除的节点的父节点node.parent",node.parent)
        //设置需要展开的菜单 删除的节点的父节点分类对应的id
        // node 即为当前点击的节点
        // this.expandedKey = [this.category.parentId]

      })

    },
    // 真正向数据库中添加分类信息
    addCategory() { //处理点击添加分类，真正的调用后端接口，将分类信息入库
      this.$http({
        // 解读这里的url是获取后端分级菜单列表的url地址，即获取带层级的商品分类表`commodity_category`所有数据
        // url: 'http://localhost:9090/commodity/category/save',
        url: this.$http.adornUrl('/commodity/category/save'),
        method: 'post',
        //发出请求时携带的数据，参考 src/views/modules/sys/role.vue:158
        data: this.$http.adornData(this.category, false)
      }).then(({data}) => { //这里的{data} 是对象解构，解构了上面url请求后端返回来的数据，
        // responseData对象的data属性
        //输出
        console.log("addCategory方法 .then(({data}) 返回的data= ", data);
        //返回需要展示的数据 data.data.data
        //如果使用{data}, 得到的要展示的数据使用 data.data 即可，因为解构了一层


        //取消显示对话框
        this.dialogVisible = false

        //将后端返回来的数据，绑定到数据池的data属性
        // this.data = data.data;
        //刷新分类列表 绑定数据
        this.getCategories();

        this.$message({
          message: '分类信息添加成功OK',
          type: 'success'
        })


        // console.log(" 删除的节点的父节点分类对应的id",node.parent.data.id)
        // console.log(" 删除的节点的父节点node.parent",node.parent)
        //设置需要展开的菜单 删除的节点的父节点分类对应的id
        // node 即为当前点击的节点
        this.expandedKey = [this.category.parentId]

      })

    },
    append(data) { //处理点击添加分类
      //输出data观察数据结构
      console.log("data=> ", data)
      //显示添加分类的对话框
      //设置对话框的类型
      this.dialogType = "add"
      this.dialogVisible = true

      //给要添加的分类category设置基础属性
      this.category.parentId = data.id

      // 在添加时，重置了this.category的属性
      this.category.sort = 0
      this.category.proUnit = ""
      this.category.proCount = 0
      this.category.name = ""
      this.category.isShow = 1
      this.category.id = null //id自增长的
      this.category.icon = ""
      // data.catLevel * 1 ,是为了将分类级别转换为数值型，而不是字符串相加
      // console.log("data.catLevel=> " , data.catLevel)
      // console.log("typeof data.catLevel => " , typeof data.catLevel)
      //
      // console.log("data.catLevel + 1 => " , data.catLevel + 1)
      // console.log("data.catLevel * 1 + 1 => " , data.catLevel * 1 + 1)
      // 添加的分类层级为其父类的层级+1，这里 * 1 是为了将字符串转换为数值
      this.category.catLevel = data.catLevel * 1 + 1

      console.log("this.category=> ", this.category)


    },
    remove(node, data) { //处理点击删除分类
      //输出node,data观察数据结构
      console.log("node=> ", node, " data=> ", data)
      //这里我们可以参考前面写过的代码来完成删除任务
      //1. 使用数组获取删除的分类的id
      var ids = [data.id]
      //2. 发送请求，完成删除
      //这里的`$http`实际上就是axios,只是在main.js中起了一个别名

      /*
      this.$confirm 在这段代码中并不是一个 axios 对象，而是一个由 Element UI 提供的方法，用于显示确认对话框。这是 Element UI 的一部分，一个基于 Vue 的桌面端组件库。$confirm 方法用于弹出一个模态对话框，询问用户是否确定执行某个操作，并且它返回一个 Promise 对象。
      在 JavaScript 中，Promise 对象用于表示未来某个将要完成（或失败）的异步操作的结果。当 this.$confirm 被调用时，它会显示一个带有确定和取消按钮的对话框。如果用户点击“确定”，Promise 将会被“解决”（resolve），执行 .then() 里的回调函数；如果用户点击“取消”，Promise 将会被“拒绝”（reject），执行 .catch() 里的回调函数。
      因此，当你看到 this.$confirm(...).then(...).catch(...) 这样的代码，它并不是说 $confirm 是一个 axios 请求，而是说 $confirm 返回的是一个可以链式调用 .then() 和 .catch() 方法的 Promise 对象。这样的设计让你可以编写代码来处理用户点击对话框中按钮后的逻辑。
       */
      this.$confirm(`是否删除【${data.name}】菜单?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        //如果点击`确定` 进行删除操作
        this.$http({
          // 解读这里的url是获取后端分级菜单列表的url地址，即获取带层级的商品分类表`commodity_category`所有数据
          // url: 'http://localhost:9090/commodity/category/delete',
          url: this.$http.adornUrl('/commodity/category/delete'),
          method: 'post',
          //发出请求时携带的数据，参考 src/views/modules/sys/role.vue:158
          data: this.$http.adornData(ids, false)
        }).then(({data}) => { //这里的{data} 是对象解构，解构了上面url请求后端返回来的数据，
          // responseData对象的data属性
          //输出
          console.log("remove(node, data)方法 .then(({data}) 返回的data= ", data);
          //返回需要展示的数据 data.data.data
          //如果使用{data}, 得到的要展示的数据使用 data.data 即可，因为解构了一层



          this.$message({
            message: '分类信息删除成功OK',
            type: 'success'
          })

          //将后端返回来的数据，绑定到数据池的data属性
          // this.data = data.data;
          //刷新分类列表 绑定数据
          this.getCategories();

          // console.log(" 删除的节点的父节点分类对应的id",node.parent.data.id)
          // console.log(" 删除的节点的父节点node.parent",node.parent)

          // 设置需要展开的菜单 删除的节点的父节点分类对应的id 这里是根据el-tree
          // 的node-key属性进行指定的，要根据什么属性来当作展开树形菜单的依据
          // node 即为当前点击的节点
          this.expandedKey = [node.parent.data.id]

        }).catch(() => {
          //如果点击`取消`，不删除
        })

      })


    },
    // 获取树形菜单分类列表，带层级
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
  },
  created() {//vue生命周期方法，这里经常发出ajax请求

    // 这个阶段组件的 data 和 methods 中的方法已初始化结束，可以访问，但是 dom 结构未
    // 初始化，页面未渲染
    // 老师说明：在这个阶段，经常会发起 Ajax 请求
    this.getCategories();
  }
};
</script>

<style scoped>

</style>
