const path = require('path')
const webpack = require('webpack')

function resolve (dir) {
  return path.join(__dirname, dir)
}

// vue.config.js
module.exports = {
  configureWebpack: {
    plugins: [
      // Ignore all locale files of moment.js
      new webpack.IgnorePlugin(/^\.\/locale$/, /moment$/),
      new webpack.ProvidePlugin({
        $: 'jquery',
        jQuery: 'jquery'
      })
    ]
  },

  chainWebpack: (config) => {
    config.resolve.alias
      .set('@$', resolve('src'))
      .set('@api', resolve('src/api'))
      .set('@assets', resolve('src/assets'))
      .set('@comp', resolve('src/components'))
      .set('@views', resolve('src/views'))
      .set('@layout', resolve('src/layout'))
      .set('@static', resolve('src/static'))
      .set('jquery', resolve('node_modules/jquery/src/jquery'))

    const svgRule = config.module.rule('svg')
    svgRule.uses.clear()
    svgRule
      .oneOf('inline')
      .resourceQuery(/inline/)
      .use('vue-svg-icon-loader')
      .loader('vue-svg-icon-loader')
      .end()
      .end()
      .oneOf('external')
      .use('file-loader')
      .loader('file-loader')
      .options({
        name: 'assets/[name].[hash:8].[ext]'
      })
  },

  css: {
    loaderOptions: {
      less: {
        modifyVars: {
          /* less 变量覆盖，用于自定义 ant design 主题 */

          /*
          'primary-color': '#F5222D',
          'link-color': '#F5222D',
          'border-radius-base': '4px',
          */
          primaryColor: '#722ED1', // primary color of ant design
          navTheme: 'light', // theme for nav menu
          layout: 'topmenu', // nav menu position: sidemenu or topmenu
          contentWidth: 'Fixed', // layout of content: Fluid or Fixed, only works when layout is topmenu
          fixedHeader: true, // sticky header
          fixSiderbar: false, // sticky siderbar
          autoHideHeader: true, //  auto hide header
          // colorWeak: false,
          // multiTab: false,
          // production: process.env.NODE_ENV === 'production' && process.env.VUE_APP_PREVIEW !== 'true',
          // vue-ls options
          storageOptions: {
            namespace: 'pro__',
            name: 'ls',
            storage: 'local',
          }
        },
        javascriptEnabled: true
      }
    }
  },

  devServer: {
    // development server port 9110
    port: 9110,
    proxy: 'http://localhost:9100'
  },

  // disable source map in production
  productionSourceMap: false,
  lintOnSave: false,
  // babel-loader no-ignore node_modules/*
  transpileDependencies: []
}
