package com.santam.blooddonortamjit;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Samad Talukder on 1/30/2017.
 */

public class RequestHandler {
    private static RequestHandler mInstance;
    private static RequestQueue mRequestQueue;
    private static Context mCtx;

    private RequestHandler(Context context){
        mCtx=context;
        mRequestQueue=getRequestQueue();
    }
    public static synchronized RequestHandler getInstance(Context context){
        if (mInstance == null){
            mInstance=new RequestHandler(context);
        }
        return mInstance;
    }
    public RequestQueue getRequestQueue(){
        if (mRequestQueue==null){
            mRequestQueue= Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req){
        getRequestQueue().add(req);
    }
}
