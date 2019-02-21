package com.grp59.cs122b.fabflix;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListActivity extends AppCompatActivity{

    private ListView lv;
    private Button btn_prev;
    private Button btn_next;
    private MyAdapter myAdapter;
    private int VIEW_COUNT = 5; //每页显示条目数
    private int index = 0;  //当前页数索引
    private int total = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        // Get the Intent that started this activity and extract the string
        ((TextView)findViewById(R.id.footer)).setText(Integer.toString(1));
        Intent intent = getIntent();
        Bundle bundle = getIntent().getExtras();
        lv = (ListView) findViewById(R.id.lv);
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,Object>>();
        String msg = bundle.getString("message");
        final Intent goToIntent = new Intent(this, SingleMovieActivity.class);
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        //Log.d("msg",)
        if (msg != null && !"".equals(msg)) {
            try {
                JSONArray array1 = new JSONArray(msg);

                for(int i=0;i<array1.length();i++)
                {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("id", array1.getJSONObject(i).getString("movie_id"));
                    map.put("title", array1.getJSONObject(i).getString("movie_title"));
                    //Log.d("title",array1.getJSONObject(i).getString("movie_title"));
                    map.put("year", array1.getJSONObject(i).getString("movie_year"));
                    //Log.d("year",array1.getJSONObject(i).getString("movie_year"));
                    map.put("director", array1.getJSONObject(i).getString("movie_director"));
                    map.put("genres", array1.getJSONObject(i).getString("movie_genre"));
                    map.put("stars", array1.getJSONObject(i).getString("movie_star"));

                    listItem.add(map);
                }
                total=listItem.size();

                myAdapter = new MyAdapter(this,listItem);
                lv.setAdapter(myAdapter);//为ListView绑定适配器
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                           long arg3) {

                        String message=((TextView)arg1.findViewById(R.id.id)).getText().toString();

                        String url="https://18.191.60.59:8443/project2-demo/api/singlemovie?id="+message;
                        Log.d("url2",url);
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
                    }});

                btn_prev = (Button) findViewById(R.id.btn_prev);
                btn_next = (Button) findViewById(R.id.btn_next);

                View.OnClickListener clickListener = new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        switch (v.getId()) {
                            case R.id.btn_prev:
                                preView();
                                ((ListView)findViewById(R.id.lv)).setSelection(0);
                                ((TextView)findViewById(R.id.footer)).setText(Integer.toString(index+1));
                                break;
                            case R.id.btn_next:
                                nextView();
                                ((ListView)findViewById(R.id.lv)).setSelection(0);
                                ((TextView)findViewById(R.id.footer)).setText(Integer.toString(index+1));
                                break;
                        }
                    }

                };


                btn_prev.setOnClickListener(clickListener);
                btn_next.setOnClickListener(clickListener);

                checkButton();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }

    public void preView() {
        index--;
        myAdapter.notifyDataSetChanged();
        checkButton();
    }

    public void nextView() {
        index++;
        myAdapter.notifyDataSetChanged();
        checkButton();
    }

    public void checkButton() {
        if (index <= 0) {
            btn_prev.setEnabled(false);
        }else{
            btn_prev.setEnabled(true);
        }
        if (total - index * VIEW_COUNT <= VIEW_COUNT) {
            btn_next.setEnabled(false);
        }

        else {
            btn_next.setEnabled(true);
        }

    }


    //rewrite the adapter to pagination
    public class MyAdapter extends BaseAdapter {
        Activity activity;
        ArrayList<HashMap<String, Object>> Items = new ArrayList<HashMap<String, Object>>();

        public MyAdapter(Activity a, ArrayList<HashMap<String, Object>> listitem) {
            activity = a;
            Items = listitem;
        }

        // the length of every page
        @Override
        public int getCount() {
            // TODO Auto-generated method stub

            // ori presents the total item number before
            int ori = VIEW_COUNT * index;


            if (Items.size() - ori < VIEW_COUNT) {
                return Items.size() - ori;
            } else {
                return VIEW_COUNT;
            }

        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            // TODO Auto-generated method stub
            // return addTestView(position);
            convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.movie_listitem, null);

            ((TextView) convertView.findViewById(R.id.id)).setText((String) Items.get(position + index * VIEW_COUNT).get("id"));
            ((TextView) convertView.findViewById(R.id.title)).setText((String) Items.get(position + index * VIEW_COUNT).get("title"));
            ((TextView) convertView.findViewById(R.id.year)).setText((String) Items.get(position + index * VIEW_COUNT).get("year"));
            ((TextView) convertView.findViewById(R.id.director)).setText((String) Items.get(position + index * VIEW_COUNT).get("director"));
            ((TextView) convertView.findViewById(R.id.genres)).setText((String) Items.get(position + index * VIEW_COUNT).get("genres"));
            ((TextView) convertView.findViewById(R.id.stars)).setText((String) Items.get(position + index * VIEW_COUNT).get("stars"));

            return convertView;
        }
    }

}
