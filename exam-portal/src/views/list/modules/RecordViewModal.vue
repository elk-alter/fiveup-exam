<template>
  <a-modal title="题目信息" :width="640" :visible="visible" :confirmLoading="confirmLoading" @cancel="handleCancel">
    <a-spin :spinning="confirmLoading">
      <a-card style="margin-top: 24px" :bordered="false" title="参加过的考试">
        <div slot="extra">
          <a-input-search style="margin-left: 16px; width: 272px;"/>
        </div>
        <a-list size="large">
          <a-list-item :key="index" v-for="(item, index) in data">
            <a-list-item-meta :description="item.examRecord.examJoinDate">
              <a slot="title">{{ item.user.userUsername }}</a>
            </a-list-item-meta>
            <div slot="actions">
              <a @click="viewExamRecordDetail(item.examRecord)">查看考试详情</a>
            </div>
            <div class="list-content-item">
              <span>分数</span>
              <p>{{ item.examRecord.examJoinScore }}</p>
            </div>
          </a-list-item>
        </a-list>

      </a-card>
    </a-spin>
    <template slot="footer">
      <a-button key="cancel" @click="handleCancel">关闭</a-button>
    </template>
  </a-modal>
</template>

<script>
import HeadInfo from '../../../components/tools/HeadInfo'
import { getExamRecordListByExam } from '@/api/exam'

export default {
  name: 'RecordViewModal',
  components: {
    HeadInfo
  },
  data () {
    return {
      data: {},
      visible: false,
      confirmLoading: false
    }
  },
  methods: {
    /**
     * 根据考试记录的id拿到本次考试的详情并查看
     * @param record 考试详情的记录
     */
    viewExamRecordDetail (record) {
      // 直接跳到参加考试的页面，查看所有题目的详细情况
      const routeUrl = this.$router.resolve({
        path: `/exam/record/${record.examId}/${record.examRecordId}`
      })
      // 和点击考试卡片效果一样，跳转到考试页面，里面有所有题目的情况，相当于就是详情了
      window.open(routeUrl.href, '_blank')
    },
    edit (examId) {
      this.visible = true
      // 把当前的记录赋值到data中的变量
      // 从后端数据获取考试列表，适配前端卡片
      console.log("开始获取记录" + examId)
      getExamRecordListByExam(examId).then(res => {
        if (res.code === 0) {
          this.data = res.data
        } else {
          this.$notification.error({
            message: '获取考试记录失败',
            description: res.msg
          })
        }
      }).catch(err => {
        // 失败就弹出警告消息
        this.$notification.error({
          message: '获取考试记录失败',
          description: err.message
        })
      })
    },
    handleCancel () {
      // clear form & currentStep
      this.visible = false
    }
  },
  mounted () {
  }
}
</script>

<style lang="less" scoped>
.ant-avatar-lg {
  width: 48px;
  height: 48px;
  line-height: 48px;
}

.list-content-item {
  color: rgba(0, 0, 0, .45);
  display: inline-block;
  vertical-align: middle;
  font-size: 14px;
  margin-left: 40px;

span {
  line-height: 20px;
}

p {
  margin-top: 4px;
  margin-bottom: 0;
  line-height: 22px;
}
}
</style>
