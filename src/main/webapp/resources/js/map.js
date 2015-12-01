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
		$.ajax({
			url: "api/map",
			type: 'GET',
			dataType: 'json',
			cache: false,
			async: false,
			success: function(data){
				data.forEach(function(e) {
					var recordFloor = e.locationId.substring(0, 1);
					if(recordFloor == floor) {
						recordTableId = parseInt(e.locationId.substring(1, 4));
						setMark(e.xPos, e.yPos, recordTableId);
					}
				});
		    }
		});
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
});


function put(table, displayX, displayY, floor) {	
	var tableId = table.id.trim();
	if(isNaN(tableId)){
		return;
	}
	var locationIdLength = tableId.length;
	var locationId;
	if(locationIdLength == 0 || locationIdLength > 3) {
		return;
	}
	else if(locationIdLength == 1) {
		locationId = "00" + tableId;
	}
	else if(locationIdLength == 2) {
		locationId = "0" + tableId;
	}
	else if(locationIdLength == 3) {
		locationId = tableId;
	} 
	var data = { locationId: floor + locationId, xPos: table.x, yPos: table.y };
	$.ajax({
		url: 'api/map',
		type: 'post',
		data: JSON.stringify(data),
		headers: { 'content-type': 'application/json' },
		dataType: 'json',
		success: function (res) {
			$('#board').html("坐标信息：" + res.locationId + ': ' + res.xPos + ',' + res.yPos);
		}
	});

	setMark(displayX, displayY, tableId);
}

function setMark(x, y, tableId) {
	$('<div style="background-color:red;color:white;width:20px;height:20px;border:1px solid;text-align:center;line-height:20px; ' +
			'box-shadow: 0 0 10px yellow;border-radius:15px;position:absolute;left:' + (x - 10) + 'px;top:' + (y-10) + 'px;">' + tableId + '</div>').appendTo('#mark');
}

