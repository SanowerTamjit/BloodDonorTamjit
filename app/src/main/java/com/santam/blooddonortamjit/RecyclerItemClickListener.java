package com.santam.blooddonortamjit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Tamjit on 18/05/2017.
 */

public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

    public OnItemClickListener mListener;

    public interface OnItemClickListener{
        public void onItemClick(View view, int position);
    }
    GestureDetector mGestureDetector;

    public RecyclerItemClickListener(Context context, OnItemClickListener listener){
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View childview = rv.findChildViewUnder(e.getX(), e.getY());
        if (childview != null && mListener != null && mGestureDetector.onTouchEvent(e)){
            mListener.onItemClick(childview,rv.getChildAdapterPosition(childview));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

}
