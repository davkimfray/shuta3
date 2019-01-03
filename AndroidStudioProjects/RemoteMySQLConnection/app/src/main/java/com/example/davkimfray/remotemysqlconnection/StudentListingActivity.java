package com.example.davkimfray.remotemysqlconnection;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.davkimfray.remotemysqlconnection.helper.HttpJsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StudentListingActivity extends AppCompatActivity {
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DATA = "data";
    private static final String KEY_STU_ID = "stu_id";
    private static final String KEY_F_NAME = "f_name";
    private static final String KEY_M_NAME = "m_name";
    private static final String KEY_L_NAME = "l_name";
    private static final String KEY_REG_NO = "reg_no";
    private static final String KEY_STU_IMAGE = "stu_image";
    private static final String KEY_COL_3 = "col_3";
    private static final String KEY_COL_4 = "col_4";
    private static final String BASE_URL  = "http://192.168.0.100/android/";
    private ArrayList<HashMap<String, String>> movieList;
    private ListView movieListView;
    private ProgressDialog pDialog;
    //defining table and column names

    private String stuImage = "stu_image";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_listing);
        movieListView = findViewById(R.id.movieList);
        new FetchStudentsAsyncTask().execute();
    }

    /**
     * Fetches the list of movies from the server
     */
    private class FetchStudentsAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            //Display preogress dialog
            pDialog = new ProgressDialog(StudentListingActivity.this);
            pDialog.setMessage("Loading Studet. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "fetch_all_data.php", "GET", null);
            try {
                int success = jsonObject.getInt(KEY_SUCCESS);
                JSONArray students;
             //   Toast.makeText(StudentListingActivity.this,success,Toast.LENGTH_SHORT).show();
                if (success == 1){
                     movieList = new ArrayList<>();
                     students = jsonObject.getJSONArray(KEY_DATA);
                    //Iterate through the response and populate movies list
                    for (int i = 0; i < students.length(); i++) {
                        JSONObject student = students.getJSONObject(i);
                        Integer stuId = student.getInt(KEY_STU_ID);
                        String fName = student.getString(KEY_F_NAME);
                        String mName;
                        if(student.getString(KEY_M_NAME) == "null"){
                            mName = "";
                        }else{
                            mName = " " + student.getString(KEY_M_NAME);
                        }

                        String lName = " " + student.getString(KEY_L_NAME);
                        String regNo = student.getString(KEY_REG_NO);

                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(KEY_STU_ID, stuId.toString());
                        map.put(KEY_F_NAME, fName);
                        map.put(KEY_M_NAME, mName);
                        map.put(KEY_L_NAME, lName);
                        map.put(KEY_REG_NO, regNo);
                      //  map.put(KEY_STU_IMAGE, movieName);
                        movieList.add(map);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result){
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    populateStudentList();
                }
            });
        }
    }

    /**
     * Updating parsed JSON data into ListView
     * */
    private void populateStudentList(){
        ListAdapter adapter = new SimpleAdapter(
                StudentListingActivity.this, movieList,
                R.layout.list_item, new String[]{KEY_STU_ID,KEY_F_NAME,KEY_M_NAME,KEY_L_NAME,KEY_REG_NO},
                new int[]{R.id.dataId, R.id.dataName, R.id.txt_mName, R.id.txt_lName, R.id.regNo});
        //Updating Listview
        movieListView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 20) {
            // If the result code is 20 that means that
            // the user has deleted/updated the movie.
            // So refresh the movie listing
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }
}
