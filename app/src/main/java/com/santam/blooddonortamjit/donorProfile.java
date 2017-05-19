package com.santam.blooddonortamjit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class donorProfile extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = donorProfile.class.getSimpleName();
    private static String cellnoforCall;
    private static String emailsend;
    private static String DONORKEY = "";
    final String token = "Profile";
    TextView DonorName, BloodGroup, BirthDateAge, Country,city, State, Zip, Addres,TotlDonate, LastDateofDonate;
    ImageView DonorPhoto;
    Button mail, call;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_profile);

        DONORKEY = getIntent().getStringExtra("cellno");

        DonorName = (TextView) findViewById(R.id.user_profile_name);
        BloodGroup = (TextView) findViewById(R.id.textbloodgrp);
        BirthDateAge = (TextView) findViewById(R.id.birthAge);
        Country = (TextView) findViewById(R.id.showcountry);
        State = (TextView) findViewById(R.id.showstate);
        city = (TextView) findViewById(R.id.showcity);
        Zip = (TextView) findViewById(R.id.showzip);
        Addres = (TextView) findViewById(R.id.showstreetAdrs);
        TotlDonate = (TextView) findViewById(R.id.showtlDonate);
        LastDateofDonate = (TextView) findViewById(R.id.showdtDonate);
        DonorPhoto = (ImageView) findViewById(R.id.user_profile_photo);
        mail = (Button) findViewById(R.id.mlBtn);
        call = (Button) findViewById(R.id.clBtn);

        mail.setOnClickListener(this);
        call.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        fetchData(DONORKEY);
    }

    @Override
    public void onClick(View view) {
        if (view == mail) {
            startMail(emailsend);
        } else if (view == call) {
            startCall(cellnoforCall);

        }
    }

    private void fetchData(String donorkey) {
        progressDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                constants.DONOR_CONTROLLER, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Donor Profile Response: " + response.toString());
                progressDialog.dismiss();
                try {
                    JSONObject jObj = new JSONObject(response);
                    String success = jObj.getString("success");
                    JSONArray ja = jObj.getJSONArray("ResultDonor");
                    {

                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject jobj = ja.getJSONObject(i);
                            DonorName.setText(jobj.getString("name"));
                            BirthDateAge.setText("Birthdate: "+jobj.getString("birthdate")+"(Age: "+jobj.getString("age")+")");
                            BloodGroup.setText(jobj.getString("bloodgrp"));
                            Country.setText(jobj.getString("country"));
                            city.setText(jobj.getString("city"));
                            State.setText(jobj.getString("state"));
                            Zip.setText(jobj.getString("zip"));
                            Addres.setText(jobj.getString("address"));
                            TotlDonate.setText(jobj.getString("totaldonate"));
                            LastDateofDonate.setText(jobj.getString("l_datedonate"));
                            Glide.with(getApplicationContext()).load(jobj.getString("imgurl")).into(DonorPhoto);
                            cellnoforCall = jobj.getString("cellno");
                            emailsend = jobj.getString("email");
//                            DonorPhoto.setImageBitmap();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error);
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", token);
                params.put("cellno", DONORKEY);

                return params;
            }

        };

        // Adding request to request queue
        RequestHandler.getInstance(this).addToRequestQueue(strReq);

    }
    public void startCall(String cellno) {
        Intent in = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+cellno));
        try {
            startActivity(Intent.createChooser(in,"Choose an Phone Client"));
        }
        catch (android.content.ActivityNotFoundException ex){
            Toast.makeText(getApplicationContext(), "Could not find an activity for make this call", Toast.LENGTH_LONG).show();
        }
    }
    public void startMail(String email) {
        Intent in = new Intent(Intent.ACTION_SENDTO);

        try {
            in.setType("text/Plain");

//            in.putExtra(Intent.EXTRA_EMAIL, new String[]{email}) ;
            in.putExtra(Intent.EXTRA_SUBJECT, "About Blood Donation");
            in.putExtra(Intent.EXTRA_TEXT,"I need 1 bag blood. Can you donate?");
            in.setData(Uri.parse("mailto:"+email));
            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(in);
//            startActivity(Intent.createChooser(in,"Choose an Email Client"));
        }
        catch (android.content.ActivityNotFoundException ex){
            Toast.makeText(getApplicationContext(), "Could not find an activity for send Mail", Toast.LENGTH_LONG).show();
        }
    }

}
