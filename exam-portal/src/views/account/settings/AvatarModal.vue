<template>
  <a-modal
    title="修改头像"
    :visible="visible"
    :maskClosable="false"
    :confirmLoading="confirmLoading"
    :width="460"
    @cancel="cancelHandel">
    <a-row>
      <div>请使用URL上传图片，可以使用图床
        <a-popconfirm
            title="是否打开SM.MS图床?"
            ok-text="Yes"
            cancel-text="No"
            @confirm="confirm"
            @cancel="cancel"
        >
          <a>SM.MS</a>
        </a-popconfirm>
      </div>
    </a-row>
    <a-row>
      <img style="margin:20px auto;" :src="option.img" :key="option.img"/>
    </a-row>
    <a-row>
      <a-input id="input" placeholder="请输入图片链接" :value="this.option.img"></a-input>
    </a-row>

    <template slot="footer">
      <a-button key="show" @click="showPre">预览</a-button>
      <a-button key="back" @click="cancelHandel">取消</a-button>
      <a-button key="submit" type="primary" :loading="confirmLoading" @click="okHandel">保存</a-button>
    </template>
  </a-modal>
</template>
<script>
export default {
  components: {
  },
  data () {
    return {
      visible: false,
      confirmLoading: false,
      option: {
        img: null,
        info: true,
        size: 1,
        outputType: 'jpeg',
        canScale: false,
        autoCrop: true,
        // 只有自动截图开启 宽度高度才生效
        autoCropWidth: 360,
        autoCropHeight: 360,
        fixedBox: true,
        // 开启宽度和高度比例
        fixed: true,
        fixedNumber: [1, 1]
      }
    }
  },
  methods: {
    edit (avatar) {
      this.visible = true
      this.option.img = avatar
      /* 获取原始头像 */
    },
    close () {
      this.option.img = null
      this.visible = false
    },
    cancelHandel () {
      this.close()
    },
    okHandel () {
      const vm = this

      vm.confirmLoading = true
      setTimeout(() => {
        vm.confirmLoading = false
        vm.close()
        vm.$message.success('上传头像成功')
      }, 2000)
    },
    showPre() {
      this.option.img = document.getElementById('input').value
      console.log(this.option.img)
    },
    confirm(e) {
      console.log(e);
      window.open('https://sm.ms/')
      this.$message.success('Click on Yes');
    },
    cancel(e) {
      console.log(e);
      this.$message.error('Click on No');
    },
  }
}
</script>

<style lang="less" scoped>

  .avatar-upload-preview {
    position: absolute;
    top: 50%;
    transform: translate(-50%, -50%);
    width: 360px;
    height: 360px;
    border-radius: 50%;
    box-shadow: 0 0 4px #ccc;
    overflow: hidden;

    img {
      width: 100%;
      height: 100%;
    }
  }
</style>
