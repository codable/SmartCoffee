$(function() {
	
	$("#searchOrder").button().click(function(event) {
    	event.preventDefault();
    	var cardId = $("#cardId").val();
    	var locationId = $("#locationId").val();
    	var url = 'rs/order?cid=' + cardId + '&lid=' + locationId;
    	$("#orderContent").jqGrid().setGridParam({url : url}).trigger("reloadGrid");
    });
    
    var retrieveOrder = function() {
    	getAjaxRequest("rs/order", false, function(data){
	    	$("#orderContent ul").empty();
	        $('#orderContentTemplate').tmpl(data).appendTo('#orderContent ul');
	        
	        $("#finished-orders").empty();
	        for(var i in data) {
	        	var order = data[i];
	        	if(order.finish) {
	        		var orderId = order.orderId;

	        		getAjaxRequest("rs/order/" + orderId + "/location", false, function(data){
				    	var lid = data.location;
				    	if(lid != "") {
				    		$('<li>Order <font color="blue">' + orderId + '</font> 已经制作完毕，由服务员递送到桌号：<font color="blue">' + lid + '</font></li>').appendTo("#finished-orders");
				    	}
				    });
	        	}
	        }
	    });
    }
    
    retrieveOrder();
    
    setInterval(function() {
    	retrieveOrder();
	}, 3000); //every 3 seconds
	
	
});