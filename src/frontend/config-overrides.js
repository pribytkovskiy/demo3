const {injectBabelPlugin} = require('react-app-rewired');
const rewireLess = require('react-app-rewire-less');

module.exports = function override(config, env) {
  config = injectBabelPlugin(
    ['import', { libraryName: 'antd', libraryDirectory: 'es', style: true }], // change importing css to less
    config,
  );
  config = rewireLess.withLoaderOptions({
    modifyVars: {
      "@layout-body-background": "#FFFFFF",
      "@layout-header-background": "#FFFFFF",
      "@layout-footer-background": "#FFFFFF",
      "@layout-sider-background": "#001529",
      "@menu-dark-bg": "#001529",
    },
    javascriptEnabled: true
  })(config, env);
  return config;
};