<template>

  <!-- brand-add-or-update.vue 是处理品牌的添加和修改的
       该文件还有ESLint语法规则的检测，对语法比较严格，右上角改为语法规则报错提示即可
       取消 ESLint 检测:
       1. 修改 renren-fast-vue\build\webpack.base.conf.js, 注销 createLintingRule 方法体内容
  -->

  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             label-width="100px">
      <el-form-item label="品牌名" prop="name">
        <!--placeholder="品牌名"是一个HTML属性，用于指定一个表单字段的占位文本。
        这里的占位文本“品牌名”将显示在对应的表单输入字段中，通常在用户还没有输入任何内容之前。
        它用来给用户一个提示，说明这个表单项希望接收的输入类型或内容。当用户开始输入时，占位文本会消失。-->
        <el-input v-model="dataForm.name" placeholder="品牌名"></el-input>
      </el-form-item>
      <el-form-item label="logo" prop="logo">
        <!--      <el-input v-model="dataForm.logo" placeholder="logo"></el-input>-->

        <!-- 在logo项，使用文件上传组件，替换原来的el-input -->
        <SingleUpload v-model="dataForm.logo"></SingleUpload>

      </el-form-item>
      <el-form-item label="说明" prop="description">
        <el-input v-model="dataForm.description" placeholder="说明"></el-input>
      </el-form-item>
      <el-form-item label="显示" prop="isshow">
        <el-input v-model="dataForm.isshow" placeholder="显示"></el-input>
      </el-form-item>
      <el-form-item label="检索首字母" prop="firstLetter">
        <el-input v-model="dataForm.firstLetter" placeholder="检索首字母"></el-input>
      </el-form-item>
      <el-form-item label="排序" prop="sort">
        <el-input v-model="dataForm.sort" placeholder="排序"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>

//引入需要使用的控件
import SingleUpload from "@/components/upload/singleUpload"

export default {
  //先导入再导出组件，即可在该页面中使用
  components: {
    SingleUpload
  },
  data() {
    return {
      visible: false,
      dataForm: {
        id: 0,
        name: '',
        logo: '',
        description: '',
        isshow: '',
        firstLetter: '',
        sort: ''
      },
      dataRule: {
        name: [
          /*
    在代码片段 `{ required: true, message: '检索首字母不能为空', trigger: 'blur' }` 中，设置了以下规则：
    - `required: true`: 这表明该字段是必填项。也就是说，用户必须在该字段中输入某些内容；否则，当验证被触发时，该字段将被视为不符合要求。
    - `message: '检索首字母不能为空'`: 这是当验证失败时显示的错误消息。在这个案例中，如果字段为空（因为它是必填的），将会显示这条消息。
    - `trigger: 'blur'`: 这指的是触发验证的事件。在这里，它被设置为 `blur`，意味着当用户离开（失焦）这个输入字段时，将触发验证。也就是说，如果用户点击进入输入框然后不输入任何内容直接点击出来，将会触发验证逻辑，因为输入框从获得焦点到失去焦点的行为触发了 `blur` 事件。
    所以，如果用户在这个表单字段中没有填写任何值，并且该字段失去焦点（例如，用户点击了该字段然后点击了其他地方或移动到了下一个字段），将会触发校验，并因为该字段是必填的但却为空，显示错误信息 '检索首字母不能为空'。
           */
          {required: true, message: '品牌名不能为空', trigger: 'blur'}
        ],
        logo: [
          {required: true, message: 'logo不能为空', trigger: 'blur'}
        ],
        description: [
          {required: true, message: '说明不能为空', trigger: 'blur'}
        ],
        isshow: [
          {required: true, message: '显示不能为空', trigger: 'blur'}
        ],
        firstLetter: [ //增加自定义检索首字母，必须是 a-z 或者 A-Z
          // { required: true, message: '检索首字母不能为空', trigger: 'blur' }
          /**
           * 老师解读
           * 0. 关于 elementUI 的表单验证，参考文档 elementUI-Form 表单-表单验证
           * 1. validator 可以编写自定义的校验器(就是一个方法)
           *    , 参考文档 elementUI-Form 表单-自定义校验规则
           * 2. 这里我们直接用箭头函数写在 validator: 后面
           * 3. 参数 rule 是规则 value 是用户填写内容 callback 是一个回调方法，会将错误信息
           *    填写在对应的表单处
           * 4. trigger: 'blur' 表示表单输入框失去输入焦点， 触发校验
           */
          {
            //增加校验器
            validator: (rule, value, callback) => {
              if (value === '') {
                callback(new Error('检索首字母必须填写'));
              } else if (!/^[a-zA-Z]$/.test(value)) { // 必须是 a-z 或者 A-Z
                // 如果不满足 a-z 或者 A-Z 就会走到这里
                callback(new Error('检索首字母必须是a-z 或者 A-Z'));
              } else {
                callback();
              }
            },
            // required: true,
            // message: '检索首字母不能为空',
            trigger: 'blur'
            // trigger: ['blur', 'change']
          }
        ],
        sort: [ // 排序值 sort 必须是大于等于 0 的整数
          // {required: true, message: '排序不能为空', trigger: 'blur'}
          {
            //增加校验器
            validator: (rule, value, callback) => {
              if (value === '') {
                callback(new Error('排序值 sort 必须填写'));
              // } else if (!/^([1-9]*|0)$/.test(value)) { // 这个不对，这样写输入的数字不能包含0 如100,30,3012都不行
              } else if (!/^([1-9]\d*|0)$/.test(value)) { // 必须是大于等于 0 的整数 , 正则表达式的解释: (1-9开头后面可以跟0到多个数字的数)或者0
                // \d 数字字符匹配。等效于 [0-9]
                // 如果不满足 必须是大于等于 0 的整数 就会走到这里
                callback(new Error('排序值 sort 必须是大于等于 0 的整数'));
              } else {
                callback();
              }
            },
            // trigger: 触发
            trigger: 'blur'
          }
        ]
      }
    }
  },
  methods: {
    init(id) {
      this.dataForm.id = id || 0
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.id) {
          this.$http({
            url: this.$http.adornUrl(`/commodity/brand/info/${this.dataForm.id}`),
            // url: `http://localhost:9090/commodity/brand/info/${this.dataForm.id}`,
            method: 'get',
            params: this.$http.adornParams()
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.dataForm.name = data.brand.name
              this.dataForm.logo = data.brand.logo
              this.dataForm.description = data.brand.description
              this.dataForm.isshow = data.brand.isshow
              this.dataForm.firstLetter = data.brand.firstLetter
              this.dataForm.sort = data.brand.sort
            }
          })
        }
      })
    },
    // 表单提交
    dataFormSubmit() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.$http({
            url: this.$http.adornUrl(`/commodity/brand/${!this.dataForm.id ? 'save' : 'update'}`),
            // url: `http://localhost:9090/commodity/brand/${!this.dataForm.id ? 'save' : 'update'}`,
            method: 'post',
            data: this.$http.adornData({
              'id': this.dataForm.id || undefined,
              'name': this.dataForm.name,
              'logo': this.dataForm.logo,
              'description': this.dataForm.description,
              'isshow': this.dataForm.isshow,
              'firstLetter': this.dataForm.firstLetter,
              'sort': this.dataForm.sort
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
