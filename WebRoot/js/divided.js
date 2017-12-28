$('#submitBtn').bind(
				'click',
				function() {
					//var searchName=$('#searchName').attr("value");
					var searchName=document.getElementById("searchName").value
					jQuery.ajax({
						url : 'SortNewsServlet',
						data : {searchName:searchName},
						type : "post", 
						cache : false,
						success : function(data) {
							window.location.href="http://localhost:8080/SearchEngine/DividePageServlet";
						}
					});
					return false;
				});