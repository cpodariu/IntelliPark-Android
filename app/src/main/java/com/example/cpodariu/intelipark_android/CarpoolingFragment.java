package com.example.cpodariu.intelipark_android;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cpodariu.intelipark_android.NetworkUtils.TCPClient;
import com.example.cpodariu.intelipark_android.Utils.SharedPreferencesHelper;

import java.util.ArrayList;

/**
 * Created by cpodariu on 04.11.2017.
 */

public class CarpoolingFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private CarpoolingAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<String> carpoolingList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.carpooling_recycler_view, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.carpooling_rv);
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new CarpoolingAdapter();
        mRecyclerView.setAdapter(mAdapter);

        RequestCarpoolingInfo r = new RequestCarpoolingInfo();
        r.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        return view;
    }

    public class RequestCarpoolingInfo extends AsyncTask<Void, Void, Boolean> {
        ArrayList<String> carpoolingInfo;

        @Override
        protected Boolean doInBackground(Void... voids) {
            ArrayList<String> args = new ArrayList<String>();
            args.add("getPeopleInZone");
            args.add(SharedPreferencesHelper.getUserEmail(getActivity()));
            args.add(SharedPreferencesHelper.getUserPassword(getActivity()));
            carpoolingInfo = new TCPClient(args).run();
            if(carpoolingInfo != null) {
                return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                mAdapter.setCarpoolingList(carpoolingInfo);
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}
