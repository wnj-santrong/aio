// 全局初始化
function init() {   
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
    		    $("code#pagename").remove();
    		  });
    	});
    	
    	$(".navigator a:first").click();
    },
    
    // 会议管理
    meeting:function() {
    	//TODO 如果界面是开启会议的状态，锁定修改
    	
    	$(".save").bindFormClick({url : Globals.ctx + '/meeting/save.action'});
    	
    	$(".openLive").bindFormClick({url : Globals.ctx + '/meeting/openLive.action',
    		afterSubmit : function(form) {
    			$(".navigator a:first").click();//刷新页面以便更新按钮
    		}
    	});
    	
    	$(".closeLive").bindFormClick({url : Globals.ctx + '/meeting/closeLive.action',
    		afterSubmit : function(form) {
    			$(".navigator a:first").click();//刷新页面以便更新按钮
    		}
    	});
    	
    	$(".startRecord").bindFormClick({url : Globals.ctx + '/meeting/startRecord.action',
    		afterSubmit : function(form) {
    			$(".navigator a:first").click();//刷新页面以便更新按钮
    		}
    	});
    	
    	$(".stopRecord").bindFormClick({url : Globals.ctx + '/meeting/stopRecord.action',
    		afterSubmit : function(form) {
    			$(".navigator a:first").click();//刷新页面以便更新按钮
    		}
    	});  	
    },
    
    
    // 视频播放
    play:function() {
    },
    
    // 文件管理
    file:function() {
    	
    	var fileCount = $("input[name=fileCount]").val();
    	var pageSize = $("input[name=pageSize]").val();
    	var currentPage = 0;
    	
    	var freshList = function(current_page, containers) {
    		if(typeof(current_page) == "number") {
    			currentPage = current_page;
    		}
    		
			$.ajax({
				data : {pageNum : currentPage},
				dataType : "json",
				url : Globals.ctx + "/file/fileList.action",
				success : function(result){
					var html = "";
					for(var i=0;i<result.length;i++){
    				    html += "<tr>";
    				    html += "<td><input type=\"checkbox\" name=\"CheckboxGroup1\" value=\"" + result[i].id + "\" id=\"CheckboxGroup1_0\" /></td>";
    				    html += "<td>" + result[i].duration + "</td>";
    				    html += "<td>" + result[i].courseName + "</td>";
    				    html += "<td>" + result[i].fileSize + "</td>";
    				    html += "<td>" + result[i].status + "</td>";
    				    html += "<td>" + result[i].level + "</td>";
    				    html += "<td>" + result[i].teacher + "</td>";
    				    html += "<td>" + result[i].remark + "</td>";
    				    html += "</tr>";
					}
					$("#fileList").html(html);
				}
			});
    	};
    	
    	$("#pagination").pagination(fileCount, {
    		items_per_page : pageSize,
    		callback : freshList
    	});
    	
    	
    	$("#fileEdit").click(function(){
    		var values = $("input[name=CheckboxGroup1]").checkboxVals({single : true});
    		if(values) {
    			Boxy.load(Globals.ctx + "/file/fileEdit.action?id=" + values, {
    				modal : true,
    				afterShow : function() {
    			    	// 绑定form提交
    			    	$(".submit").bindFormClick({afterSubmit : freshList});
    			    	
    			    	// 绑定取消
    			    	$(".close").bindFormClose();
    				}
    			});
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
    			$.delConfirm(function(){
    				$.simplePost(Globals.ctx + "/file/fileDel.action", {ids : values}, freshList);
    			});
    		}
    	});
    	
    	$("#fileDownload").click(function(){
    		var values = $("input[name=CheckboxGroup1]").checkboxVals();
    		if(values) {
        		$.simplePost(Globals.ctx + "/file/fileDownload.action", {ids : values});    			
    		}
    	});
    },
    
    // 系统管理
    setting:function() {
    	
    	// 绑定所有form提交
    	$(".submit").bindFormClick({afterSubmit : function(form){
    		if(form.attr("id") == "setting_user") {
    			form.clearForm();
    		}
    	}});
    	
    	// 获取wan
    	$.ajax({
    		data : {type : 1},
    		dataType : "json",
    		url : Globals.ctx + "/setting/networkGet.action",
    		success : function(result) {
    			$("#setting_wan input[name=ip]").val(result.ip);
    			$("#setting_wan input[name=mask]").val(result.mask);
    			$("#setting_wan input[name=gateway]").val(result.gateway);
    		}
    	}); 
    	
    	// 获取lan
    	$.ajax({
    		data : {type : 0},
    		dataType : "json",
    		url : Globals.ctx + "/setting/networkGet.action",
    		success : function(result) {
    			$("#setting_lan input[name=ip]").val(result.ip);
    			$("#setting_lan input[name=mask]").val(result.mask);
    			$("#setting_lan input[name=gateway]").val(result.gateway);
    		}
    	});
    	
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
						$.simplePost(Globals.ctx + "/setting/dbDel.action", {"filename" : filename}, freshDbList);
					});
					
					// 恢复数据库
					$(".dbRestore").click(function(){
						var filename = $(this).parent().find("span").text();
						$.simplePost(Globals.ctx + "/setting/dbRestore.action", {"filename" : filename});
					});				
				}
    		});
    	};
    	
		// 备份数据库
		$("#dbBackup").click(function(){
			$.simplePost(Globals.ctx + "/setting/dbBackup.action", null, freshDbList);
		});
    	
		freshDbList();
		
    }
};