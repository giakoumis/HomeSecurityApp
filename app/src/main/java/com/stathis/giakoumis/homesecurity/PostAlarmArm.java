package com.stathis.giakoumis.homesecurity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by vallia on 11/8/2016.
 */
public class PostAlarmArm extends AsyncTask<String,Void,String> {
private TaskCallback mCallback;
    public PostAlarmArm(TaskCallback callback){
        mCallback = callback;
    }

    @Override
    protected String doInBackground(String... params) {

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        String responseJson = null;

        try {
            URL url = new URL("http://192.168.1.65:3000/api/arm");

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            InputStream in = connection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (in == null){
                return  null;
            }

            reader = new BufferedReader(new InputStreamReader(in));

            String line="";
            while ((line = reader.readLine())!= null){
                buffer.append(line + "\n");

            }
            responseJson = buffer.toString();
            return responseJson;
        }catch (IOException e){
            Log.e("HTTP Error","Error",e);
            return  null;
        }finally {
            if (connection!=null){
                connection.disconnect();
            }
            if (reader != null){
                try {
                    reader.close();
                }catch (final IOException e){
                    Log.e("READER ERROR","error",e);

                }
            }
        }

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        mCallback.done();

    }
}
