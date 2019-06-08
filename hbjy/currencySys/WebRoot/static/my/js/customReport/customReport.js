var httpUtil = new HttpUtil();
define(function(require){
	var multiMethods=require('../common/ultidimensional/multioperation');
	$(function(){
		//自定义度量内容，拼接到ztree里
		var treevaluedata=[
			{id:'value_root',pId:'value_base',name:'度量',parentId:'value_base',ownId:'value_root'},
			{id:'JEBJDZC',pId:'value_root',name:'本季度支出金额（元）',parentId:'value_root',ownId:'JEBJDZC'},
			{id:'JEBJDSR',pId:'value_root',name:'本季度收入金额（元）',parentId:'value_root',ownId:'JEBJDSR'},
			{id:'JENLJZC',pId:'value_root',name:'年累计支出金额（元）',parentId:'value_root',ownId:'JENLJZC'},
			{id:'JENLJSR',pId:'value_root',name:'年累计收入金额（元）',parentId:'value_root',ownId:'JENLJSR'},
			{id:'TBZJBJDZC',pId:'value_root',name:'本季度支出同比增减（元）',parentId:'value_root',ownId:'TBZJBJDZC'},
			{id:'TBZJBJDSR',pId:'value_root',name:'本季度收入同比增减（元）',parentId:'value_root',ownId:'TBZJBJDSR'},
			{id:'TBZJNLJZC',pId:'value_root',name:'年累计支出同比增减（元）',parentId:'value_root',ownId:'TBZJNLJZC'},
			{id:'TBZJNLJSR',pId:'value_root',name:'年累计收入同比增减（元）',parentId:'value_root',ownId:'TBZJNLJSR'},
			{id:'TBBHBJDZC',pId:'value_root',name:'本季度支出同比变化（%）',parentId:'value_root',ownId:'TBBHBJDZC'},
			{id:'TBBHBJDSR',pId:'value_root',name:'本季度收入同比变化（%）',parentId:'value_root',ownId:'TBBHBJDSR'},
			{id:'TBBHNLJZC',pId:'value_root',name:'年累计支出同比变化（%）',parentId:'value_root',ownId:'TBBHNLJZC'},
			{id:'TBBHNLJSR',pId:'value_root',name:'年累计收入同比变化（%）',parentId:'value_root',ownId:'TBBHNLJSR'},
			{id:'ZBBJDZC',pId:'value_root',name:'本季度支出占比（%）',parentId:'value_root',ownId:'ZBBJDZC'},
			{id:'ZBBJDSR',pId:'value_root',name:'本季度收入占比（%）',parentId:'value_root',ownId:'ZBBJDSR'},
			{id:'ZBNLJZC',pId:'value_root',name:'年累计支出占比（%）',parentId:'value_root',ownId:'ZBNLJZC'},
			{id:'ZBNLJSR',pId:'value_root',name:'年累计收入占比（%）',parentId:'value_root',ownId:'ZBNLJSR'}
   		];
		
		//初始化多维分析
		multiMethods.setmultiTree({
			treeDataUrl:httpUtil.url+'/customReportController/getTree_dep',			//trees接口
			content:'#analysisbox',
			treevaluedata:treevaluedata,//自定义度量拼接内容
			saveUrl:'',			//保存模板接口
			tableDataUrl:httpUtil.url+'/customReportController/getCbs_bdpal',	//table接口
			getType:false,//可选：新建模板 false ,模板回填 true,
			modelId:''		//模板回填时modelID
		})
	})
	
	
})