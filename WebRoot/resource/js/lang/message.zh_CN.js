var Message = {
	success : "\u64cd\u4f5c\u6210\u529f",
	fail : "\u64cd\u4f5c\u5931\u8d25",
	
	error_param : "\u53c2\u6570\u9519\u8bef",
	error_oldpwd : "\u65e7\u5bc6\u7801\u9519\u8bef",
	error_file_exists : "\u6587\u4ef6\u5df2\u5b58\u5728", 
	error_file_not_exists : "\u6587\u4ef6\u4e0d\u5b58\u5728",
	error_meeting_already_begin : "\u4f1a\u8bae\u5df2\u7ecf\u88ab\u5f00\u542f\uff0c\u4e0d\u53ef\u91cd\u590d\u64cd\u4f5c",
	error_meeting_already_close : "\u4f1a\u8bae\u5df2\u7ecf\u88ab\u5173\u95ed\uff0c\u4e0d\u53ef\u91cd\u590d\u64cd\u4f5c",
	error_record_already_begin : "\u5df2\u7ecf\u6709\u8bfe\u4ef6\u6b63\u5728\u5f55\u5236\u4e2d\uff0c\u8bf7\u5148\u505c\u6b62\u5f55\u5236",
	error_record_already_close : "\u5f55\u5236\u5df2\u7ecf\u505c\u6b62\uff0c\u4e0d\u53ef\u91cd\u590d\u64cd\u4f5c",
	error_disk_lack : "\u78c1\u76d8\u5269\u4f59\u7a7a\u95f4\u4e0d\u8db3",
	error_datasource_already_max : "\u6700\u591a\u53ea\u80fd\u6709{0}\u4e2a\u6570\u636e\u6e90",
	error_datasource_already_exists : "\u8be5\u6570\u636e\u6e90\u5df2\u7ecf\u5b58\u5728",
	
	warn_del_confirm : "\u786e\u5b9a\u8981\u5220\u9664\u5417", 
	warn_datasource_already_max : "\u6700\u591a\u53ea\u80fd\u6709{0}\u4e2a\u6570\u636e\u6e90",
	
	notice_must_select_one : "\u8bf7\u9009\u62e9\u4e00\u6761\u8bb0\u5f55",
	notice_only_one_select : "\u53ea\u80fd\u9009\u62e9\u4e00\u6761\u8bb0\u5f55",
	
	text_confirm : "\u786e\u8ba4",
	text_cancel : "\u53d6\u6d88",
	
	dynamic : function(key, value) {
		if (key == "") {
			return "";
		}
		var msg = Message[key];
		if (!msg) {
			return key;
		}
		if (msg.indexOf("{") != -1) {
			if (typeof(value) == "object") {
				for (var i = 0; i < value.length; i++) {
					if (typeof(value) == "object" || typeof(value) == "string") {
						value[i] = value[i].toString();
						if (value[i].charAt(0) == '#') {
							value[i] = Message[value[i].substring(1)];
						}
						if (!value[i]) value[i] = "";
					}
					msg = msg.replace("{" + i + "}", value[i]);
				}
			} else if (typeof(value) == "string") {
				if (value.charAt(0) == '#') {
					value = Message[value.substring(1)];
				}
				if (!value) value = "";
				msg = msg.replace("{0}", value);
			} else {
				msg = msg.replace("{0}", value);
			}
		}
		return msg;
	},
	
	dateString : function(dateString) {
		var year, month, day, week;
		if (dateString) {
			var dateStrings = dateString.split("-");
			year = dateStrings[0];
			month = dateStrings[1];
			day = dateStrings[2];
			week = dateStrings[3];
		} else {
			var date = new Date();
			year = date.getFullYear();
			month = date.getMonth()+1;
			day = date.getDate();
			week = date.getDay();
		}
		
		return Message.dynamic("date_string", [year, month, day, Message.week[week]]);
	}
};