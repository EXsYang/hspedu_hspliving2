<template>
  <el-dialog
    :title="!dataForm.attrId ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">

    <!-- :rules="dataRule" 这里是对el-form表单进行校验
    ，因为下面使用的是 el-cascader 对 categoryId 进行处理的，
    所以需要给 categoryId 一个 默认值 0，用于通过校验，
    即用于含有 `this.$refs['dataForm'].validate((valid) => {`的表单校验
    因为在下面的表单填写过程中，并没有涉及到给 categoryId赋值的情况
    ，因此，在数据池中定义的 categoryId：'' 改为 categoryId：0
    就可以通过，前端的非空校验！
     -->
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             label-width="300px">
      <el-form-item label="属性名" prop="attrName">
        <el-input v-model="dataForm.attrName" placeholder="属性名"></el-input>
      </el-form-item>
      <el-form-item label="是否需要检索[0-不需要，1-需要]" prop="searchType">
        <el-input v-model="dataForm.searchType" placeholder="是否需要检索[0-不需要，1-需要]"></el-input>
      </el-form-item>
      <el-form-item label="图标" prop="icon">
        <el-input v-model="dataForm.icon" placeholder="图标"></el-input>
      </el-form-item>
      <el-form-item label="可选值列表[用逗号分隔]" prop="valueSelect">
        <el-input v-model="dataForm.valueSelect" placeholder="可选值列表[用逗号分隔]"></el-input>
      </el-form-item>
      <el-form-item label="属性类型[0-销售属性，1-基本属性]" prop="attrType">
        <!--      <el-input v-model="dataForm.attrType" placeholder="属性类型[0-销售属性，1-基本属性]"></el-input>-->

        <!--将属性 el-input输入框类型 改成 el-select 控件-->
        <!--        <el-form-item label="属性类型" prop="attrType">-->
        <!-- <el-input v-model="dataForm.attrType" placeholder="属性类型[0-销售属性，
        1-基本属性，2-既是销售属性又是基本属性]"></el-input>-->
        <el-select v-model="dataForm.attrType" placeholder="请选择">
          <el-option
            label="基本属性"
            :value="1">
          </el-option>
          <el-option
            label="销售属性"
            :value="0">
          </el-option>
        </el-select>
        <!--        </el-form-item>-->

      </el-form-item>
      <el-form-item label="启用状态[0 - 禁用，1 - 启用]" prop="enable">
        <el-input v-model="dataForm.enable" placeholder="启用状态[0 - 禁用，1 - 启用]"></el-input>
      </el-form-item>
      <el-form-item label="所属分类" prop="categoryId">
        <!--        <el-input v-model="dataForm.categoryId" placeholder="所属分类"></el-input>-->
        <!-- 把所属分类 el-input输入框改成级联选择器 -->
        <el-cascader
          filterable
          placeholder="搜索: "
          v-model="cascadedCategoryId"
          :options="categories"
          :props="props"
        ></el-cascader>
      </el-form-item>

      <!-- 增加所属分组，当点击某个分类时，会联动出现对应的属性分组下拉选择框
       当用户选择时 便会将 item.id 的值 动态的绑定到 dataForm.attrGroupId
       attrGroups 是根据用户点击选择的 所属分类动态的从后端取回来的属性组的
       集合/数组以json形式，映射到前端的数组中，形式如 [{...},{...}]
       item.id 是从后端取回来的属性组的id
      -->
<!--      <el-form-item label="所属分组">-->
      <el-form-item label="所属分组" prop="attrGroupId">
        <el-select ref="groupSelect" v-model="dataForm.attrGroupId" placeholder="请选择">
          <el-option
            v-for="item in attrGroups"
            :key="item.id"
            :label="item.name"
            :value="item.id"></el-option>
        </el-select>
      </el-form-item>


      <el-form-item label="快速展示【是否展示在介绍上；0-否 1-是】
" prop="showDesc">
        <el-input v-model="dataForm.showDesc" placeholder="快速展示【是否展示在介绍上；0-否 1-是】
