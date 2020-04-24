/**
 * jquery表单验证提示信息扩展
 */
(function($) {
	if ($.validationEngineLanguage == undefined
			|| $.validationEngineLanguage.allRules == undefined)
		alert("please import this file below the jquery.validationEngine-zh-CN.js");
	else {
		$.validationEngineLanguage.allRules["idCard"] = {
			"regex":/^\d{15}(\d{2}[A-Za-z0-9])?$/,
            "alertText": "* Please input the correct id number"
		};
		$.validationEngineLanguage.allRules["password"] = {
			"regex":/^([a-zA-Z]+[0-9]+|[0-9]+[a-zA-Z]+)[0-9a-zA-Z]*$/,
            "alertText": "* Please enter the combination of letters and Numbers"
		};
		$.validationEngineLanguage.allRules["specialChar"] = {
			"regex" : /^[><]?[\-\+]?(([0-9]+)([\.,]([0-9]+))?|[\.,]([0-9]+))$|^NA$|^ND$|^UK$|^[\+\-]$/,
			"alertText": "Please enter Numbers or NA,ND,UK,+,-,>n,<n"
		};
	}
})(jQuery);
