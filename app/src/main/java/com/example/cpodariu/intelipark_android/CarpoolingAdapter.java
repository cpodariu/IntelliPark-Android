package com.example.cpodariu.intelipark_android;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cpodariu.intelipark_android.NetworkUtils.TCPClient;
import com.example.cpodariu.intelipark_android.Utils.SharedPreferencesHelper;

import java.util.ArrayList;

/**
 * Created by cpodariu on 04.11.2017.
 */

class CarpoolingAdapter extends RecyclerView.Adapter<CarpoolingAdapter.CarpoolingAdapterViewHolder> {
    private ArrayList<String> carpoolingList;

    Context c;

    public void setCarpoolingList(ArrayList<String> carpoolingList)
    {
        this.carpoolingList = carpoolingList;
        if (carpoolingList.get(0).equals(""))
            this.carpoolingList = new ArrayList<String>();
        this.notifyDataSetChanged();
    }

    class CarpoolingAdapterViewHolder extends RecyclerView.ViewHolder {
        public final TextView mCarpoolingTextView;
        public ImageButton button;

        public CarpoolingAdapterViewHolder(View view) {
            super(view);
            mCarpoolingTextView = (TextView) view.findViewById(R.id.tv_carpooling_data);
            button = (ImageButton) view.findViewById(R.id.request_button);
        }
    }


    @Override
    public CarpoolingAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.carpooling_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        this.c = parent.getContext();

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new CarpoolingAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CarpoolingAdapterViewHolder holder, final int position) {
        final String carPoolingDataForPosition = carpoolingList.get(position);
        holder.mCarpoolingTextView.setText(carPoolingDataForPosition.split(",")[0] + " " + carPoolingDataForPosition.split(",")[1]);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RequestRide(carPoolingDataForPosition, position, CarpoolingAdapter.this.c).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (carpoolingList == null)
            return 0;
        return carpoolingList.size();
    }

    public class RequestRide extends AsyncTask<Void, Void, Boolean> {
        ArrayList<String> result;
        String id;
        int position;

        String mail;
        Context ctx;

        public RequestRide(String ar, int position, Context ctx)
        {
            this.id = ar.split(",")[0];
            this.mail = ar.split(",")[2];
            this.position = position;
            this.ctx = ctx;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            ArrayList<String> args = new ArrayList<String>();
            args.add("requestRide");
            //mymail, mypass
            //hismail
            args.add(SharedPreferencesHelper.getUserEmail(ctx));
            args.add(SharedPreferencesHelper.getUserPassword(ctx));
            args.add(mail);
            new TCPClient(args).runWithoutReturn();
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            CarpoolingAdapter.this.carpoolingList.remove(position);
            CarpoolingAdapter.this.notifyDataSetChanged();
        }
    }
}
