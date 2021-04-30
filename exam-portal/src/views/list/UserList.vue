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
    <!-- ref是为了方便用this.$refs.modal直接引用，下同 -->
    <update-user-avatar-modal ref="updateUserAvatarModal" @ok="handleOk" />
    <summernote-update-modal ref="userUpdateModal" @ok="handleOk" />
  </a-card>
</template>

<script>
import '../../plugins/bootstrap-table'
import { getUserList,userUpdate } from '@api/user'
import UpdateUserAvatarModal from '@views/list/modules/UpdateUserAvatarModal'
import SummernoteUpdateModal from './modules/SummernoteUpdateModal'

export default {
  name: 'UserList',
  components: {
    SummernoteUpdateModal,
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
          title: '头像',
          field: 'avatar',
          width: 50,
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
          title: '用户名',
          field: 'username',
          width: 200,
          formatter: (value, row) => {
            return '<div class="username" style="height: 100%;width: 100%">' + value + '</div>'
          },
          events: {
            'click .username': function (e, value, row, index) {
              that.$refs.userUpdateModal.edit('summernote-user-username-update', row, 'username', '更新用户名', userUpdate)
            }
          }
        },
        {
          title: '昵称',
          field: 'nickname',
          width: 200,
          formatter: (value, row) => {
            return '<div class="nickname">' + value + '</div>'
          },
          events: {
            'click .nickname': function (e, value, row, index) {
              that.$refs.userUpdateModal.edit('summernote-user-nickname-update', row, 'nickname', '更新昵称', userUpdate)
            }
          }
        },
        {
          title: '角色',
          field: 'role',
          formatter: (value, row) => {
            return '<div class="role">' + value + '</div>'
          },
          events: {
            'click .role': function (e, value, row, index) {
              that.$refs.userUpdateModal.edit('summernote-user-role-update', row, 'role', '更新角色', userUpdate)
            }
          }
        },
        {
          title: '签名',
          field: 'description',
          formatter: (value, row) => {
            return '<div class="description">' + value + '</div>'
          },
          events: {
            'click .description': function (e, value, row, index) {
              that.$refs.userUpdateModal.edit('summernote-user-description-update', row, 'description', '更新签名', userUpdate)
            }
          }
        },
        {
          title: '邮箱',
          field: 'email',
          formatter: (value, row) => {
            return '<div class="email">' + value + '</div>'
          },
          events: {
            'click .email': function (e, value, row, index) {
              that.$refs.userUpdateModal.edit('summernote-user-email-update', row, 'email', '更新邮箱', userUpdate)
            }
          }
        },
        {
          title: '手机号',
          field: 'phone',
          formatter: (value, row) => {
            return '<div class="phone">' + value + '</div>'
          },
          events: {
            'click .phone': function (e, value, row, index) {
              that.$refs.userUpdateModal.edit('summernote-user-phone-update', row, 'phone', '更新手机号', userUpdate)
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
        onDblClickCell: that.dblClickCell // 单元格双击事件
      }
    }
  },
  mounted () {
    this.loadAll() // 加载所有问题的数据
  },
  methods: {
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
