/**
 * 
 */
var lang = {
	account: {
		requiredUsername: '请填写用户名',
		requiredPassword: '请填写密码',
		requiredEmail: '请填写邮箱',
		correctEmail: '邮箱格式不正确',
		rtPasswordEquals: '确认密码不正确'
	},
	sys: {
		sure_delete: '确认删除？',
		cancel: '取消',
		upload_file: '上传文件',
		file_upload_success: '文件上传成功！！！',
		file_uploading: '文件上传中...'
	}, 
	delta: {
		sure_pass: '确认通过测试？',
		sure_nopass: '确认不通过？',
		sure_publish: '确认发布？',
		sure_retest: '确认重新测试？',
		sure_undo: '确认撤销发布？'
	}, 
	testimei: {
		sure_active: '确认激活？',
		sure_forbidden: '确认阻止？',
	}
};

/**
 * jquery.validator.js 汉化
 */
var cnMsg = {  
	required: "必选字段",   
	remote: "请修正该字段",   
	email: "请输入正确格式的电子邮件",   
	url: "请输入合法的网址",  
	date: "请输入合法的日期",   
	dateISO: "请输入合法的日期 (ISO).",  
	number: "请输入合法的数字",   
	digits: "只能输入整数",   
	creditcard: "请输入合法的信用卡号",   
	equalTo: "请再次输入相同的值",   
	accept: "请输入拥有合法后缀名的字符串",   
	maxlength: jQuery.format("请输入一个长度最多是 {0} 的字符串"),   
	minlength: jQuery.format("请输入一个长度最少是 {0} 的字符串"),   
	rangelength: jQuery.format("请输入一个长度介于 {0} 和 {1} 之间的字符串"),   
	range: jQuery.format("请输入一个介于 {0} 和 {1} 之间的值"),   
	max: jQuery.format("请输入一个最大为 {0} 的值"),  
	min: jQuery.format("请输入一个最小为 {0} 的值")
};
jQuery.extend(jQuery.validator.messages, cnMsg);

/**
 * select2.js 汉化
 */
(function ($) {
    "use strict";
    $.extend($.fn.select2.defaults, {
        formatNoMatches: function () { return "没有找到匹配项"; },
        formatInputTooShort: function (input, min) { var n = min - input.length; return "请再输入" + n + "个字符";},
        formatInputTooLong: function (input, max) { var n = input.length - max; return "请删掉" + n + "个字符";},
        formatSelectionTooBig: function (limit) { return "你只能选择最多" + limit + "项"; },
        formatLoadMore: function (pageNumber) { return "加载结果中..."; },
        formatSearching: function () { return "搜索中..."; }
    });
})(jQuery);
