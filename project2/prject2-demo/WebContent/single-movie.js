/* The movielist.js needs to handle the normal search submit event
 * which happened on the head of page
 * 
 * However, its main job is getting the parameter from url, sent it to the MovieListServlet
 * and show the result accroding to the response
 */

function handleSearchResult(resultData) {
    console.log("handleMainSearchResult: jump to the corresponding movie list page");
    window.location.replace("movie-list.html?title="+resultData[0]["movie_title"]);    
}
function submitMainSearch(formSubmitEvent) {
	console.log("submit main search in movie-list page");
    console.log(jQuery("#main_search").serialize());
    // Important: disable the default action of submitting the form
    //   which will cause the page to refresh
    //   see jQuery reference for details: https://api.jquery.com/submit/
    formSubmitEvent.preventDefault();
    
    jQuery.ajax({
        dataType: "json", // Setting return data type
        method: "GET", // Setting request method
        url: "api/main", // Setting request url, which is mapped to the TestServlet
        data: jQuery("#main_search").serialize(),
        success: (resultData) => handleSearchResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
    });
}
//Bind the submit action of the form to a handler function
jQuery("#main_search").submit((event) => submitMainSearch(event));

/**
 * Once this .js is loaded, following scripts will be executed by the browser
 */
function getParameterByName(target) {
    // Get request URL
    let url = window.location.href;
    console.log("url: "+url);
    // Encode target parameter name to url encoding
    target = target.replace(/[\[\]]/g, "\\$&");
    

    // Use regular expression to find matched parameter value
    let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    results[2]=results[2].split("%").join("");
    // Return the decoded parameter value
    console.log("result: "+ decodeURIComponent(results[2].replace(/\+/g, " ")));
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

function handleMovieResult(resultData) {
	//console.log("handleResult: populating movie info from resultData");

    // populate the star info h3
    // find the empty h3 body by id "star_info"
    //let starInfoElement = jQuery("#movie_page");

    // append two html <p> created to the h3 body, which will refresh the page
    //starInfoElement.append("<p>Star Name: " + resultData[0]["star_name"] + "</p>" +
     //   "<p>Date Of Birth: " + resultData[0]["star_dob"] + "</p>");

    console.log("handleResult: populating movie table from resultData");
    // resultDataJson = resultDataString;
    // Populate the star table
    // Find the empty table body by id "movie_table_body"
    let movieTableBodyElement = jQuery("#single_movie_table");
    //if(resultData[0]["movie_title"]!=""){
    // Concatenate the html tags with resultData jsonObject to create table rows
    console.log(resultData.length);
    for (let i = 0; i < resultData.length; i++) {
    	var genre = resultData[i]['movie_genre'].split(",");
        var star_name = resultData[i]["movie_star"].split(",");
        var star_id = resultData[i]['star_id'].split(",");
        let rowHTML = "";
        rowHTML += "<tr>";
        rowHTML += "<th>" + resultData[i]["movie_title"] + "</th>";
        rowHTML += "<th>" + resultData[i]["movie_year"] + "</th>";
        rowHTML += "<th>" + resultData[i]["movie_director"] + "</th>";
        rowHTML += "<th>";
        for(let j = 0; j < genre.length; j++){
        	rowHTML +=
                // Add a link to single-star.html with id passed with GET url parameter
                '<a href="movie-list.html?genre=' + genre[j].split(" ").join("") + '">'
                + genre[j]+", "+     // display star_name for the link text
                '</a> ';
        }
        rowHTML += "</th>";
        rowHTML = rowHTML.slice(0,rowHTML.lastIndexOf(",")).concat(rowHTML.slice(rowHTML.lastIndexOf(",")+1,rowHTML.length));
        rowHTML += "<th>";
        for(let j = 0; j < star_id.length; j++){
        	rowHTML +=
                // Add a link to single-star.html with id passed with GET url parameter
                '<a href="single-star.html?id=' + star_id[j].split(" ").join("") + '">'
                + star_name[j]+", "+     // display star_name for the link text
                '</a> ';
        }
        rowHTML += "</th>";
        rowHTML = rowHTML.slice(0,rowHTML.lastIndexOf(",")).concat(rowHTML.slice(rowHTML.lastIndexOf(",")+1,rowHTML.length));
        //rowHTML += "<th>" + resultData[i]["movie_genre"] + "</th>";
        //rowHTML += "<th>" + resultData[i]["movie_star"] + "</th>";
        rowHTML += "<th><BUTTON id='add_to_cart' class='btn btn-success' onclick=\"handle_add_to_cart('"+resultData[i]['movie_id']+"','"+resultData[i]['movie_title']+"')\">" +
		"Add</BUTTON></th>";
        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        movieTableBodyElement.append(rowHTML);
        
    }
    console.log(resultData[0]["movie_id"]);
}



function handle_add_to_cart(id, title){
	console.log("movie_id: "+id);
	console.log("movie_title: "+title);
	jQuery.ajax({
        dataType: "json", // Setting return data type
        method: "GET", // Setting request method
        url: "api/shoppingcart?id="+id+"&title="+title, // Setting request url, which is mapped to the TestServlet
        //success: (resultData) => handleSearchResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
    });
}







let movieid = getParameterByName('id');
let title=getParameterByName('title');

jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/singlemovie?id="+movieid, // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleMovieResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});


