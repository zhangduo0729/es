<template>
  <div class="main">
    <p>本平台申明如下：</p>
    <ol class="description">
      <li>只包含常用的ES操作API，便于没有ES基础的童靴操作ES;</li>
      <li>只针对目前业务线经常用到的ES操作提供了便利，不包含复杂的API操作;</li>
      <li>只包含部分工具API;</li>
      <li>目前只有两个功能，后续会继续完善更多功能;</li>
      <li>有想增加功能的需求可以提到我这里;</li>
      <li>欢迎各位提出更好的建议共同建设此平台;</li>
    </ol>
  </div>
</template>

<script>
export default {
  name: 'PlatformDesc'
}
</script>

<style scoped>
.main{
  font-size: 18px;
}
.description{
  line-height: 45px;
}
</style>
