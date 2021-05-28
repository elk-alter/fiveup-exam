<template>
  <page-view :title="false">
    <banner/>
    <show/>
    <page1/>
  </page-view>
</template>

<script>
import { PageView } from '../layouts'
import Banner from './home/Banner'
import Page1 from './home/Page1'
import Show from './home/Show'

export default {
  name: 'Home',
  components: {
    PageView,
    Banner,
    Show,
    Page1
  }
}
</script>
