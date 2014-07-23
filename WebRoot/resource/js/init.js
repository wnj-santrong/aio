// 全局初始化
function init() {   
};

// index模块初始化
function IndexClass() {
};

//index模块的具体页面初始化
IndexClass.prototype = {
	login:function() {
		$(".submit").bindFormClick({afterSubmit : function() {
			window.location.href= Globals.ctx + "/index.action";
		}});
	},
	
	// 首页外部框架
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
    	// 如果界面是开启会议的状态，锁定修改
    	if(isLive == 1) {
    		// 设置disabled属性会导致jquery form提交的时候拿不到数据，所以在按钮操作前要还原为false
    		$(".sub_content input").attr('disabled', true);
    		$(".sub_content select").attr('disabled', true);
    		$(".sub_content textarea").attr('disabled', true);
    	}
    	
    	var recordMode = $("input[name=recordMode]").val();
    	$("#a" + recordMode).addClass("cur_layout");
    	
    	$("#layoutContainer img").click(function() {
    		var recordMode = $(this).attr("id").substr(1);
    		alert(recordMode);
    		$("input[name=recordMode]").val(recordMode);
    		
    		$("#layoutContainer img").removeClass("cur_layout");
    		$(this).addClass("cur_layout");
    	});
    	
    	// 激活界面
    	var activeElements = function() {
    		$(".sub_content input").attr('disabled', false);
    		$(".sub_content select").attr('disabled', false);
    		$(".sub_content textarea").attr('disabled', false);
    	};
    	
    	var freshCurrentModel = function() {$(".navigator a:first").click();}
    	
    	$(".save").bindFormClick({url : Globals.ctx + '/meeting/save.action'});
    	
    	$(".openLive").bindFormClick({url : Globals.ctx + '/meeting/openLive.action', beforeSubmit : activeElements, afterSubmit : freshCurrentModel});
    	
    	$(".closeLive").bindFormClick({url : Globals.ctx + '/meeting/closeLive.action', beforeSubmit : activeElements, afterSubmit : freshCurrentModel});
    	
    	$(".startRecord").bindFormClick({url : Globals.ctx + '/meeting/startRecord.action', beforeSubmit : activeElements, afterSubmit : freshCurrentModel});
    	
    	$(".stopRecord").bindFormClick({url : Globals.ctx + '/meeting/stopRecord.action', beforeSubmit : activeElements, afterSubmit : freshCurrentModel});
    	
    	// 获取显示数据源的弹框
    	var dsGet = function(id, mid) {
			Boxy.load(Globals.ctx + "/datasource/dsGet.action?id=" + id + "&meetingId=" + mid, {
				modal : true,
				afterShow : function() {
			    	// 绑定form提交
			    	$(".submit").bindFormClick({afterSubmit : freshCurrentModel});
			    	
			    	// 绑定取消
			    	$(".close").bindFormClose();
				}
			});
    	};
    	
    	$(".add").click(function() {
    		var index = $(".dsList input[type=text]").length + 1;
    		if(index > 3) {
    			Boxy.alert(Message.dynamic("warn_datasource_already_max", Globals.vedioCount));
    			return;
    		}
    		
    		var id = '';
    		var mid = $(".sub_content input[name=id]").val();
    		dsGet(id, mid);

    	});
    	
    	$(".dsEdit").click(function() {
    		var id = $(this).parent().find("input[name=dsId]").val();
    		var mid = $(".sub_content input[name=id]").val();
    		dsGet(id, mid);
    	});
    	
    	$(".dsDel").click(function() {
    		var id = $(this).parent().find("input[name=dsId]").val();
    		$.delConfirm(function(){
    			$.simplePost({url : Globals.ctx + "/datasource/dsDel.action", data : {"id" : id}, msgParams : Globals.vedioCount, callback : freshCurrentModel});
    		});
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
    				$.simplePost({url : Globals.ctx + "/file/fileDel.action", data : {ids : values}, callback : freshList});
    			});
    		}
    	});
    	
    	$("#fileDownload").click(function(){
    		var values = $("input[name=CheckboxGroup1]").checkboxVals();
    		if(values) {
        		$.simplePost({url : Globals.ctx + "/file/fileDownload.action", data : {ids : values}});    			
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
						$.simplePost({url : Globals.ctx + "/setting/dbDel.action", data : {"filename" : filename}, callback : freshDbList});
					});
					
					// 恢复数据库
					$(".dbRestore").click(function(){
						var filename = $(this).parent().find("span").text();
						$.simplePost({url : Globals.ctx + "/setting/dbRestore.action", data : {"filename" : filename}});
					});				
				}
    		});
    	};
    	
		// 备份数据库
		$("#dbBackup").click(function(){
			$.simplePost({url : Globals.ctx + "/setting/dbBackup.action", callback : freshDbList});
		});
    	
		freshDbList();
		
    }
};