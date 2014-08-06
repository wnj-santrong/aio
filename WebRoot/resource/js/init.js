// 全局初始化
function init() {  
	//页面竖直居中
	$(window).unbind("resize").resize(function() {
		var b_height = $(this).height();
		var m_height = $(".mainbav").height();
		var height = b_height - m_height;
		height = height > 0? height/2 : 0;
		$(".mainbav").css({"margin-top" : height + "px"});
	});
	$(window).resize();
}
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
    	
    	// 回车登录
    	$(document).keydown(function(e){
    		if(e.keyCode==13){
    			$(".login_submit").click();
    		}
    	});
	},
	
	// 登录页
	login:function() {
		this._bindLogin();
	},
	
	// 首页
	index:function() {
		var _this = this;
		$("code#pagename").remove();
		
		var pageUrl = 'play/homeAnonymous.action';
		
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
    	// 计算数据源顺序，新增和删除后一定要调用重算顺序// 0，1，2-------s1，s2，vga
    	var calDsPriority = function() {
    		$("#dsList .dsItem").each(function(index, e) {
    			$(e).find("input[name=priority]").val(index);
    		});
    	};
    	calDsPriority();
    	
    	// 设置layout
    	var recordModeSetting = function() {
    		var count = $("#dsList .dsItem").length;
    		var layout = $("input[name=recordMode]").val();
    		$(".layoutContainer img").removeClass("cur_layout").hide();
    		$(".layoutContainer .mode" + count).show();
    		$(".layoutContainer #v" + layout).addClass("cur_layout");
    	};
    	recordModeSetting();
    	
    	var freshCurrentModel = function() {$(".navigator a:first").click();}
    	
    	$(".save").bindFormClick({url : Globals.ctx + '/meeting/save.action'});
    	
    	$(".openLive").bindFormClick({url : Globals.ctx + '/meeting/openLive.action', afterSubmit : freshCurrentModel});
    	
    	$(".closeLive").bindFormClick({url : Globals.ctx + '/meeting/closeLive.action', afterSubmit : freshCurrentModel});
    	
    	$(".startRecord").bindFormClick({url : Globals.ctx + '/meeting/startRecord.action', afterSubmit : freshCurrentModel});
    	
    	$(".stopRecord").bindFormClick({url : Globals.ctx + '/meeting/stopRecord.action', afterSubmit : freshCurrentModel});
    	
    	// 获取显示数据源的弹框
    	var dsGet = function(el, id, mid) {
			Boxy.load(Globals.ctx + "/datasource/dsGet.action?id=" + id + "&meetingId=" + mid, {
				modal : true,
				afterShow : function() {
		    		var dsItem = null;
					var dsForm = $("#datasource_dsPost");
					
					// 如果是修改，则从页面转移值
					if($(el).hasClass("dsEdit")) {
						dsItem = $(el).parents(".dsItem");
						dsForm.find("input[name=addr]").val(dsItem.find("input[name=addr]").val());
						dsForm.find("input[name=port]").val(dsItem.find("input[name=port]").val());
						dsForm.find("input[name=username]").val(dsItem.find("input[name=username]").val());
						dsForm.find("input[name=password]").val(dsItem.find("input[name=password]").val());
					}
					// 点击保存，先暂存在页面里
			    	$(".submit").click(function() {
			    		// 数据校验
			    		if(!dsForm.validate()) {
			    			return;
			    		}
			    		
			    		if($(el).hasClass("ds_add")) {
			    			var html = '<li class="dsItem">';
					        html += '<input type="hidden" name="dsId" value=""/>';
					        html += '<input type="hidden" name="addr" value=""/>';
					        html += '<input type="hidden" name="port" value=""/>';
					        html += '<input type="hidden" name="username" value=""/>';
					        html += '<input type="hidden" name="password" value=""/>';
					        html += '<input type="hidden" name="priority" value=""/>';
					        html += '<span class="addr"></span>';
					        html += '<img class="status" src="' + Globals.ctx + '/resource/photo/connected.gif" width="12" height="12" />';
					        html += '<img class="opert hide dsEdit" src="' + Globals.ctx + '/resource/photo/draw-freehand.png" />';
					        html += '<img class="opert hide dsDel" src="' + Globals.ctx + '/resource/photo/syicon_net.png" />';
					        html += '</li>';
			    			$("#dsList .dsItem:last").before(html);
			    			var count = $("#dsList .dsItem").length;
			    			dsItem = $("#dsList").find(".dsItem").eq(count-2);
			    			calDsPriority();
			    			
			    			var count = $("#dsList .dsItem").length;
			    			var val = $(".layoutContainer .mode" + count).eq(0).attr("id").substr(1);
			    			$("input[name=recordMode]").val(val);
			    			recordModeSetting();
			    		}else{
			    			dsItem = $(el).parents(".dsItem");
			    		}
			    		dsItem.find(".addr").text(dsForm.find("input[name=addr]").val());
						dsItem.find("input[name=addr]").val(dsForm.find("input[name=addr]").val());
						dsItem.find("input[name=port]").val(dsForm.find("input[name=port]").val());
						dsItem.find("input[name=username]").val(dsForm.find("input[name=username]").val());
						dsItem.find("input[name=password]").val(dsForm.find("input[name=password]").val());
						
						$(".close").click();
			    	});
			    	
			    	// 绑定取消
			    	$(".close").bindFormClose();
				}
			});
    	};
    	
    	$(".ds_add").click(function() {
    		var index = $("#dsList .dsItem").length;
    		if(index >= 3) {
    			Boxy.alert(Message.dynamic("warn_datasource_already_max", Globals.vedioCount));
    			return;
    		}
    		
    		var id = '';
    		var mid = $(".sub_content input[name=id]").val();
    		dsGet(this, id, mid);

    	});
    	
    	$("#dsList").click(function(e) {
    		var target = $(e.target);
    		
    		if(target.hasClass("dsEdit")) {
    			
        		var id = target.parent().find("input[name=dsId]").val();
        		var mid = $(".sub_content input[name=id]").val();
        		dsGet(target, id, mid);
        		
    		}else if(target.hasClass("dsDel")) {
    			
    			target.parents(".dsItem").remove();
    			calDsPriority();
    			
    			var count = $("#dsList .dsItem").length;
    			var val = $(".layoutContainer .mode" + count).eq(0).attr("id").substr(1);
    			$("input[name=recordMode]").val(val);
    			recordModeSetting();
    			
    		}
    	});
    	
    	$(".layoutContainer img").click(function(first) {
    		$("input[name=recordMode]").val($(this).attr("id").substr(1));
    		recordModeSetting();
    	});
    },
    
    
    // 视频播放
    play:function() {
    	// 标签修改删除显示切换
    	if(Globals.isLogined) {
    		$(".tag_add").show();
    		$(".tag").mouseover(function() {
    			$(this).find(".tmd").show();
    			$(this).find(".tsd").css({"border-top" : "solid 1px #86b9e3", "border-left" : "solid 1px #86b9e3", "border-right" : "solid 1px #86b9e3"});
    		});
    		$(".tag").mouseout(function() {
    			$(this).find(".tmd").hide();
    			$(this).find(".tsd").css({"border" : "solid 1px #fff"});
    		});
    	}
    	
    	var freshPage = function(opts) {
    		opts = $.extend({
    			keyword : keyword,
    			pageNum : 0
    			}, opts || {});
    		
    		var pageSource = $("#pageSource").text();
    		var pageUrl = Globals.ctx + "/play/" + pageSource + ".action";
    		
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
			prev_text: Message.dynamic("play_page_prev"),
			next_text: Message.dynamic("play_page_next"),
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
    	
    	$(".tsd a").click(function() {
    		freshPage({keyword : $(this).text()});
    	});
    	
    	$(".tag_add, .tag_edit").click(function() {
    		var id = $(this).parent().parent().attr("opt");
    		if(typeof(id) == "undefined")id = "";
			Boxy.load(Globals.ctx + "/tag/tagGet.action?id=" + id, {
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
    		$.simplePost({url : Globals.ctx + "/tag/tagDel.action", data : {id : $(this).parent().parent().attr("opt")}, tip : false, callback : freshPage})
    	});
    	
	    $(".meeting_vod li a").click(function() {
    		if(!/MSIE/.test(navigator.userAgent)) {
    			Boxy.alert(Message.dynamic('notice_only_support_ie'));
    		}
    		
    		var values = $(this).attr("rel");
    		if(values) {
    			$.simplePost({url : Globals.ctx + "/file/filePlay.action", data : {id : values}, tip : false, callback : function(result) {
    				if(result.indexOf('{') != -1) {
    					var json = eval('(' + result + ')');
        				RecCtrl1.StartPlayEX(json.type, json.addr, json.confId, json.filePath, json.liveType);    					
    				}else {
    					Boxy.alert(Message.dynamic(result));
    				}
    			}});
    		}
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
			prev_text: Message.dynamic("play_page_prev"),
			next_text: Message.dynamic("play_page_next"),    		
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
    		if(!/MSIE/.test(navigator.userAgent)) {
    			Boxy.alert(Message.dynamic('notice_only_support_ie'));
    		}
    		
    		var values = $("input[name=CheckboxGroup1]").checkboxVals({single : true});
    		if(values) {
    			$.simplePost({url : Globals.ctx + "/file/filePlay.action", data : {id : values}, tip : false, callback : function(result) {
    				if(result.indexOf('{') != -1) {
    					var json = eval('(' + result + ')');
        				RecCtrl1.StartPlayEX(json.type, json.addr, json.confId, json.filePath, json.liveType);    					
    				}else {
    					Boxy.alert(Message.dynamic(result));
    				}
    			}});
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
    	
    	$("#fileOpen").click(function(){
    		var values = $("input[name=CheckboxGroup1]").checkboxVals();
    		if(values) {
        		$.simplePost({url : Globals.ctx + "/file/fileOpen.action", data : {ids : values}, callback : freshPage});    			
    		}
    	});
    	
    	$("#fileClose").click(function(){
    		var values = $("input[name=CheckboxGroup1]").checkboxVals();
    		if(values) {
        		$.simplePost({url : Globals.ctx + "/file/fileClose.action", data : {ids : values}, callback : freshPage});    			
    		}
    	});    	
    	
    	$("#fileDownload").click(function(){
    		var values = $("input[name=CheckboxGroup1]").checkboxVals({single : true});
    		if(values) {
    			window.location.href =Globals.ctx + "/file/fileDownload.action?id=" + values;
    		}
    	});
    	
    	$(".checkAll").bindCheckAll("input[name=CheckboxGroup1]");
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
						html += "<li><span>" + result[i] + "</span><a href=\"#\" class=\"dbRestore\">还原</a><a href=\"#\" class=\"dbDel\">删除</a></li>";
					}
					$("#dbList").html(html);
					
					
					// 删除数据库
					$(".dbDel").click(function(){
						var filename = $(this).parent().find("span").text();
						$.delConfirm(function() {
							$.simplePost({url : Globals.ctx + "/setting/dbDel.action", data : {"filename" : filename}, callback : freshDbList});
						});					
					});
					
					// 恢复数据库
					$(".dbRestore").click(function(){
						var filename = $(this).parent().find("span").text();
						Boxy.ask(Message.dynamic("warn_db_restore_confirm"), [Message.dynamic("text_confirm"), Message.dynamic("text_cancel")], function(response) {
				            if (response == Message.dynamic("text_confirm")) {
								$.simplePost({url : Globals.ctx + "/setting/dbRestore.action", data : {"filename" : filename}});
				            }
						});							
					});				
				}
    		});
    	};
    	
		// 备份数据库
		$("#dbBackup").click(function(){
			Boxy.ask(Message.dynamic("warn_db_backup_confirm"), [Message.dynamic("text_confirm"), Message.dynamic("text_cancel")], function(response) {
	            if (response == Message.dynamic("text_confirm")) {
	            	$.simplePost({url : Globals.ctx + "/setting/dbBackup.action", callback : freshDbList});
	            }
			});
		});
    	
		freshDbList();
		
    }
};