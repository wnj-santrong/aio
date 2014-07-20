String.prototype.trim=function(){
	return this.replace(/(^\s*)|(\s*$)/g, "");
};

//jquery扩展
jQuery(function($){
	
	/*
	* $.fn在jquery对象上扩展
	* $在$上扩展
	*/
	
	// 获取checkbox的值
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
			Boxy.alert(Message.dynamic("notice_must_select_one"));
			return false;
		}
		
		if(finalParams.single && vals.indexOf(",") != -1) {
			Boxy.alert(Message.dynamic("notice_only_one_select"));
			return false;
		}
		return vals;
	};
	
	// 让form使用ajax提交
	$.fn.bindFormClick = function(callback) {
		$(this).click(function(){
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
		    	        
//		    	        //日期类型检测
//		    	        var textboxs = form.find(".required_date");
//		    	        for(var i=0;i<textboxs.size();i++){
//		    	            //var reg_date = /^[2]\d{3}\-[01]{0,1}\d\-[0123]{0,1}\d$/ig;//强检测
//		    	            var reg_date = /^\d{4}\-\d{1,2}\-\d{1,2}$/ig;//弱检测
//		    	            var val = textboxs.eq(i).val().trim();
//		    	            if(!val == "" && !reg_date.test(val)){
//		    	                isPass = false;
//		    	                break;
//		    	            }
//		    	        }
//		    	        
//		    	        //正整数检测        
//		    	        textboxs = form.find(".required_int");
//		    	        for(var i=0;i<textboxs.size();i++){
//		    	            var reg_int = /^\d*$/ig;
//		    	            var val = textboxs.eq(i).val().trim();
//		    	            if(!val == "" && !reg_int.test(val)){
//		    	                isPass = false;
//		    	                break;
//		    	            }
//		    	        }            
		    	        
		    	        return isPass;
		    		},
		    		success : function(result) {
		    			Boxy.alert(Message.dynamic(result));
		    			if(result == "success") {
		    				$(".close").click();
		    				if(callback)callback(form);
		    			}
		    		}
		    	});
	    	}
	    	return false;
		});
	};
	
	// 绑定关闭弹框
	$.fn.bindFormClose = function(callback) {
	    $(this).click(function(e){
	    	Boxy.get(this).hideAndUnload();
	    	if(callback)callback();
	    });
	};
	
	// 简单的发送post
	$.simplePost = function(url, data, success) {
		if(!url) {
			return;
		}
		
		$.ajax({
			type : "POST",
			data : data,
			url : url,
			cache : false,
			success : function(result){
				Boxy.alert(Message.dynamic(result));
				if(success)success();
			}
		});
	};
	
	// 删除确认
	$.delConfirm = function(callback) {
		Boxy.ask(Message.dynamic("warn_del_confirm"), [Message.dynamic("text_confirm"), Message.dynamic("text_cancel")], function(response) {
            if (response == Message.dynamic("text_confirm")) callback();
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
        	console.info("error:parsePageName---" + $(this).html());
        }
    });
    qs = null;
}

$(function() {
    parsePageName();
});