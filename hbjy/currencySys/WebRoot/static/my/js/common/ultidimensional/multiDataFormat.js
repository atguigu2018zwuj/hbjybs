define(function(){
	var dataChange=function(dataArr,fileds,Flip,treesdata){
		/*
		 	fileds 为请求参数,dataArr为返回数据，Flip为是否行列变换,treesdata为文档树数据
		 * */
		var params=fileds.fileds;
		/*var params={
			rows:['城市','行业'],
			column:['银行','币种'],
			filter:[],
			value:['金额','笔数'],
		};*/
		var chooseIDsbox=fileds.filedids.rows;//横向已选择id
		var rows=params.rows,
			column=params.column,
			filter=params.filter,
			value=params.value;
		/*
		 * dataArr:原始数据
		 * params:计算参数
		 * 
		 */
		var bodydata=[],//table数据盒子;二维数组
		leftHeader={},//左侧内容盒子;
		topHeader={},//头部内容盒子;
		headerTit=[];//头部
		
		var getarrs=function(filed){	//循环出来左侧各个层级字段内容
			var arr=[];
			dataArr.map(function(item){
				var judge=true;
				arr.map(function(name){
					if(item[filed]==name){
						judge=false;
					}
				});
				if(judge){
					arr.push(item[filed]);
				}
			});
			return arr;
		};
		
		var topbox=[];//头部字段内容容器
		column.map(function(item){
			topbox.push(getarrs(item));
		});
		/*var valuebox=[];
		value.map(function(item){
			valuebox.push(item)
		});
		topbox['度量']=valuebox;*/
		var leftbox=[];
		rows.map(function(item){
			leftbox.push(getarrs(item));
		});
		var nums1=topbox.length;
		var nums2=leftbox.length;
		var getallthings=function(nums,data,fileds){	//字段组合排序
			var arrbox=[]
			function getArr(i,obj){
				var objs=$.extend({}, obj);
				if(i<nums-1){
					data[i].map(function(item){
						var objsone=$.extend({}, objs);
						objsone[fileds[i]]=item;
						getArr(i+1,objsone);
					})
				}else{
					data[i].map(function(item){
					var objsone=$.extend({}, objs);
						objsone[fileds[i]]=item;
						arrbox.push(objsone);
					})
				}
				
			};
			getArr(0,{})
			return arrbox;
		};
		var newtopbox=getallthings(nums1,topbox,column);	//所有顺序组合
		var newleftbox=getallthings(nums2,leftbox,rows);	//所有顺序组合
		
		var cmp = function( x, y ) { //判断是否两个对象相等
			var keys=Object.keys(x);
			for(var i=0;i<keys.length;i++){
				if(x[keys[i]]!=y[keys[i]]){
					return false
				}
			};
			return true
		};
		
		var leftheaders=[],//左侧所有可能；
			topheaders=[];//头部所有可能；
		var leftits=[];//左侧组合tit
			toptits=[];//头部tit组合
		dataArr.map(function(item){	//获取左侧和头部的所有可能
			var obj={},obj2={},arr1=[];arr2=[];
			for(var i=0;i<rows.length;i++){
				obj[rows[i]]=item[rows[i]];
				arr1.push(item[rows[i]]);
			};
			for(var i=0;i<column.length;i++){
				obj2[column[i]]=item[column[i]];
				arr2.push(item[column[i]]);
			};
			var judge=true,judge2=true;
			for(var i=0;i<leftheaders.length;i++){
				if(cmp(leftheaders[i],obj)){
					judge=false;
					break
				}
			};
			for(var i=0;i<topheaders.length;i++){
				if(cmp(topheaders[i],obj2)){
					judge2=false;
					break
				}
			};
			if(judge){
				leftheaders.push(obj);
				leftits.push(arr1.join('_'));
			}
			if(judge2){
				topheaders.push(obj2);
				toptits.push(arr2.join('_'));
			}
		});
		
		//给字段排序
		var combination=function(roleArr,arr,type){	//生成所有组合，按组合排序
			var data=[];var mytype=[];//捎带给所有title组合排序
			roleArr.map(function(item){
				arr.map(function(one,index){
					if(objInclude(item,one)){
						data.push(one);
						if(type=="left"){
							mytype.push(leftits[index]);
						}else{
							mytype.push(toptits[index]);
						}
					};
				})
			});
			if(type=="left"){
				leftits=mytype
			}else{
				toptits=mytype
			}
			return data;
		};
		leftheaders=combination(newleftbox,leftheaders,'left');//横坐标可以下钻
		topheaders=combination(newtopbox,topheaders,'top');//纵向不允许下钻
		function objInclude(fatherObj,sonObj){
			var arr=Object.keys(sonObj);
			var judge=true;
			for(var i=0;i<arr.length;i++){
				if(fatherObj[arr[i]]!=sonObj[arr[i]]){
					judge=false
				}
			};
			return judge;
		}
		//循环生成bodydata；
		if(!Flip){
			leftheaders.map(function(leftitem){
				var onRow=[];
				topheaders.map(function(topitem){
					var judge=false;
					dataArr.map(function(dataitem){
						if(objInclude(dataitem,leftitem)&&objInclude(dataitem,topitem)){
							value.map(function(valuekey){
								onRow.push(dataitem[valuekey])
							});
							judge=true;
						}
					})
					if(!judge){
						value.map(function(valuekey){
							onRow.push(0)
						});
					}
				});
				bodydata.push(onRow);
			})
		}else{
			topheaders.map(function(topitem){
				var len=value.length;
				var box=[];
				for(var i=0;i<len;i++){
					box.push([]);
				};
				leftheaders.map(function(leftitem){
					var judge=false;
					dataArr.map(function(dataitem){
						if(objInclude(dataitem,leftitem)&&objInclude(dataitem,topitem)){
							value.map(function(valuekey,index){
								box[index].push(dataitem[valuekey])
							});
							judge=true;
						}
					});
					if(!judge){
						value.map(function(valuekey,index){
							box[index].push(0)
						});
					}
				});
				bodydata=bodydata.concat(box);
			});
		}
		
		/**************生成左侧table数据************/
		/*console.log(leftbox);
		console.log(topbox);
		console.log(newleftbox);
		console.log(newtopbox);
		console.log(leftheaders);
		console.log(topheaders);*/
		//判断当前字段是否允许上卷或者下钻
		var getfather=function(name){	//是否允许上卷
			var nameid="",baseid="",judge=false;
			treesdata.map(function(item){
				if(item.name==name){
					var pid=item.pId;
					treesdata.map(function(father){
						if(father.id==pid){
							treesdata.map(function(grandpar){
								if(grandpar.id==father.pId){
									nameid=item.id;
									baseid=item.baseid;
									var filedkeysbox=Object.keys(chooseIDsbox);
									filedkeysbox.map(function(thebase){
										if(thebase==baseid&&item.level>1){
											judge=true;
										}
									})
								}
							})
						}
					})
				};
			});
			if(judge){
				return {nameid:nameid,baseid:baseid}
			}else{
				return [];
			}
		};
		var getson=function(name){	//是否允许下钻
			var nameid="";
			for(var i=0;i<treesdata.length;i++){
				var item=treesdata[i];
				if(item.name==name){
					var id=item.id;
					for(var j=0;j<treesdata.length;j++){
						var son=treesdata[j];
						if(son.pId==id){
							nameid=item.id;
							baseid=item.baseid;
							var filedkeysbox=Object.keys(chooseIDsbox);
							for(var n=0;n<filedkeysbox.length;n++){
								var thebase=filedkeysbox[n];
								if(thebase==baseid&&item.level>0){
									return {nameid:nameid,baseid:baseid}
								}
							}
						}
					}
				}
			};
			return [];
		}
		
		//获取table格式方法
		var getlineNums=function(parentArr,nums,roles,rolesbox,canExcavate){//parentArr为二维数组；nums为要取出的层级；roles为所选行或列数组
			var datas=[];													//canExcavate是允许上卷下钻的table，（左侧允许，头部不允许）
			var getmoreFloors=function(parents,floor){
				var morefloordata=[];
				parents.map(function(item){
					var index=floor;
					for(var j=0;j<rolesbox[index].length;j++){
						var arrs=[];
						item.map(function(onelist){
							if(onelist[roles[index]]==rolesbox[index][j]){
								arrs.push(onelist);
							};
						});
						morefloordata.push(arrs);
					};
				});
				return morefloordata;
			}
			var nowdata=parentArr;
			for(var i=0;i<=nums;i++){
				if(i!=nums){
					nowdata=getmoreFloors(nowdata,i);
				}else{
					nowdata.map(function(item){
						for(var j=0;j<rolesbox[nums].length;j++){
							var obj={
								text:rolesbox[nums][j],
								dataNums:0,
								updata:0,
								downdata:0
							};
							if(!Flip && canExcavate){//判断是否下钻上卷
								var name=rolesbox[nums][j];
								if(getfather(name).length!=0){
									obj.updata=1;
									obj.id=getfather(name).nameid;
									obj.baseid=getfather(name).baseid;
								};
								if(getson(name).length!=0){
									obj.downdata=1;
									obj.id=getson(name).nameid;
									obj.baseid=getson(name).baseid;
								}
							};
							item.map(function(onelist){
								if(onelist[roles[nums]]==rolesbox[nums][j]){
									obj.dataNums=obj.dataNums+1;
								};
							});
							if(obj.dataNums!=0){
								datas.push(obj);
							}
						};
					});
				}
			};
			return datas;
		}
		
		var gettabledata=function(roles,parentArr,rolesbox,canExcavate){
			var arr=[];
			for(var i=0;i<roles.length;i++){
				var arrs=[];
				arrs.push(parentArr);
				arr[i]=getlineNums(arrs,i,roles,rolesbox,canExcavate)
			};
			return arr;
		};
		leftHeader=gettabledata(rows,leftheaders,leftbox,true);
		topHeader=gettabledata(column,topheaders,topbox,false);
		
		var setvalueTit=function(myarr,roles){	//插入度量字段
			myarr.map(function(item){
				item.map(function(one){
					one.dataNums=one.dataNums*value.length;
				})
			});
			var arr=[];
			for(var i=0;i<myarr[myarr.length-1].length;i++){
				value.map(function(item){
					arr.push({text:item,dataNums:1})
				})
			};
			myarr.push(arr);
			return myarr;
		}
		
		if(Flip){//如果行列转换，变换标题对应关系
			headerTit=column.concat();
			headerTit.push('度量');
			topHeader=setvalueTit(topHeader,column);
			var arr=topHeader.concat();
			topHeader=leftHeader;
			leftHeader=arr;
			var arr2=leftits.concat();
			leftits=toptits;
			toptits=arr2
		}else{
			headerTit=rows.concat();
			topHeader=setvalueTit(topHeader,column)
		};
		
		return {
			topHeader:topHeader,
			leftHeader:leftHeader,
			bodydata:bodydata,
			headerTit:headerTit,
			leftits:leftits,
			toptits:toptits
		}
	};
	
	return dataChange
})