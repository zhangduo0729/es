<template>
  <div class="my-con">
    <el-form label-width="10px" size="small">
      <div class="inline-tip"><span>基础信息</span></div>
      <el-row :gutter="10">
        <el-col :span="6">
          <el-form-item>
            <el-select v-model="analyzer" placeholder="请选择或指定分词器" no-match-text="无匹配分词器"
                       allow-create filterable clearable default-first-option style="width: 255px;">
              <el-option value="standard" label="Standard"></el-option>
              <el-option value="ik_smart" label="IK_smart"></el-option>
              <el-option value="ik_max_word" label="IK_max_word"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item>
            <el-input v-model="text" placeholder="请输入待分词的字符串"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="5">
          <el-form-item>
            <el-button type="primary"  :loading="testing" @click="submitTest">测试</el-button>
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
  name: 'CustomerAnalyzer',
  data () {
    return {
      analyzer: '',
      text: '',
      testing: false,
      tableData: []
    }
  },
  methods: {
    submitTest () {
      console.log(this.analyzer, this.text)
      this.$ajax({
        url: 'http://localhost:8069/tools/index/customer/analyzer',
        method: 'post',
        data: {analyzer: this.analyzer, text: this.text}
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
    }
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
