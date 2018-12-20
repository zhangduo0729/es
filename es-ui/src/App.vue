<template>
    <el-container>
      <el-header class="header" style="height: 80px;">
        <span>ElasticSearch常用操作平台</span>
        <div class="header-info">
          <el-dropdown @command="handleCommand" trigger="click">
            <span class="el-dropdown-link">
              游客<i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="a">退出登录</el-dropdown-item>
              <el-dropdown-item command="b">个人信息</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </el-header>
      <el-container>
        <el-aside class="menu-aside" style="width: 205px; border-right: solid 1px #e6e6e6;">
          <el-menu default-active="1-4-1" class="el-menu-vertical-demo" :router="true">
            <el-submenu index="1">
              <template slot="title">
                <i class="el-icon-menu"></i>
                <span slot="title">测试分词</span>
              </template>
              <el-menu-item-group>
                <el-menu-item index="/specify-analyzer">指定字段的分词</el-menu-item>
                <el-menu-item index="/customer-analyzer">自定义分词</el-menu-item>
              </el-menu-item-group>
            </el-submenu>
            <el-menu-item index="2">
              <i class="el-icon-search"></i>
              <span slot="title">索引查询</span>
            </el-menu-item>
            <el-menu-item index="/delete-index">
              <i class="el-icon-search"></i>
              <span slot="title">删除索引</span>
            </el-menu-item>
            <el-menu-item index="/create-mapping">
              <i class="el-icon-tickets"></i>
              <span slot="title">创建映射</span>
            </el-menu-item>
            <el-menu-item index="/update-mapping">
              <i class="el-icon-tickets"></i>
              <span slot="title">修改映射</span>
            </el-menu-item>
            <el-menu-item index="platform-desc">
              <i class="el-icon-tickets"></i>
              <span slot="title">平台说明</span>
            </el-menu-item>
          </el-menu>
        </el-aside>
        <el-main class="main-content">
          <router-view/>
        </el-main>
      </el-container>
    </el-container>
</template>

<script>
export default {
  name: 'App',
  methods: {
    handleCommand: function (flag) {
      this.$message.info(flag)
    }
  }
}
</script>

<style>
body{
  margin: 0;
  padding: 0;
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}
.header{
  height: 80px;
  line-height: 80px;
  font-family: "Microsoft YaHei","微软雅黑";
  font-size: 28px;
  background-color: rgb(64, 158, 255);
  color: white;
}
.header-info{
  float: right;
  width: 50%;
  text-align: right;
  padding-right: 50px;
}
.el-menu{
  border-right: 0;
}
.el-dropdown-link {
  cursor: pointer;
  color: white;
}
.menu-aside{
  min-height: 340px;
}
</style>
