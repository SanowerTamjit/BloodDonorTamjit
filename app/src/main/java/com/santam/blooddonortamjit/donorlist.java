package com.santam.blooddonortamjit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class donorlist extends AppCompatActivity {
    private static final String TAG = donorlist.class.getSimpleName();

    final static String tokecn = "view";

    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private CustomAdapter adapter;
    private List<DonorData> data_list;


   public ProgressDialog PD;


    public static final String name="name";
    public static final String cellno="cellno";
    public static final String bloodgrp="bloodgrp";
    public static final String country="country";
    public static final String imgurl="imgurl";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donorlist);


        PD = new ProgressDialog(this);
        PD.setMessage("Loading Donor Data...");


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        data_list  = new ArrayList<>();
        PD.show();
        ReadDataFromDB();


        recyclerView.setLayoutManager(new LinearLayoutManager(this));



//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//
//                if(gridLayoutManager.findLastCompletelyVisibleItemPosition() == data_list.size()-1){
//                    PD.show();
//                    ReadDataFromDB();
//                }
//
//            }
//        });

    }


    private void ReadDataFromDB() {



        StringRequest strReq = new StringRequest(Request.Method.POST,
                constants.DONOR_CONTROLLER, new Response.Listener<String>() {

            @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);
//                            String success = jObj.getString("success");
                            JSONArray ja = jObj.getJSONArray("ResultDonor"); {

                                for (int i = 0; i < ja.length(); i++) {
                                    JSONObject jobj = ja.getJSONObject(i);
//                                    HashMap<String, String> item = new HashMap<String, String>();
//                                    //   Toast.makeText(root.getContext(),jobj.getString(DATE),Toast.LENGTH_LONG).show();
//                                    item.put((name),jobj.getString(name));
//                                    item.put((cellno),jobj.getString(cellno));
//                                    item.put(bloodgrp,jobj.getString(bloodgrp));
//                                    item.put(country,jobj.getString(country));
//                                    item.put(imgurl,jobj.getString(imgurl));
//
////                                    donorArrayList.add(item);
//                                    Toast.makeText(getApplicationContext(), item.toString(), Toast.LENGTH_LONG).show();

                                    DonorData data = new DonorData(jobj.getString(name),jobj.getString(cellno),jobj.getString(bloodgrp),jobj.getString(country),
                                            jobj.getString(imgurl));

                                    data_list.add(data);

                                    adapter = new CustomAdapter(getApplicationContext(),data_list);
                                    recyclerView.setAdapter(adapter);

                                    recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(),
                                            new RecyclerItemClickListener.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(View view, int position) {
//                                                    TextView dateShow = (TextView) view.findViewById(R.id.donorphone);
//                                                    String DateShowStr = dateShow.getText().toString().trim();
                                                    TextView cellno = (TextView) view.findViewById(R.id.donorphone);
                                                    String cellnoString = cellno.getText().toString().trim();
                                                    Intent intent = new Intent(donorlist.this, donorProfile.class);
                                                    intent.putExtra("cellno", cellnoString);
                                                    startActivity(intent);
                                                    Toast.makeText(getApplicationContext(),cellnoString, Toast.LENGTH_LONG).show();

                                                }
                                            }));

                                } // for loop ends




                                PD.dismiss();

                            } // if ends

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error);
                error.printStackTrace();
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

                PD.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", tokecn);

                return params;
            }
        };

        // Adding request to request queue
        RequestHandler.getInstance(this).addToRequestQueue(strReq);

    }
}
