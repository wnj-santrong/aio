String.prototype.trim=function(){
	return this.replace(/(^\s*)|(\s*$)/g, "");
};

//jquery扩展
jQuery(function($){
	
	/*
	* $.fn在jquery对象上扩展
	* $在$上扩展
	*/
	
	 // 备份jquery的ajax方法  
    var _ajax=$.ajax;
      
    // 重写ajax方法，先判断登录在执行success函数 
    $.ajax=function(opt){
    	var _success = opt && opt.success || function(a, b){};
        var _opt = $.extend(opt, {
        	cache : false,// 顺便关闭所有缓存
        	success:function(data, textStatus){
        		// 如果后台将请求重定向到了登录页，则data里面存放的就是登录页的源码，这里需要找到data是登录页的证据(标记)
        		if(data && data.indexOf && data.indexOf('loginPage') != -1) {
        			window.location.href= Globals.ctx + "/login.action";
        			return;
        		}
        		_success(data, textStatus);
            }  
        });
        _ajax(_opt);
    };
    
    // 弹出进行中
    $.showFloatExcuting = function() {
		var w_width = $(window).width();
		var w_height = $(window).height();
		var html = '<img id="floatExcuting" src="/resource/photo/excuting.gif" class="boxy-content" style="position:fixed; left:' + w_width/2 + 'px; top:' + w_height/2 + 'px;">';
		html += '<div id="floatExcuting_layer" class="boxy-modal-blackout" style="z-index: 1996; opacity: 0.2; width: ' + w_width + 'px; height: ' + w_height + 'px;"></div>';
		if($("#floatExcuting").size() < 1) {
			$('body').append(html);
		}
    };
	
	// 取消进行中
	$.hideFloatExcuting = function() {
		$("#floatExcuting").remove();
		$("#floatExcuting_layer").remove();
	};
	
	// 删除确认
	$.delConfirm = function(callback) {
		Boxy.ask(Message.dynamic("warn_del_confirm"), [Message.dynamic("text_confirm"), Message.dynamic("text_cancel")], function(response) {
            if (response == Message.dynamic("text_confirm")) callback();
		});
	};
	
	// 判断是否是IE，包括11
	$.isIE = function isIE() {
	    if (!!window.ActiveXObject || "ActiveXObject" in window)
	        return true;
	    else
	        return false;
	};
	
	// 获取checkbox的值
	$.fn.checkboxVals = function(options) {
		var _options = {
			must : true,
			single : false
		};
		_options = $.extend(_options, options);
		
		var vals = "";
		$(this).each(function(){
			if($(this).attr("checked")) {
				vals += "," + $(this).val();
			}
		});
		if(vals != "") {
			vals = vals.substr(1);
		}
		
		if(_options.must && vals == "") {
			Boxy.alert(Message.dynamic("notice_must_select_one"));
			return false;
		}
		
		if(_options.single && vals.indexOf(",") != -1) {
			Boxy.alert(Message.dynamic("notice_only_one_select"));
			return false;
		}
		return vals;
	};
	
	// 绑定全选
	$.fn.bindCheckAll = function(obj, options) {
		var children = $(obj);
		$(this).click(function() {
			children.attr("checked", this.checked);
		});
	}
	
	// 数据校验
	$.fn.validate = function() {
		$(this).find(".text_warn").removeClass("text_warn");
        var isPass = true;
        
        // 必填检测
        $(this).find("[required]").each(function(){
        	if($(this).val().trim() == "") {
        		$(this).addClass("text_warn");
                isPass = false;
        	}
        });
        
        // 相同检测
        $(this).find("[equalTo]").each(function(){
        	var equalTo = $(this).attr("equalTo");
        	if($(this).val().trim() != $("input[name=" + equalTo + "]").val().trim()) {
        		$(this).addClass("text_warn");
                isPass = false;
        	}
        });
        
        // 必填IP
        // 正则表达式别用g属性，否则第二次检测的时候会混合第一次结果导致错误
        var re_ip = /^(\d+)\.(\d+)\.(\d+)\.(\d+)$/i
        $(this).find("[required_Ip]").each(function(){
        	var val = $(this).val().trim();
        	if(val == "" || !re_ip.test(val) || RegExp.$1 > 256 || RegExp.$2 > 256 || RegExp.$3 > 256 || RegExp.$4 > 256) {
        		$(this).addClass("text_warn");
                isPass = false;
        	}
        });	
        
        // 必填0或正整数
        var re_number = /^[1-9][0-9]*$/i
        $(this).find("[required_Number]").each(function(){
        	var val = $(this).val().trim();
        	if(val == "" || !re_number.test(val)) {
        		$(this).addClass("text_warn");
                isPass = false;
        	}
        });	
        
        // 日期类型检测
        var re_Date = /^[2]\d{3}\-[01]{0,1}\d\-[0123]{0,1}\d$/i;
        $(this).find("[required_Date]").each(function(){
        	var val = $(this).val().trim();
        	if(val == "" || !re_Date.test(val)) {
        		$(this).addClass("text_warn");
                isPass = false;
        	}
        });     
        
        // 时间类型检测
        var re_Time = /[012]{0,1}\d:[0123456]{0,1}\d:[0123456]{0,1}\d$/i;
        $(this).find("[required_Time]").each(function(){
        	var val = $(this).val().trim();
        	if(val == "" || !re_Time.test(val)) {
        		$(this).addClass("text_warn");
                isPass = false;
        	}
        });         
        
        return isPass;
	}
	
	// 简单的发送post
	$.simplePost = function(options) {
		if(!options.url) {
			return;
		}
		
		var isComplete = false;
		setTimeout(function() {
			if(!isComplete) {
				$.showFloatExcuting();
			}
		}, 360);//360ms没有完成请求则显示loading
		$.ajax({
			type : "POST",
			data : options.data,
			url : options.url,
			success : function(result){
    			isComplete = true;
    			$.hideFloatExcuting();
				if(options.tip != false) {
					Boxy.alert(Message.dynamic(result, options.msgParams));
				}
				if(options.callback)options.callback(result);
			}
		});
	};
	
	// 让form使用ajax提交
	$.fn.bindFormClick = function(options) {
		options = $.extend({
//			url : null,//不能设置默认的URL，否则jquery使用$.extend合并参数的时候会发现options.url存在，则最终结果会是null
			tip : true,
			isGoodCall : true,//是否只有返回success才回调afterSubmit
			beforeSubmit : function(){},
			afterSubmit : function(){}
		}, options||{});
		
		$(this).unbind("click").click(function(){
	    	var form = $(this).closest("form");
	    	if(form.length > 0){
	    		
    			var rs = options.beforeSubmit(form, options);
    			if(rs == false) {
    				return;
    			}
	    		
    			var isComplete = false;
	    		form.ajaxSubmit({
	    			url : options.url,//如果url参数为空，jquery form会调用form的action地址作为url
		    		beforeSubmit : function(){
		    			var rt = form.validate();
		    			if(rt != false) {
			        		setTimeout(function() {
			        			if(!isComplete) {
			        				$.showFloatExcuting();
			        			}
			        		}, 360);//360ms没有完成请求则显示loading
		    			}
		    			return rt;
		    		},
		    		success : function(result) {
		    			isComplete = true;
		    			$.hideFloatExcuting();
		    			if(options.tip) {
		    				Boxy.alert(Message.dynamic(result));
		    			}
		    			if(result == "success") {
		    				$(".close").click();
		    			}
		    			if((options.isGoodCall && result == "success") || !options.isGoodCall){
		    				options.afterSubmit(form, result);
		    			}
		    		}
		    	});
	    	}
	    	return false;
		});
	};
	
	// 绑定关闭弹框
	$.fn.bindFormClose = function(callback) {
	    $(this).unbind("click").click(function(e){
	    	Boxy.get(this).hideAndUnload();
	    	if(callback)callback();
	    });
	};
	
});

