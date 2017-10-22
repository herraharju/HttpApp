package com.example.ktkoiju.httpapp;

import android.app.ProgressDialog;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import com.android.volley.*;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements IHttpDataReady {

    private ListView listView;
    private ArrayList<Item> items;
    private CustomAdapter adapter;
    private String[] urls;
    private int colorPicker=0;
    private SwipeRefreshLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Init();
    }

    private void Init()
    {
        urls = new String[]{
                "http://freevector.co/wp-content/uploads/2010/04/42994-question-sign-on-person-head.png",
                "https://previews.123rf.com/images/glebstock/glebstock1405/glebstock140501332/29470355-male-silhouette-Unknown-person-concept-Stock-Photo.jpg",
                "https://vignette.wikia.nocookie.net/johnnys-survivor-orgs/images/2/23/Unknown_Person.png/revision/latest?cb=20150715075805"
        };

        layout = (SwipeRefreshLayout)findViewById(R.id.srl);
        listView = (ListView)findViewById(R.id.lv_items);
        items = new ArrayList<Item>();
        adapter = new CustomAdapter(this,items, urls);
        listView.setAdapter(adapter);
    }

    public void VolleyClicked(View v)
    {
        items.clear();
        String url = "http://webd.savonia.fi/home/ktkoiju/j2me/test_json.php?dates&delay=1";
        volleyJSONArrayRequest(url);

        //HTTP + AsyncTask
        Toast.makeText(this, "Fetching data", Toast.LENGTH_LONG).show();
        //new DownloadHttpTask(this, this).execute(url);
    }



    public void volleyJSONArrayRequest(String url){

        final ProgressDialog progressDialog;
        String  REQUEST_TAG = "com.androidtutorialpoint.volleyStringRequest";

        switch (colorPicker){
            case 0:
                progressDialog = new ProgressDialog(MainActivity.this,R.style.DialogStyle);
                break;
            case 1:
                progressDialog= new ProgressDialog(MainActivity.this,R.style.DialogStyle1);
                break;
            case 2:
                progressDialog = new ProgressDialog(MainActivity.this,R.style.DialogStyle2);
                break;
            case 3:
                progressDialog = new ProgressDialog(MainActivity.this,R.style.DialogStyle3);
                break;
            default:
                progressDialog = new ProgressDialog(MainActivity.this);
                break;
        }
        if(colorPicker >=3){
            colorPicker = 0;
        }
        else{
            colorPicker++;
        }


        progressDialog.setMessage("Loading...");
        progressDialog.show();


        JsonRequest jsonRequest = new JsonArrayRequest(Request.Method.GET, url,null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {
                Log.d("Response value: ", response.toString());
                ParseJSONArray(response);
                progressDialog.hide();
            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error value: ", error.getMessage());

                progressDialog.hide();

            }
        }
        );
        // Adding String request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonRequest, REQUEST_TAG);
    }

    private void ParseJSONArray(JSONArray jsonArray)
    {
        try
        {
            for(int i = 0; i<jsonArray.length();i++){

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String date = jsonObject.getString("pvm");
                String name = jsonObject.getString("nimi");
                items.add(new Item(date, name));

            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        VolleyClicked(findViewById(android.R.id.content));
    }

    @Override
    public void onDataReady(String responseData) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()){
            case R.id.fetch:
                VolleyClicked(findViewById(android.R.id.content));
                return true;
            default:
                return false;
        }

    }
    @Override
    public boolean onTouchEvent(MotionEvent event){

        int action = MotionEventCompat.getActionMasked(event);

        switch(action) {
            case (MotionEvent.ACTION_DOWN) :
                Log.d("Motion","Action was DOWN");
                RefreshList();
                return true;
            case (MotionEvent.ACTION_MOVE) :
                Log.d("Motion","Action was MOVE");
                RefreshList();
                return true;
            case (MotionEvent.ACTION_UP) :
                Log.d("Motion","Action was UP");
                RefreshList();
                return true;
            case (MotionEvent.ACTION_CANCEL) :
                Log.d("Motion","Action was CANCEL");
                return true;
            case (MotionEvent.ACTION_OUTSIDE) :
                Log.d("Motion","Movement occurred outside bounds " +
                        "of current screen element");
                return true;
            default :
                return super.onTouchEvent(event);
        }
    }
    public void RefreshList(){
        VolleyClicked(findViewById(android.R.id.content));
        layout.setRefreshing(false);
    }
}
