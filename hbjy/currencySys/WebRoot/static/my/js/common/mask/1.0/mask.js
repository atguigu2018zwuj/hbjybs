define(['./style.css'],function(require){
/**
 * 通用loading遮罩效果
 * Author:czy
 * 2015年12月14日17:08:05
 * Ver: 1.0
 * @param {Object} $
 */
(function($){
	$.extend($.fn,{
		mask: function(){
			var ele=this,$ele=$(ele),$mask;//dom元素
			var maskObj = {
				holder: ele
			}
		    /**
		     * 添加遮罩
		     */
		    var mask = function(){
		    	if(!$mask){
		    		$mask=$('<div style="display:none;" class="mask"><div class="loading"></div></div>');
		    		$ele.after($mask);
		    	}
		    		
				$mask.css('width',$ele.css('width'));
				$mask.css('height',$ele.css('height'));
				$mask.show();
				//offset只对可见元素有效，所以先显示后设置
				$mask.offset({left:$ele.offset().left,top: $ele.offset().top});
		    }
		    /**
		     * 取消遮罩
		     */
		    var unmask = function(){
		    	$mask.hide();	
		    }
		    
			maskObj.show = mask;
			maskObj.hide = unmask
			
			maskObj.getMaskObj = function(){
				return $mask;
			}
		    
			return maskObj;
		}
	});
})(jQuery);
});