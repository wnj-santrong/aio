/**
 * load js file with the filename specified and call callback function if present.
 * @param obj
 * obj = {};
 * obj.urlprefix: specified the path for the js file.
 * obj.filename: the filename  (example:  obj.filename = "/search.js?rev={%SEARCH_JS%}";)
 * obj.fn: callback function. (example:  obj.fn = function(jsfiles) {alert("hello" + jsfiles)}; )
 */
jQuery(function($){});

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

function IndexClass() {
};

IndexClass.prototype = {
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
    
    meeting:function() {
    },
    
    play:function() {
    },
    
    file:function() {
    },
    
    setting:function() {
    	$("code#pagename").remove();
    	var completeCount = 0;
    	
    	var modules = ['/setting/user.action', '/setting/database.action'];
    	for(var i=0; i<modules.length; i++) {
			$.ajax({
				type : "GET",
				url : Globals.ctx + modules[i],
				cache : false,
				success : function(result){
					$(".modules").append(result);
			    	completeCount++;
				}
			});
    	};
    	
    	var thread = setInterval(function(){
    		if(completeCount == modules.length) {
    			parsePageName();
    			clearInterval(thread);
    		}
    	}, 100);
    }
};

function SettingClass() {
};

SettingClass.prototype = {
	database:function() {
		// 备份数据库
		$("#dbbackup").click(function(){
			$.ajax({
				type : "POST",
				url : Globals.ctx + "/setting/dbbackup.action",
				cache : false,
				success : function(result){
					alert(Message.dynamic(result));
				}
			});
		});
		
		// 删除数据库
		$(".deldb").click(function(){
			var filename = $(this).parent().find("span").text()
			$.ajax({
				type : "POST",
				data : {"filename" : filename},
				url : Globals.ctx + "/setting/dbdel.action",
				cache : false,
				success : function(result){
					alert(Message.dynamic(result));
				}
			});
		});
		
		// 恢复数据库
		$(".restore").click(function(){
			var filename = $(this).parent().find("span").text()
			$.ajax({
				type : "POST",
				data : {"filename" : filename},
				url : Globals.ctx + "/setting/dbrestore.action",
				cache : false,
				success : function(result){
					alert(Message.dynamic(result));
				}
			});
		});
	}
};