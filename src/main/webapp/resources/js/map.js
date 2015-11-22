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
			$('<div style="background-color:red;color:white;width:20px;height:20px;border:1px solid;text-align:center;line-height:20px;box-shadow: 0 0 10px yellow;border-radius:15px;position:absolute;left:' + (x - 10) + 'px;top:' + (y-10) + 'px;">' + tableId + '</div>').appendTo('#mark');

	    }
	});
});

