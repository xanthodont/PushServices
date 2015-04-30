var Utils = (function(){
	function dateTimeFormatter(val, row) {
		if (val == null) return 'time is null';
		var d = new Date(val);
		return dateFormatter(d);
	}
	function dateFormatter(d) {
		return d.getFullYear() + '-' + (d.getMonth()+1) + '-' + d.getUTCDate() + ' ' + d.getHours().toFormatString(2) + ':' + d.getMinutes().toFormatString(2);
	}
	/**
	 * example:
	 * 	1.toFormatString(2) => 01,  7.toFormatString(3) => 007
	 * @param digits 位数
	 */
	Number.prototype.toFormatString = function(digits) {
		var n = this;
		if (n < 0) return;
		var a = [];
		var digit;
		while (digits-- > 0 || n > 0) {
			digit = n % 10;
			a.push(digit);
			n = Math.floor(n / 10);
		}
		return a.reverse().join('');
	};

	return {
		dateTimeFormatter: dateTimeFormatter,
		dateFormatter: dateFormatter
	};
})();