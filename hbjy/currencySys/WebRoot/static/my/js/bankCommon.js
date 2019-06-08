define(function(require){
	require('select2');
	require('datetimepicker');
	require('My97DatePicker');
	require('timeFormat');
	require('daterangepicker');
	$(function(){
		// 导航一级菜单展开切换
		$(document).off('click','.zl_nav_lists h4').on('click','.zl_nav_lists h4',function(event){
			var $this = $(this).children('.zl_triangle');
			if($this.hasClass('zl_triangle_open')){
			 	$this.removeClass('zl_triangle_open').addClass('zl_triangle_close');
			 	$this.parents('h4').siblings('.zl_sublist').css('display','none');
			 	$this.parents('.zl_nav_lists').removeClass('zl_menu_active');
			}else if($this.hasClass('zl_triangle_close')){
				$('.zl_nav_lists .zl_sublist').css('display','none');
				$('.zl_nav_lists').removeClass('zl_menu_active');
				$('.zl_nav_lists .zl_triangle').removeClass('zl_triangle_open').addClass('zl_triangle_close');
				$this.removeClass('zl_triangle_close').addClass('zl_triangle_open');
			 	$this.parents('h4').siblings('.zl_sublist').css('display','block');
			 	$this.parents('.zl_nav_lists').addClass('zl_menu_active');
			}
		});
		
		// 导航二级菜单展开切换
		$(document).off('click','.zl_nav_lists1 h4').on('click','.zl_nav_lists1 h4',function(event){
			var $this = $(this).children('.zl_triangle1');
			if($this.hasClass('zl_triangle_open1')){
				$('.zl_nav_lists1 .zl_sublist1').css('display','none');
			 	$this.removeClass('zl_triangle_open1').addClass('zl_triangle_close1');
			 	$this.parents('h4').siblings('.zl_sublist1').css('display','none');
			 	$this.parents('.zl_nav_lists1').removeClass('zl_menu_active1');
			}else if($this.hasClass('zl_triangle_close1')){
				$('.zl_nav_lists1 .zl_sublists1').css('display','block');
				$('.zl_sublist1').css('display','none');
				$('.zl_nav_lists1').removeClass('zl_menu_active1');
				$('.zl_nav_lists1 .zl_triangle1').removeClass('zl_triangle_open1').addClass('zl_triangle_close1');
			 	$this.removeClass('zl_triangle_close1').addClass('zl_triangle_open1');
			 	$this.parents('h4').siblings('.zl_sublist1').css('display','block');
			 	$this.parents('.zl_nav_lists1').addClass('zl_menu_active1');
			}
		})
		
		// 导航三级菜单展开切换
		$(document).off('click','.zl_nav_lists2 h4').on('click','.zl_nav_lists2 h4',function(event){
			var $this = $(this).children('.zl_triangle2');
			if($this.hasClass('zl_triangle_open2')){
				$('.zl_nav_lists2 .zl_sublist2').css('display','none');
			 	$this.removeClass('zl_triangle_open2').addClass('zl_triangle_close2');
			 	$this.parents('h4').siblings('.zl_sublist2').css('display','none');
			 	$this.parents('.zl_nav_lists2').removeClass('zl_menu_active2');
			}else if($this.hasClass('zl_triangle_close2')){
				$('.zl_nav_lists2 .zl_sublists2').css('display','block');
				$('.zl_sublist2').css('display','none');
				$('.zl_nav_lists2').removeClass('zl_menu_active2');
				$('.zl_nav_lists2 .zl_triangle2').removeClass('zl_triangle_open2').addClass('zl_triangle_close2');
			 	$this.removeClass('zl_triangle_close2').addClass('zl_triangle_open2');
			 	$this.parents('h4').siblings('.zl_sublist2').css('display','block');
			 	$this.parents('.zl_nav_lists2').addClass('zl_menu_active2');
			}
		})

		// 导航栏左右收缩
		$(document).off('click','.zl_collapse').on('click','.zl_collapse',function(event){
			var $this = $(this);
			if($this.hasClass('zl_collapse_open')){
				$(".zl_nav").width('30px');
				$(".zl_main").css("marginLeft","30px");
				$('#zl_menu_nav').css('display','none');
				$this.text('>>');
				$this.removeClass('zl_collapse_open').addClass('zl_collapse_close');
			}else{
				$(".zl_nav").width('279px');
				$(".zl_main").css("marginLeft","279px");
				$('#zl_menu_nav').css('display','block');
				$this.text('<<');
				$this.removeClass('zl_collapse_close').addClass('zl_collapse_open');
			}
			$('#tabs').tabs('resize',{  
				plain : false,  
				boder : false,  
				width:$(window).width(),  
				height:$(window).height()-50  
			});
		})
	});
});

