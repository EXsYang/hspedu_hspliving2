/**
 * 开发环境
 */
;(function () {
  window.SITE_CONFIG = {};

  // api接口请求地址
  // window.SITE_CONFIG['baseUrl'] = 'http://localhost:8080/renren-fast';

  // 解读，把baseUrl 改成请求到 gateway , 而且携带了 `/api`, 是用于在网关进行断言的
  // , 当然，这里也可以自己设计,只要保证没有冲突就行
  window.SITE_CONFIG['baseUrl'] = 'http://localhost:5050/api';
  // window.SITE_CONFIG['baseUrl'] = 'http://www.hspliving22.com/api';

  // window.SITE_CONFIG['baseUrl'] = 'http://localhost:9090/commodity/category/list';

  // cdn地址 = 域名 + 版本号
  window.SITE_CONFIG['domain']  = './'; // 域名
  window.SITE_CONFIG['version'] = '';   // 版本号(年月日时分)
  window.SITE_CONFIG['cdnUrl']  = window.SITE_CONFIG.domain + window.SITE_CONFIG.version;
})();
