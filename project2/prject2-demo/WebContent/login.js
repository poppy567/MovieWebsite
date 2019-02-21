/**
 * Handle the data returned by LoginServlet
 * @param resultDataString jsonObject
 */
function handleLoginResult(resultDataString) {
	//console.log("resultDataString: "+typeof(resultDataString));
    resultDataJson = resultDataString;
    
    console.log("handle login response");
    
    //console.log("resultDatastatus: "+JSON.stringify(resultDataJson[0]));
    //jQuery("#login_error_message").text(resultDataJson["test"]);
    // If login success, redirect to index.html page
    if (resultDataJson[0]["status"] === "success"&&resultDataJson[0]["identification"]==="user") {
        window.location.replace("index.html");
    	//console.log("success");
    }
    else if(resultDataJson[0]["status"] === "success"&&resultDataJson[0]["identification"]==="employee"){
    	window.location.replace("_dashboard.html");
    }
    // If login fail, display error message on <div> with id "login_error_message"
    else {

        console.log("show error message");
        console.log(resultDataJson[0]["message"]);
        jQuery("#login_error_message").text(resultDataJson[0]["message"]);
    }
}

/**
 * Submit the form content with POST method
 * @param formSubmitEvent
 */
function submitLoginForm(formSubmitEvent) {
    console.log("submit login form");
    console.log(jQuery("#login_form").serialize());
    
    // Important: disable the default action of submitting the form
    //   which will cause the page to refresh
    //   see jQuery reference for details: https://api.jquery.com/submit/
    formSubmitEvent.preventDefault();

    /*jQuery.post(
        "api/login",
        // Serialize the login form to the data sent by POST request
        jQuery("#login_form").serialize(),
        (resultDataString) => handleLoginResult(resultDataString));*/
    jQuery.ajax({
        dataType: "json", // Setting return data type
        method: "POST", // Setting request method
        url: "api/login", // Setting request url, which is mapped by LoginServelet in LoginServelet.java
        data: jQuery("#login_form").serialize(),
        success: (resultData) => handleLoginResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
    });

    }


// Bind the submit action of the form to a handler function
jQuery("#login_form").submit((event) => submitLoginForm(event));

