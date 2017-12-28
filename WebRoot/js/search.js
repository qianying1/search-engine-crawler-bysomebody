$('#submitBtn').bind(
				'click',
				function() {
					//var searchName=$('#searchName').attr("value");
					var searchName=document.getElementById("searchName").value
					jQuery.ajax({
						url : 'SortNewsServlet',
						data : {
							      searchName:searchName,
							      first:true
							   },
						type : "post", 
						cache : false,
						success : function(data) {
							//alert(searchName);
							//从SortNewsServlet跳转到DividePageServlet
							window.location.href="http://localhost:8080/SearchEngine/DividePageServlet";
						}
					});
					return false;
				});