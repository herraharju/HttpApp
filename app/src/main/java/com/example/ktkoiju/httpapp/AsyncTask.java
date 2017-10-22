package com.example.ktkoiju.httpapp;

/**
 * Created by ktkoiju on 18.9.2017.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


class DownloadHttpTask extends AsyncTask<String, Void, String> {

    ProgressDialog dialog;
    private EditText _tView;
    private IHttpDataReady _dataReady;
    private Activity _activity;

    public DownloadHttpTask(IHttpDataReady dataReady , Activity activity)
    {
        //_tView = tView;
        _dataReady = dataReady;
        //_activity = activity;
        dialog = new ProgressDialog(activity);
    }

    @Override
    protected String doInBackground(String... params) {
        String result = "";
        URL url;

        try {
            url = new URL(params[0]);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            StringBuffer buffer = new StringBuffer();

            while ((line = rd.readLine()) != null)
                buffer.append(line);

            result = buffer.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("HTTP result: ", result);
        dialog.dismiss();

        if ( _dataReady != null)
            _dataReady.onDataReady(result);
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub

        dialog.setMessage("Wait for a while ...");
        dialog.setTitle("Wait");
        dialog.show();

    }
}
