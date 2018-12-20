import Vue from 'vue'
import Router from 'vue-router'
// import HelloWorld from '@/components/HelloWorld'
import CreateMapping from '@/components/CreateMapping'
import UpdateMapping from '@/components/UpdateMapping'
import PlatformDesc from '@/components/PlatformDesc'
import SpecifyAnalyzer from '@/components/SpecifyAnalyzer'
import CustomerAnalyzer from '@/components/CustomerAnalyzer'
import DeleteIndex from '@/components/DeleteIndex'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'PlatformDesc',
      component: PlatformDesc,
      desc: '平台说明'
    },
    {
      path: '/specify-analyzer',
      name: 'SpecifyAnalyzer',
      component: SpecifyAnalyzer,
      desc: '指定字段的分词'
    },
    {
      path: '/customer-analyzer',
      name: 'CustomerAnalyzer',
      component: CustomerAnalyzer,
      desc: '自定义分词'
    },
    {
      path: '/delete-index',
      name: 'DeleteIndex',
      component: DeleteIndex,
      desc: '删除索引'
    },
    {
      path: '/create-mapping',
      name: 'CreateMapping',
      component: CreateMapping,
      desc: '创建映射'
    },
    {
      path: '/update-mapping',
      name: 'UpdateMapping',
      component: UpdateMapping,
      desc: '修改映射'
    },
    {
      path: '/platform-desc',
      name: 'PlatformDesc',
      component: PlatformDesc,
      desc: '平台说明'
    }
  ]
})
