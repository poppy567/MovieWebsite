/**
 * The movielist.js needs to handle the normal search submit event
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
	//location.reload(false);
    console.log("handleResult: populating movie info from resultData");

    // populate the star info h3
    // find the empty h3 body by id "star_info"
    /*let starInfoElement = jQuery("#star_info");

    // append two html <p> created to the h3 body, which will refresh the page
    starInfoElement.append("<p>Star Name: " + resultData[0]["star_name"] + "</p>" +
        "<p>Date Of Birth: " + resultData[0]["star_dob"] + "</p>");
    */
    result = resultData;
    console.log("handleResult: populating movie table from resultData");

    // Populate the star table
    // Find the empty table body by id "movie_table_body"
    let movieTableBodyElement = jQuery("#movie_table_body");
    movieTableBodyElement.empty();
    console.log("resultData.length: "+resultData.length);
    console.log("rangeint: "+rangeint);
    console.log("offset: "+offset);
    // Concatenate the html tags with resultData jsonObject to create table rows
    if(offset<0 ){
    	console.log("Error:pagenumber you choose is out of the range of pages we have");
    	alert("Error:pagenumber you choose is invalid(smaller than 0)\n We will go back to the first page");
    	offset = 0;
    	page = 1;
    }
    else if(offset>resultData.length){
    	alert("Error:pagenumber you choose is invalid(bigger than we have)\n We will go back to the last page");
    	page = resultData.length/rangeint;
    	console.log("page jump: "+ page);
    	offset = (page-1)*rangeint;
    }
    for (let i = offset; i < Math.min(offset+rangeint, resultData.length); i++) {
        let rowHTML = "";
        var star_id = resultData[i]['star_id'].split(",");
        var star_name = resultData[i]["movie_star"].split(",");
        rowHTML += "<tr>";
        rowHTML += "<th>" + resultData[i]["movie_id"] + "</th>";
        rowHTML +=
            "<th>" +
            // Add a link to single-star.html with id passed with GET url parameter
            '<a href="single-movie.html?id=' + resultData[i]['movie_id'] + '">'
            + resultData[i]["movie_title"] +     // display star_name for the link text
            '</a>' +
            "</th>";
        rowHTML += "<th>" + resultData[i]["movie_year"] + "</th>";
        rowHTML += "<th>" + resultData[i]["movie_director"] + "</th>";
        rowHTML += "<th>" + resultData[i]["movie_genre"] + "</th>";
        console.log()
        rowHTML += "<th>";
        
        for(let j = 0; j < star_id.length; j++){
        	rowHTML +=
                // Add a link to single-star.html with id passed with GET url parameter
                '<a href="single-star.html?id=' + star_id[j].split(" ").join("") + '">'
                + star_name[j]+", "+     // display star_name for the link text
                '</a> ';
        }
        rowHTML = rowHTML.slice(0,rowHTML.lastIndexOf(",")).concat(rowHTML.slice(rowHTML.lastIndexOf(",")+1,rowHTML.length));
        rowHTML += "</th>";
        rowHTML += "<th>" + resultData[i]["movie_rating"] + "</th>";

        //rowHTML += "<th><BUTTON id='add_to_cart' class='btn btn-success' onclick=\"handle_add_to_cart('"+resultData[i]['movie_id']+"','"+resultData[i]['movie_title']+"')\">" +
        //		"<IMG src='./pic/add_to_cart.png' width='100%'></BUTTON></th>";

        if(resultData[i]["movie_id"]!=""){
        rowHTML += "<th class='col-md-3'><BUTTON id='add_to_cart' class='btn btn-success col-sm-3' onclick=\"handle_add_to_cart('"+resultData[i]['movie_id']+"','"+resultData[i]['movie_title']+"')\">Add</BUTTON></th>";
        }

        rowHTML += "</tr>";
        // Append the row created to the table body, which will refresh the page
        movieTableBodyElement.append(rowHTML);
    }
}
function handlesort(a,b){
	console.log("a,b: "+a+", "+b);
	if(a.indexOf("rating") != -1){
		if(b.indexOf("ascend") != -1){
			sortonrating = "asc";
			sortontitle = null;
		}
		else{
			sortonrating = "desc";
			sortontitle = null;
		}
	}
	else if(a.indexOf("title") != -1){
		if(b.indexOf("ascend") != -1){
			sortontitle = "asc";
			sortonrating = null;
		}
		else{
			sortontitle = "desc";
			sortonrating = null;
		}
	}
	console.log("sortontitle: "+sortontitle);
	console.log("sortonrating: "+sortonrating);
	//location.reload(true);
	jQuery.ajax({
	    dataType: "json", // Setting return data type
	    method: "GET", // Setting request method
	    url: "api/movielist?title="+movieTitle+"&year="+movieYear+"&director="+director+"&starname="+
    	starname+"&genre="+genre+"&sortontitle="+sortontitle+"&sortonrating="+sortonrating, // Setting request url, which is mapped by StarsServlet in Stars.java
	    success: (resultData) => handleMovieResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
	});
}
function handlerange(a){
	if(a.indexOf("10") != -1){
		rangeint = 10;
	}
	else if(a.indexOf("20") != -1){
		rangeint =20;
	}
	else if(a.indexOf("30") != -1){
		rangeint =30;
	}
	console.log("rangeint in handlerange function: "+rangeint);
	handleMovieResult(result);
}
function handlepage(a){
	if(a==true){
		page = page-1;
		offset = (page-1)*rangeint;
	}
	else{
		page = page+1;
		offset = (page-1)*rangeint;
	}
	handleMovieResult(result);
} 

function handle_add_to_cart(id, title){
	console.log("movie_id: "+id);
	console.log("movie_title: "+title);
	jQuery.ajax({
        dataType: "json", // Setting return data type
        method: "GET", // Setting request method
        url: "api/shoppingcart?id="+id+"&title="+title, // Setting request url, which is mapped to the TestServlet
        success: (resultData) => handleSearchResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
    });
}
//get the parameter from url
let movieTitle = getParameterByName('title');
let movieYear = getParameterByName('year');
let director = getParameterByName('director');
let starname = getParameterByName('starname');
let genre = getParameterByName('genre');
let sortontitle = getParameterByName('sortontitle');
let sortonrating = getParameterByName('sortonrating');
let pagenumber  = getParameterByName('pagenumber');
let range = getParameterByName('range');
var result;
if(!range){
	var rangeint = 20;
}
else{
	var rangeint = parseInt(range);
}
if(!pagenumber){
	var offset = 0;
	var page = 1;
}
else{
	var page = parseInt(pagenumber);
	var offset = (page-1)*rangeint;
}
// Makes the HTTP GET request and registers on success callback function handleStarResult
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/movielist?title="+movieTitle+"&year="+movieYear+"&director="+director+"&starname="+
    	starname+"&genre="+genre+"&sortontitle="+sortontitle+"&sortonrating="+sortonrating, // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleMovieResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});