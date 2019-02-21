/**
 * 
 */
function handleCartResult(resultData){
	console.log("handleResult: populating shopping cart from resultData");
	let movieTableBodyElement = jQuery("#movie_table_body");
	movieTableBodyElement.empty();
	console.log("resultData.length: "+resultData.length);
	for (let i = 0; i < resultData.length; i++) {
		let rowHTML = "";
		rowHTML += "<tr>";
        rowHTML += "<th>" +
        // Add a link to single-star.html with id passed with GET url parameter
        '<a href="single-movie.html?id=' + resultData[i]['movie_id'] + '">'
        + resultData[i]["movie_title"] +     // display star_name for the link text
        '</a>' +
        "</th>";
        rowHTML += "<th>" + resultData[i]["movie_qty"] + "</th>";
        rowHTML += "<th><input type='text' name='qty'></th> " +
        		"<th><input type='button' class='btn btn-success col-sm-5' value='confirm' onclick=\"handle_update_qty('"+resultData[i]['movie_id']+"','"+resultData[i]['movie_title']+"','"+i+"')\"></th>" +
        		"<th><button type='submit' class='btn btn-success col-sm-5' value='Update'> Update </button> </th>";
        
        rowHTML += "<th><input type='button' class='btn btn-success col-sm-5' value='Delete' onclick=\"handle_delete_item('"+resultData[i]['movie_id']+"')\"></th>";
        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        movieTableBodyElement.append(rowHTML);
	}
}

function UpdateQty(formSubmitEvent) {
	console.log("update qty in shopping cart page");
	str = jQuery("#update_qty").serialize();
	console.log("update_i: "+update_i);
	var qty;
	for(let i = 0; i < update_i; i++){
		str = str.slice(str.indexOf("&")+1,str.length);
		console.log("str: "+str);
	}
	if(str.indexOf("&")!=-1){
		qty=parseInt(str.slice(4,str.indexOf("&")));
		console.log("test: "+str.slice(4,str.indexOf("&")));
	}else{
		qty=parseInt(str.slice(4,str.length));
		console.log("test: "+str.slice(4,str.length));
	}
	
	if(qty<0){
		alert("the quantity you choose is an invalid number, please try again");
		return;
	}
    console.log(str);
    // Important: disable the default action of submitting the form
    //   which will cause the page to refresh
    //   see jQuery reference for details: https://api.jquery.com/submit/
    formSubmitEvent.preventDefault();
    
	jQuery.ajax({
        dataType: "json", // Setting return data type
        method: "GET", // Setting request method
        url: "api/shoppingcart?showcart=true&id="+update_id+"&title="+update_title, // Setting request url, which is mapped to the TestServlet
        data: str,
        success: (resultData) => handleCartResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
    });
}

jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/shoppingcart?showcart=true", // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleCartResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});

//Bind the submit action of the form to a handler function
jQuery("#update_qty").submit((event) => UpdateQty(event));
var update_id;
var update_title;
var update_i;
function handle_update_qty(id, title, i){
	console.log("id: "+id);
	console.log("title: "+title);
	update_id=id;
	update_title=title;
	update_i=i;
}
function handle_delete_item(id){
	console.log("delete an item in shopcart page");
	console.log("delete id: "+id);
	jQuery.ajax({
        dataType: "json", // Setting return data type
        method: "GET", // Setting request method
        url: "api/shoppingcart?showcart=true&delete=true&id="+id, // Setting request url, which is mapped to the TestServlet
        success: (resultData) => handleCartResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
    });
}