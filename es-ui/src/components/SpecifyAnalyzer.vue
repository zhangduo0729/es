<template>
  <div class="my-con">
    <el-form label-width="10px" size="small">
      <div class="inline-tip"><span>基础信息</span></div>
      <el-row :gutter="10">
        <el-col :span="6">
          <el-form-item>
            <el-select v-model="index" placeholder="请选择索引" no-match-text="无匹配索引" :remote-method="remoteMethod"
                       remote filterable clearable :loading="loading" style="width: 255px;">
              <el-option v-for="it of options" :key="it" :value="it" :label="it"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="5">
          <el-form-item>
            <el-input v-model="field" placeholder="请输入要使用其分词器的字段"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item>
            <el-input v-model="text" placeholder="请输入待分词的字符串"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="5">
          <el-form-item>
            <el-button type="primary" :loading="testing" @click="submitTest">测试</el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div class="inline-tip"><span>分词结果</span><span style="color: red;"></span></div>
    <div style="margin-left: 10px;">
      <el-table :data="tableData" stripe style="width: 76%">
        <el-table-column prop="term" label="Token" width="180"></el-table-column>
        <el-table-column prop="startOffset" label="Start_Offset" width="180"></el-table-column>
        <el-table-column prop="endOffset" label="End_Offset"></el-table-column>
        <el-table-column prop="type" label="Type"></el-table-column>
        <el-table-column prop="position" label="Position"></el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SpecifyAnalyzer',
  data () {
    return {
      index: '',
      field: '',
      text: '',
      testing: false,
      loading: false,
      options: [],
      indexList: [],
      tableData: [
        // {
        //   'term': '常用',
        //   'startOffset': 0,
        //   'endOffset': 2,
        //   'type': 'CN_WORD',
        //   'position': 0
        // }
      ]
    }
  },
  methods: {
    submitTest () {
      console.log(this.index, this.field, this.text)
      this.$ajax({
        url: 'http://localhost:8069/tools/index/specify/analyzer',
        method: 'post',
        data: {index: this.index, field: this.field, text: this.text}
      })
        .then(response => {
          // console.log(response)
          if (response.status === 200 && response.data.status.statusCode === 0) {
            this.tableData = response.data.result
          } else {
            this.$message.error(response.data.status.statusReason)
          }
        })
        .catch(err => {
          this.$message.error(err)
        })
    },
    getIndexName () {
      this.$ajax({url: 'http://localhost:8069/tools/index/all', method: 'get'})
        .then(response => {
          if (response.status === 200 && response.data.status.statusCode === 0) {
            this.indexList = response.data.result
          } else {
            this.$message.error(response.data.status.statusReason)
          }
        })
        .catch(err => {
          this.$message.error(err)
        })
    },
    remoteMethod (query) {
      // console.log(this.indexList)
      if (query !== '') {
        this.loading = true
        setTimeout(() => {
          this.loading = false
          this.options = this.indexList.filter(item => {
            return item.toLowerCase().indexOf(query.toLowerCase()) > -1
          })
        }, 300)
      } else {
        this.options = []
      }
    }
  },
  mounted () {
    this.getIndexName()
  }
}
</script>

<style scoped>
.my-con{
  width: 1100px;
  min-height: 500px;
}
.inline-tip{
  color: #C0C4CC;
  margin-left: 10px;
  margin-bottom: 15px;
  padding-bottom: 5px;
  border-bottom: 1px solid #C0C4CC;
  width: 75.5%;
  font-size: 17px;
}
</style>