"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  data() {
    return {
      categories: [], // 商品分类列表，带层级的数据 作为级联选择器cascader的数据源
      // 级联选择器cascader是根据cascadedCategoryId数组中的值 来展示级联层级关系的
      cascadedCategoryId: [], // 级联选择器cascader v-model 的值  [1,21,301] 该数据从categories中获取

      /**
       * 当我们配置 el-cascader 的 props，我们实际上是告诉组件如何从 categories 数组中的每个对象里提取相应的数据(如对象的id属性)来显示和组织级联选择器的结构。
       * props中的value属性，是每一级选中的项的 id 属性（也就是 props 中定义的 value 字段）。'1', '21', '301'
       */
      props: { // 显示级联菜单的属性指定
        value: "id",
        label: "name",
        children: "childrenCategories"
      },
      attrGroups: [],
      visible: false,
      dataForm: {
        attrId: 0,
        attrName: '',
        searchType: '',
        icon: '',
        valueSelect: '',
        attrType: '',
        enable: '',
        // categoryId: '',
        categoryId: 0, // 为了通过非空校验，初始值设置为0
        attrGroupId: '', //属性组id
        showDesc: ''
      },
      dataRule: {
        attrName: [
          {required: true, message: '属性名不能为空', trigger: 'blur'}
        ],
        searchType: [
          {required: true, message: '是否需要检索[0-不需要，1-需要]不能为空', trigger: 'blur'}
        ],
        icon: [
          {required: true, message: '图标不能为空', trigger: 'blur'}
        ],
        valueSelect: [
          {required: true, message: '可选值列表[用逗号分隔]不能为空', trigger: 'blur'}
        ],
        attrType: [
          {required: true, message: '属性类型[0-销售属性，1-基本属性]不能为空', trigger: 'blur,change'}
        ],
        enable: [
          {required: true, message: '启用状态[0 - 禁用，1 - 启用]不能为空', trigger: 'blur'}
        ],
        categoryId: [
          {required: true, message: '所属分类不能为空', trigger: 'blur'}
        ],
        showDesc: [
          {required: true, message: '快速展示【是否展示在介绍上；0-否 1-是】不能为空', trigger: 'blur'}
        ]
      }
    }
  },
  created() {
    // 调用方法，返回 级联选择器 el-cascader 中的数据
    this.getCategories()
  },
  //显示添加商品属性界面的 watch, 当选择的分类变化，就重新获取该分类的所有属性分组信息. watch: {
  watch: {
    cascadedCategoryId(path) { // 监控到数据池中 cascadedCategoryId 的变化
      console.log("路径=", path); // path 数据格式 [1,21,301]
      this.attrGroups = [];
      this.dataForm.attrGroupId = "";
      // 是在这里给categoryId赋值的，如果没有在这里赋值，则初始值为0
      this.dataForm.categoryId = path[path.length - 1];
      if (path && path.length == 3) {
        this.$http({
          //如果启动了网关服务, 则 url 修改成走网关服务即可
          // url: `http://localhost:9090/commodity/attrgroup/list/${path[path.length - 1]}`,
          url: this.$http.adornUrl(`/commodity/attrgroup/list/${path[path.length - 1]}`),
          method: "get",
          params: this.$http.adornParams({page: 1, limit: 10000000})
        }).then(({data}) => {
          console.log("data=", data.page.list)
          if (data && data.code === 0) {
            // 将返回的当前分类对应的属性组信息，赋给 this.attrGroups
            this.attrGroups = data.page.list;

          } else {
            this.$message.error(data.msg);
          }
        });
      } else if (path.length == 0) {
        this.dataForm.categoryId = "";
      } else {
        this.$message.error("请选择正确的分类");
        this.dataForm.categoryId = "";
      }
    }
  },
  methods: {
    // 增加方法，返回所有的分类信息 - 可以用来返回级联选择器的数据源
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
        //返回需要展示的数据 data.data.data
        //如果使用{data}, 得到的要展示的数据使用 data.data 即可，因为解构了一层

        //将后端返回来的数据，绑定到数据池的data属性 注意绑定数据后，页面就会相应地变化！
        // this.data = data.data;
        // 将返回来的带层级的数据绑定到数据池 this.categories
        this.categories = data.data
      })
    },

    // 当点击上方的新增和 列表某一行右边的修改时触发
    init(id) {
      // 如果点击的是修改，则会将当前行的id传过来,这里即传过来的是
      // 当前行的 attrId 是根据返回的DataList 中的属性，即Entity中
      // 的属性确定的属性名
      this.dataForm.attrId = id || 0
      this.visible = true
      this.$nextTick(() => {
        // 当重新点击 新增和 列表某一行右边的修改时 从新设置dataForm表单中的数值为初始值
        // ，即这里会影响到新打开一个新增/修改对话框时，dataForm表单中的数值
        // 如果一个表单项有初始值，并且通过 v-model 与 dataForm 中的某个属性绑定
        // ，那么调用 resetFields() 方法时，这个表单项将会被重置到它的初始值。
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.attrId) {
          this.$http({
            url: this.$http.adornUrl(`/commodity/attr/info/${this.dataForm.attrId}`),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.dataForm.attrName = data.attr.attrName
              this.dataForm.searchType = data.attr.searchType
              this.dataForm.icon = data.attr.icon
              this.dataForm.valueSelect = data.attr.valueSelect
              this.dataForm.attrType = data.attr.attrType
              this.dataForm.enable = data.attr.enable

              this.dataForm.categoryId = data.attr.categoryId
              this.dataForm.showDesc = data.attr.showDesc
            }
          })
        }
      })
    },
    // 表单提交
    dataFormSubmit() {

      this.$refs['dataForm'].validate((valid) => {

        // console.log("this.dataForm.attrGroupId=> ",this.dataForm.attrGroupId)

        if (valid) {
          this.$http({
            url: this.$http.adornUrl(`/commodity/attr/${!this.dataForm.attrId ? 'save' : 'update'}`),
            method: 'post',
            // 请求体中携带的数据 后端要和这里携带数据的data中定义的属性名保持一致才能正确封装
            data: this.$http.adornData({
              'attrId': this.dataForm.attrId || undefined,
              'attrName': this.dataForm.attrName,
              'searchType': this.dataForm.searchType,
              'icon': this.dataForm.icon,
              'valueSelect': this.dataForm.valueSelect,
              'attrType': this.dataForm.attrType,
              'enable': this.dataForm.enable,
              'categoryId': this.dataForm.categoryId,
              'attrGroupId': this.dataForm.attrGroupId, // 在添加基本属性时，同时携带用户选择的属性组Id
              'showDesc': this.dataForm.showDesc,
              'id': this.dataForm.showDesc
            })
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.visible = false
                  this.$emit('refreshDataList')
                }
              })
            } else {
              this.$message.error(data.msg)
            }
          })
        }
      })
    }
  }
}
</script>
