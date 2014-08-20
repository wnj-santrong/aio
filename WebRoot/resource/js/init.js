// 全局初始化
function init() {
	
	// 页面竖直居中
	$(window).unbind("resize").resize(function() {
		var b_height = $(this).height();
		var m_height = $(".mainbav").height();
		var height = b_height - m_height;
		height = height > 0? height/2 : 0;
		$(".mainbav").css({"margin-top" : height + "px"});
	});
	$(window).resize();
	
	 // 时间选择器
    $('.clock').unbind("click").click(function(){
        $('#timeRange_div').remove();
         
        var hourOpts = '';
        for (i=0;i<10;i++) hourOpts += '<option>0'+i+'</option>';
        for (i=10;i<24;i++) hourOpts += '<option>'+i+'</option>';
        var minuteOpts = '<option>00</option><option>05</option>';
        for (i=10;i<60;i+=5) minuteOpts += '<option>'+i+'</option>';
         
        var html = $('<div id="timeRange_div"><select id="timeRange_a">'+hourOpts+
            '</select>:<select id="timeRange_b">'+minuteOpts+
            '</select>--<select id="timeRange_c">'+hourOpts+
            '</select>:<select id="timeRange_d">'+minuteOpts+
            '</select>&nbsp;<input type="button" value="' + Message.dynamic('text_confirm') + '" id="timeRange_btn" /></div>')
            .css({
                "position": "absolute",
                "z-index": "9",
                "padding": "5px",
                "border": "1px solid #AAA",
                "background-color": "#FFF",
                "box-shadow": "1px 1px 3px rgba(0,0,0,.4)",
                "left": "0px",
                "top" : "27px",
                "width" : "230px"
            })
            .click(function(){return false});
        
        var to = $(this).attr("to");
        var target = $("input[name=" + to + "]");
        // 点击确定的时候
        html.find('#timeRange_btn').click(function(){
            var str = html.find('#timeRange_a').val()+':'
                +html.find('#timeRange_b').val()+'--'
                +html.find('#timeRange_c').val()+':'
                +html.find('#timeRange_d').val();
            target.val(str);
            $('#timeRange_div').remove();
        });
         
        $(this).after(html);
        
        // 还原值
		var curVal = target.val();
		var arr = curVal.split("--");
		if(arr.length == 2) {
			var arr1 = arr[0].split(":");
			var arr2 = arr[1].split(":");
			$("#timeRange_a").val(arr1[0]);
			$("#timeRange_b").val(arr1[1]);
			$("#timeRange_c").val(arr2[0]);
			$("#timeRange_d").val(arr2[1]);
		}        
        
        return false;
    });
    $(document).click(function(){
        $('#timeRange_div').remove();
    });
}
// index模块初始化
function IndexClass() {
};

