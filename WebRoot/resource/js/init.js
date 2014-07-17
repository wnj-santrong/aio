/**
 * load js file with the filename specified and call callback function if present.
 * @param obj
 * obj = {};
 * obj.urlprefix: specified the path for the js file.
 * obj.filename: the filename  (example:  obj.filename = "/search.js?rev={%SEARCH_JS%}";)
 * obj.fn: callback function. (example:  obj.fn = function(jsfiles) {alert("hello" + jsfiles)}; )
 */
// jquery扩展
jQuery(function($){
	
	$.fn.checkboxVals = function(params) {
		var finalParams = {
			must : true,
			single : false
		};
		finalParams = $.extend(finalParams, params);
		
		var vals = "";
		$(this).each(function(){
			if($(this).attr("checked")) {
				vals += "," + $(this).val();
			}
		});
		if(vals != "") {
			vals = vals.substr(1);
		}
		
		if(finalParams.must && vals == "") {
			alert("请选择一条记录");
			return false;
		}
		
		if(finalParams.single && vals.indexOf(",") != -1) {
			alert("只能选择一条记录");
			return false;
		}
		return vals;
	};
	
});

// 全局初始化
function init() {   
    $(".close").click(function(e){
    	$(this).closest(".dialog").hide();
    });
    
	$(".submit").click(function(){
    	var form = $(this).closest("form");
    	if(form.length > 0){
    		form.ajaxSubmit({
	    		beforeSubmit : function(){
	    			form.find(".text_warn").removeClass("text_warn");
	    	        var isPass = true;
	    	        
	    	        // 必填检测
	    	        form.find("[required]").each(function(){
	    	        	if($(this).val().trim() == "") {
	    	        		$(this).addClass("text_warn");
	    	                isPass = false;
	    	        	}
	    	        });
	    	        
	    	        // 相同检测
	    	        form.find("[equalTo]").each(function(){
	    	        	var equalTo = $(this).attr("equalTo");
	    	        	if($(this).val().trim() != $("input[name=" + equalTo + "]").val().trim()) {
	    	        		$(this).addClass("text_warn");
	    	                isPass = false;
	    	        	}
	    	        });
	    	        
//	    	        //日期类型检测
//	    	        var textboxs = form.find(".required_date");
//	    	        for(var i=0;i<textboxs.size();i++){
//	    	            //var reg_date = /^[2]\d{3}\-[01]{0,1}\d\-[0123]{0,1}\d$/ig;//强检测
//	    	            var reg_date = /^\d{4}\-\d{1,2}\-\d{1,2}$/ig;//弱检测
//	    	            var val = textboxs.eq(i).val().trim();
//	    	            if(!val == "" && !reg_date.test(val)){
//	    	                isPass = false;
//	    	                break;
//	    	            }
//	    	        }
//	    	        
//	    	        //正整数检测        
//	    	        textboxs = form.find(".required_int");
//	    	        for(var i=0;i<textboxs.size();i++){
//	    	            var reg_int = /^\d*$/ig;
//	    	            var val = textboxs.eq(i).val().trim();
//	    	            if(!val == "" && !reg_int.test(val)){
//	    	                isPass = false;
//	    	                break;
//	    	            }
//	    	        }            
	    	        
	    	        return isPass;
	    		},
	    		success : function(result) {
	    			if(result == "success") {
	    				form.clearForm();
	    			}
	    			alert(Message.dynamic(result));
	    		}
	    	});
    	}
    	return false;
	});
};

// index模块初始化
function IndexClass() {
};

//index模块的具体页面初始化
IndexClass.prototype = {
	// 全页面
    index:function() {
    	$("code#pagename").remove();
    	
    	$(".navigator a").click(function() {
    		var pageName = $(this).text();
    		var pageUrl = $(this).attr("rel");
    		
    		$(".sub_top").html(pageName);
    		$.get(pageUrl, function(result){
    		    $(".sub_content").html(result);
    		    parsePageName();
    		  });
    	});
    	
    	$(".navigator a:first").click();
    },
    
    // 会议管理
    meeting:function() {
    },
    
    
    // 视频播放
    play:function() {
    },
    
    // 文件管理
    file:function() {
    	
    	$("#fileEdit").click(function(){
    		var values = $("input[name=CheckboxGroup1]").checkboxVals({single : true});
    		if(values) {
    			Boxy.load(Globals.ctx + "/file/fileEdit.action?id=" + values);
    		}
    	});
    	
    	$("#filePlay").click(function(){
    		var values = $("input[name=CheckboxGroup1]").checkboxVals({single : true});
    		if(values) {
    			var url = Globals.ctx + "/file/filePlay.action?id=" + values;
    			window.open(url,"play","fullscreen=no,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no");
    		}
    	});
    	
    	$("#fileDel").click(function(){
    		var values = $("input[name=CheckboxGroup1]").checkboxVals();
    		if(values) {
    			$.ajax({
    				type : "POST",
    				date : {ids : values},
    				url : Globals.ctx + "/file/fileDownload.action",
    				cache : false,
    				success : function(result){
    					alert(Message.dynamic(result));
    				}
    			});
    		}
    	});
    	
    	$("#fileDownload").click(function(){
    		var values = $("input[name=CheckboxGroup1]").checkboxVals();
    		if(values) {
    			$.ajax({
    				type : "POST",
    				date : {ids : values},
    				url : Globals.ctx + "/file/fileDownload.action",
    				cache : false,
    				success : function(result){
    					alert(Message.dynamic(result));
    					//TODO refleshList
    				}
    			});
    		}
    	});
    },
    
    // 系统管理
    setting:function() {
    	
    	// 获取数据库
    	var freshDbList = function() {
    		$.ajax({
				data : {},
				dataType : "json",
				url : Globals.ctx + "/setting/dbList.action",
				success : function(result){
					var html = "";
					for(var i=0;i<result.length;i++){
						html += "<li><span>" + result[i] + "</span>-----<a href=\"#\" class=\"dbRestore\">还原</a>---<a href=\"#\" class=\"dbDel\">删除</a></li>";
					}
					$("#dbList").html(html);
					
					
					// 删除数据库
					$(".dbDel").click(function(){
						var filename = $(this).parent().find("span").text();
						$.ajax({
							type : "POST",
							data : {"filename" : filename},
							url : Globals.ctx + "/setting/dbDel.action",
							cache : false,
							success : function(result){
								alert(Message.dynamic(result));
								freshDbList();
							}
						});
					});
					
					// 恢复数据库
					$(".dbRestore").click(function(){
						var filename = $(this).parent().find("span").text();
						$.ajax({
							type : "POST",
							data : {"filename" : filename},
							url : Globals.ctx + "/setting/dbRestore.action",
							cache : false,
							success : function(result){
								alert(Message.dynamic(result));
								freshDbList();
							}
						});
					});				
				}
    		});
    	};
    	
		// 备份数据库
		$("#dbBackup").click(function(){
			$.ajax({
				type : "POST",
				url : Globals.ctx + "/setting/dbBackup.action",
				cache : false,
				success : function(result){
					alert(Message.dynamic(result));
					freshDbList();
				}
			});
		});
    	
		freshDbList();
		
    }
};