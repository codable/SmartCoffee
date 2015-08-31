$(function() {
	
	var category = getUrlParam("category");
    
    var retrieveOrder = function() {
    	getAjaxRequest("rs/order/" + category, false, function(data){
	    	$("#orderContent ul").empty();
	        $('#orderContentTemplate').tmpl(data).appendTo('#orderContent ul');
	    });
    }
    
    retrieveOrder();
    
    var cookEventFunc = function(event) {
    	event.preventDefault();
    	var orderId = $(this).attr('id');
    	var isReset = $(this).attr('reset');
    	
    	//get cooker's menus id
    	var cids = "";
    	$(this).parent().siblings().each(function( i, val ) {
  			var cid = $(val).attr("cid");
  			cids += cid + ",";
  		});
    	
    	//set these menus to finish
    	var url = "rs/order/" + orderId + "/cook?isReset=" + isReset + "&cids=" + cids;
    	postAjaxRequest(url, false, function(data){});
	    retrieveOrder();
	    
    	$(".cook").on('click', cookEventFunc);
    };
    
    
    $(".cook").click(cookEventFunc);
	
});

function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg);  //匹配目标参数
    if (r != null) return unescape(r[2]); return null; //返回参数值
}