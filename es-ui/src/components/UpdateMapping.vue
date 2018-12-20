<template>
  <div class="my-con">
    <el-form :model="formData" ref="myForm" label-width="10px" size="small">
      <div class="inline-tip"><span>基础信息</span></div>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item prop="indexName" :rules="[{required: true, message: '请输入索引名称', trigger: 'blur'},
                        {pattern:/^[a-zA-Z_]([a-zA-Z0-9_-]){2,30}$/, message: '以字母开头，只能包含字符、数字和下划线', trigger: 'blur'}]">
            <el-tooltip class="item" effect="dark" content="索引名称以业务线简称开头" placement="top-start">
              <el-input v-model="formData.indexName" auto-complete="on" :clearable="true" placeholder="请输入索引名称"></el-input>
            </el-tooltip>
          </el-form-item>
        </el-col>
        <el-col :span="4"></el-col>
        <el-col :span="8">
          <el-form-item prop="typeName" :rules="[{required: true, message: '请输入Type名称', trigger: 'blur'},
                        {pattern:/^[a-zA-Z_]([a-zA-Z0-9_-]){2,30}$/, message: '以字母开头，只能包含字符、数字和下划线', trigger: 'blur'}]">
            <el-input v-model="formData.typeName" :clearable="true" placeholder="请输入Type名称"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <div class="inline-tip"><span>映射信息</span><span style="color: red;">(支持增加字段和修改字段名称)</span></div>
      <el-row v-for="(it, index) of formData.dataList" :key="it.index">
          <el-col :span="4.5">
            <el-form-item :prop="'dataList.'+ index +'.oldField'"
                      :rules="[{required: true, message: '请输入原字段名称', trigger: 'blur'},
                      {pattern:/^[a-zA-Z_]([a-zA-Z0-9_-]){2,30}$/, message: '以字母开头，只能包含字符、数字和下划线', trigger: 'blur'}]">
              <el-input v-model="it.oldField" placeholder="请输入原字段名称"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item>
              <el-select v-model="it.oldType">
                <el-option value="boolean" label="Boolean"></el-option>
                <el-option value="integer" label="Integer"></el-option>
                <el-option value="long" label="Long"></el-option>
                <el-option value="float" label="Float"></el-option>
                <el-option value="double" label="Double"></el-option>
                <el-option value="date" label="Date"></el-option>
                <el-option value="keyword" label="Keyword"></el-option>
                <el-option value="text" label="Text"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="5">
            <el-form-item :prop="'dataList.'+ index +'.newField'"
                      :rules="[{required: true, message: '请输入字段名称，不修改请同原字段名称', trigger: 'blur'},
                      {pattern:/^[a-zA-Z_]([a-zA-Z0-9_-]){2,30}$/, message: '以字母开头，只能包含字符、数字和下划线', trigger: 'blur'}]">
              <el-input v-model="it.newField" placeholder="请输入要改成的字段名称，不修改请同原字段名称"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item :prop="'dataList.'+ index +'.newType'"
                  :rules="[{required: true, message: '请选择字段类型', trigger: 'change'}]">
              <el-select v-model="it.newType">
                <el-option value="boolean" label="Boolean"></el-option>
                <el-option value="integer" label="Integer"></el-option>
                <el-option value="long" label="Long"></el-option>
                <el-option value="float" label="Float"></el-option>
                <el-option value="double" label="Double"></el-option>
                <el-option value="date" label="Date"></el-option>
                <el-option value="keyword" label="Keyword"></el-option>
                <el-option value="text" label="Text"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="1">
            <el-form-item>
              <el-button type="danger" circle icon="el-icon-delete"
                         :disabled="deleteDisabled" size="small" @click="removeItem(index)"></el-button>
            </el-form-item>
          </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" :offset="6">
          <el-button size="medium" @click="addItem">增加Field</el-button>
          <el-button size="medium" type="primary" @click="submitData('myForm')" :loading="isLoading">提交</el-button>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>

<script>
export default {
  data () {
    return {
      isLoading: false,
      deleteDisabled: true,
      formData: {
        indexName: 'my_index_v5',
        typeName: 'my_index',
        dataList: [
          {oldField: 'driverPhone', oldType: '', newField: 'driverPhone', newType: 'keyword'}
        ]
      }
    }
  },
  methods: {
    addItem () {
      let obj = {oldField: '', oldType: '', newField: '', newType: ''}
      this.formData.dataList.push(obj)
      this.deleteDisabled = false
    },
    removeItem (index) {
      if (this.formData.dataList.length > 1) {
        this.formData.dataList.splice(index, 1)
        if (this.formData.dataList.length === 1) {
          this.deleteDisabled = true
        }
      }
    },
    submitData (myForm) {
      this.$refs[myForm].validate((validate) => {
        if (validate) {
          let formData = this.formData
          // console.log(JSON.stringify(formData))
          this.isLoading = true
          this.commitData(formData)
        }
      })
    },
    commitData: function (data) {
      this.$ajax({
        url: 'http://localhost:8069/tools/index/mappings',
        method: 'PUT',
        data: data,
        header: {'Content-Type': 'application/json'}
      })
        .then((response) => {
          // console.log(response)
          this.isLoading = false
          if (response.status === 200 && response.data.status.statusCode === 0) {
            let result = response.data.result
            if (result.getMappingResult === false) {
              this.$message.error('get mapping error!!!')
              return false
            }
            if (result.createTmpIndexResult === false) {
              this.$message.error('create tmp mapping error!!!')
              return false
            }
            let r = result.reIndexResult
            let html = `
                <ul class="cus-ul" style="margin: 0;padding-left: 10px; list-style: none; font-size: 17px;">
                  <li>总耗时：<span style="font-weight: bold;">${r.took} ms</span></li>
                  <li>数据条数：<span style="font-weight: bold;">${r.total}</span></li>
                  <li>成功条数：<span style="color: green;font-weight: bold;">${r.created}</span></li>
                `
            if (r.failures === 0) {
              let suc = html + `</ul>`
              this.$alert(suc, '修改映射结果如下:', {
                dangerouslyUseHTMLString: true,
                type: 'success',
                showClose: true,
                showConfirmButton: false,
                closeOnPressEscape: true,
                callback (action, instance) {
                  console.log(action)
                }
              })
            } else {
              let err = html + `
                    <li>错误条数：<span style="color: red;font-weight: bold;">${r.failures}</span></li>
                    <li>错误信息：<span style="color: red;font-weight: bold;">${r.cause}</span></li>
                  </ul>
                 `
              this.$alert(err, '修改映射结果如下:', {
                dangerouslyUseHTMLString: true,
                type: 'warning',
                showClose: true,
                showConfirmButton: false,
                closeOnPressEscape: true,
                callback (action, instance) {
                  console.log(action)
                }
              })
            }
          } else {
            this.$alert(response.data.status.statusReason, '修改映射结果如下', {
              dangerouslyUseHTMLString: true,
              type: 'error',
              showClose: true,
              showConfirmButton: false,
              closeOnPressEscape: true,
              callback (action, instance) {
                console.log(action)
              }
            })
          }
        })
        .catch((error) => {
          this.isLoading = false
          this.$message.error(error)
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
.inline-tip:nth-child(3){
  margin-top: 20px;
}
.cus-ul{
  margin: 0;padding-left: 10px; list-style: none; font-size: 17px;
}
</style>
