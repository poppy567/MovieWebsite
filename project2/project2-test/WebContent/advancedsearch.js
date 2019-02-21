/**
 * 
 */

function handleAdvancedSearchResult(resultData) {
    console.log("handleAdvancedSearchResult: jump to the corresponding movie list page");
    var href="movie-list.html?";
    if(resultData[0]["movie_title"]!=null){
    	href+="title="+resultData[0]["movie_title"]+"&";
    }
    if(resultData[0]["movie_year"]!=null){
    	href+="year="+resultData[0]["movie_year"]+"&";
    }
    if(resultData[0]["movie_director"]!=null){
    	href+="director="+resultData[0]["movie_director"]+"&";
    }
    if(resultData[0]["movie_star"]!=null){
    	href+="starname="+resultData[0]["movie_star"]+"&";
    }
    href=href.slice(0,href.length-1);
    console.log("href="+href);
    window.location.replace(href);    
}

function submitAdvancedSearch(formSubmitEvent){
	console.log("submit advanced search");
    console.log(jQuery("#advanced_search").serialize());
    // Important: disable the default action of submitting the form
    //   which will cause the page to refresh
    //   see jQuery reference for details: https://api.jquery.com/submit/
    formSubmitEvent.preventDefault();
    
    jQuery.ajax({
        dataType: "json", // Setting return data type
        method: "GET", // Setting request method
        url: "api/main", // Setting request url, which is mapped to the TestServlet
        data: jQuery("#advanced_search").serialize(),
        success: (resultData) => handleAdvancedSearchResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
    });
}

//Bind the submit action of the form to a handler function
jQuery("#advanced_search").submit((event) => submitAdvancedSearch(event));
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