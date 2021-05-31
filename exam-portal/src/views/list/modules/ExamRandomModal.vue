<template>
  <a-modal title="自动组卷" :width="640" :visible="visible" :confirmLoading="confirmLoading" @cancel="handleCancel">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <div>
          <a-form-item
              label="考试名称"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
          >
            <a-input v-decorator="['name', {rules: [{required: true}]}]"/>
          </a-form-item>
          <a-form-item
              label="考试限时"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
          >
            <a-input-number
                :min="1"
                :max="200"
                v-decorator="['elapse', {initialValue: '90',rules: [{required: true}]}]"
            />
            分钟
          </a-form-item>
          <a-form-item
              label="题目类别"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol">
            <a-select
                v-decorator="[
          'categories',
          {
            rules: [
              { required: true, message: '请选择题目类别，可多选', type: 'array' },
            ],
          },
        ]"
                mode="multiple"
                placeholder="请选择题目类别，可多选"
            >
              <a-select-option v-for="category in categoryList" :value="category.id" :key="category.id"> {{ category.name }}
              </a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item
              label="考试简述"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
          >
            <a-textarea :rows="2" v-decorator="['desc', {rules: [{required: true}]}]"></a-textarea>
          </a-form-item>
          <a-form-item
              label="考试封面"
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
          >
            <!-- 创建考试的时候传入的图片 -->
            <div id="summernote-exam-avatar-create"></div>
          </a-form-item>
        </div>
      </a-form>
    </a-spin>
    <template slot="footer">
      <a-button key="cancel" @click="handleCancel">取消</a-button>
      <a-button key="forward" :loading="confirmLoading" type="primary" @click="handleSubmit()">完成</a-button>
    </template>
  </a-modal>
</template>

<script>
import '../../../plugins/summernote'
import $ from 'jquery'
import { examRandomCreate, getQuestionSelection } from '../../../api/exam'

export default {
  name: 'ExamRandomModal',
  data () {
    return {
      labelCol: {
        xs: { span: 24 },
        sm: { span: 7 }
      },
      size: 'default',
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 13 }
      },
      visible: false,
      confirmLoading: false,
      mdl: {},
      categoryList: [],
      form: this.$form.createForm(this),
    }
  },
  updated () {
    this.initSummernote()
  },
  methods: {
    initCate() {
      getQuestionSelection().then(res => {
        this.categoryList = res.data.categories
        console.log(this.categoryList)
      })
    },
    initSummernote () {
      console.log('初始化富文本插件')
      $('#summernote-exam-avatar-create').summernote({
        lang: 'zh-CN',
        placeholder: '粘贴截图到这即可，图片最好不要大于80*80',
        height: 200,
        width: 320,
        htmlMode: true,
        toolbar: [],
        fontSizes: ['8', '9', '10', '11', '12', '14', '18', '24', '36'],
        fontNames: [
          'Arial', 'Arial Black', 'Comic Sans MS', 'Courier New',
          'Helvetica Neue', 'Helvetica', 'Impact', 'Lucida Grande',
          'Tahoma', 'Times New Roman', 'Verdana'
        ]
      })
    },
    create () {
      this.initCate()
      this.visible = true
    },
    popupScroll () {
      console.log('popupScroll')
    },
    handleCancel () {
      // clear form & currentStep
      this.visible = false
    },
    handleSubmit() {
      const { form: { validateFields } } = this

      this.confirmLoading = true
      validateFields((errors, values) => { // values就是表单中校验的值，后面提交到后端接口时主要就是用这个values
        values.avatar = $('#summernote-exam-avatar-create').summernote('code')

        console.log('提交数据到后端')
        console.log(values)
        this.confirmLoading = false
        if (!errors) {
          // 在这里把创建的考试的内容(存放在values中)提交给后端接口，需要的参数都已经封装成values这个json啦
          console.log('values:', values)
          // 把data中的question属性提交到后端，待写完后端接口.把前端的创建的题型提交到后端
          examRandomCreate(values)
              .then(res => {
            // 成功就跳转到结果页面
            console.log(res)
            if (res.code === 0) {
              this.$notification.success({
                message: '创建成功',
                description: '考试创建成功'
              })
              // 关闭弹出框
              this.visible = false
              this.$emit('ok')
            }
          }).catch(err => {
            // 失败就弹出警告消息
            this.$notification.error({
              message: '考试创建失败',
              description: err.message
            })
          })
        } else {
          console.log("????")
          this.confirmLoading = false
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
