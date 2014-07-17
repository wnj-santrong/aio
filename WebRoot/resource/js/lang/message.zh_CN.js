var Message = {
	success : "\u64cd\u4f5c\u6210\u529f",
	fail : "\u64cd\u4f5c\u5931\u8d25",
	error_param : "\u53c2\u6570\u9519\u8bef",
	error_oldpwd : "\u65e7\u5bc6\u7801\u9519\u8bef",
	error_file_exists : "\u6587\u4ef6\u5df2\u5b58\u5728", 
	error_file_not_exists : "\u6587\u4ef6\u4e0d\u5b58\u5728", 
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