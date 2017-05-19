package com.santam.blooddonortamjit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by filipp on 9/16/2016.
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private Context context;
    private List<DonorData> my_data;

    public CustomAdapter(Context context, List<DonorData> my_data) {
        this.context = context;
        this.my_data = my_data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.donorlistview,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String nm = my_data.get(position).getName();
        String phone = my_data.get(position).getCellno();
        String country = my_data.get(position).getCountry();
        String donornme = "<html><b>"+nm+"</b></html>";
//        String phncountry = "<html>"+phone+"<br>("+country+")</html>";

        holder.bloodgrpup.setText(my_data.get(position).getBloodgrp());
        holder.blooddonor.setText(Html.fromHtml(donornme));
//        holder.blooddonorphn.setText(Html.fromHtml(phncountry));
        holder.blooddonorphn.setText(phone);
        Glide.with(context).load(my_data.get(position).getImgurl()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }

    public  class ViewHolder extends  RecyclerView.ViewHolder{

        public TextView blooddonor;
        public TextView blooddonorphn;
        public TextView bloodgrpup;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            bloodgrpup = (TextView) itemView.findViewById(R.id.bloodgrpup);
            blooddonor = (TextView) itemView.findViewById(R.id.donornm);
            blooddonorphn = (TextView) itemView.findViewById(R.id.donorphone);
            imageView = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}
