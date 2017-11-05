package com.example.cpodariu.intelipark_android;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by cpodariu on 04.11.2017.
 */

class CarpoolingAdapter extends RecyclerView.Adapter<CarpoolingAdapter.CarpoolingAdapterViewHolder> {
    private ArrayList<String> carpoolingList;

    public void setCarpoolingList(ArrayList<String> carpoolingList)
    {
        this.carpoolingList = carpoolingList;
        this.notifyDataSetChanged();
    }

    class CarpoolingAdapterViewHolder extends RecyclerView.ViewHolder {
        public final TextView mCarpoolingTextView;

        public CarpoolingAdapterViewHolder(View view) {
            super(view);
            mCarpoolingTextView = (TextView) view.findViewById(R.id.tv_carpooling_data);
        }
    }


    @Override
    public CarpoolingAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.carpooling_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new CarpoolingAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CarpoolingAdapterViewHolder holder, int position) {
        final String carPoolingDataForPosition = carpoolingList.get(position);
        holder.mCarpoolingTextView.setText(carPoolingDataForPosition.split(",")[0] + " " + carPoolingDataForPosition.split(",")[1]);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), carPoolingDataForPosition, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (carpoolingList == null)
            return 0;
        return carpoolingList.size();
    }
}
