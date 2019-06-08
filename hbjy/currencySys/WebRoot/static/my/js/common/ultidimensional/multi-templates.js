define(function(require){
	var ptl='<div class="multiAnalysis">'+
			'<div class="multi-operation-box">'+
				'<div class="multi-treescont">'+
					'<h4 class="multi-tit-h4">字段选择</h4>'+
					'<div class="multitreebox">'+
						/*'<div class="multisearchbox">'+
							'<span  class="multi-searchbtn"></span>'+
							'<input type="text" placeholder="关键字" class="multi-search">'+
						'</div>'+*/
						'<ul id="multitree" class="ztree"></ul>'+
					'</div>'+
				'</div>'+
				'<div class="multi-dragsortarea">'+
					'<h4 class="multi-tit-h4">拖拽排序</h4>'+
					'<div class="multi-drag-zoom" id="multidrag">'+
						'<ul class="multi-conditions">'+
							'<li oncontextmenu="return false">'+
								'<span class="multi-condition-tit multicons-rows">行</span>'+
								'<div class="multi-conditioncont multi-dimension" id="multi-condition-row"></div>'+
							'</li>'+
							'<li oncontextmenu="return false">'+
								'<span class="multi-condition-tit multicons-lie">列</span>'+
								'<div class="multi-conditioncont multi-dimension" id="multi-condition-column"></div>'+
							'</li>'+
							'<li oncontextmenu="return false">'+
								'<span class="multi-condition-tit multicons-filters">过滤</span>'+
								'<div class="multi-conditioncont" id="multi-condition-filter"></div>'+
							'</li>'+
							'<li oncontextmenu="return false">'+
								'<span class="multi-condition-tit multicons-value">度量</span>'+
								'<div class="multi-conditioncont multi-dimension" id="multi-condition-size"></div>'+
							'</li>'+
						'</ul>'+
						'<div class="multi-retract"><span class="multi-sqbtn glyphicon glyphicon-chevron-left"></span></div>'+
					'</div>'+
				'</div>'+
			'</div>'+
			'<div class="multi-result-box">'+
				'<div class="multi-toolsbox">'+
					'<div class="multi-button multi-loadmodel">导出数据</div>'+
					/*'<div class="multi-button multi-savemodel">保存列表模板</div>'+*/
					'<ul class="multi-toolslist">'+
						/*<li class="multi-choses">
							<span class="multi-btn multi-btn-js" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" id="multi-dropdownMenu1"></span>
							<ul class="dropdown-menu" aria-labelledby="dropdownMenu1">
							    <li ><a class="multi-js-choses" href="#">占比</a></li>
							    <li ><a class="multi-js-choses" href="#">计算</a></li>
						  	</ul>
						</li>*/
						'<li class="multi-choses">'+
							'<span chartstype="bar" class="multi-btn multi-btn-charts  multi-btn-charts-bar"></span>'+
						'</li>'+
						'<li class="multi-choses">'+
							'<span chartstype="line" class="multi-btn multi-btn-charts  multi-btn-charts-line"></span>'+
						'</li>'+
						'<li class="multi-choses">'+
							'<span chartstype="pei" class="multi-btn multi-btn-charts multi-btn-charts-pei"></span>'+
						'</li>'+
						'<li class="multi-choses">'+
							'<span class="multi-btn multi-btn-change"></span>'+
						'</li>'+
					'</ul>'+
				'</div>'+
				'<div class="multi-tablebox">'+
					'<table class="multi-table" id="multi-table"></table>'+
					'<div class="multi-chartbox"></div>'+
				'</div>'+
				'<div style="position:absolute;width:20px;left:0;top:0;height:99%;background:#fff;z-index:99"></div>'+
				'<div class="multi-table-positionbox"><table id="multi-table-rowstit" class="multi-table-rowstit"></table></div>'+
			'</div>'+
		'</div>';
		
		return ptl;
})