var dev = false;//是否在开发环境
seajs.config({
    alias:{
        /* plugins */
       	'mock':'../../js/common/mock/1.0/mock',
       	'icheck':'../../js/common/icheck/0.0.7/icheck',
       	'i18n':'../../js/common/i18n/1.2.2/jquery.i18n.properties',
       	
        /* init */
        'myinit':'myJsPath/js/common/init',
        'mockData': 'myJsPath/js/common/mockData'
    },
    paths: {
    	myJsPath: (typeof staticUrl == 'undefined' ? '/static' : staticUrl) + '/my'
    },
    preload: dev ? [ 'myinit','mockData'] : [ 'myinit']//开发环境模拟接口，拦截ajax接口
});
seajs.use('myinit');
