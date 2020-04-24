/**
 * jquery表单验证提示信息扩展
 */
(function($) {
	if ($.validationEngineLanguage == undefined
			|| $.validationEngineLanguage.allRules == undefined)
		alert("请在jquery.validationEngine-zh-CN.js文件后引入该JS文件");
	else {
		$.validationEngineLanguage.allRules["idCard"] = {
			"regex":/^\d{15}(\d{2}[A-Za-z0-9])?$/,
            "alertText": "* 请输入正确的身份证号码"
		};
		$.validationEngineLanguage.allRules["password"] = {
			"regex":/^([a-zA-Z]+[0-9]+|[0-9]+[a-zA-Z]+)[0-9a-zA-Z]*$/,
            "alertText": "* 请输入字母和数字的组合"
		};
		$.validationEngineLanguage.allRules["customerDateCheck"] = {
			"regex" : "none",
			"alertText": "* 请输入会议开始之前的日期 ",
            "alertText2": "* 请输入名单收集初步截止之后的日期 ",
            "alertText3": "* 请输入名单收集最终截止之后的日期 ",
            "alertText4": "* 预计会议日期与正式会议日期不能同时填写 "
		};
		$.validationEngineLanguage.allRules["customerCheck"] = {
			"regex" : "none",
			"zipCode": "* 请输入正确的邮编号码 ",
			"isMobile": "* 请输入正确的11位手机号 "
		};
		$.validationEngineLanguage.allRules["specialChar"] = {
			"regex" : /^[><]?[\-\+]?(([0-9]+)([\.,]([0-9]+))?|[\.,]([0-9]+))$|^NA$|^ND$|^UK$|^[\+\-]$/,
			"alertText": "请输入数字或(NA,ND,UK,+,-,>数字,<数字)"
		};
		
	}
})(jQuery);
