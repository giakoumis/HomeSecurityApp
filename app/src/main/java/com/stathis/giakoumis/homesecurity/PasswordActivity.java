package com.stathis.giakoumis.homesecurity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PasswordActivity extends AppCompatActivity {


    EditText password;
    Button  btn_submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        password = (EditText) findViewById(R.id.password_input);
        btn_submit = (Button) findViewById(R.id.button);

       btn_submit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
              new GetPassword().execute(password.getText().toString());



           }
       });


    }

    private class GetPassword extends AsyncTask<String,Void,String>{


        @Override
        protected String doInBackground(String... params) {
            String pass = params[0];
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            String responseJson = null;

            try {
                URL url = new URL("http://192.168.1.65:3000/api/password/"+pass);

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

            if (s != "") {

                Toast toast = Toast.makeText(getApplicationContext(), "Correct", Toast.LENGTH_LONG);
                toast.show();


                Intent securityPanelIntent = new Intent(getApplicationContext(), HomeSecurityPanel.class);
                startActivity(securityPanelIntent);
            }else {
                Toast toast = Toast.makeText(getApplicationContext(), "Wrong Password", Toast.LENGTH_LONG);
                toast.show();

            }
        }
    }
}