//index模块的具体页面初始化
IndexClass.prototype = {
	// 公共方法，绑定登录
	_bindLogin:function() {
		$("#index_login input[name=username]").focus();
		
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
    			var answerBtn = $(".answers .boxy-btn1");
    			if(answerBtn.size() > 0) {
    				answerBtn.click();
    			}else {
    				$(".login_submit").click();
    			}
    		}
    	});
	},
	
	// 直播点播函数
	_santrongPlay:function(options) {
//		if(!/MSIE/.test(navigator.userAgent)) {// IE11不兼容
		if(!$.isIE()) {
			Boxy.alert(Message.dynamic('notice_only_support_ie'));
		}
		
		if(options.id) {
			$.simplePost({url : Globals.ctx + "/file/filePlay.action", data : {id : options.id, type : options.type}, tip : false, callback : function(result) {
				if(result.indexOf('{') != -1) {
					var json = eval('(' + result + ')');
    				SantrongPlayer.StartPlayEX(json.type, window.location.host, json.confId, json.filePath, json.liveType);
				}else {
					Boxy.alert(Message.dynamic(result));
				}
			}});
		}
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
    		$(".navigator li a").removeClass("current_menu");
    		$(this).addClass("current_menu");
    		
    		var pageName = $(this).text();
    		var pageUrl = $(this).attr("rel");
    		
    		$(".sub_top").html(pageName);
    		Globals.pageName = pageName;
    		$(".sub_content").html("");
    		setTimeout(function() {
    			if($(".sub_content").text() == "") {
    				$(".sub_content").html('<img id="floatExcuting" style="margin:292px 0 0 500px;" src="' + Globals.ctx + '/resource/photo/loading.gif" />');// 设置载入状态
    			}
    		}, 500);//500ms没有拿到新页面则显示loading
    		
    		$.get(pageUrl, null, function(result){
    			if(pageName == Globals.pageName) {// 在异步的情况下，用户最后一次点击的页面才具有载入权
	    		    $(".sub_content").html(result);
	    		    parsePageName();
	    		    $("code#pagename").remove();
    			}
    		  });
    		
    		// 设置cookie，用户刷新后依然能标识本页面
    		$.cookie('managePage', $(this).parent().index());
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
    	
		var pageIndex = $.cookie('managePage') || 0;
		$(".navigator a").eq(pageIndex).click();
    },
    
    // 会议管理
    meeting:function() {
    	var _this = this;
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
    	
    	var preValidate = function() {
    		if($("#dsList .edit").size() != 0) {
    			Boxy.alert(Message.dynamic("notice_datasource_edit"));
    			return false;
    		}
    		return true;
    	}
    	
    	$(".save").bindFormClick({url : Globals.ctx + '/meeting/save.action', beforeSubmit : preValidate});
    	
    	$(".openLive").bindFormClick({url : Globals.ctx + '/meeting/openLive.action', beforeSubmit : preValidate, afterSubmit : freshCurrentModel});
    	
    	$(".closeLive").bindFormClick({url : Globals.ctx + '/meeting/closeLive.action', beforeSubmit : preValidate, afterSubmit : freshCurrentModel});
    	
    	$(".startRecord").bindFormClick({url : Globals.ctx + '/meeting/startRecord.action', beforeSubmit : preValidate, afterSubmit : freshCurrentModel});
    	
    	$(".stopRecord").bindFormClick({url : Globals.ctx + '/meeting/stopRecord.action', beforeSubmit : preValidate, afterSubmit : freshCurrentModel});
    	
    	$(".preview").click(function() {
    		_this._santrongPlay({id : $("input[name=id]").val(), type : 1});
    	});
    	
    	// 显示新增或者修改的输入框
    	var dsGet = function(el, id, mid) {
	        var dynamicHtml = '<span class="filed">' + Message.dynamic("text_addr") + ':</span><input type="text" class="addr_input" required />';
	        dynamicHtml += '<span class="filed">' + Message.dynamic("text_port") + ':</span><input type="text" class="port_input" />';
	        dynamicHtml += '<span class="filed">' + Message.dynamic("text_username") + ':</span><input type="text" class="username_input" />';
	        dynamicHtml += '<span class="filed">' + Message.dynamic("text_password") + ':</span><input type="text" class="password_input" />';
	        dynamicHtml += '<img class="opert dsSave" src="' + Globals.ctx + '/resource/photo/save.png" title="' + Message.dynamic("text_confirm") + '" />';
	        dynamicHtml += '<img class="opert dsDel" src="' + Globals.ctx + '/resource/photo/syicon_net.png" title="' + Message.dynamic("text_del") + '" />';
	        
	        if($(el).hasClass("ds_add")) {// 新增
				var html = '<li class="dsItem edit">';
		        html += '<input type="hidden" name="dsId" value=""/>';
		        html += '<input type="hidden" name="addr" value=""/>';
		        html += '<input type="hidden" name="port" value=""/>';
		        html += '<input type="hidden" name="username" value=""/>';
		        html += '<input type="hidden" name="password" value=""/>';
		        html += '<input type="hidden" name="priority" value=""/>';
		        html += '<div class="dynamic">';
		        html += dynamicHtml;
		        html += '</div>';
		        html += '</li>';
	        	$("#dsList .dsItem:last").before(html);
	        	
    			var val = $(".layoutContainer .mode" + count).eq(0).attr("id").substr(1);
    			$("input[name=recordMode]").val(val);
	        	
	        }else{// 修改
	        	var dsItem = $(el).parents(".dsItem");
	        	var dynamic = $(el).parents(".dynamic");
	        	dynamic.html(dynamicHtml);
	        	dsItem.addClass("edit");
	        	
	        	// 值转移
	        	dsItem.find(".addr_input").val(dsItem.find("input[name=addr]").val());
	        	dsItem.find(".port_input").val(dsItem.find("input[name=port]").val());
	        	dsItem.find(".username_input").val(dsItem.find("input[name=username]").val());
	        	dsItem.find(".password_input").val(dsItem.find("input[name=password]").val());
    		}
    	};
    	
    	$(".ds_add").click(function() {
    		var index = $("#dsList .dsItem").length;
    		if(index >= 3) {
    			Boxy.alert(Message.dynamic("warn_datasource_already_max", 3));
    			return;
    		}
    		
    		var id = '';
    		var mid = $(".sub_content input[name=id]").val();
    		dsGet(this, id, mid);
    		recordModeSetting();
    	});
    	
    	$("#dsList").click(function(e) {
    		var target = $(e.target);
    		
    		if(target.hasClass("dsEdit")) {// 修改
    			
        		var id = target.parent().find("input[name=dsId]").val();
        		var mid = $(".sub_content input[name=id]").val();
        		dsGet(target, id, mid);
        		
    		}else if(target.hasClass("dsDel")) {// 删除
    			
    			target.parents(".dsItem").remove();
    			calDsPriority();
    			
    			var count = $("#dsList .dsItem").length;
    			var val = $(".layoutContainer .mode" + count).eq(0).attr("id").substr(1);
    			$("input[name=recordMode]").val(val);
    			recordModeSetting();
    			
    		}else if(target.hasClass("dsSave")) {// 保存
    			var dsItem = target.parents(".dsItem");
    			var dynamic = target.parents(".dynamic");
    			
        		// 数据校验
        		if(!dynamic.validate()) {
        			return;
        		}
    			
	        	// 值转移
	        	dsItem.find("input[name=addr]").val(dsItem.find(".addr_input").val());
	        	dsItem.find("input[name=port]").val(dsItem.find(".port_input").val());
	        	dsItem.find("input[name=username]").val(dsItem.find(".username_input").val());
	        	dsItem.find("input[name=password]").val(dsItem.find(".password_input").val());
    			
    			var dynamicHtml = '<span class="addr">' + dsItem.find(".addr_input").val() + '</span>';
    			dynamicHtml += '<img class="status" src="' + Globals.ctx + '/resource/photo/disconnected.gif" width="12" height="12" title="' + Message.dynamic("text_disconnected") + '" />';
    			dynamicHtml += '<img class="opert dsEdit" src="' + Globals.ctx + '/resource/photo/draw-freehand.png" title="' + Message.dynamic("text_edit") + '" />';
    			dynamicHtml += '<img class="opert dsDel" src="' + Globals.ctx + '/resource/photo/syicon_net.png" title="' + Message.dynamic("text_del") + '" />';
    			dynamic.html(dynamicHtml);
    			dsItem.removeClass("edit");
    			
    			calDsPriority();
    		}
    	});
    	
    	$(".layoutContainer img").click(function(first) {
    		$("input[name=recordMode]").val($(this).attr("id").substr(1));
    		recordModeSetting();
    	});
    },
    
    
    // 视频播放
    play:function() {
    	var _this = this;
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
	    	_this._santrongPlay({id : $(this).attr("rel"), type : $(this).attr("type")});
	    }); 
    },
    
    // 文件管理
    file:function() {
    	var _this = this;
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
    		var values = $("input[name=CheckboxGroup1]").checkboxVals({single : true});
    		if(values) {
    			var status = $("input[value= " + values + "]").attr("st");//0 是正在录制中
    			if(status != 0) {
    				_this._santrongPlay({id : values, type : 0});
    			}else {
    				Boxy.alert(Message.dynamic("notice_file_recording"));
    			}
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
    	
    	$(".cdetail").click(function() {
    		var id = $(this).parents("tr").find("input[name=CheckboxGroup1]").val();
			Boxy.load(Globals.ctx + "/file/fileDetail.action?id=" + id, {
				modal : true,
				afterShow : function() {
			    	// 绑定取消
			    	$(".close").bindFormClose();
				}
			}); 		
    		
    	});
    	
    	$(".checkAll").bindCheckAll("input[name=CheckboxGroup1]");
    },
    
    // 系统管理
    setting:function() {
    	
    	// 绑定所有form提交
    	$(".submit").bindFormClick({
    		beforeSubmit : function(form, options){
    			// 上传升级文件开始弹出蒙层
    			if(form.attr("action").indexOf("updateLocal") != -1) {
    				options.isGoodCall = false;
        			$.showfloatExcuting();
        		}
    		},
    		afterSubmit : function(form){
    			// 用户修改后清空表单
	    		if(form.attr("id") == "setting_user") {
	    			form.clearForm();
	    		}
    			// 上传升级文件完毕关闭蒙层
    			if(form.attr("action").indexOf("updateLocal") != -1) {
        			$.hideFloatExcuting();
        		}
    		}
    	});
    	
    	// 系统升级--立刻检测升级（这个比较特殊）
    	$(".updateNow").click(function() {
    		$.showfloatExcuting();
    		$.simplePost({url : Globals.ctx + "/setting/updateOnlineNow.action", callback : function() {
    			$.hideFloatExcuting();
    		}});
    	});
    	
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
    	
    	// 获取ftp
    	$.ajax({
    		dataType : "json",
    		url : Globals.ctx + "/setting/ftp.action",
    		success : function(result) {
    			if(result.ftpEnable == 1) {
    				$("#setting_ftp input[name=useFtp]").attr("checked", "checked");
    			}
    			if(result.beginTime != '' && result.endTime != '') {
    				$("#setting_ftp input[name=duration]").val(result.beginTime + '--' + result.endTime);
    			}
    			$("#setting_ftp input[name=host]").val(result.ftpIp);
    			$("#setting_ftp input[name=port]").val(result.ftpPort);
    			$("#setting_ftp input[name=username]").val(result.username);
    			$("#setting_ftp input[name=password]").val(result.password);
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
		
		// 获取升级配置
    	$.ajax({
    		dataType : "json",
    		url : Globals.ctx + "/setting/updateOnlineGet.action",
    		success : function(result) {
    			if(result.autoUpdate == 1) {
    				$("#online_update input[name=autoUpdate]").attr("checked", "checked");
    			}
    			if(result.updateTime) {
    				var arr = result.updateTime.split(":")
    			}
    			$("#online_update select[name=hours]").val(arr[0]);
    			$("#online_update select[name=minutes]").val(arr[1]);
    		}
    	});
		
    	
		freshDbList();
		
    }
};