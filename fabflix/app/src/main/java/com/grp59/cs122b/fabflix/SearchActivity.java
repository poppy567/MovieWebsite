package com.grp59.cs122b.fabflix;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.SearchView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity{

    private SearchView msearchview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        msearchview=(SearchView)findViewById(R.id.search_bar);
        final Intent goToIntent;
        goToIntent = new Intent(this, ListActivity.class);
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;

        msearchview.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                //log.d("log",query);
                Log.d("mylog", query);
                query=query.replace(" ",",");

                String url="https://18.191.60.59:8443/project2-demo/api/movielist?title="+query;
                Log.d("myurl",url);
                final StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                Log.d("response2", response);
                                //EditText editText = (EditText) findViewById(R.id.editText);
                                //String message = editText.getText().toString();
                                String message=response.toString();
                                goToIntent.putExtra("message", message);
                                startActivity(goToIntent);
                                //((TextView) findViewById(R.id.http_response)).setText(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                Log.d("security.error", error.toString());
                                error.printStackTrace();
                            }
                        }
                ){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String,String> params = new HashMap<String, String>();
                        return params;
                    }
                };
                queue.add(getRequest);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)){
                    //mListView.setFilterText(newText);
                }else{
                    //mListView.clearTextFilter();
                }
                return false;
            }
        });
    }

}
