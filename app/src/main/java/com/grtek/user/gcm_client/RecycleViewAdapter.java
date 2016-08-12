package com.grtek.user.gcm_client;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


import com.grtek.user.gcm_client.google_map.MapsActivity;

import java.util.List;

/**
 * Created by user on 2016/8/10.
 */
public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> implements View.OnClickListener{

    private List<EmgUnit> itemsData;
    private Context mContext;

    public RecycleViewAdapter(Context context, List<EmgUnit> data){
        this.mContext = context;
        this.itemsData = data;
    }


    @Override
    public RecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycleview_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);

        itemLayoutView.setOnClickListener(this);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecycleViewAdapter.ViewHolder holder, final int position) {

        holder.tvDate.setText(itemsData.get(position).getDateTime());
        holder.tvMac.setText(itemsData.get(position).getMAC());
        holder.imgbtnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent googleMapIntent = new Intent(mContext, MapsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putDouble("GPS_LAT", itemsData.get(position).getLAT());
                bundle.putDouble("GPS_LNG", itemsData.get(position).getLNG());
                googleMapIntent.putExtras(bundle);
                mContext.startActivity(googleMapIntent);
            }
        });

        holder.imgbtnCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent webCamIntent = new Intent(mContext, WebCamActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("Web_Cam_URL", itemsData.get(position).getCameraIP());
                webCamIntent.putExtras(bundle);
                mContext.startActivity(webCamIntent);
            }
        });

        holder.itemView.setTag(itemsData.get(position).getMAC());


    }



    @Override
    public int getItemCount() {
        return itemsData.size();
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null){
            mOnItemClickListener.onItemClick(view , (String)view.getTag());
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tvDate;
        public TextView tvMac;
        public ImageButton imgbtnMap;
        public ImageButton imgbtnCam;

        public ViewHolder(View itemView){
            super(itemView);

            tvDate = (TextView) itemView.findViewById(R.id.textView_rv_date);
            tvMac = (TextView) itemView.findViewById(R.id.textView_rv_mac);
            imgbtnMap = (ImageButton) itemView.findViewById(R.id.imageButton_rv_map);
            imgbtnCam = (ImageButton) itemView.findViewById(R.id.imageButton_rv_webcam);
        }
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public static interface OnRecyclerViewItemClickListener{
        void onItemClick(View view, String data);
    }

    public void setmOnItemClickListener(OnRecyclerViewItemClickListener listener){
        this.mOnItemClickListener = listener;
    }



}
