jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/metadata", // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handletableResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});


function handletableResult(resultData){
	 resultDataJson = resultData;
	 console.log(resultDataJson.length);
	 console.log(resultDataJson.toString());
	 console.log(resultDataJson[1]["table_name"]);
	 for(var i=resultDataJson.length-1;i>=0;i--){
		 var s = i.toString();
		 jQuery("#table_title"+s).html(resultDataJson[i]["table_name"]);
		 jQuery("#table_list"+s).html(resultDataJson[i]["data"]);
	 }
}