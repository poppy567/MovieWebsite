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
var cache = new Array();

function handleLookup(query, doneCallback) {
	console.log("autocomplete initiated")
	
	
	// TODO: if you want to check past query results first, you can do it here
	//if there is an old query which contains this query
	for (i in cache){
		console.log(cache[i]);
	}
	for (i in cache){
		//if the new query is same as one of the old query(except the space)
		if(String(cache[i].input)==query.trim()){
			console.log("Use old result");
			handleLookupAjaxSuccess(cache[i].result, query, doneCallback);
			return;
		}
	}
	console.log("sending AJAX request to backend Java Servlet")
	// sending the HTTP GET request to the Java Servlet endpoint hero-suggestion
	// with the query data
	jQuery.ajax({
		"method": "GET",
		// generate the request url from the query.
		// escape the query string to avoid errors caused by special characters 
		"url": "api/movie-suggestion?query=" + escape(query),
		"success": function(data) {
			//store the result of this query
			var old_query = {
				input:query.trim(),
				result:data
			};
			cache.push(old_query);
			// pass the data, query, and doneCallback function into the success handler
			handleLookupAjaxSuccess(data, query, doneCallback) 
		},
		"error": function(errorData) {
			console.log("lookup ajax error")
			console.log(errorData)
		}
	});
}

/*
 * This function is used to handle the ajax success callback function.
 * It is called by our own code upon the success of the AJAX request
 * 
 * data is the JSON data string you get from your Java Servlet
 * 
 */
function handleLookupAjaxSuccess(data, query, doneCallback) {
	console.log("lookup ajax successful")
	// parse the string into JSON
	var jsonData = JSON.parse(data);
	console.log(jsonData)
	
	// TODO: if you want to cache the result into a global variable you can do it here

	// call the callback function provided by the autocomplete library
	// add "{suggestions: jsonData}" to satisfy the library response format according to
	//   the "Response Format" section in documentation
	doneCallback( { suggestions: jsonData } );
}

/*
 * This function is the select suggestion handler function. 
 * When a suggestion is selected, this function is called by the library.
 * 
 * You can redirect to the page you want using the suggestion data.
 */
function handleSelectSuggestion(suggestion) {
	// TODO: jump to the specific result page based on the selected suggestion
	
	console.log("you select " + suggestion["value"]);
	var url = "single-movie.html?id="+suggestion["data"]["ID"];
	console.log(url);
	window.location.replace(url);
}

/*
 * This statement binds the autocomplete library with the input box element and 
 *   sets necessary parameters of the library.
 * 
 * The library documentation can be find here: 
 *   https://github.com/devbridge/jQuery-Autocomplete
 *   https://www.devbridge.com/sourcery/components/jquery-autocomplete/
 * 
 */
// $('#autocomplete') is to find element by the ID "autocomplete"
$('#autocomplete').autocomplete({
	// documentation of the lookup function can be found under the "Custom lookup function" section
    lookup: function (query, doneCallback) {
    		handleLookup(query, doneCallback)
    },
    onSelect: function(suggestion) {
    		handleSelectSuggestion(suggestion)
    },
    // set delay time
    deferRequestBy: 300,
    groupBy: "category",
    // there are some other parameters that you might want to use to satisfy all the requirements
    // TODO: add other parameters, such as minimum characters
    minChars:3
});

function handleNormalSearch(query) {
	console.log("doing normal search with query: " + query);
	// TODO: you should do normal search here
	jQuery.ajax({
        dataType: "json", // Setting return data type
        method: "GET", // Setting request method
        url: "api/main?title=" + escape(query), // Setting request url, which is mapped by StarsServlet in Stars.java
        success: (resultData) => handleSearchResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
    });
}

//bind pressing enter key to a handler function
$('#autocomplete').keypress(function(event) {
	// keyCode 13 is the enter key
	if (event.keyCode == 13) {
		// pass the value of the input box to the handler function
		handleNormalSearch($('#autocomplete').val())
	}
})