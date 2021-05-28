<template>
  <div class="card-list" ref="content">
    <a-list
        :grid="{gutter: 24, lg: 3, md: 2, sm: 1, xs: 1}"
        :dataSource="data"
    >
      <a-list-item slot="renderItem" slot-scope="item">
        <a-card :hoverable="true" @click="jump(item.id)">
          <a-card-meta>
            <div style="margin-bottom: 3px" slot="title">{{ item.title }}</div>
            <div class="meta-content" slot="description">{{ item.desc }}</div>
          </a-card-meta>
        </a-card>
      </a-list-item>
    </a-list>
  </div>
</template>

<script>
const data = [
  {
    id: 1,
    title: '培养目标的设定',
    desc: '为五育并举提供培养方案'
  },
  {
    id: 2,
    title: '问卷平台与数据采集',
    desc: '为五育并举收集数据'
  },
  {
    id: 3,
    title: '在线测验平台',
    desc: '为师生提供便捷的线上测验服务'
  },
  {
    id: 4,
    title: '学习过程分析',
    desc: '为老师提供便捷的五育成绩分析平台'
  },
];

export default {
  name: "Show",
  data() {
    return {
      data,
    }
  },
  methods: {
    jump(id) {
      switch (id) {
        case 1: this.jumpPeiYang()
              break
        case 2: this.jumpWengjuan()
              break
        case 3: this.jumpPeiYang()
              break
        case 4: this.jumpFenxi()
              break
      }
    },
    jumpPeiYang() {
      const url = "http://test.uwis.cn:9495/examApi?username=" + this.getUsername() + "&password=" + this.getPassword();
      window.open(url)
    },
    jumpWengjuan() {
      const url = "http://test.uwis.cn:9495/info?username=" + this.getUsername() + "&password=" + this.getPassword();
      window.open(url)
    },
    jumpFenxi() {
      const url = "http://test.uwis.cn:9495/process?username=" + this.getUsername() + "&password=" + this.getPassword();
      window.open(url)
    },
    getUsername() {
      console.log("username")
      return "test"
    },
    getPassword() {
      return "123456"
    }
  }
}
</script>

<style scoped>

</style>
