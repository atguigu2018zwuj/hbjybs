function CookieUtil(){
	// 添加cookie (有参: key, value, time(单位：秒), 无返)
	this.addCookie=function(key,value,time){
		if (typeof time == "number") {
			//数字类型
			document.cookie = key + "=" + value + ";path=/;max-age=" + time;
		}else if (typeof time == "string") {
			//字符串类型
			document.cookie = key + "=" + value + ";path=/;expires=" + time;
		}else {
			//对象类型 先toString 后expires
			document.cookie = key + "=" + value + ";path=/;expires=" + time.toString();
		}
	}
 
	// 获取cookie (有参: key, 有返:key对应的value)
	this.getValue=function(key){
		var arr1 = document.cookie.split("; ")
		for (var i = 0 ; i < arr1.length; i++) {
			var arr2 = arr1[i].split("=")
			if (arr2[0] == key) {
				return arr2[1];
			}
		}
	}
 
	// 删除cookie (有参:key , 无返)
	this.deleteCookie=function(key){
		document.cookie = key + "=;max-age=-1";
	}
 
	// 清除所有cookie函数
	this.clearAllCookie=function(){
		var keys = document.cookie.match(/[^ =;]+(?=\=)/g);
		if(keys) {
			for(var i = keys.length; i--;){
				this.deleteCookie(keys[i]);
			}
		}
	}
	
	/**
	 * 获取用户信息
	 * @return 用户信息（{DW_JB:"1",TELEPH:"15981943396",DW_NAME:"",SSBM:"",DW_CODE:"C3234641000015",YH_PWD:"e10adc3949ba59abbe56e057f20f883e",NAME:"",AUTHORITY:"all",ALEVEL:"1",ID:1,YH_NAME:"admin",NBJGH:"1699999998",DWBM:"1699999998"}）
	 * （对象属性说明：
	 * 		ID:1（用户ID）
	 * 		YH_NAME:"admin"（用户名）
	 * 		NAME:""（姓名）
	 * 		TELEPH:"15981943396"（用户手机号码）
	 * 		AUTHORITY:"all"（用户权限）
	 * 		DW_CODE:"C3234641000015"（金融机构编码）
	 * 		DW_NAME:""（单位名称）
	 * 		NBJGH:"1699999998"（内部机构号，同DWBM）
	 * 		DW_JB:"1"（机构级别，同ALEVEL）
	 * 		SSBM:""（所属部门）
	 * 		LOGIN_TIMES:0（用户登录系统次数，不包括当前登录的这一次）
	 *  ）
	 */
	this.getUserInfoFromCookie=function(){
		return JSON.parse(JSON.parse(this.getValue('userInfo')));
	}
	
	/**
	 * 设置用户信息
	 * @param key {string} 要设置的用户信息的键：
	 * 		ID:1（用户ID）
	 * 		YH_NAME:"admin"（用户名）
	 * 		NAME:""（姓名）
	 * 		TELEPH:"15981943396"（用户手机号码）
	 * 		AUTHORITY:"all"（用户权限）
	 * 		DW_CODE:"C3234641000015"（金融机构编码）
	 * 		DW_NAME:""（单位名称）
	 * 		NBJGH:"1699999998"（内部机构号，同DWBM）
	 * 		DW_JB:"1"（机构级别，同ALEVEL）
	 * 		SSBM:""（所属部门）
	 * 		LOGIN_TIMES:0（用户登录系统次数，不包括当前登录的这一次）
	 * @param value 设置的值
	 */
	this.setUserInfoToCookie=function(key,value){
		var userInfo = this.getUserInfoFromCookie();
		userInfo[key] = value;
		// 先删除原始数据
		this.deleteCookie('userInfo');
		// 30天
		this.addCookie('userInfo',JSON.stringify(JSON.stringify(userInfo)),60*60*24*30);
	}
}

