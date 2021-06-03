<template>
  <div class="account-settings-info-view">
    <a-row :gutter="16">
      <a-col :md="24" :lg="16">

        <a-form layout="vertical">
          <a-form-item
              label="用户名"
          >
            <a-input placeholder="设置登录用的用户名" v-model="form.username" />
          </a-form-item>
          <a-form-item
            label="昵称"
          >
            <a-input placeholder="给自己起个名字" v-model="form.name" />
          </a-form-item>
          <a-form-item
            label="签名"
          >
            <a-textarea rows="4" placeholder="You are not alone." v-model="form.welcome"/>
          </a-form-item>

          <a-form-item
            label="电子邮件"
            :required="false"
          >
            <a-input placeholder="exp@admin.com" v-model="form.email"/>
          </a-form-item>
          <a-form-item
              label="绑定学号"
              :required="false"
          >
            <a-input placeholder="例如:2017081142" v-model="form.snumber"/>
          </a-form-item>
          <a-form-item
            label="登录密码"
            :required="false"
            hidden
          >
            <a-input placeholder="密码"/>
          </a-form-item>

          <a-form-item>
            <a-button type="primary" @click="updateUserInfo()">提交</a-button>
            <a-button style="margin-left: 8px">保存</a-button>
          </a-form-item>
        </a-form>

      </a-col>
      <a-col :md="24" :lg="8" :style="{ minHeight: '180px' }">
        <div class="ant-upload-preview" @click="$refs.modal.edit(form)" >
          <a-icon type="cloud-upload-o" class="upload-icon"/>
          <div class="mask">
            <a-icon type="plus" />
          </div>
          <img :src="option.img"/>
        </div>
      </a-col>

    </a-row>
    <avatar-modal ref="modal" @ok="handleOk"/>
  </div>
</template>

<script>
import AvatarModal from './AvatarModal'
import { userInfo, userUpdate } from '@api/user'

export default {
  components: {
    AvatarModal
  },
  data () {
    return {
      // cropper
      preview: {},
      option: {
        img: '/avatar2.jpg',
        info: true,
        size: 1,
        outputType: 'jpeg',
        canScale: false,
        autoCrop: true,
        // 只有自动截图开启 宽度高度才生效
        autoCropWidth: 180,
        autoCropHeight: 180,
        fixedBox: true,
        // 开启宽度和高度比例
        fixed: true,
        fixedNumber: [1, 1]
      },

      form: {},
      user: {}
    }
  },
  created() {
    this.getUserInfo()
  },
  methods: {
    getUserInfo() {
      userInfo().then(res => {
        this.form = res.data
        this.option.img = res.data.avatar
        console.log(this.form)
      })
    },
    updateUserInfo() {
      this.user.id = this.form.id
      this.user.username = this.form.username
      this.user.nikename = this.form.name
      this.user.email = this.form.email
      this.user.description = this.form.welcome
      this.user.snumber = this.form.snumber
      if (this.form.password !== "") this.user.password = this.form.password
      console.log(this.user)
      userUpdate(this.user).then(res => {
        this.$message.success('更新信息成功')
      })
    },
    handleOk() {
      this.getUserInfo()
    }
  }
}
</script>

<style lang="less" scoped>

  .avatar-upload-wrapper {
    height: 200px;
    width: 100%;
  }

  .ant-upload-preview {
    position: relative;
    margin: 0 auto;
    width: 100%;
    max-width: 180px;
    border-radius: 50%;
    box-shadow: 0 0 4px #ccc;

    .upload-icon {
      position: absolute;
      top: 0;
      right: 10px;
      font-size: 1.4rem;
      padding: 0.5rem;
      background: rgba(222, 221, 221, 0.7);
      border-radius: 50%;
      border: 1px solid rgba(0, 0, 0, 0.2);
    }
    .mask {
      opacity: 0;
      position: absolute;
      background: rgba(0,0,0,0.4);
      cursor: pointer;
      transition: opacity 0.4s;

      &:hover {
        opacity: 1;
      }

      i {
        font-size: 2rem;
        position: absolute;
        top: 50%;
        left: 50%;
        margin-left: -1rem;
        margin-top: -1rem;
        color: #d6d6d6;
      }
    }

    img, .mask {
      width: 100%;
      max-width: 180px;
      height: 100%;
      border-radius: 50%;
      overflow: hidden;
    }
  }
</style>
