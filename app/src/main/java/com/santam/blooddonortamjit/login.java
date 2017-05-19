package com.santam.blooddonortamjit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = login.class.getSimpleName();

    private CoordinatorLayout coordinatorLayout;

    protected static EditText editTextUsername, editTextPassword;
    protected static TextView registerClick;
    private Button buttonLogin;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);
        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            startActivity(new Intent(getApplicationContext(), Dashboard.class));
            finish();
//                startActivity(new Intent(this, ProfileView.class));
            //showItem();
            return;
        }
        else{
            //hideItem();

            editTextUsername = (EditText) findViewById(R.id.txt_user);
            editTextPassword = (EditText) findViewById(R.id.editPassword);
            buttonLogin = (Button) findViewById(R.id.btnLogin);
            registerClick = (TextView) findViewById(R.id.register);


            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait...");

            buttonLogin.setOnClickListener(this);
            registerClick.setOnClickListener(this);
        }

    }
    @Override
    public void onClick(View view) {
        if(view == registerClick){
            startActivity(new Intent(getApplicationContext(), donoreg.class));
        }
        else if(view == buttonLogin){
            if(editTextUsername.getText().length() == 0 || editTextPassword.getText().length() == 0){
                Toast.makeText(getApplicationContext(),constants.loginerrmsg,Toast.LENGTH_LONG).show();
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, constants.loginerrmsg, Snackbar.LENGTH_LONG);

                snackbar.show();
            }else{
//                Toast.makeText(getApplicationContext(),constants.loginsucrmsg,Toast.LENGTH_LONG).show();
//                Snackbar snackbar = Snackbar
//                        .make(coordinatorLayout, constants.loginsucrmsg, Snackbar.LENGTH_LONG);
//
//                snackbar.show();
                userLogin();
            }

        }
    }
    @Override
    public void onBackPressed() {
        System.exit(0);
    }

    public void userLogin(){
        progressDialog.show();
        final String id = editTextUsername.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                constants.URL_LOGIN, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                progressDialog.dismiss();
//                ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
//                if(isConnected) {
//                }
//                else {
//                    new AlertDialog.Builder(getApplicationContext())
//                            .setTitle("Network Status")
//                            .setMessage("Please Check Out Your Internet Connection!!")
//
//                            .setNegativeButton("OK",null).show();
//                    Toast.makeText(getApplicationContext(),"No Internet Connection!!",Toast.LENGTH_LONG).show();
//                }
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean success = jObj.getBoolean("success");

                    // Check for error node in json
                    if (success) {
                        // user successfully logged in
                        // Create login session

                        SharedPrefManager.getInstance(getApplicationContext())
                                .userLogin(
                                        jObj.getString("cellno"),
                                        jObj.getString("name"),
                                        jObj.getString("imgurl")
                                );

                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                        finish();

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                        Snackbar snackbar = Snackbar
                                .make(coordinatorLayout, "Error From Online", Snackbar.LENGTH_LONG);

                        snackbar.show();

                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error);
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout,
                                "Error--> Not Response", Snackbar.LENGTH_LONG);

                snackbar.show();
                progressDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("cellno", id);
                params.put("email", id);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        RequestHandler.getInstance(this).addToRequestQueue(strReq);

    }
}
