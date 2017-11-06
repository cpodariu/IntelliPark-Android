package com.example.cpodariu.intelipark_android.Notifs;


import android.app.Notification;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cpodariu.intelipark_android.NetworkUtils.TCPClient;
import com.example.cpodariu.intelipark_android.R;
import com.example.cpodariu.intelipark_android.Utils.SharedPreferencesHelper;

import java.util.ArrayList;

/**
 * Created by cpodariu on 05.11.2017.
 */

public class NotificationsFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private NotificationAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<String> carpoolingList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.carpooling_recycler_view, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.carpooling_rv);
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new NotificationAdapter();
        mRecyclerView.setAdapter(mAdapter);

        new NotificationRequestThread().execute();

        return view;
    }

    public class NotificationRequestThread extends AsyncTask<Void, Void, Boolean> {
        ArrayList<ArrayList<String>> result;

        @Override
        protected Boolean doInBackground(Void... voids) {
            ArrayList<String> params = new ArrayList<String>();
            params.add("getNotifications");
            params.add(SharedPreferencesHelper.getUserEmail(getContext()));
            params.add(SharedPreferencesHelper.getUserPassword(getContext()));
            result = new TCPClient(params).runForTable();
            if (result != null && result.size() >= 1)
                return true;
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                NotificationsFragment.this.mAdapter.setNotifications(result);
                NotificationsFragment.this.mAdapter.notifyDataSetChanged();
            }
        }
    }

}