//--------------------以下开始格式化页面----------------------------

Querystring = function() {
    this.params = {};
};

Querystring.prototype = {
    /**
     * 将参数qs分解为 key:value的形式，并存放在 this.params里。
     * @method parse
     * @param qs 待分解的字符串，字符串格式："c:className_a:action_rid:xxx"
     */
    parse:function(qs) {
        for (var a in this.params) {
            delete this.params[a];
        }
        if (qs === null || qs.length === 0) {
            //jslog("js error","Querystring.parse qs");
            return;
        }
        qs = qs.replace(/\+/g, ' ');
        var args = qs.split('_');
        for (var i = 0; i < args.length; i++) {
            var pair = args[i].split(':');
            var name = pair[0];
            var value = pair[1];
            this.params[name] = value;
        }
    },
    get:function(key, _default) {
        var value = this.params[key];
        return (value !== null) ? value : _default;
    },
    set:function(key, value) {
        this.params[key] = value;
    },
    has:function(key) {
        var value = this.params[key];
        return (value !== null && value !== undefined);
    }
};

/**
 * 分析当前页面的<code id="pagename">c:xxx_a:xxx_par1:xxx_par2:xxx...</code>
 */
function parsePageName() {
	init();
	
    var qs = new Querystring();
    var def = Globals.define;
    def.pgn = {c:"", a:""};
    
    $("code#pagename").each(function(){
    	qs.parse($(this).html());
        def.pgn.c = qs.has("c") ? qs.get("c") : '';
        def.pgn.a = qs.has("a") ? qs.get("a") : '';
        try {
            if(def.pgn.c === "") {
                return;
            }
            def.pgn.c = def.pgn.c.substr(0, 1).toUpperCase() + def.pgn.c.substr(1);
            var command  = 'var obj = new ' + def.pgn.c + 'Class(); ';
            if(def.pgn.a !== "") {
                command += 'obj.' + def.pgn.a + '()';
            }
            eval(command);
        } catch (e) {
//        	console.info("error:parsePageName---" + $(this).html());
        }
    });
    qs = null;
}

$(function() {
    parsePageName();
});