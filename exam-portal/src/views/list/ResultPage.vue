<template>
  <div id="container">
    <a-result
        :status="status"
        :title="title"
        :sub-title="subTitle"
    >
      <template #extra>
        <a-button key="console" type="primary" @click="jump()">
          返回
        </a-button>
      </template>
    </a-result>
    <div id="c1"></div>
  </div>
</template>

<script>
import { Liquid, measureTextWidth } from '@antv/g2plot';
import { getExamPercentage, getRecord } from '../../api/exam'

const resultEnum = ['success', 'error']

export default {
  name: 'ResultPage',
  components: { },
  data () {
    return {
      recordId: "",
      record: { examJoinScore: 0 },
      P: 0.0,
      title: "恭喜你的得分为100分",
      subTitle: "继续加油！！！",
      status: "success"
    }
  },
  mounted () {
    this.loadRecord()
  },
  computed: {
    localIsSuccess: function () {
      return this.type === resultEnum[0]
    }
  },
  methods: {
    loadRecord() {
      this.recordId = this.$route.query.record.examRecordId
      console.log(this.recordId)
      getRecord(this.recordId).then(res => {
        if (res.code === 0) {
          this.record = res.data
          console.log(this.record)
          this.loadP()
          this.title = "恭喜你的得分为" + this.record.examJoinScore +"分"
        } else {
          this.$notification.error({
            message: '获取record失败',
            description: res.msg
          })
        }
      })
    },
    loadG2() {
      const liquidPlot = new Liquid(document.getElementById('c1'), {
        percent: this.P,
        radius: 0.8,
        statistic: {
          title: {
            formatter: () => '成绩',
            style: ({ percent }) => ({
              fill: percent > 0.65 ? 'white' : 'rgba(44,53,66,0.85)',
            }),
          },
          content: {
            style: ({ percent }) => ({
              fontSize: 60,
              lineHeight: 1,
              fill: percent > 0.65 ? 'white' : 'rgba(44,53,66,0.85)',
            }),
            customHtml: (container, view, { percent }) => {
              const { width, height } = container.getBoundingClientRect();
              const d = Math.sqrt(Math.pow(width / 2, 2) + Math.pow(height / 2, 2));
              const text = `超过 ${(percent * 100).toFixed(0)}%`;
              const textWidth = measureTextWidth(text, { fontSize: 60 });
              const scale = Math.min(d / textWidth, 1);
              return `<div style="width:${d}px;display:flex;align-items:center;justify-content:center;font-size:${scale}em;line-height:${
                  scale <= 1 ? 1 : 'inherit'
              }">${text}</div>`;
            },
          },
        },
        liquidStyle: ({ percent }) => {
          return {
            fill: percent > 0.45 ? '#5B8FF9' : '#FAAD14',
            stroke: percent > 0.45 ? '#5B8FF9' : '#FAAD14',
          };
        },
        color: () => '#5B8FF9',
      });
      liquidPlot.render();

      let data = 0;
      const interval = setInterval(() => {
        data += Math.min(Math.random() * 0.1, 0.05);
        if (data > this.P - 0.05) {
          data = this.P
          liquidPlot.changeData(data);
        }
        if (data < this.P) {
          liquidPlot.changeData(data);
        } else {
          clearInterval(interval);
        }
      }, 200);
    },
    loadP() {
      getExamPercentage(this.recordId).then(res => {
        if (res.code === 0) {
          this.P = res.data
          // this.P /= 100
          console.log(this.P)
          this.loadG2()
        } else {
          this.$notification.error({
            message: '获取record失败',
            description: res.msg
          })
        }
      })
    },
    jump() {
      this.$router.push('/list/exam-record-list')
    }
  }
}
</script>

<style lang="less" scoped>
.result {
  text-align: center;
  width: 72%;
  margin: 0 auto;
  padding: 24px 0 8px;

.icon {
  font-size: 72px;
  line-height: 72px;
  margin-bottom: 24px;
}
.success {
  color: #52c41a;
}
.error {
  color: red;
}
.title {
  font-size: 24px;
  color: rgba(0, 0, 0, .85);
  font-weight: 500;
  line-height: 32px;
  margin-bottom: 16px;
}
.description {
  font-size: 14px;
  line-height: 22px;
  color: rgba(0, 0, 0, 0.45);
  margin-bottom: 24px;
}
.extra {
  background: #fafafa;
  padding: 24px 40px;
  border-radius: 2px;
  text-align: left;
}
.action {
  margin-top: 32px;
}
}

.mobile {
.result {
  width: 100%;
  margin: 0 auto;
  padding: unset;
}
}
</style>
