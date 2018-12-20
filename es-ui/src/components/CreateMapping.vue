<template>
  <div class="my-con">
    <el-form :model="formData" ref="mapping" class="demo-dynamic" label-width="10px" size="small">
      <div class="inline-tip"><span>基础信息</span></div>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item prop="indexName" :rules="[{required: true, message: '请输入索引名称', trigger: 'blur'},
                        {pattern:/^[a-zA-Z_]([a-zA-Z0-9_-]){2,30}$/, message: '以字母开头，只能包含字符、数字和下划线', trigger: 'blur'}]">
            <el-tooltip class="item" effect="dark" content="索引名称以业务线简称开头" placement="top-start">
              <el-input v-model="formData.indexName" :clearable="true" placeholder="请输入索引名称"></el-input>
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
      <div class="inline-tip"><span>映射信息</span><span style="color: red;">(目前只支持一级嵌套)</span></div>
      <el-row :gutter="10" v-for="(it, index) in formData.dataList" :key="it.index" >
        <el-col :span="4.5">
          <el-form-item :prop="'dataList.'+ index +'.field'" inline-message="true"
                        :rules="[{required: true, message: '请输入字段名称', trigger: 'blur'},
                        {pattern:/^[a-zA-Z_-]([a-zA-Z0-9_-]){2,30}$/, message: '以字母开头，只能包含字符、数字和下划线', trigger: 'blur'}]">
            <el-input v-model="it.field" placeholder="Field名称"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="2"></el-col>
        <el-col :span="4.5">
          <el-form-item :prop="'dataList.'+ index +'.type'"
                        :rules="[{required: true, message: '请选择字段类型', trigger: 'change'}]">
            <el-select v-model="it.type" @change="changeNextSelect($event, it)">
              <el-option value="boolean" label="Boolean"></el-option>
              <el-option value="integer" label="Integer"></el-option>
              <el-option value="long" label="Long"></el-option>
              <el-option value="float" label="Float"></el-option>
              <el-option value="double" label="Double"></el-option>
              <el-option value="date" label="Date"></el-option>
              <el-option value="keyword" label="Keyword"></el-option>
              <el-option value="text" label="Text"></el-option>
              <el-option value="nested" label="Nested"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="2"></el-col>
        <el-col :span="4.5">
          <el-form-item v-if="it.analyzerShow" :prop="'dataList.'+ index +'.analyzer'"
                        :rules="[{required: !it.analyzerDisabled, message: '请选择分词类型', trigger: 'change'}]">
            <el-select v-model="it.analyzer" :disabled="it.analyzerDisabled">
              <el-option value="ik_smart" label="IK_smart"></el-option>
              <el-option value="ik_max_word" label="IK_max_word"></el-option>
              <el-option value="standard" label="Standard"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item v-if="it.formatShow" :prop="'dataList.'+ index +'.format'"
                        :rules="[{required: it.formatShow, message: '请选择时间格式', trigger: 'change'}]">
            <el-select v-model="it.format">
              <el-option value="yyyy-MM-dd" label="yyyy-MM-dd"></el-option>
              <el-option value="yyyy-MM-dd HH:mm:ss" label="yyyy-MM-dd HH:mm:ss"></el-option>
              <el-option value="epoch_millis" label="epoch_millis"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="2"></el-col>
        <el-col :span="4.5">
          <el-form-item>
            <el-button @click="addChildItem(index)" :disabled="it.disabled">增加子项</el-button>
            <el-button @click="removeItem(false, index)" type="danger" :disabled="deleteDisabled">删除</el-button>
          </el-form-item>
        </el-col>
        <div v-if="it.props.length > 0" class="content">
          <el-row :gutter="10" v-for="(child, childIndex) of it.props" :key="'child' + childIndex">
            <el-col :span="4.5">
              <el-form-item :prop="'dataList.'+index+ '.props.' + childIndex + '.field'"
                        :rules="[{required: true, message: '请输入字段名称', trigger: 'blur'},
                        {pattern:/^[a-zA-Z_-]([a-zA-Z0-9_-]){2,30}$/, message: '以字母开头，只能包含字符、数字和下划线', trigger: 'blur'}]">
                <el-input v-model="child.field" placeholder="Field名称"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="2"></el-col>
            <el-col :span="4.5">
              <el-form-item :prop="'dataList.'+ index +'.props.'+ childIndex +'.type'"
                            :rules="[{required: true, message: '请选择字段类型', trigger: 'blur'}]">
                <el-select v-model="child.type" @change="changeNextSelect($event, child)">
                  <el-option value="boolean" label="Boolean"></el-option>
                  <el-option value="integer" label="Integer"></el-option>
                  <el-option value="long" label="Long"></el-option>
                  <el-option value="float" label="Float"></el-option>
                  <el-option value="double" label="Double"></el-option>
                  <el-option value="date" label="Date"></el-option>
                  <el-option value="keyword" label="Keyword"></el-option>
                  <el-option value="text" label="Text"></el-option>
                  <!--<el-option value="nested" label="Nested"></el-option>-->
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="2"></el-col>
            <el-col :span="4.5">
              <el-form-item v-if="child.analyzerShow" :prop="'dataList.'+ index +'.props.'+ childIndex +'.analyzer'"
                            :rules="[{required: !child.analyzerDisabled, message: '请选择分词类型', trigger: 'change'}]">
                <el-select v-model="child.analyzer" :disabled="child.analyzerDisabled">
                  <el-option value="ik_smart" label="IK_smart"></el-option>
                  <el-option value="ik_max_word" label="IK_max_word"></el-option>
                  <el-option value="standard" label="Standard"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item v-if="child.formatShow" :prop="'dataList.'+ index +'.props.'+ childIndex +'.format'"
                            :rules="[{required: child.formatShow, message: '请选择时间格式', trigger: 'change'}]">
                <el-select v-model="child.format">
                  <el-option value="yyyy-MM-dd" label="yyyy-MM-dd"></el-option>
                  <el-option value="yyyy-MM-dd HH:mm:ss" label="yyyy-MM-dd HH:mm:ss"></el-option>
                  <el-option value="epoch_millis" label="epoch_millis"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="2"></el-col>
            <el-col :span="4.5">
              <el-form-item>
                <!--<el-button @click="addChildItem(childIndex)" :disabled="it.disabled">增加子项</el-button>-->
                <el-button size="small" type="danger" icon="el-icon-delete" circle @click="removeItem(true, index, childIndex)"></el-button>
              </el-form-item>
            </el-col>
          </el-row>
        </div>
      </el-row>
      <el-row>
        <el-col :span="12" :offset="6">
          <el-button size="medium" @click="addItem">增加Field</el-button>
          <el-button size="medium" type="primary" @click="submitData('mapping')" :loading="isLoading">提交</el-button>
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
      deleteDisabled: false,
      formData: {
        indexName: 'my_index_vv5',
        typeName: 'my_type_list',
        dataList: [
          {field: 'my_id', type: 'integer', analyzer: '', analyzerDisabled: true, analyzerShow: true, format: '', formatShow: false, disabled: true, props: []},
          {field: 'my_name', type: 'keyword', analyzer: '', analyzerDisabled: true, analyzerShow: true, format: '', formatShow: false, disabled: true, props: []},
          {
            field: 'my_birthday',
            type: 'date',
            analyzer: '',
            analyzerDisabled: true,
            analyzerShow: false,
            format: 'yyyy-MM-dd HH:mm:ss',
            formatShow: true,
            disabled: true,
            props: []
          },
          {
            field: 'my_address',
            type: 'nested',
            analyzer: '',
            analyzerDisabled: true,
            analyzerShow: true,
            format: '',
            formatShow: false,
            disabled: true,
            props: [
              {field: 'cityId', type: 'integer', analyzer: '', analyzerDisabled: true, analyzerShow: true, format: '', formatShow: false, disabled: true, props: []},
              {field: 'detail', type: 'text', analyzer: 'ik_max_word', analyzerDisabled: false, analyzerShow: true, format: '', formatShow: false, disabled: true, props: []}
            ]
          }
        ]
      }
    }
  },
  methods: {
    addChildItem (index) {
      let obj = {
        field: '',
        type: '',
        analyzer: '',
        analyzerDisabled: true,
        analyzerShow: true,
        format: '',
        formatShow: false,
        disabled: true,
        props: []
      }
      this.formData.dataList[index].props.push(obj)
    },
    addItem () {
      let obj = {
        field: '',
        type: '',
        analyzer: '',
        analyzerDisabled: true,
        analyzerShow: true,
        format: '',
        formatShow: false,
        disabled: true,
        props: []
      }
      this.formData.dataList.push(obj)
    },
    removeItem (isChild, index, childIndex) {
      if (!isChild) {
        let len = this.formData.dataList.length
        if (len > 1) {
          this.formData.dataList.splice(index, 1)
        }
      } else {
        this.formData.dataList[index].props.splice(childIndex, 1)
      }
    },
    changeNextSelect (val, obj) {
      if (val === 'text') {
        obj.analyzerDisabled = false
        obj.analyzerShow = true
        obj.formatShow = false
      } else {
        if (val === 'date') {
          obj.formatShow = true
          obj.analyzerDisabled = true
          obj.analyzerShow = false
        } else {
          obj.formatShow = false
          obj.analyzerDisabled = true
          obj.analyzerShow = true
        }
      }
      if (val === 'nested') {
        obj.disabled = false
      } else {
        obj.disabled = true
      }
    },
    submitData (mapping) {
      // let listObject = this.formData.dataList
      // console.log(JSON.stringify(listObject))
      this.$refs[mapping].validate((valid) => {
        if (valid) {
          // forEach循环不能通过return/return false等跳出循环，只能通过try{}catch{}的方式在满足条件时抛出一个错误
          // 然后再catch中捕获再进行其他逻辑处理，若不想执行下面的语句，则增加return即可。
          // try {
          //   this.formData.dataList.forEach((arr, index) => {
          //     if (arr.type === 'nested') {
          //       if (arr.props.length < 1) {
          //         this.$message.warning('请为字段' + arr.field + '设置子项')
          //         foreach.break = new Error('specify child item for nested field')
          //       }
          //     }
          //   })
          // } catch (e) {
          //   console.log(e.message)
          //   return
          // }
          for (let arr in this.formData.dataList) {
            if (arr.type === 'nested') {
              if (arr.props.length < 1) {
                this.$message.warning('请为字段' + arr.field + '设置子项')
                return
              }
            }
          }
          this.isLoading = true
          this.commitData(this.formData)
        }
      })
    },
    commitData: function (data) {
      this.$ajax({
        url: 'http://localhost:8069/tools/index/mappings',
        method: 'post',
        data: data,
        header: {'Content-Type': 'application/json'}
      })
        .then((response) => {
          this.isLoading = false
          // console.log(response)
          // console.log(JSON.stringify(response))
          if (response.status === 200 && response.data.result === true) {
            this.$message.success('索引创建成功!!!')
          } else {
            this.$message({
              message: response.data.status.statusReason,
              type: 'error',
              duration: 5000,
              showClose: true,
              center: true
            })
          }
        })
        .catch((err) => {
          this.isLoading = false
          this.$message.error(err)
        })
    }
  },
  watch: {
    'formData.dataList' () {
      let len = this.formData.dataList.length
      if (len > 1) {
        this.deleteDisabled = false
      } else {
        this.deleteDisabled = true
      }
    }
  }
}
</script>

<style scoped>
.my-con{
  width: 1100px;
  min-height: 500px;
}
.content{
  width: 860px;
  margin-top:40px;
  margin-left: 30px;
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
