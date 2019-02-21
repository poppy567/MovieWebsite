package com.grp59.cs122b.fabflix;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

//import org.apache.commons.lang3.StringUtils;

public class MainActivity extends AppCompatActivity {
    private TextView infotv,regtv;
    private Button logbtn;
    EditText e_mail,pass_word;
    private static Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    public void sendMessage(View view) {

        e_mail=(EditText)findViewById(R.id.email);
        pass_word=(EditText)findViewById(R.id.pswd);

        // Do something in response to button
        String email = e_mail.getText().toString();
        String password=pass_word.getText().toString();
        Log.d("email", email);
        Log.d("pswd", password);

        final Intent goToIntent = new Intent(this, SearchActivity.class);

        final Map<String, String> params = new HashMap<String, String>();
        params.put("username", email);
        params.put("password", password);
        params.put("optionsRadiosinline","option1");

        // no user is logged in, so we must connect to the server
        //RequestQueue queue = Volley.newRequestQueue(this);
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;

        final Context context = this;
        String url= "https://18.191.60.59:8443/project2-demo/api/login";
        Log.d("mylog", url);

        final StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                        Log.d("response", response);


                        try {
                            JSONArray array1 = new JSONArray(response);
                            JSONObject reader=array1.getJSONObject(0);
                            if(reader.getString("status").equals("success")){

                                String message=reader.getString("message");
                                Log.d("mylog","into next page");
                                startActivity(goToIntent);
                            }
                            else{
                                String message=reader.getString("message");
                                ((TextView)findViewById(R.id.errormsg)).setText(message);
                            }
                        }catch (JSONException e) {
                            Log.e("myerror", "Json parsing error: " + e.getMessage());
                            Toast.makeText(getApplicationContext(),"Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }



                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("security.error", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(postRequest);

        return ;
    }
}
