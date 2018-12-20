<template>
  <div class="my-con">
    <el-form size="small" label-width="10px">
      <div class="inline-tip"><span>批量<span style="font-weight: bold; color: red;">删除</span>指定的索引</span></div>
      <el-row :gutter="10">
        <el-col :span="17">
          <el-form-item>
            <el-select multiple placeholder="请选择" filterable :multiple-limit=5 v-model="indexName" style="width: 760px;">
              <el-option v-for="(it, index) of indexList" :key="index" :value="it" :label="it"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="4">
          <el-form-item>
            <el-button type="primary" @click="deleteIndex">删除</el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>

<script>
export default {
  name: 'DeleteIndex',
  data () {
    return {
      indexName: ['my_index_vv6', 'my_index_v55_tmp'],
      indexList: []
    }
  },
  methods: {
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
    deleteIndex () {
      this.$ajax({
        url: 'http://localhost:8069/tools/index/multiple',
        method: 'delete',
        data: JSON.stringify({indexNames: this.indexName}),
        contentType: 'application/json;charset=UTF-8'
      })
        .then(response => {
          if (response.status === 200 && response.data.status.statusCode === 0 && response.data.result === true) {
            this.$message.success('删除成功!!!')
          } else {
            this.$message({
              type: 'error',
              message: response.data.status.statusReason,
              duration: 5000,
              showClose: true,
              center: true
            })
          }
        })
        .catch(err => {
          this.$message.error(err)
        })
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
.inline-tip:nth-child(3){
  margin-top: 20px;
}
</style>
