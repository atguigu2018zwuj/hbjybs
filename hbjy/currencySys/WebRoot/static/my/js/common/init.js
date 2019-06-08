define(function(require){
    $(function(){
    	//扩展mining对象
    	$.extend(mining,{
    		//st-grid分页模板
    		grid:{
    			pageTemplate : '<input type="button" value="首页" class="page-bt first-page" title="首页">' +
    	        '<input type="button" value="上一页" class="page-bt pre-page" title="上一页">' +
    	        '第<span class="pageNow">{pageNo}</span>页' +
    	        '<input type="button" value="下一页" class="page-bt next-page" title="下一页">' +
    	        '<input type="button" value="末页" class="page-bt last-page" title="末页">' +
    	        '共{totalPage}页&nbsp;&nbsp;' +
    	        '共{totalCount}条&nbsp;&nbsp;' +
    	        '<input type="text" id="somePage" size="4" maxlength="5" class="some-page" style="padding:0;text-align:center;"/>'+
    	        '<input type="button" value="跳转" class="page-bt turn-page">'
    		},
    		//JSON格式请求
    		jsonAjax: function(op){
    			$ajax.ajax($.extend(op,{
    				type: op.type==undefined?"post":op.type,
    				datatype: 'json',
    				contentType: 'application/json',
    				data: JSON.stringify(mining.utils.isEmpty(op.data)?'{}':op.data)
    			}));
    		}
    	});
    	
    	$.extend(mining.utils,{
    		icheckInit:function(){
				require.async('icheck',function(){
					$('input[type=checkbox]').iCheck({
						checkboxClass: 'icheckbox_square-green'
					});
					$('input[type=radio]').iCheck({
						radioClass: 'iradio_square-green'
					});
				});
			},
			i18n:function(){
				require.async('i18n',function(){
					jQuery.i18n.properties({
					    name:'Language', 
					    path:staticUrl+'/my/js/common/language/', 
					    mode:'both',
					    language:$cookie.get('language') || 'zh_CN',//zh_CN,en_GB
					    callback: function() {
					    	 $("[data-locale]").each(function(){  
			                    $(this).html($.i18n.prop($(this).data("locale")));  
			                });  
					    }
					});
				})
			}
    	});
    });
});