function ObjectUtil(){
	/**
	 * 获取对象的副本（深度复制）
	 * @param {Object} data 支持string、array、object、date
	 * @return 深度复制副本，若为不支持的类型则返回原始对象
	 */
	this.deepCopy=function(data) {
		var t = this.typeOf(data);
		// 初始化返回值
		var o;
		if (t === 'array') {
			o = [];
		} else if ( t === 'object') {
			o = {};
		} else if (t === 'date') {
			o = new Date();
		} else {
			return data;
		}
		// 复制
		if (t === 'array') {
			for (var i = 0; i < data.length; i++) {
				o.push(this.deepCopy(data[i]));
			}
		} else if ( t === 'object') {
			for (var j in data) {
				o[j] = this.deepCopy(data[j]);
			}
		} else if (t === 'date') {
			o.setTime(data.getTime());
		}
		return o;
	}
 
	/**
	 * 获取对象类型
	 * @param {Object} obj 指定对象
	 */
	this.typeOf = function(obj) {
		var toString = Object.prototype.toString;
		var map = {
			'[object Boolean]'  : 'boolean',
			'[object Number]'   : 'number',
			'[object String]'   : 'string',
			'[object Function]' : 'function',
			'[object Array]'    : 'array',
			'[object Date]'     : 'date',
			'[object RegExp]'   : 'regExp',
			'[object Undefined]': 'undefined',
			'[object Null]'     : 'null',
			'[object Object]'   : 'object'
		};
		return map[toString.call(obj)];
	}
}

