define(function(){ 
 	var json2xls = function(options) {
 			var timestamp = Date.parse(new Date());
	    	var defaults = {
	    			data: {thead:[],tbody:[]},
	        		filename: '多维分析'+timestamp,
					ignoreColumn: [],
					consoleLog:'false'
				}
	        
			var options = $.extend(defaults, options);
			var el = this;
			var excel="<table>";
/*			// Header
			$.each(options.data.thead, function(i,trs){
				excel += "<tr>";
				$.each(trs, function(index,data){
					if(defaults.ignoreColumn.indexOf(index) == -1){
						excel += "<td>" + data+ "</td>";
					}
				});
				excel += '</tr>';	
			});
*/			
			// Row Vs Column
			var rowCount = 1;
			
			$.each(options.data.tbody, function(i,trs){
				excel += "<tr>";
				var colCount=0;
				$.each(trs, function(index,data){
					if(defaults.ignoreColumn.indexOf(index) == -1){
						excel += "<td>"+data+"</td>";
					}
					colCount++;
				});
				rowCount++;
				excel += '</tr>';
			});
			excel += '</table>'
			
			if(defaults.consoleLog == 'true'){
				console.log(excel);
			}
			
			var excelFile = "<html xmlns:o='urn:schemas-microsoft-com:office:office' xmlns:x='urn:schemas-microsoft-com:office:"+defaults.type+"' xmlns='http://www.w3.org/TR/REC-html40'>";
			excelFile += "<head><meta charset='UTF-8'>";
			excelFile += "<!--[if gte mso 9]>";
			excelFile += "<xml>";
			excelFile += "<x:ExcelWorkbook>";
			excelFile += "<x:ExcelWorksheets>";
			excelFile += "<x:ExcelWorksheet>";
			excelFile += "<x:Name>";
			excelFile += "{worksheet}";
			excelFile += "</x:Name>";
			excelFile += "<x:WorksheetOptions>";
			excelFile += "<x:DisplayGridlines/>";
			excelFile += "</x:WorksheetOptions>";
			excelFile += "</x:ExcelWorksheet>";
			excelFile += "</x:ExcelWorksheets>";
			excelFile += "</x:ExcelWorkbook>";
			excelFile += "</xml>";
			excelFile += "<![endif]-->";
			excelFile += "</head>";
			excelFile += "<body>";
			excelFile += excel;
			excelFile += "</body>";
			excelFile += "</html>";
			
			var uri = 'data:application/vnd.ms-excel;charset=utf-8,' + encodeURIComponent(excelFile);
	    
		    var link = document.createElement("a");    
		    link.href = uri;
		    
		    link.style = "visibility:hidden";
		    link.download = defaults.filename + ".xls";
		    
		    document.body.appendChild(link);
		    link.click();
		    document.body.removeChild(link);
	};
	
	return json2xls
 	
})