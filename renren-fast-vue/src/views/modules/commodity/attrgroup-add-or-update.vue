<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    @close="dialogClose"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="组名" prop="name">
      <el-input v-model="dataForm.name" placeholder="组名"></el-input>
    </el-form-item>
    <el-form-item label="排序" prop="sort">
      <el-input v-model="dataForm.sort" placeholder="排序"></el-input>
    </el-form-item>
    <el-form-item label="说明" prop="description">
      <el-input v-model="dataForm.description" placeholder="说明"></el-input>
    </el-form-item>
    <el-form-item label="组图标" prop="icon">
      <el-input v-model="dataForm.icon" placeholder="组图标"></el-input>
    </el-form-item>
    <el-form-item label="所属分类 id" prop="categoryId">
<!--      <el-input v-model="dataForm.categoryId" placeholder="所属分类 id"></el-input>-->

      <!-- 引入Cascader级联选择器
      老师解读
      1. 在添加选择所属分类 ID 时，我们使用了 Cascader 级联选择器, 具体用法参考
        elementui-Cascader 级联选择器-基础用法
      2. v-model="cascadedCategoryId" : 是最终绑定的值,因为 el-cascader 关联的
        v-model 是一个数组，记录的是三级分类全部 id,即[第 1 级分类 id,第 2 级分类 id
        ,第 3 级分类 id], 比如[1,21,301]因此在数据池的 categoryId 是一个数组
      3. :options="categories" 表示级联显示的时候各个层的选值是从 categories 数组来的
      4. :props="props" 显示的选项值(value)/显示的标签(label)/子选项(children) 分别和
        返回的 category 的对象的哪个字段关联。简单地说就是
        props指定options数据源(categories)的哪些数据绑定到v-model(cascadedCategoryId)的数据中来
        props中的value属性，是每一级选中的项的 id 属性（也就是 props 中定义的 value 字段）。'1', '21', '301'
      5.v-model 则是存储用户选择的整个路径的标识值数组['1', '2', '3']
      6. 写 `filterable` 或者 `filterable="true"` 和 placeholder="搜索: " 在级联选择器增加搜索分类的功能
      -->
    <!-- <el-cascader
           filterable="true"   -->
      <el-cascader
        filterable
        placeholder="搜索: "
        v-model="cascadedCategoryId"
        :options="categories"
        :props="props"
        ></el-cascader>
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
    data () {
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
        visible: false,
        dataForm: {
          id: 0,
          name: '',
          sort: '',
          description: '',
          icon: '',
          // categoryId: ''
          categoryId: 0 //默认给categoryId=0，即可通过非空校验
        },
        dataRule: {
          name: [
            { required: true, message: '组名不能为空', trigger: 'blur' }
          ],
          sort: [
            { required: true, message: '排序不能为空', trigger: 'blur' }
          ],
          description: [
            { required: true, message: '说明不能为空', trigger: 'blur' }
          ],
          icon: [
            { required: true, message: '组图标不能为空', trigger: 'blur' }
          ],
          categoryId: [
            { required: true, message: '所属分类 id不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    created() { //第一次进入到新增页面，才触发该方法
      this.getCategories();
    },
    // activated() {
    //   this.getCategories();
    // },
    methods: {
      // 增加方法, 清空 cascadedCategoryId
      dialogClose(){
        this.cascadedCategoryId = []
      },
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
          // responseData对象的data属性
          //输出
          // console.log("attrgroup-add-or-update.vue getCategories()方法中 .then(({data}) => {..) 返回的data= ", data);
          // console.log("data.data.name", data.data.name); //data.data.name undefined
          // console.log("data.data[0].name", data.data[0].name); //data.data[0].name 家用电器
          //返回需要展示的数据 data.data.data
          //如果使用{data}, 得到的要展示的数据使用 data.data 即可，因为解构了一层

          //将后端返回来的数据，绑定到数据池的data属性 注意绑定数据后，页面就会相应地变化！
          // this.data = data.data;

          // 将返回来的带层级的数据绑定到数据池 this.categories
          this.categories = data.data
        })
      },

      // 新增 / 修改会在父组件attrgroup.vue中方法池的addOrUpdateHandle(id) {}
      // 调用到init (id) 方法,因此点击新增 / 修改时
      // ，实际触发的是下面这个 init(id)方法
      init (id) {
        //!dataForm.id ? '新增' : '修改'
        // 如果点击的是新增按钮 就清空 cascadedCategoryId
        // console.log("id=>",id)
        // if (!id){
        //   console.log("是新增哦")
        //   this.cascadedCategoryId = []
        // }

        // 因为级联选择器，在展示分类层级关系时，依据 cascadedCategoryId
        // console.log("cascadedCategoryId=> ", this.cascadedCategoryId)


        this.dataForm.id = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.id) {
            this.$http({
              url: this.$http.adornUrl(`/commodity/attrgroup/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.name = data.attrgroup.name
                this.dataForm.sort = data.attrgroup.sort
                this.dataForm.description = data.attrgroup.description
                this.dataForm.icon = data.attrgroup.icon
                this.dataForm.categoryId = data.attrgroup.categoryId
                // 为了实现每次点击修改，能够对应显示分类层级
                // 需要获取到对应的 this.cascadedCategoryId 就OK
                this.cascadedCategoryId = data.attrgroup.cascadedCategoryId

              }
            })
          }
        })
      },
      // 表单提交
      dataFormSubmit () {

        // this.$refs['dataForm'].validate 这里对表单dataForm进行校验
        // 因此在下面 即使没有使用 'categoryId': this.dataForm.categoryId
        // 也会对其进行非空校验
        this.$refs['dataForm'].validate((valid) => {
          // 输出 cascadedCategoryId
          // console.log("cascadedCategoryId= " , this.cascadedCategoryId)

          // 方案1:解决categoryId校验问题，给categoryId赋值，使其不为空即可
          // 方案2:在数据池给categoryId 赋默认值 0 即可，保证不为空 （最好的方案，简单）
          // 方案3:拿掉 categoryId 字段的校验规则即可，不对该字段进行校验
          // this.dataForm.categoryId = this.cascadedCategoryId[this.cascadedCategoryId.length-1]

          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/commodity/attrgroup/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'name': this.dataForm.name,
                'sort': this.dataForm.sort,
                'description': this.dataForm.description,
                'icon': this.dataForm.icon,
                // 'categoryId': this.dataForm.categoryId

                // 说明: this.cascadedCategoryId 数组保存了当前用户选择的分类层级关系
                // 形式为: [ 1, 22, 202 ] 我们要提交的categoryId 就是数组的最后一个元素的值

                'categoryId': this.cascadedCategoryId[this.cascadedCategoryId.length-1]
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