function ZTreeUtil(){
	/**
	 * 根据属性ID勾选对应的节点
	 * @param treeObj 指定节点所属的treeObj
	 * @param id 属性ID值
	 * @param checked、checkTypeFlag、callbackFlag 参照ztree官方文档中【checkNode】的说明
	 */
	this.checkZtreeNodeById = function(treeObj,id,checked,checkTypeFlag,callbackFlag){
		var nodeById = treeObj.getNodeByParam("id",id);
		if (nodeById != null && nodeById != undefined) {
			treeObj.checkNode(nodeById, checked, checkTypeFlag,callbackFlag);
		}
	}
 
	/**
	 * 去除节点array中重复的所有子节点
	 * @param nodes 要处理的节点array
	 */
	this.removeRepeatChildren = function(nodes){
		var tempNodes = [];
		tempNodes = tempNodes.concat(nodes);
		// 防止查询出重复的结果
		for (var pNodeIndex in tempNodes) {
			for (var cNodeIndex in nodes) {
				// 去除所有子节点
				if (nodes[cNodeIndex].parentTId == tempNodes[pNodeIndex].tId) {
					nodes.splice(cNodeIndex,1,"REPEAT_"+cNodeIndex);
				}
			}
		}
		
		tempNodes = [];
		tempNodes = tempNodes.concat(nodes);
		// 清空且不更换数组
		nodes.length = 0;
		for (var index in tempNodes) {
			if (typeof tempNodes[index] != "string") {
				nodes.push(tempNodes[index]);
			}
		}
	}
	
	/**
	 * 初始化机构树型选择对话框
	 * 注释详见方法 jrjgMcDialog
	 * @param initTreeCheckedData 初始机构树中选中的值的数组
	 */
	this.jrjgMcDialogClickCount = 0;// 自初始化后点击的次数
	this.jrjgMcDialogInit = function(initTreeCheckedData,codeInputId,mcInputId,chkStyle,showSearch){
		var _this = this;
		_this.jrjgMcDialogClickCount = 0;
		
		// 点击名称input框开启对话框
		$("#"+mcInputId).unbind("click");
		$("#"+mcInputId).click(function() {
			_this.jrjgMcDialogClickCount++;
			// 第一次点击时加载initTreeCheckedData，其余时候加载保存code的input的值
			if (_this.jrjgMcDialogClickCount == 1) {
				_this.jrjgMcDialog(initTreeCheckedData,codeInputId,mcInputId,chkStyle,showSearch);
			} else {
				var treeCheckedData = [$("#"+codeInputId).val()];
				_this.jrjgMcDialog(treeCheckedData,codeInputId,mcInputId,chkStyle,showSearch)
			}
		});
	}
	
	/**
	 * 机构树型选择对话框
	 * 需要的依赖：ztree_all、ztree_exhide、fuzzysearch
	 * 对应的HTML标签：
	 * 		保存code的：<input id="mcInputId" name="mcInputId" type="text" style="cursor:pointer;" class="input-tag" readonly="readonly"/>
	 *		保存名称的：<input id="codeInputId" name="codeInputId" type="hidden"/>
	 * @param treeCheckedData 机构树中选中的值的数组
	 * @param codeInputId 机构树控件保存已选中code的input标签的id
	 * @param mcInputId 机构树控件保存已选中 机构名称 的input标签的id
	 * @param chkStyle 单选框还是多选框（radio：单元框；checkbox：多选框；默认单选框）
	 * @param showSearch 是否显示搜索框（true：显示；false：不显示；默认不显示）
	 */
	this.jrjgMcDialog = function(treeCheckedData,codeInputId,mcInputId,chkStyle,showSearch){
		showSearch = (showSearch == undefined || showSearch == "") ? false : showSearch;
		chkStyle = (chkStyle == undefined || chkStyle == "") ? "radio" : chkStyle;
		var httpUtil = new HttpUtil();
		var _this = this;
		
		// 机构树型结构
		var treeObj;
		var nodes;
		var areaIds;
		var setting1 = {
				check:{
					enable: true,
					chkStyle: (chkStyle == undefined || chkStyle == "") ? "radio" : chkStyle,  //单选框
							radioType: "all"   //对所有节点设置单选
				},
				data:{
					simpleData:{
						enable:true
					}
				}
		};
		$dialog({
			title:'请选择机构',
			title_html:true,
			width:600,
			height:200,
			content:'<div style="width: 600px;'+(showSearch ? '' : 'display:none;')+'">'
			+'<input id="keyword" type="search" placeHolder="搜索关键字"/>'
			+'</div>'
			+'<div style="height: ;overflow: auto;"><ul id="treeDiv" class="ztree"></ul></div>',
			onshow:function(){
				$('.artui-dialog-content').css({"max-height":"200px","overflow":"auto"});
				$.ajax({
					type:"POST",
					url: httpUtil.url+"userManage/queryDwCode",
					data:'',
					dataType:"json",
					success: function(result) {
						if(result == "" && result.length <= 0){
							return;
						}
						if(result.code==1000){
							if(treeCheckedData != undefined){
								for(var j=0; j < treeCheckedData.length; j++){
									$.each(result.data,function(i,obj){
										if(treeCheckedData[j] == obj.id){ 
											result.data[i].checked = true;
										}
									});
								}
							}
							$.fn.zTree.init($("#treeDiv"),setting1,result.data);
							treeObj = $.fn.zTree.getZTreeObj("treeDiv");
							nodes = _this.getCheckedLength(treeObj);
							fuzzySearch('treeDiv','#keyword',null,true); //初始化模糊搜索方法
						}else{
							$dialog.alert('拉取地区列表失败！','warning');
						}
					}
				});
			},
			okValue: '确定',
			ok:function(){
				var _this = $.fn.zTree.getZTreeObj("treeDiv").getCheckedNodes();
				if (_this[0] != undefined) {
					$("#"+codeInputId).val(_this[0].id);
					$("#"+mcInputId).val(_this[0].name);
				} else {
					$("#"+codeInputId).val("");
					$("#"+mcInputId).val("");
				}
				$("#"+mcInputId).data("nodes",nodes);
			},
			cancelValue:'取消',
			cancel:function(){}
		}).showModal();
		
		// 将jrjg对话框置于input控件(暂时只支持easyui对话框中的input，其他用到再加)之上
		var $jrjgInputDialog = $('#'+codeInputId).parents("div.panel.window.panel-htop");
		if ($jrjgInputDialog != undefined && $jrjgInputDialog.size() > 0) {
			var $treeDialog = $('#treeDiv').parents("[role='alertdialog']");
			var $treeDialogShade = $('#treeDiv').parents("[role='alertdialog']").next();
			$treeDialog.css('z-index',parseInt($jrjgInputDialog.css('z-index'))+2);
			$treeDialogShade.css('z-index',parseInt($jrjgInputDialog.css('z-index'))+1);
		}
	}
	
	this.getCheckedLength = function(treeObj) {
		var nodes = treeObj.getCheckedNodes(true);
		var newNodes = new Array();
		if (nodes) {
			for (var i = 0; i < nodes.length; i++) {
				if (!nodes[i].isParent) {// isParent判断是否为父级，也就是是否有下级
					newNodes.push(nodes[i].KEY);// 存放所选节点的名称
				}
			}
		}
		return newNodes;
	}
}

