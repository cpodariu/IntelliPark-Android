package com.example.cpodariu.intelipark_android.Notifs;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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

    public void setNotifications(ArrayList<ArrayList<String>> notifications)
    {
        this.notifications = notifications;
    }

    class NotificationAdapterViewHolder extends RecyclerView.ViewHolder {

        public TextView mNotificationTv;
        public ImageButton acceptButton;
        public ImageButton declineButton;

        public NotificationAdapterViewHolder(View view)
        {
            super(view);
            mNotificationTv = (TextView) view.findViewById(R.id.tv_notification_data);
            acceptButton = (ImageButton) view.findViewById(R.id.accept_button);
            declineButton = (ImageButton) view.findViewById(R.id.declie_button);
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
                    new AcceptRequest(carPoolingDataForPosition.get(1), position).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            });
            holder.declineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Refuse(carPoolingDataForPosition.get(1), position).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            });
        }
        else
        {
            if (carPoolingDataForPosition.get(0).equals("acceptRide"))
                holder.mNotificationTv.setText(carPoolingDataForPosition.get(2) + " " + carPoolingDataForPosition.get(3) + " accepted your request");
            if (carPoolingDataForPosition.get(0).equals("rejectRide"))
                holder.mNotificationTv.setText(carPoolingDataForPosition.get(2) + " " + carPoolingDataForPosition.get(3) + " refused your request");
            if (carPoolingDataForPosition.get(0).equals("isVacation"))
                holder.mNotificationTv.setText("You are in vacation between " + carPoolingDataForPosition.get(2) + " and " + carPoolingDataForPosition.get(3));


            holder.acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Seen(carPoolingDataForPosition.get(1), position).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            });
            holder.declineButton.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        if (notifications == null)
            return 0;
        return notifications.size();
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
            new TCPClient(args).runWithoutReturn();
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
            new TCPClient(args).runWithoutReturn();
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
            args.add("seen");
            args.add(id);
            new TCPClient(args).runWithoutReturn();
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            NotificationAdapter.this.notifications.remove(position);
            NotificationAdapter.this.notifyDataSetChanged();
        }
    }
}
