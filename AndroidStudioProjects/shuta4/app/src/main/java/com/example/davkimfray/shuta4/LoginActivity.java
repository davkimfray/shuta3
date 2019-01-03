package com.example.davkimfray.shuta4;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.davkimfray.shuta4.helper.CheckNetworkStatus;
import com.example.davkimfray.shuta4.helper.HttpJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DATA= "data";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_STU_ID = "stu_id";
    private static final String KEY_TEA_ID = "tea_id";
    private static final String BASE_URL = "http://192.168.0.101/android/";
    private ProgressDialog pDialog;
    private EditText txtUsername, txtPassword;
    private TextView txtIncorectUserPass, txtErrorUser, txtErrorPass;
    private Button btnLogin;
    private String userName;
    private String passWord;
    private String studentId;
    private String teacherId;
    private  int success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUsername = findViewById(R.id.txt_username);
        txtPassword = findViewById(R.id.txt_password);
        txtIncorectUserPass = findViewById(R.id.txt_incorect_user_pass);
        txtErrorUser = findViewById(R.id.txt_erroe_user);
        txtErrorPass = findViewById(R.id.txt_error_pass);
        btnLogin = findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    //validate username and password
                    if(txtUsername.getText().toString().trim().isEmpty()){
                        txtErrorUser.setText("* Enter Username *");
                      //  requestFocus(txtUsername);
                        Intent i = new Intent(getApplicationContext(),
                                StudentTabbedActivity.class);
                        i.putExtra(KEY_STU_ID, studentId);
                        startActivity(i);
                        finish();
                    }else{
                        txtErrorUser.setText("");
                        if(txtPassword.getText().toString().trim().isEmpty()){
                            txtErrorPass.setText("* Enter Password *");




                            //new FetchStudentDetailsAsyncTask().execute();
                        }else{
                            txtErrorPass.setText("");

                            userName = txtUsername.getText().toString();
                            passWord = txtPassword.getText().toString();

                        }
                    }
                } else {
                    //Display error message if not connected to internet
                    Toast.makeText(LoginActivity.this,
                            "Unable to connect to internet",
                            Toast.LENGTH_LONG).show();

                }


            }
        });
    }



    private class FetchStudentDetailsAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display progress bar
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Loading. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_USERNAME, userName);
            httpParams.put(KEY_PASSWORD, passWord);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "get_login_details.php", "GET", httpParams);
            try {
                success = jsonObject.getInt(KEY_SUCCESS);
                JSONObject loginData;
                if (success == 1) {
                    //Parse the JSON response
                    loginData = jsonObject.getJSONObject(KEY_DATA);
                    teacherId = loginData.getString(KEY_TEA_ID);
                    studentId = loginData.getString(KEY_STU_ID);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
          pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    //Populate the Edit Texts once the network activity is finished executing
                    if(success == 0) {
                        txtIncorectUserPass.setText("* Incorrect Username or Password *");
                    }else if(teacherId != "null"){
                        txtIncorectUserPass.setText("");
                        Toast.makeText(LoginActivity.this,
                                "You are a Teacher",
                                Toast.LENGTH_LONG).show();
                    }else{
                        txtIncorectUserPass.setText("");
                        Intent i = new Intent(getApplicationContext(),
                            StudentTabbedActivity.class);
                            i.putExtra(KEY_STU_ID, studentId);
                        startActivity(i);

                    }

                }
            });
        }
    }
}
