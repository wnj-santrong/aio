// 全局初始化
function init() {   
};

// index模块初始化
function IndexClass() {
};

//index模块的具体页面初始化
IndexClass.prototype = {
	// 公共方法，绑定登录
	_bindLogin:function() {
    	$(".login_submit").bindFormClick({tip : false, isGoodCall : false, afterSubmit : 
    		function(form, result) {
    			if(result != "success") {
    				Boxy.alert(Message.dynamic(result));
    			}else{
    				window.location.href= Globals.ctx + "/index.action";
    				return;
    			}
    		}
    	});
    	// 绑定取消
    	$(".close").bindFormClose();
	},
	
	// 登录页
	login:function() {
		this._bindLogin();
	},
	
	// 首页
	index:function() {
		var _this = this;
		$("code#pagename").remove();
		
		var pageName = '';
		var pageUrl = 'play/home.action';
		
		$(".sub_top").html(pageName);
		$.get(pageUrl, null, function(result){
		    $(".sub_content").html(result);
		    parsePageName();
		    $("code#pagename").remove();
		  });
		
		$(".user_login").click(function() {
			Boxy.load(Globals.ctx + "/loginForm.action", {
				modal : true,
				afterShow : _this._bindLogin
			});
		});
	},
	
	// 管理页面主框架
    manage:function() {
    	$("code#pagename").remove();
    	
    	$(".navigator a").click(function() {
    		var pageName = $(this).text();
    		var pageUrl = $(this).attr("rel");
    		
    		$(".sub_top").html(pageName);
    		$.get(pageUrl, null, function(result){
    		    $(".sub_content").html(result);
    		    parsePageName();
    		    $("code#pagename").remove();
    		  });
    	});
    	
		$(".logout_submit").click(function() {
			$.simplePost({url : Globals.ctx + "/logout.action", tip : false, callback : function(result) {
				if(result != 'success') {
					Boxy.alert(Message.dynamic(result));
				}else{
					window.location.href= Globals.ctx + "/index.action";
				}
			}});
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
    	var freshPage = function(opts) {
    		opts = $.extend({
    			keyword : keyword,
    			pageNum : 0
    			}, opts || {});
    		
    		var pageUrl = Globals.ctx + "/play/home.action";
    		
    		$.get(pageUrl, {keyword : opts.keyword, pageNum : opts.pageNum}, function(result){
    		    $(".sub_content").html(result);
    		    //虽然自身已经被替换，JS仍可以继续执行
    		    parsePageName();
    		    $("code#pagename").remove();
    		  });
    	};    	
    	
    	$("#pagination").pagination(fileCount, {
    		items_per_page : pageSize,
    		current_page : pageNum,
    		callback : function(current_page, containers) {
    			var params = {
    				keyword : keyword,
    				pageNum : current_page
    			};
    			freshPage(params);
    		}
    	});
    	
    	$(".search_btn").click(function() {
    		freshPage({keyword : $("input[name=keywork]").val()});
    	});
    	
    	$(".tag").click(function() {
    		$(".tag").removeClass(".tag");
    		$(this).addClass("cur_tag");
    		freshPage({keyword : $(this).text()});
    	});
    	
    	$(".tag_add").click(function() {
			Boxy.load(Globals.ctx + "/tag/tagGet.action", {
				modal : true,
				afterShow : function() {
			    	// 绑定form提交
			    	$(".submit").bindFormClick({tip : false, afterSubmit : freshPage});
			    	
			    	// 绑定取消
			    	$(".close").bindFormClose();
				}
			});
    	});
    	
    	$(".tag_del").click(function() {
    		$.simplePost({url : Globals.ctx + "/tag/tagDel.action", data : {tagName :$(this).prev().text()}, tip : false, callback : freshPage})
    	});
    	
	    $(".meeting_vod li").click(function() {
//	    	window.open(url,"play","fullscreen=no,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no");
    	alert(1);
	    }); 
    },
    
    // 文件管理
    file:function() {
    	var freshPage = function(opts) {
    		opts = $.extend({
    			keyword : keyword,
    			pageNum : pageNum
    			}, opts || {});
    		
    		var pageUrl = Globals.ctx + "/file/home.action";
    		
    		$.get(pageUrl, {keyword : opts.keyword, pageNum : opts.pageNum}, function(result){
    		    $(".sub_content").html(result);
    		    parsePageName();
    		    $("code#pagename").remove();
    		  });
    	};
    	
    	$("#pagination").pagination(fileCount, {
    		items_per_page : pageSize,
    		current_page : pageNum,
    		callback : function(current_page, containers) {
    			var params = {
    				keyword : keyword,
    				pageNum : current_page
    			};
    			freshPage(params);
    		}
    	});
    	
    	$(".search_btn").click(function() {
    		freshPage({keyword : $("input[name=keywork]").val(), pageNum : 0});
    	});
    	
    	$("#fileEdit").click(function(){
    		var values = $("input[name=CheckboxGroup1]").checkboxVals({single : true});
    		if(values) {
    			Boxy.load(Globals.ctx + "/file/fileEdit.action?id=" + values, {
    				modal : true,
    				afterShow : function() {
    			    	// 绑定form提交
    			    	$(".submit").bindFormClick({afterSubmit : freshPage});
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
    				$.simplePost({url : Globals.ctx + "/file/fileDel.action", data : {ids : values}, callback : freshPage});
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