function MsgUtil(){
	/**
	 * 弹窗提示(默认succeed)
	 * TODO 现在更改icon没有效果，故全部设成succeed，后续可以自定义时更改之
	 * @param message 显示的消息
	 * @param flag 是否成功
	 */
	this.diaLog = function(message,flag){
		var width = '300px';
		var height = '80px';
		if (message.length > 25) {
			width = '500px';
			height = '160px';
		}
		
		$dialog({
			title:"提示",
			content: "<div style='font-size:30px;color:red;text-align:center'>"+message+"</div>",
			icon: flag == undefined || flag ? 'succeed' : 'succeed',
			width: width,
			height: height,
			follow: document.getElementById('btn2')
		}).showModal();
	}
}

function HttpUtil(){
	/** 请求后台的url */
	this.url = $("#url").val() != undefined ? $("#url").val() : "/currencySys";
	
	/**
	 * ajax请求（得到参数后进行预处理，若参数为字符串则转为json对象，若转换失败则提示信息）
	 * @param types [string] 请求类型
	 * @param urls [string] 请求的url
	 * @param datas [object] 数据
	 * @param func [function] 成功取得结果后的回调函数(参数为一个对象)
	 * @param isAsync [boolean] 是否以异步方式执行(可不填，默认以异步方式执行)
	 */
	this.ajax = function(types,urls,datas,func,isAsync){
		// 是否以异步方式执行
		isAsync = (isAsync == undefined || isAsync == null || isAsync == true);
		$.ajax({
			type: types,
			url: this.url+urls,
			async: isAsync,
			data: datas,
			success: function(data){
				var funcParam = null;
				var msgUtil = new MsgUtil();
				var objUtil = new ObjectUtil();
				
				// 取得结果对象：若参数为字符串则转为json对象，若转换失败则提示信息
				if (objUtil.typeOf(data) == 'string') {
					try{
						funcParam = data.length <= 0 ? data : JSON.parse(data);
					}catch(err){
						// 会话过期(取得后台返回的脚本并执行)
						// 若返回页面则不执行任何操作
						if (data.indexOf("<!DOCTYPE html") >= 0) {
							return;
						}
						// 弹出提示框
						var scripts = /\<script\>[\s\S]*\<\/script\>/.exec(data);
						if (scripts != null && scripts.length > 0) {
							eval(scripts[0].replaceAll('<script>','').replaceAll('</script>',''))
						}
						return;
					}
				} else {
					funcParam = data;
				}
				
				// 若结果为undefined则提示信息
				if(funcParam == null){
					console.log("未取得结果，或结果为空");
					msgUtil.diaLog("操作未成功过执行！",false);
				} else {
					func(funcParam);
				}
			}
		});
	}
}

