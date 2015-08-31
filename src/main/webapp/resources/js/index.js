$(function() {
    mapFloor1 = [  //座位图 
            'aaaaaaaaaa', 
            'aaaaaaaaaa', 
            'aaaaaaaaaa', 
            'aaaaaaaaaa', 
            'aaaaaaaaaa', 
            'aaaaaaaaaa', 
            'aaaaaaaaaa', 
            'aaaaaaaaaa', 
            'aaaaaaaaaa',
            'aaaaaaaaaa'
    ];
    
    mapFloor2 = [  //座位图 
            'aaaaaaaaaa', 
            'aaaaaaaaaa', 
            'aaaaaaaaaa', 
            'aaaaaaaaaa', 
            '__________', 
            'aaaaaaaaaa', 
            'aaaaaaaaaa', 
            'aaaaaaaaaa', 
            '__________',
            'aaaaaaaaaa'
    ];
    
    var sc1 = $('#seat-map1').seatCharts({ 
        map: mapFloor1, 
        legend : { //定义图例 
            node : $('#legend1'), 
            items : [ 
                [ 'a', 'available',   '空座' ], 
                [ 'a', 'unavailable', '有人'] 
            ]                     
        },
		focus  : function() {
		    if (this.status() == 'unavailable') {
		    	var cid = getCardId(this.node().attr('id'), $("body").data("locations"));
		         
		         $("#cardMessage").empty();
		         getAjaxRequest("rs/order/menus?cardId=" + cid, false, function(data){
		         	$("#cardMessage").append("Card ID: " + cid + "<br/>");
			    	for(var i in data) {
			    		var process = data[i].process;
			    		var processStr;
			    		if(process == 1) {
			    			processStr = "已经提交";
			    		}
			    		else if(process == 2) {
			    			processStr = "正在制作";
			    		}
			    		else {
			    			processStr = "<font color=\"red\">已经完成</font>";
			    		}
			    		$("#cardMessage").append(data[i].name + ", " + data[i].copies + "份, " + processStr + "<br/>");
			    	}
			    });
			    
		         $("#cardMessage").show();
		        return 'focused';
		    } else  {
		        return this.style();
		    }
		},
		blur: function() {
			$("#cardMessage").hide();
			return this.status();
		}
    }); 
    var sc2 = $('#seat-map2').seatCharts({ 
        map: mapFloor2, 
        legend : { //定义图例 
            node : $('#legend2'), 
            items : [ 
                [ 'a', 'available',   '空座' ], 
                [ 'a', 'unavailable', '有人'] 
            ]                     
        }
    });
    
    //已售出的座位 
    var floor = getFloor();
	var sc;
	if(floor == 1) {
		sc = sc1;
	} 
	else if(floor == 2) {
		sc =sc2;
	}
	getLocation(sc);

    setInterval(function() {
    	var floor = getFloor();
    	var sc;
    	if(floor == 1) {
			sc = sc1;
		} 
		else if(floor == 2) {
			sc =sc2;
		}
		getLocation(sc);
	    
	}, 5000); //every 5 seconds

	$('#seat-map1').show();
	$('#seat-map2').hide();
	$('#legend1').show();
	$('#legend2').hide();
			
	$("#floorChange").click(function() {
		var floor = $("#floors").find("option:selected").val();
		if(floor == 1) {
			$('#seat-map1').show();
			$('#seat-map2').hide();
			$('#legend1').show();
			$('#legend2').hide();
		} 
		else if(floor == 2) {
			$('#seat-map1').hide();
			$('#seat-map2').show();
			$('#legend1').hide();
			$('#legend2').show();
		}
	});
	
});

function getFloor() {
	var floor = $("#floors").find("option:selected").val();
	return floor;
}

function getCardId(id, data) {
	for(var i in data) {
		var lid = data[i].locationId;
		var cid = data[i].cardId;
		if(id == lid) {
			return cid;
		}
	}
	return 'Not Found!';
}

function getLocation(sc) {
	getAjaxRequest("rs/location", false, function(data){
    	$("body").data("locations", data);
    	for(var i in data) {
			var seatId = data[i].locationId;
			
			if(typeof sc.get(seatId) != "undefined") {
				sc.status(seatId, 'unavailable');
			}
		}
    });
}
