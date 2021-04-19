<template>
  <div class="footer">
    <div class="links">
      <a href="https://github.com/elk-alter/fiveup-exam" target="_blank">代码仓</a>
      <a href="https://github.com/elk-alter" target="_blank">关于我</a>
      <a href="mailto:peiyaoren@gmail.com">联系我</a>
    </div>
    <div class="copyright">
      Copyright
      <a-icon type="copyright" /> 2021 <span>Ren Pei Yao</span>
    </div>
  </div>
</template>

<script>
export default {
  name: 'GlobalFooter',
  data () {
    return {}
  }
}
</script>

<style lang="less" scoped>
.footer {
  padding: 0 16px;
  margin: 24px 0 24px;
  text-align: center;

  .links {
    margin-bottom: 8px;

    a {
      color: rgba(0, 0, 0, 0.45);

      &:hover {
        color: rgba(0, 0, 0, 0.65);
      }

      &:not(:last-child) {
        margin-right: 40px;
      }
    }
  }
  .copyright {
    color: rgba(0, 0, 0, 0.45);
    font-size: 14px;
  }
}
</style>
