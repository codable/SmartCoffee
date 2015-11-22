$(function() {
	$("#commit").click(function() {
		var data = {pdata: $("#blue_data").val()};
		$.ajax({
			url: "api/location/blue",
			type: 'POST',
			data: data,
			cache: false,
			async: false,
			success: function(data){
				console.log(data);
				$("#result").html(data);
		    },
		    error:function(data){
		    	alert("Failed");
		    }
		});
		
		
	});
	
});

