$(function() {
    var mapInfo = {};
	getAjaxRequest("api/map/location", false, function(data){
		mapInfo = data;
    });
	
    var retrieveOrder = function() {
    	getAjaxRequest("api/order/all", false, function(data){
	    	//$("#orderContent").empty();
	    	var locations = [];
	    	data.forEach(function(e) {
	    		item = e.location;
	    		map = mapInfo[item.locationId];
	    		if(typeof(map) != 'undefined') {
	    			item.x = map.xPos;
		    		item.y = map.yPos;
		    		item.locationDis = map.floor + '层' + map.area + '区';
		    		item.flag = 1;
	    		}
	    		else {
	    			item.locationDis = item.locationId;
	    			item.flag = 0;
	    		}
	    		locations.push(item);
	    	});
	    	$("#orderContent").html("<thead><tr><th>小熊</th><th>号码牌</th><th>位置</th></tr></thead>");
	        $('#orderContentTemplate').tmpl(locations).appendTo('#orderContent');
	        //console.log(data);
	    });
    }
    
    retrieveOrder();
    
    /*setInterval(function() {
    	retrieveOrder();
	}, 3000); //every 3 seconds*/
    $('#orderContent a').miniPreview({ prefetch: 'none', width: '900', height: '460', scale: 0.75 });

	
});