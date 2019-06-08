var url = $("#url").val();
var urseVerify = '';
define(function(require) {
	$(function() {
		var $dialog = require('dialog');
		/*Verify();*/
		/*alert("1");*/
		// 如果是ie就不显示清除按钮
		/*var ieReg = /(msie\s|trident.*rv:)([\w.]+)/;
		if (!ieReg.exec(navigator.userAgent.toLowerCase())) {
			
			$('.form-import #urseName').focus(function() {
				$('.clear_name').show();
				$('.clear_name').css('display', 'inline-block');
				$('.clear_name').click(function() {
					$('#urseName').val('')
				})
			})
			$('.bank-account').mouseleave(function() {
				if (!$('#urseName').val()) {
					$('.clear_name').hide();
				}
			})
		}*/

		/*$('#checkCode').mousedown(function() {
			Verify();
		});*/

		$('.form-import :input').blur(function() {
			// 用户名
			if ($(this).is('#urseName')) {
				checkUname();
			}
			// 验证密码 
			if ($(this).is('#ursePassword')) {
				checkPwd();
			}
			// 验证码 
			/*if ($(this).is('#urseVerify')) {
				checkVerify();
			}*/
		});
		function checkUname() {
			var name = $('#urseName').val();
			if (name.length < 1 || name.length > 12) {
				$('#urseName').parent().next('.validation-tips').html(
						'<font class="correct">柜员号不能为空</font>');
			} else {
				$('#urseName').parent().next('.validation-tips').html('');
				return true;
			}
		}
		function checkPwd() {
			var psd = $('#ursePassword').val();
			if (psd.length < 1 || psd.length > 12) {
				$('#ursePassword').parent().next('.validation-tips').html(
						'<font class="correct">密码不能为空</font>');
			} else {
				$('#ursePassword').parent().next('.validation-tips').html('');
				return true;
			}
		}
		/*function checkVerify() {
			var ver = $('#urseVerify').val();
			if (ver.length !== 4) {
				$('#urseVerify').parent().siblings('.validation-tips').html(
						'<font class="correct">长度是4个字符</font>');
			} else {
				if (urseVerify.toUpperCase() != ver.toUpperCase()) {
					$('#urseVerify').parent().siblings('.validation-tips').html(
							'<font class="correct">验证码错误！</font>');
				} else {
					$('#urseVerify').parent().siblings('.validation-tips').html('');
					return true;
				}
			}
		}*/

		// //提交，最终验证。
		$('#subButton').click(function() {
			/*alert("b");*/
			if (checkPwd()&checkUname()) {
				/*Verify();*/
				var url = $("#url").val();
				$.ajax({
					url : url + 'signIn',
					type : 'post',
					data : {
						id : $('#urseName').val(),
						username : $('#urseName').val(),
						password : $('#ursePassword').val()
					},
					async : true,
					success : function(data) {
						if (data.code == '1000') {
							window.location = url + "index";
						} else if(data.code == '6666'){//确认登录
							loginFla(data);
						}else if (data.code = '3102') {
							$('.form-import :input').parent().next('.validation-tips').html('<font class="correct">' + data.message + '</font>');
						}else{
							$('.form-import :input').parent().next('.validation-tips').html('<font class="correct">' + data.message + '</font>');
						}
					},
					error : function(data) {
						$('.form-import :input').parent().next('.validation-tips').html('<font class="correct">' + data.message + '</font>');
					}
				});
			}else{
				/*Verify();*/
			}
		});

		// 更新验证码
		/*function Verify() {
			code = "";
			var codeLength = 4; // 验证码的长度
			var checkCode = document.getElementById("checkCode");
			var codeChars = new Array(2, 3, 4, 5, 6, 7, 8, 9, 'A', 'B', 'C',
					'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q',
					'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'); // 所有候选组成验证码的字符，当然也可以用中文的
			for (var i = 0; i < codeLength; i++) {
				var charNum = Math.floor(Math.random() * 32);
				code += codeChars[charNum];
			}
			if (checkCode) {
				checkCode.className = "code";
				checkCode.innerHTML = code.trim();
			}
			urseVerify = code;
		}*/

		// 验证验证码
		function VerificationCode() {
			var ver = $(this).val();
			if (ver.length !== 4) {
				$(this).parent().siblings('.validation-tips').html(
						'<font class="correct">长度是4个字符</font>');
			} else {
				if (urseVerify.toUpperCase() != ver.toUpperCase()) {
					$(this).parent().siblings('.validation-tips').html(
							'<font class="correct">验证码错误！</font>');
				} else {
					$(this).parent().siblings('.validation-tips').html('');
				}
			}
		}
	});
});

//enter键
$(document).keydown(function(event){
	var n = event.keyCode;
	if(n=='13'){
		$("#subButton").click();
	}
});

//确认登录
function loginFla(data){
	$dialog.confirm({
        title:'提示',
        width:350,
        height:50, 
        content:data.message,
        okValue: '是',
        ok: function(){
        	$.ajax({
				url : url + 'signIn',
				type : 'post',
				data : {
					id : $('#urseName').val(),
					username : $('#urseName').val(),
					password : $('#ursePassword').val(),
					loginfla:'ok'
				},
				async : true,
				success : function(data) {
					if (data.code == '1000') {
						window.location = url + "index";
					}else{
						$('.form-import :input').parent().next('.validation-tips').html('<font class="correct">' + data.message + '</font>');
					}
				},
				error : function(data) {
					$('.form-import :input').parent().next('.validation-tips').html('<font class="correct">' + data.message + '</font>');
				}
        	});
        },
        cancelValue: '否',
            cancel: true,
            onclose: function(){}
       }).showModal();
}