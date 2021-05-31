<template>
  <a-modal title="考试分析" :width="640" :visible="visible" :confirmLoading="confirmLoading" @cancel="handleCancel" destroyOnClose>
    <a-spin :spinning="confirmLoading">
      <div id="pie"></div>
    </a-spin>
    <template slot="footer">
      <a-button key="cancel" @click="handleCancel">关闭</a-button>
    </template>
  </a-modal>
</template>

<script>
import { Pie } from '@antv/g2plot';
import { getExamAnal } from '../../../api/exam'

export default {
  name: 'ExamPieModal',
  data () {
    return {
      pie_data: [],
      visible: false,
      confirmLoading: false
    }
  },
  methods: {
    getG2() {
      const data = this.pie_data
      const piePlot = new Pie('pie', {
        appendPadding: 10,
        data,
        angleField: 'value',
        colorField: 'type',
        radius: 1,
        innerRadius: 0.64,
        meta: {
          value: {
            formatter: (v) => `${v} 条记录`,
          },
        },
        label: {
          type: 'inner',
          offset: '-50%',
          autoRotate: false,
          style: { textAlign: 'center' },
          formatter: ({ percent }) => `${(percent * 100).toFixed(0)}%`,
        },
        statistic: {
          title: {
            offsetY: -8,
          },
          content: {
            offsetY: -4,
          },
        },
        // 添加 中心统计文本 交互
        interactions: [
          { type: 'element-selected' },
          { type: 'element-active' },
          {
            type: 'pie-statistic-active',
            cfg: {
              start: [
                { trigger: 'element:mouseenter', action: 'pie-statistic:change' },
                { trigger: 'legend-item:mouseenter', action: 'pie-statistic:change' },
              ],
              end: [
                { trigger: 'element:mouseleave', action: 'pie-statistic:reset' },
                { trigger: 'legend-item:mouseleave', action: 'pie-statistic:reset' },
              ],
            },
          },
        ],
      });

      piePlot.render();

    },
    anal (examId) {
      this.visible = true
      // 把当前的记录赋值到data中的变量
      // 从后端数据获取考试列表，适配前端卡片
      console.log("开始获取记录" + examId)
      getExamAnal(examId).then(res => {
        this.pie_data = res.data

        console.log(this.pie_data)

        this.getG2()
      })
    },
    handleCancel () {
      // clear form & currentStep
      this.visible = false
    },
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
