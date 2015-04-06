/**
 * 后台导航栏效果初始化
 */
var Navigation = (function() {
	var dataConfig = [
		{admin: []},
		{ota: ['project', 'version', 'delta', 'testimei']},
		{user: ['account', 'role']},
		{chart: []},
		{system: []}
	];
	var data = {barMenu: 0, subMenu: 0};
	
	var initData = function() {
		var pathName = window.location.pathname;
		var p = pathName.split('/', 4);
		//console.log(p);
		dataConfig.filter(function(x, i) {
			if (p[2] in x) {
				data.barMenu = i;
				x[p[2]].filter(function(y, j) {
					if (p[3] == y) data.subMenu = j;
				});
			}
		});
		
		if (data.barMenu == undefined) data.barMenu = 0;
		if (data.subMenu == undefined) data.subMenu = 0;
	};
	
	var initNavigation = function() {
		var navList = $('.page-sidebar-menu .menu-bar');
		var activeBarMenu = navList[data.barMenu];
		$(activeBarMenu).addClass('active');	// 
		$(activeBarMenu).find('span.title').after('<span class="selected"></span>'); 
		var activeSubMenu = $(activeBarMenu).children('.sub-menu').children('li')[data.subMenu];
		$(activeSubMenu).addClass('active');
	}
	
	var InitClickFunc = function() {
		var navList = $('.page-sidebar-menu .menu-bar');
		navList.each(function(i){
			var barMenu = navList[i];
			$(barMenu).click(function() {
				//$.cookie('navigation_barMenu', i);
				var subList = $(this).children('.sub-menu').children('li');
				subList.each(function(ii) {
					var subMenu = subList[ii]; 
					$(subMenu).click(function(){
						//$.cookie('navigation_barMenu', i);
					});
				});
			});
		});
	}
	
	
	return {
		init: function() {
			initData();
			initNavigation();
		}
	};
})();
