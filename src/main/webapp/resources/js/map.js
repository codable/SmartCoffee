$(function() {
	var getUrlParameter = function getUrlParameter(sParam) {
	    var sPageURL = decodeURIComponent(window.location.search.substring(1)),
	        sURLVariables = sPageURL.split('&'),
	        sParameterName,
	        i;

	    for (i = 0; i < sURLVariables.length; i++) {
	        sParameterName = sURLVariables[i].split('=');

	        if (sParameterName[0] === sParam) {
	            return sParameterName[1] === undefined ? true : sParameterName[1];
	        }
	    }
	};
	
	var floor = getUrlParameter('floor');
	var tableId = getUrlParameter('locationId');
	var x = getUrlParameter('x');
	var y = getUrlParameter('y');
	
	if(typeof floor == 'undefined') {
		alert("Floor不能为空");
		return;
	}
	
	//get the image for specific floor
	$.ajax({
		url: "api/map/picture2/" + floor,
		type: 'GET',
		dataType: 'json',
		cache: false,
		async: false,
		success: function(data){
			$("#location").attr("src", "data:image/png;base64," + data.responseText);
	    },
	    error:function(data){
			$("#location").attr("src", "data:image/png;base64," + data.responseText);
	    }
	});
	
	//display all position data or specific one
	if(typeof x == 'undefined' || typeof y == 'undefined' || typeof locationId == 'undefined') {
		loadAllMark(floor);
	}
	else {
		setMark(x, y, tableId);
	}
	
	//on click send location info to db
	$('#location').click(function(e) {
		var target = e.target;
		var x = (e.pageX - target.offsetLeft);
		var y = (e.pageY - target.offsetTop);
		var table_id = prompt(x + ',' + y);
		var table = {
			id: table_id,
			x: x,
			y: y
		};
		put(table, e.pageX, e.pageY, floor);
	});
	

	$('#floors').change(function() {
		var val = $(this).children('option:selected').val();
		$("#floor_info").html("");
		for(var i=1; i <= val; i++) {
			$("#floor_info").append("<p>第" + i + "层编号范围：<input name=\"floor_begin[]\" type=\"text\" id=\"floor_" + i + "_begin\"> ~ <input name=\"floor_end[]\" type=\"text\" id=\"floor_" + i + "_end\"></p>");
		}
	});
	
	$('#total_area').change(function() {
		var val = $(this).val();
		$("#area_info").html("");
		for(var i=1; i <= val; i++) {
			$("#area_info").append("<p>区域号：<input name=\"area[]\" type=\"text\" id=\"area_" + i + "\"/>，编号范围：<input name=\"area_begin[]\" type=\"text\" id=\"area_" + i + "_begin\"> ~ <input name=\"area_end[]\" type=\"text\" id=\"area_" + i + "_end\"></p>");
		}
	});
	
	$('#clearAll').click(function() {
		var a = confirm("确认要清空吗？");
		if (a == true){
			$.ajax({
				url: "api/map",
				type: 'DELETE',
				dataType: 'json',
				cache: false,
				async: false,
				success: function(data){
					$('#mark').html("");
			    },
			    error:function(data){
			    	$('#mark').html("");
			    }
			});
		}
	});
	$('#revokeLatest').click(function() {
		var a = confirm("确认要删除吗？");
		if (a == true){
			$.ajax({
				url: "api/map/revoke",
				type: 'DELETE',
				dataType: 'json',
				cache: false,
				async: false,
				success: function(data){
					$('#mark').html("");
					loadAllMark(floor);
			    },
			    error:function(data){
			    	$('#mark').html("");
			    	loadAllMark(floor);
			    }
			});
		}
	});

	$('#commitData').click(function() {
		$.ajax({
			url: "api/map/mapping",
			type: 'POST',
			cache: false,
			async: false,
			dataType:'json',
			data: $('#formData').serialize(),
			success: function(data){
				if(data.res == false) {
					alert("请正确填写！");
				}
				else {
					alert("更新成功！");
				}
		    },
		    error:function(data){
		    	//console.log(data);
		    }
		});
	});
});

function loadAllMark(floor) {
	$.ajax({
		url: "api/map",
		type: 'GET',
		dataType: 'json',
		cache: false,
		async: false,
		success: function(data){
			data.forEach(function(e) {
				if(e.floor == floor) {
					if(e.xPos != 0 && e.yPos != 0) {
						setMark(e.xPos, e.yPos, e.locationId);
					}
				}
			});
	    }
	});
}

function put(table, displayX, displayY, floor) {	
	var tableId = table.id.trim();
	if(isNaN(tableId)){
		return;
	}
	var locationIdLength = tableId.length;
	if(locationIdLength == 0 || locationIdLength > 3) {
		return;
	}
	var data = { locationId: tableId, xPos: table.x, yPos: table.y, floor: floor };
	$.ajax({
		url: 'api/map',
		type: 'post',
		data: JSON.stringify(data),
		headers: { 'content-type': 'application/json' },
		dataType: 'json',
		async: false,
		success: function (res) {
			$('#board').html("坐标信息：编号(" + res.locationId + '): X(' + res.xPos + '), Y(' + res.yPos + ')' );
		}
	});

	$('#mark').html("");
	loadAllMark(floor);
}

function setMark(x, y, tableId) {
	$('<div style="background-color:red;color:white;width:20px;height:20px;border:1px solid;text-align:center;line-height:20px; ' +
			'box-shadow: 0 0 10px yellow;border-radius:15px;position:absolute;left:' + (x - 10) + 'px;top:' + (y-10) + 'px;">' + tableId + '</div>').appendTo('#mark');
}

