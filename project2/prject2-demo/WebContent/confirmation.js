/**
 * 
 */
function handleConfirmResult(resultData){
	console.log("customer_id: "+resultData[0]["customer_id"]);
}
console.log("load confirmation page");
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/confirm", // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleConfirmResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});