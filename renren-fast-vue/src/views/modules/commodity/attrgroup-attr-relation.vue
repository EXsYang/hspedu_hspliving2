<template>
  <div>
    <el-dialog :close-on-click-modal="false" :visible.sync="visible" @closed="dialogClose">
      <el-dialog width="40%" title="选择属性" :visible.sync="innerVisible" append-to-body>
        <div>
          <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
            <el-form-item>
              <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
            </el-form-item>
            <el-form-item>
              <el-button @click="getDataList()">查询</el-button>
            </el-form-item>
          </el-form>
          <el-table
            :data="dataList"
            border
            v-loading="dataListLoading"
            @selection-change="innerSelectionChangeHandle"
            style="width: 100%;"
          >
            <el-table-column type="selection" header-align="center" align="center"></el-table-column>
            <el-table-column prop="attrId" header-align="center" align="center" label="属性id"></el-table-column>
            <el-table-column prop="attrName" header-align="center" align="center" label="属性名"></el-table-column>
            <el-table-column prop="icon" header-align="center" align="center" label="属性图标"></el-table-column>
            <el-table-column prop="valueSelect" header-align="center" align="center" label="可选值列表"></el-table-column>
          </el-table>
          <el-pagination
            @size-change="sizeChangeHandle"
            @current-change="currentChangeHandle"
            :current-page="pageIndex"
            :page-sizes="[10, 20, 50, 100]"
            :page-size="pageSize"
            :total="totalPage"
            layout="total, sizes, prev, pager, next, jumper"
          ></el-pagination>
        </div>
        <div slot="footer" class="dialog-footer">
          <el-button @click="innerVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitAddRealtion">确认新增</el-button>
        </div>
      </el-dialog>
      <el-row>
        <el-col :span="24">
          <el-button type="primary" @click="addRelation">新建关联</el-button>
          <el-button
            type="danger"
            @click="batchDeleteRelation"
            :disabled="dataListSelections.length <= 0">批量删除</el-button>
          <!-- 通过el-table 绑定的relationAttrs 展示该属性组id 关联的所有的基本属性信息 -->
          <el-table
            :data="relationAttrs"
            style="width: 100%"
            @selection-change="selectionChangeHandle"
            border
          >
            <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
            <el-table-column prop="attrId" label="attrId#"></el-table-column>
            <el-table-column prop="attrName" label="属性名"></el-table-column>
            <el-table-column prop="valueSelect" label="可选值">
              <template slot-scope="scope">
                <el-tooltip placement="top">
                  <div slot="content">
                    <span v-for="(i,index) in scope.row.valueSelect.split(';')" :key="index">
                      {{i}}
                      <br />
                    </span>
                  </div>
                  <el-tag>{{scope.row.valueSelect.split(";")[0]+" ..."}}</el-tag>
                </el-tooltip>
              </template>
            </el-table-column>
            <el-table-column fixed="right" header-align="center" align="center" label="操作">
              <template slot-scope="scope">
                <el-button type="text" size="small" @click="relationRemove(scope.row.attrId)">移除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-row>
    </el-dialog>
  </div>
</template>

<script>

