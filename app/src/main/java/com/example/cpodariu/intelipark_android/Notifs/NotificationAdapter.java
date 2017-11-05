package com.example.cpodariu.intelipark_android.Notifs;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.cpodariu.intelipark_android.NetworkUtils.TCPClient;
import com.example.cpodariu.intelipark_android.R;
import com.example.cpodariu.intelipark_android.Utils.SharedPreferencesHelper;

import java.util.ArrayList;

/**
 * Created by cpodariu on 05.11.2017.
 */

class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationAdapterViewHolder> {

    ArrayList<ArrayList<String>> notifications;

    class NotificationAdapterViewHolder extends RecyclerView.ViewHolder {

        public TextView mNotificationTv;
        public Button acceptButton;
        public Button declineButton;

        public NotificationAdapterViewHolder(View view)
        {
            super(view);
            mNotificationTv = (TextView) view.findViewById(R.id.tv_notification_data);
            acceptButton = (Button) view.findViewById(R.id.accept_button);
            declineButton = (Button) view.findViewById(R.id.declie_button);
        }
    }

    @Override
    public NotificationAdapter.NotificationAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.notification_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new NotificationAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotificationAdapter.NotificationAdapterViewHolder holder, final int position) {
        final ArrayList<String> carPoolingDataForPosition = notifications.get(position);

        if (carPoolingDataForPosition.get(0).equals("requestRide"))
        {
            holder.mNotificationTv.setText(carPoolingDataForPosition.get(2) + " " + carPoolingDataForPosition.get(3) + " asked you for a ride");
            holder.acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AcceptRequest(carPoolingDataForPosition.get(1), position).execute();
                }
            });
            holder.declineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Refuse(carPoolingDataForPosition.get(1), position).execute();
                }
            });
        }
        else
        {
            holder.acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            holder.declineButton.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class AcceptRequest extends AsyncTask<Void, Void, Boolean> {
        ArrayList<String> result;
        String id;
        int position;

        public AcceptRequest(String id, int position)
        {
            this.id = id;
            this.position = position;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            ArrayList<String> args = new ArrayList<String>();
            args.add("acceptRide");
            args.add(id);
            result = new TCPClient(args).run();
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            NotificationAdapter.this.notifications.remove(position);
            NotificationAdapter.this.notifyDataSetChanged();
        }
    }

    public class Refuse extends AsyncTask<Void, Void, Boolean> {
        ArrayList<String> result;
        String id;
        int position;

        public Refuse(String id, int position)
        {
            this.id = id;
            this.position = position;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            ArrayList<String> args = new ArrayList<String>();
            args.add("rejectRide");
            args.add(id);
            result = new TCPClient(args).run();
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            NotificationAdapter.this.notifications.remove(position);
            NotificationAdapter.this.notifyDataSetChanged();
        }
    }

    public class Seen extends AsyncTask<Void, Void, Boolean> {
        ArrayList<String> result;
        String id;
        int position;

        public Seen(String id, int position)
        {
            this.id = id;
            this.position = position;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            ArrayList<String> args = new ArrayList<String>();
            args.add(id);
            args.add("seen");
            result = new TCPClient(args).run();
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            NotificationAdapter.this.notifications.remove(position);
            NotificationAdapter.this.notifyDataSetChanged();
        }
    }
}