/**
 * 页面相关操作
 */
function WindowUtil(){
	/**
	 * 在右边center区域打开菜单，新增tab
	 * @param text tab名
	 * @param url tab内容的加载路径
	 * @param ywbm 业务编码
	 * @param isReportForms 是否是业务报表页面
	 */
	this.openTab = function(text, url,ywbm,isReportForms){
		var httpUtil = new HttpUtil();
		var content = url;
		if (content.search(/\<iframe[\S\s]*\<\/iframe\>/i) < 0) {
			content = '<iframe width="100%" height="100%" frameborder="0"  src="'+httpUtil.url+url+'" style="width:100%;height:100%;margin:0px 0px;"></iframe>';
		}
		var editTableName = ywbm;
		// 特殊的业务编码
		if(editTableName == 'finfwh'){
			editTableName = 'jgxx';
		}
		
		if (isReportForms != undefined && isReportForms) {
			var flg = false;
			// 是业务报表页面，将报表配置信息加载进session
			httpUtil.ajax("post","/manaProController/getEditData",{editTableName:editTableName},function(result){
				flg = true;
			},false);
			if (!flg) return;
		}
		
		// 打开tab，若直接获取不到则从iframe父元素中获取
		var $tabs = $("#tabs").size() > 0 ? $("#tabs") : parent.$("#tabs");
		if ($tabs.tabs('exists', text)) {
			// 有参数，即为需使用参数进行查询，需关闭已存在的页面，并重新打开
			if (url.search(/\?[a-z,A-Z]*=[^=]*/i) >= 0) {
				$tabs.tabs('close', text);
				$tabs.tabs('add', {
					title : text,
					closable : true,
					content : content
				});
			} else {
				$tabs.tabs('select', text);
			}
		} else {
			$tabs.tabs('add', {
				title : text,
				closable : true,
				content : content
			});
		}
	}
}

/**
 * 日期相关操作
 */
