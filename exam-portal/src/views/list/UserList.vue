<template>
  <a-card :bordered="false">
    <div id="toolbar">
      <a-button type="primary" icon="plus" @click="$refs.createQuestionModal.create()">新建</a-button>&nbsp;
      <a-button type="primary" icon="reload" @click="loadAll()">刷新</a-button>
    </div>
    <BootstrapTable
        ref="table"
        :columns="columns"
        :data="tableData"
        :options="options"
    />
    <!-- ref是为了方便用this.$refs.modal直接引用，下同 -->
    <update-user-avatar-modal ref="updateUserAvatarModal" @ok="handleOk" />
    < ref="questionUpdateModal" @ok="handleOk" />
  </a-card>
</template>

<script>
import '../../plugins/bootstrap-table'
import { getUserList,userUpdate } from '@api/user'
import UpdateUserAvatarModal from '@views/list/modules/UpdateUserAvatarModal'

export default {
  name: 'UserList',
  components: {
    UpdateUserAvatarModal
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
          title: '用户名',
          field: 'username',
          width: 200,
          formatter: (value, row) => {
            return '<div class="username" style="height: 100%;width: 100%">' + value + '</div>'
          }
        },
        {
          title: '昵称',
          field: 'nickname',
          width: 200,
          formatter: (value, row) => {
            return '<div class="nickname">' + value + '</div>'
          }
        },
        {
          title: '角色',
          field: 'role',
          formatter: (value, row) => {
            return '<div class="role">' + value + '</div>'
          }
        },
        {
          title: '头像',
          field: 'avatar',
          formatter: (value, row) => {
            return '<div class="user-avatar">' + value + '</div>'
          },
          events: {
            'click .user-avatar': function (e, value, row, index) {
              that.handleUserAvatarEdit(row)
            }
          }
        },
        {
          title: '签名',
          field: 'description',
          formatter: (value, row) => {
            return '<div class="question-level">' + value + '</div>'
          }
        },
        {
          title: '邮箱',
          field: 'email',
          formatter: (value, row) => {
            return '<div class="question-type">' + value + '</div>'
          }
        },
        {
          title: '手机号',
          field: 'phone',
          formatter: (value, row) => {
            return '<div class="question-category">' + value + '</div>'
          }
        },
        {
          title: '操作',
          field: 'action',
          align: 'center',
          formatter: (value, row) => {
            return '<button type="button" class="btn btn-success view-question">详情</button>' +
                '&nbsp;&nbsp;' +
                '<button type="button" class="btn btn-success edit-question">编辑</button>'
          },
          events: {
            'click .view-question': function (e, value, row, index) {
              that.handleSub(row)
            },
            'click .edit-question': function (e, value, row, index) {
              that.handleEdit(row)
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
        idTable: 'advancedTable',
        // 下面是常用的事件，更多的点击事件可以参考：http://www.itxst.com/bootstrap-table-events/tutorial.html
        // onClickRow: that.clickRow,
        // onClickCell: that.clickCell // 单元格单击事件
        // onDblClickCell: that.dblClickCell // 单元格双击事件
      }
    }
  },
  mounted () {
    this.loadAll() // 加载所有问题的数据
  },
  methods: {
    edit (user) {
      Object.assign(this.user, user) // 深度拷贝
      this.visible = true
      // 每次编辑需要先清空下之前的数据
      this.username = user.username
      this.nickname = user.nickname
      this.description = user.description
      this.avatar = user.avatar
      this.id = user.id
      this.role = user.role
      const that = this
    },
    handleEdit (record) {
      this.$refs.modalEdit.edit(record)
    },
    handleSub (record) {
      // 查看题目
      console.log(record)
      this.$refs.modalView.edit(record)
    },
    handleUserAvatarEdit (record) {
      console.log('开始更新封面啦')
      console.log(record)
      this.$refs.updateUserAvatarModal.edit(record)
    },
    handleOk () {
      this.loadAll() // 加载所有问题的数据
    },
    loadAll () {
      const that = this
      getUserList()
          .then(res => {
            if (res.code === 0) {
              that.tableData = res.data
              that.$refs.table._initTable()
            } else {
              that.$notification.error({
                message: '获取全部用户的列表失败',
                description: res.msg
              })
            }
          })
    }
  }
}
</script>

<style scoped>

</style>
