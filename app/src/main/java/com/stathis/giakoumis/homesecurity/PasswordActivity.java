package com.stathis.giakoumis.homesecurity;

import android.content.Intent;
import android.os.AsyncTask;
import android.security.keystore.KeyNotYetValidException;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
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

public class PasswordActivity extends AppCompatActivity implements View.OnClickListener{


   // EditText password;
   // Button  btn_submit;
   // KeyboardView myKeyboard;
   EditText mPasswordField;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keyboard);
        initViews();

       //   myKeyboard = new KeyboardView(this);




     /*  btn_submit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
             Toast toast = Toast.makeText(getApplicationContext(), myKeyboard.getInputText(), Toast.LENGTH_LONG);
               toast.show();
           //   new GetPassword().execute(myKeyboard.getInputText());



           }
       });

     */
    }

    private void initViews() {
        mPasswordField = (EditText)findViewById(R.id.password_field);
        $(R.id.t9_key_0).setOnClickListener(this);
        $(R.id.t9_key_1).setOnClickListener(this);
        $(R.id.t9_key_2).setOnClickListener(this);
        $(R.id.t9_key_3).setOnClickListener(this);
        $(R.id.t9_key_4).setOnClickListener(this);
        $(R.id.t9_key_5).setOnClickListener(this);
        $(R.id.t9_key_6).setOnClickListener(this);
        $(R.id.t9_key_7).setOnClickListener(this);
        $(R.id.t9_key_8).setOnClickListener(this);
        $(R.id.t9_key_9).setOnClickListener(this);
        $(R.id.t9_key_clear).setOnClickListener(this);
        $(R.id.t9_key_backspace).setOnClickListener(this);
        $(R.id.t9_key_submin).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // handle number button click
        if (v.getTag() != null && "number_button".equals(v.getTag())) {
            mPasswordField.append(((TextView) v).getText());
            Log.i("Keyboar",mPasswordField.getText().toString());
            return;
        }
        switch (v.getId()) {
            case R.id.t9_key_clear: { // handle clear button
                mPasswordField.setText(null);
            }
            break;
            case R.id.t9_key_backspace: { // handle backspace button
                // delete one character
                Editable editable = mPasswordField.getText();
                int charCount = editable.length();
                if (charCount > 0) {
                    editable.delete(charCount - 1, charCount);
                }
            }
            case R.id.t9_key_submin: { // handle backspace button

                   new GetPassword().execute(mPasswordField.getText().toString());
            }

            break;
        }
    }
    protected <T extends View> T $(@IdRes int id) {
        return (T) super.findViewById(id);
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

            if (s.contains("OK")) {

                Toast toast = Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_LONG);
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
