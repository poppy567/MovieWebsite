/**
 * Handle the data returned by LoginServlet
 * @param resultDataString jsonObject
 */
function handle_insert_star_Result(resultDataString) {
	//console.log("resultDataString: "+typeof(resultDataString));
    resultDataJson = resultDataString;
    
    console.log("handle success response");
   
    if (resultDataJson[0]["status"] === "success") {
        /*//window.location.replace("index.html");
    	//console.log("success");
    	document.write("<div class='alert alert-success'>");
    	//document.write("");
    	document.write("<button type='button' aria-hidden='true' class='close' data-dismiss='alert'></button>");
    	document.write("<span>");
    	document.write("<b> Success - </b> success to insert "+resultDataJson[0]["starname"]+"("+resultDataJson[0]["birthday"]+")</span> </div>");    
   */
    	
    	jQuery("#show_massage").text("Success -success to insert "+resultDataJson[0]["starname"]+"("+resultDataJson[0]["birthday"]+")");
    }
    // If login fail, display error message on <div> with id "show_massage"
    else {

        console.log("show error message");
        console.log(resultDataJson[0]["message"]);
        //jQuery("#show_massage").text(resultDataJson[0]["message"]);
        
        jQuery("#show_massage").text("<b> fail - </b> fail to insert star</span> </div>"); 
    }
}


function handle_add_movie_Result(resultDataString) {
	//console.log("resultDataString: "+typeof(resultDataString));
    resultDataJson = resultDataString;
    
    console.log("handle success response");
   
    switch(resultDataJson[0]["status"])
    {
    case "1":jQuery("#show_movie_massage").text("Success -success to insert new movie with new star and genre");
    break;
    case "2":jQuery("#show_movie_massage").text("Success -success to insert new movie with new star");
    break;
    case "3":jQuery("#show_movie_massage").text("Success -success to insert new movie with new genre");
    break;
    case "4":jQuery("#show_movie_massage").text("Success -success to insert new movie");
    break;
    case "5":jQuery("#show_movie_massage").text("Existed the movie, but insert new star and new genre");
    break;
    case "6":jQuery("#show_movie_massage").text("Existed the movie, but insert new star");
    break;
    case "7":jQuery("#show_movie_massage").text("Existed the movie, but insert new genre");
    break;
    case "8":jQuery("#show_movie_massage").text("Existed the movie");
    break;
    default:
    	jQuery("#show_movie_massage").text("fail to add");
    }
    
}

/**
 * Submit the form content with POST method
 * @param formSubmitEvent
 */
function submit_insert_starForm(formSubmitEvent) {
    console.log("submit_insert_starForm");
    console.log(jQuery("#insert_star").serialize());
    
    // Important: disable the default action of submitting the form
    //   which will cause the page to refresh
    //   see jQuery reference for details: https://api.jquery.com/submit/
    formSubmitEvent.preventDefault();
    var str=jQuery("#star_year").serialize();
    if(str.length==13){
    	str=str.substr(9,4);
    	console.log(str);
    	}
    else{
    	window.alert("please input the correct birthyear!");
		return false;
    }
    var pattern = /[\d]{4}/g;
    if(!pattern.test(str))
	{
	window.alert("please input the correct birthyear!");
	return false;
	}
    /*jQuery.post(
        "api/login",
        // Serialize the login form to the data sent by POST request
        jQuery("#login_form").serialize(),
        (resultDataString) => handleLoginResult(resultDataString));*/
    jQuery.ajax({
        dataType: "json", // Setting return data type
        method: "Get", // Setting request method
        url: "api/insertstar", // Setting request url, which is mapped by LoginServelet in LoginServelet.java
        data: jQuery("#insert_star").serialize(),
        success: (resultData) => handle_insert_star_Result(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
    });

    }



function submit_add_movieForm(formSubmitEvent) {
    console.log("submit_add_movieForm");
    console.log(jQuery("#add_movie").serialize());
    formSubmitEvent.preventDefault();

    var str=jQuery("#year").serialize();
    if(str.length==15){
    	str=str.substr(11,4);
    	console.log(str);
    	}
    else{
    	window.alert("please input the correct year!");
		return false;
    }
    var pattern = /[\d]{4}/g;
    if(!pattern.test(str))
	{
	window.alert("please input the correct year!");
	return false;
	}
    jQuery.ajax({
        dataType: "json", // Setting return data type
        method: "Get", // Setting request method
        url: "api/add_movie", // Setting request url, which is mapped by LoginServelet in LoginServelet.java
        data: jQuery("#add_movie").serialize(),
        success: (resultData) => handle_add_movie_Result(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
    });

    }


// Bind the submit action of the form to a handler function
jQuery("#insert_star").submit((event) => submit_insert_starForm(event));
jQuery("#add_movie").submit((event) => submit_add_movieForm(event));