export default {
  //import引入的组件需要注入到对象中才能使用
  components: {},
  props: {},
  data() {
    //这里存放数据
    return {
      attrGroupId: 0,
      visible: false,
      innerVisible: false,
      relationAttrs: [],
      dataListSelections: [],
      dataForm: {
        key: ""
      },
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      innerdataListSelections: []
    };
  },
  //计算属性类似于data概念
  computed: {},
  //监控data中的数据变化
  watch: {},
  //方法集合
  methods: {
    selectionChangeHandle(val) {
      this.dataListSelections = val;
    },
    innerSelectionChangeHandle(val) {
      this.innerdataListSelections = val;
    },
    addRelation() {
      this.getDataList();
      this.innerVisible = true;
    },
    // 批量删除属性组和商品属性(基本属性)的关联关系
    batchDeleteRelation(val) {
      let postData = [];
      this.dataListSelections.forEach(item => {
        postData.push({ attrId: item.attrId, attrGroupId: this.attrGroupId });
      });
      this.$http({
        url: this.$http.adornUrl("/commodity/attrgroup/attr/relation/delete"),
        method: "post",
        data: this.$http.adornData(postData, false)
      }).then(({ data }) => {
        if (data.code == 0) {
          this.$message({ type: "success", message: "批量删除成功" });
          // 刷新当前属性组列表 即到后端拿回已经和本属性组this.attrGroupId 关联上的基本属性列表
          this.init(this.attrGroupId);
        } else {
          this.$message({ type: "error", message: data.msg });
        }
      });
    },
    //移除关联
    relationRemove(attrId) {
      // 查看前端拿到的数据
      console.log("attrId-> ",attrId,"  attrGroupId-> ",this.attrGroupId)


      // 定义一个数组叫data,也就是说下面往后端发送数据的形式是json数组的形式
      // ，而不是json对象，后端需要使用数组进行接收数据
      // 这里的数组封装的是后端Javabean对象的属性的值，因此在后端可以使用对应的对象数组进行接收
      // 即 =》 public R deleteRelation(@RequestBody AttrAttrgroupRelationEntity[] attrAttrgroupRelationEntities){
      /**
       * 发送给后端的数据形式data数组如下:
        [
           {
            "attrId": 1000,
            "attrGroupId": 1000
           },
           {
            "attrId": 2000,
            "attrGroupId": 2000
           }
       ]
       */
      let data = [];
      // push() 方法将新的元素添加到数组的末尾，并返回新数组的长度。
      data.push({ attrId, attrGroupId: this.attrGroupId });

      this.$http({
        url: this.$http.adornUrl("/commodity/attrgroup/attr/relation/delete"),
        method: "post",
        data: this.$http.adornData(data, false)
      }).then(({ data }) => {
        if (data.code == 0) {
          this.$message({ type: "success", message: "删除成功" });
          // 刷新当前属性组列表 即到后端拿回已经和本属性组this.attrGroupId 关联上的基本属性列表
          this.init(this.attrGroupId);
        } else {
          this.$message({ type: "error", message: data.msg });
        }
      });
    },
    submitAddRealtion() {
      this.innerVisible = false;
      //准备数据
      console.log("准备新增的数据", this.innerdataListSelections);
      console.log("当前点击的关联的属性组id this.attrGroupId=> ", this.attrGroupId);
      if (this.innerdataListSelections.length > 0) {
        let postData = [];
        this.innerdataListSelections.forEach(item => {
          postData.push({ attrId: item.attrId, attrGroupId: this.attrGroupId });
        });
        this.$http({
          url: this.$http.adornUrl("/commodity/attrgroup/attr/relation"),
          method: "post",
          data: this.$http.adornData(postData, false)
        }).then(({ data }) => {
          if (data.code == 0) {
            this.$message({ type: "success", message: "新增关联成功" });
          }
          this.$emit("refreshData");
          //
          this.init(this.attrGroupId);
        });
      } else {
      }
    },
    // 属性组页面点击'关联'按钮后会触发init方法 并将当前行的属性组id传递过来
    // 该方法的作用为:刷新点击关联后显示的dialog对话框中的 和当前属性组id 已经关联的基本属性的列表
    init(id) {
      console.log("属性组id attrgroupId=> ", id)
      // 将 id 赋值给 this.attrGroupId；如果 id 不存在或为 "falsy" 值，则默认赋值为 0
      this.attrGroupId = id || 0;
      this.visible = true;
      this.$http({
        //发出请求，返回当前属性组，已经关联的商品属性(基本属性)的数据
        // url: http://localhost:5050/api/commodity/attrgroup/1/attr/relation
        url: this.$http.adornUrl(
          "/commodity/attrgroup/" + this.attrGroupId + "/attr/relation"
        ),
        method: "get",
        params: this.$http.adornParams({})
      }).then(({ data }) => {
        this.relationAttrs = data.data;
      });
    },
    dialogClose() {},

    //========
    // 获取数据列表
    getDataList() {
      this.dataListLoading = true;
      this.$http({
        url: this.$http.adornUrl(
          "/commodity/attrgroup/" + this.attrGroupId + "/attr/allowrelation"
        ),
        method: "get",
        params: this.$http.adornParams({
          page: this.pageIndex,
          limit: this.pageSize,
          key: this.dataForm.key
        })
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.dataList = data.page.list;
          this.totalPage = data.page.totalCount;
        } else {
          this.dataList = [];
          this.totalPage = 0;
        }
        this.dataListLoading = false;
      });
    },
    // 每页数
    sizeChangeHandle(val) {
      this.pageSize = val;
      this.pageIndex = 1;
      this.getDataList();
    },
    // 当前页
    currentChangeHandle(val) {
      this.pageIndex = val;
      this.getDataList();
    }
  }
};
</script>
<style scoped>
</style>
