/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */
function handleSearchResult(resultData) {
    console.log("handleMainSearchResult: jump to the corresponding movie list page");
    window.location.replace("movie-list.html?title="+resultData[0]["movie_title"]);    
}

function submitMainSearch(formSubmitEvent) {
	console.log("submit main search");
    console.log(jQuery("#main_search").serialize());
    // Important: disable the default action of submitting the form
    //   which will cause the page to refresh
    //   see jQuery reference for details: https://api.jquery.com/submit/
    formSubmitEvent.preventDefault();
    
    jQuery.ajax({
        dataType: "json", // Setting return data type
        method: "GET", // Setting request method
        url: "api/main", // Setting request url, which is mapped by StarsServlet in Stars.java
        data: jQuery("#main_search").serialize(),
        success: (resultData) => handleSearchResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
    });
}


//Bind the submit action of the form to a handler function
jQuery("#main_search").submit((event) => submitMainSearch(event));