function DateUtil (){
	// 用于时间字符串转时间对象时的格式化正则表达式
	this.dateRegexpMap = new Map();
	// 用于时间字符串转时间对象时的格式化正则表达式（不带分割符）
	var dateRegexpNoSplit = new RegExp("(\\d{4})(\\d{2})(\\d{2})([T\\s](\\d{2})(\\d{2})(\\d{2})((\\d{3}))?)?");
	// 用于时间字符串转时间对象时的格式化正则表达式（带分割符）
	var dateRegexpSplit = new RegExp("(\\d{4})-(\\d{2})-(\\d{2})([T\\s](\\d{2}):(\\d{2}):(\\d{2})(\\.(\\d{3}))?)?");
	// 用于获取时间字符串中指定部分的正则表达式
	this.dateGetRegexpMap = new Map();
	
	// ---------------------formatStr-NoSplit---------------------
	this.dateRegexpMap.set('yyyyMMdd HHmmss',dateRegexpNoSplit);
	this.dateRegexpMap.set('yyyyMMdd HHmmssSSS',dateRegexpNoSplit);
	this.dateRegexpMap.set('yyyyMMdd',dateRegexpNoSplit);
	this.dateRegexpMap.set('HHmmss',dateRegexpNoSplit);
	this.dateRegexpMap.set('HHmmssSSS',dateRegexpNoSplit);
	// ---------------------formatStr-HasSplit----------------------
	this.dateRegexpMap.set('yyyy-MM-dd HH:mm:ss',dateRegexpSplit);
	this.dateRegexpMap.set('yyyy-MM-dd HH:mm:ss.SSS',dateRegexpSplit);
	this.dateRegexpMap.set('yyyy-MM-dd',dateRegexpSplit);
	this.dateRegexpMap.set('HH:mm:ss',dateRegexpSplit);
	this.dateRegexpMap.set('HH:mm:ss.SSS',dateRegexpSplit);
	// -----------------hasSplit---------------------------
	this.dateRegexpMap.set(true,dateRegexpSplit);
	this.dateRegexpMap.set(false,dateRegexpNoSplit);
	
	// -----------------util正则表达式------------------------
	this.dateGetRegexpMap.set('HHmmss',new RegExp("^(\\d{2})(\\d{2})(\\d{2})$"));
	this.dateGetRegexpMap.set('HHmmssSSS',new RegExp("^(\\d{2})(\\d{2})(\\d{2})(\\d{3})$"));
	this.dateGetRegexpMap.set('HH:mm:ss',new RegExp("^(\\d{2}):(\\d{2}):(\\d{2})$"));
	this.dateGetRegexpMap.set('HH:mm:ss.SSS',new RegExp("^(\\d{2}):(\\d{2}):(\\d{2})(\\.\\d{3})$"));
	this.dateGetRegexpMap.set('DateValidata',new RegExp("^yyyy.*MM.*dd.* ?HH.*mm.*ss$|^yyyy.*MM.*dd.* ?HH.*mm.*ss.*SSS$|^yyyy.*MM.*dd$|^HH.*mm.*ss$|^HH.*mm.*ss.*SSS$"));
	
	/**
	 * 获取所有支持的格式化字符串数组
	 */
	this.getSupportFormatStrs = function () {
		// 支持的格式化字符串
		var supportFormatStrsArray = new Array();
		this.dateRegexpMap.forEach((value,key,map)=>{
			if (key !== true && key !== false) {
				supportFormatStrsArray.push(key);
			}
		});
		return supportFormatStrsArray;
	}
	
	/**
	 * 时间字符串转时间对象（手动解析字符串）
	 * @param dateString 时间字符串（不为空时必须）
	 * @param isHasSplit 时间字符串中是否有分隔符（true/false）（dateString不为空时必须）
	 * @param formatStr 时间格式化字符串（暂时废弃）
	 * @param isShowLog 是否显示日志（true/false）
	 * @return 转换后的时间对象（当dateString为空时返回当前时间，当发生异常时返回null）
	 */
	this.toDateFromStr = function(dateString,isHasSplit,formatStr,isShowLog){
		if (dateString == undefined || dateString == null || dateString.length == 0) {
			if (isShowLog == true) {
				console.log('时间字符串为空，返回当前时间');
			}
			return new Date();
		} else {
			// 时间字符串预处理
			
			// 支持【/】分隔符
			dateString = dateString.replace(new RegExp( '/' , 'g' ),'-');
			
			// 针对仅有时分秒的时间字符串的预处理
			if (dateString.split(' ').length == 1 
				&& (this.dateGetRegexpMap.get('HHmmss').test(dateString) 
					|| this.dateGetRegexpMap.get('HHmmssSSS').test(dateString))) {
				// 无分隔符
				dateString = `00000000 ${dateString}`;
			} else if (dateString.split(' ').length == 1 
				&& (this.dateGetRegexpMap.get('HH:mm:ss').test(dateString) 
					|| this.dateGetRegexpMap.get('HH:mm:ss.SSS').test(dateString))) {
				// 有分隔符
				dateString = `0000-00-00 ${dateString}`;
			}
		}
		
		// 根据参数【isHasSplit】、【formatStr】选定的格式化正则表达式
		var dateRegexp = (isHasSplit === true || isHasSplit === false) ? this.dateRegexpMap.get(isHasSplit) : this.dateRegexpMap.get(formatStr);
		if (isShowLog == true) {
			console.log('dateRegexp',`|${dateRegexp}|`);
		}
		if (dateRegexp == undefined || dateRegexp == null || dateRegexp == '') {
			console.error(`不支持的格式化字符串【${formatStr}】，仅支持以下格式化字符串【${this.getSupportFormatStrs()}】${isShowLog === true ? '' : '，详细信息请指定参数isShowLog为true以查看日志'}`);
			return null;
		}
		
		// 校验时间字符串
	    if(dateRegexp.test(dateString)){
	    	// 真正被使用的时间字符串
			var usedDateString = dateString.match(dateRegexp)[0];
	    	// 仅部分匹配
	    	if (usedDateString != dateString) {
	    		console.error(`时间字符串【${dateString}】与格式化字符串部分不匹配，此时的有效部分仅为【${usedDateString}】${isShowLog === true ? '' : '，详细信息请指定参数isShowLog为true以查看日志'}`);
	    		dateString = usedDateString;
	    	}
	    	
	        var timestamp = dateString.replace(dateRegexp, function($all,$year,$month,$day,$part2,$hour,$minute,$second,$part3,$milliscond){
	            if (isShowLog == true) {
	            	console.log('$all',`|${$all}|`);
			        console.log('$year',`|${$year}|`);
			        console.log('$month',`|${$month}|`);
			        console.log('$day',`|${$day}|`);
			        console.log('$part2',`|${$part2}|`);
			        console.log('$hour',`|${$hour}|`);
			        console.log('$minute',`|${$minute}|`);
			        console.log('$second',`|${$second}|`);
			        console.log('$part3',`|${$part3}|`);
			        console.log('$milliscond',`|${$milliscond}|`);
	            }
	            var date = new Date($year, $month-1,$day, $hour||"00", $minute||"00", $second||"00", $milliscond||"00");
	            return date.getTime();
	        });
	        var date = new Date();
	        date.setTime(timestamp);
	        return date;
	    } else {
	    	console.error(`时间字符串【${dateString}】与格式化字符串不匹配${isHasSplit === true ? '，时间字符串应有分割符' : (isHasSplit === false ? '，时间字符串应无分割符' : '【'+formatStr+'】')}${isShowLog === true ? '' : '，详细信息请指定参数isShowLog为true以查看日志'}`);
	    	return null;
	    }
	}
	/**
	 * 将日期对象转成指定格式的字符串
	 * @param date 日期对象
	 * @param formatStr 指定的格式的字符串
	 * @param isShowLog 是否显示日志（true/false）
	 * @return 转成的指定格式字符串（若formatStr为空或不被支持，则默认【yyyy-MM-dd HH:mm:ss】）
	 */
	this.toStrFromDate = function (date,formatStr,isShowLog) {
		// 参数校验
		if (formatStr == undefined || formatStr == null || formatStr.length == 0) {
	    	formatStr = "yyyy-MM-dd HH:mm:ss";
	    	if (isShowLog === true) {
	   			console.log(`格式化字符串【${formatStr}】为空，设为默认【yyyy-MM-dd HH:mm:ss】`);
	   		}
	    } else if (!this.dateGetRegexpMap.get('DateValidata').test(formatStr)) {
	   		console.error(`不支持的格式化字符串【${formatStr}】`);
	   		if (isShowLog === true) {
	   			console.log(`格式化字符串【${formatStr}】不被支持，设为默认【yyyy-MM-dd HH:mm:ss】`);
	   		}
	   		formatStr = "yyyy-MM-dd HH:mm:ss";
	    }
		
		// 获取年月日时分秒
	    var y = date.getFullYear();  
	    var m = date.getMonth() + 1;  
	    m = m < 10 ? ('0' + m) : m;  
	    var d = date.getDate();  
	    d = d < 10 ? ('0' + d) : d;  
	    var h = date.getHours();  
	    h = h < 10 ? ('0' + h) : h;
	    var minute = date.getMinutes();  
	    minute = minute < 10 ? ('0' + minute) : minute; 
	    var second= date.getSeconds();  
	    second = second < 10 ? ('0' + second) : second;
	    var ms = date.getMilliseconds();
	    ms = minute < 10 ? ('0' + ms) : ms;
	    
	    return formatStr.replace('yyyy',y).replace('MM',m).replace('dd',d).replace('HH',h).replace('mm',minute).replace('ss',second).replace('SSS',ms);
	}
 
	/**
	 * 减去对应的日期
	 * 例子（获取当前日期向前推3天的日期）：Util.subtractDate(new Date(), 'day', 3)
	 * @param date 要处理的日期对象(Date())
	 * @param type 要处理的类型(year,month,day)
	 * @param num 减去的数量
	 */
	this.subtractDate = function(date,type,num){
		var srcDate = new Date(date.getTime());
		if (type == 'year') {
			date.setFullYear(date.getFullYear() - num);
		} else if (type == 'month') {
			date.setMonth(date.getMonth() - num);
		} else if (type == 'day') {
			date.setDate(date.getDate() - num);
		}
		if (((type == 'year' || type == 'month') && date.getDate() == srcDate.getDate())
			|| type == 'day') {
			return date;
		}
			
		// 原始日期当前月的最大日期 比 处理后当前月的最大日期大(即原始日期处于当前月最大日期)
		// 处理后应为2月，实际处理后为3月
		if (date.getMonth() == 2) {
			// 闰年
			if ((date.getFullYear() % 4 == 0) && (date.getFullYear() % 100 != 0 || date.getFullYear() % 400 == 0)){
				date.setDate(29);
			} else {
				date.setDate(28);
			}
			date.setMonth(1);
			
		// 处理后应为4 6 9 11月，实际处理后为5 7 10 12月
		} else if (date.getMonth() == 4 || date.getMonth() == 6 || date.getMonth() == 9 || date.getMonth() == 11) {
			date.setDate(30);
			date.setMonth(date.getMonth() - 1);
		}
		return date;
	}
	
	/**
	 * 取得当前月份的第一天
	 * @param date 要处理的日期对象(Date())
	 */
	this.getFirstDayInMonth = function(date){
		return this.toDateFromStr(dateUtil.toStrFromDate(new Date(),"yyyy-MM-dd HH:mm:ss",false).replace(/-\d{2} /,"-01 "),true);
	}
	
	/**
	 * 取得当前月份的最后一天
	 * @param date 要处理的日期对象(Date())
	 */
	this.getLastDayInMonth = function(date){
		// 下一个月的当天
		var nextMonthDay = dateUtil.subtractDate(date, 'month', -1);
		return this.subtractDate(dateUtil.toDateFromStr(dateUtil.toStrFromDate(nextMonthDay,"yyyy-MM-dd HH:mm:ss",false).replace(/-\d{2} /,"-01 "),true),"day",1);
	}
	
	/**
	 * 取得当前季度的最后一天
	 * @param date 要处理的日期对象(Date())
	 */
	this.getLastDayInQuarter = function(date){
		var currentMonth = date.getMonth()+1;
		if (currentMonth >=1 && currentMonth <=3) {
			date.setMonth(2);
		} else if (currentMonth >=4 && currentMonth <=6) {
			date.setMonth(5);
		} else if (currentMonth >=7 && currentMonth <=9) {
			date.setMonth(8);
		} else if (currentMonth >=10 && currentMonth <=12) {
			date.setMonth(11);
		}
		return this.getLastDayInMonth(date);
	}
	
	/**
	 * 取得当前半年的最后一天
	 * @param date 要处理的日期对象(Date())
	 */
	this.getLastDayInHalfYear = function(date){
		var currentMonth = date.getMonth()+1;
		if (currentMonth >=1 && currentMonth <=6) {
			date.setMonth(5);
		} else if (currentMonth >=7 && currentMonth <=12) {
			date.setMonth(11);
		}
		return this.getLastDayInMonth(date);
	}
	
	/**
	 * 取得当前年的最后一天
	 * @param date 要处理的日期对象(Date())
	 */
	this.getLastDayInYear = function(date){
		date.setMonth(11);
		return this.getLastDayInMonth(date);
	}
}
