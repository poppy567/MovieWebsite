
/**
 * Handle the data returned by LoginServlet
=======
//import java.util.*;
/**
 
* Handle the data returned by LoginServlet
>>>>>>> chen
 * @param resultDataString jsonObject
 */
function handleCardResult(resultDataString) {
	//console.log("resultDataString: "+typeof(resultDataString));
    resultDataJson = resultDataString;
    
    console.log("handle login response");
    
    //console.log("resultDatastatus: "+JSON.stringify(resultDataJson[0]));
    //jQuery("#login_error_message").text(resultDataJson["test"]);
    // If login success, redirect to index.html page
    if (resultDataJson[0]["status"] === "success") {
        window.location.replace("confirmation.html");
    }
    // If login fail, display error message on <div> with id "login_error_message"
    else {

        console.log("show error message");
        console.log(resultDataJson[0]["message"]);
        jQuery("#login_error_message").text(resultDataJson[0]["message"]);
        window.location.replace("cus-info.html");
    }
}


function check(){
	var pattern = /[\d]{4}[\/-]{1}[\d]{1,2}[\/-]{1}[\d]{1,2}/g; 
	var expiration=doucument.getElementById("expiration");
	console.log("in check function");

	if(!strcmp(pattern,expiration))
		{
		window.alert("please input the correct date like 20XX-MM-DD!");
		return false;
		}
	return true;
}
/**
 * Submit the form content with POST method
 * @param formSubmitEvent
 */
function submitCardForm(formSubmitEvent) {
    console.log("submit creditcard_form");
    console.log(jQuery("#creditcard_form").serialize());
    // Important: disable the default action of submitting the form
    //   which will cause the page to refresh
    //   see jQuery reference for details: https://api.jquery.com/submit/
    formSubmitEvent.preventDefault();
    //alert("lol");
    var last_equal=jQuery("#creditcard_form").serialize().lastIndexOf("=");
    var length=jQuery("#creditcard_form").serialize().length;
    var date=jQuery("#creditcard_form").serialize().slice(last_equal,length);
    console.log(date);
    
    if(!validateDateFormart(date)){
    	window.location.replace("cus-info.html");
    	return;
    }
    /*jQuery.post(
        "api/login",
        // Serialize the login form to the data sent by POST request
        jQuery("#login_form").serialize(),
        (resultDataString) => handleLoginResult(resultDataString));*/
    //lert("lol");
    var last_equal=jQuery("#creditcard_form").serialize().lastIndexOf("=");
    var length=jQuery("#creditcard_form").serialize().length;
    var date=jQuery("#creditcard_form").serialize().slice(last_equal+1,length);
    console.log(date);
    var pattern = /[\d]{4}[\/-]{1}[\d]{1,2}[\/-]{1}[\d]{1,2}/g; 
	
	if(!pattern.test(date))
		{
		window.alert("please input the correct date like 20XX-MM-DD!");
		return false;
		}
	
    jQuery.ajax({
        dataType: "json", // Setting return data type
        method: "POST", // Setting request method
        url: "api/customerinfor", // Setting request url, which is mapped by LoginServelet in LoginServelet.java
        data: jQuery("#creditcard_form").serialize(),
        success: (resultData) => handleCardResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
    });

    }


// Bind the submit action of the form to a handler function
jQuery("#creditcard_form").submit((event) => submitCardForm(event));







