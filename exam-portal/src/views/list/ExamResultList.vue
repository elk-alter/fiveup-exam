<template>
  <a-card :bordered="false">
    <div id="toolbar">
      <a-button type="primary" icon="reload" @click="loadAll()">刷新</a-button>
    </div>
    <BootstrapTable
        ref="table"
        :columns="columns"
        :data="tableData"
        :options="options"
    />
    <!-- 这里的详情需要传进去  -->
    <record-view-modal ref="recordViewModal" @ok="handleOk" />
    <exam-pie-modal ref="showPieModal" @ok="handleOk" />
  </a-card>
</template>

<script>
import '@/plugins/bootstrap-table'
import { getExamAll } from '@/api/exam'
import RecordViewModal from './modules/RecordViewModal'
import ExamPieModal from './modules/ExamPieModal'

export default {
  name: 'ExamTableList',
  components: {
    ExamPieModal,
    RecordViewModal
  },
  data () {
    const that = this // 方便在bootstrap-table中引用methods
    return {
      // 表头
      columns: [
        {
          title: '序号',
          field: 'serial',
          formatter: function (value, row, index) {
            return index + 1 // 这样的话每翻一页都会重新从1开始，
          }
        },
        {
          title: '封面',
          field: 'avatar',
          width: 50
        },
        {
          title: '名称',
          field: 'name',
          width: 250
        },
        {
          title: '总分数',
          field: 'score'
        },
        {
          title: '时长',
          field: 'elapse'
        },
        {
          title: '操作',
          field: 'action',
          width: '150px',
          formatter: (value, row) => {
            return '<button type="button" class="btn btn-success anal-exam">分析</button>' +
                '&nbsp;&nbsp;' +
                '<button type="button" class="btn btn-success view-exam">记录</button>'
          },
          events: {
            'click .anal-exam': function (e, value, row, index) {
              that.handleAnal(row)
            },
            'click .view-exam': function (e, value, row, index) {
              that.handleView(row)
            }
          }
        }
      ],
      tableData: [], // bootstrap-table的数据
      // custom bootstrap-table
      options: {
        search: true,
        showColumns: true,
        showExport: true,
        pagination: true,
        toolbar: '#toolbar',
        // 下面两行是支持高级搜索，即按照字段搜索
        advancedSearch: true,
        idTable: 'advancedTable'
        // 下面是常用的事件，更多的点击事件可以参考：http://www.itxst.com/bootstrap-table-events/tutorial.html
        // onClickRow: that.clickRow,
        // onClickCell: that.clickCell, // 单元格单击事件
        // onDblClickCell: that.dblClickCell // 单元格双击事件
      }
    }
  },
  mounted () {
    this.loadAll() // 加载所有问题的数据
  },
  methods: {
    handleView (record) {
      // Todo:修改考试信息和下面的题目，弹出一个可修改的输入框，实际上复用创建题目的模态框即可，还没做完
      console.log('开始编辑啦')
      console.log(record.id)
      this.$refs.recordViewModal.view(record.id)
    },
    handleAnal (record) {
      console.log('开始分析啦')
      console.log(record.id)
      this.$refs.showPieModal.anal(record.id);
    },
    handleOk () {
      this.loadAll()
    },
    loadAll () {
      const that = this
      getExamAll()
          .then(res => {
            if (res.code === 0) {
              that.tableData = res.data
              that.$refs.table._initTable()
            } else {
              that.$notification.error({
                message: '获取全部考试的列表失败',
                description: res.msg
              })
            }
          })
    }
  }
}
</script>
