package com.example.cpodariu.intelipark_android;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.example.cpodariu.intelipark_android.Models.MessageDialog;
import com.example.cpodariu.intelipark_android.NetworkUtils.TCPClient;
import com.example.cpodariu.intelipark_android.Utils.SharedPreferencesHelper;

import java.util.ArrayList;

/**
 * Created by cpodariu on 04.11.2017.
 */

public class ParkingSpotFragment extends Fragment {

    private TextView spotAssignedTv;
    private Long parkingSpot = Long.valueOf(0);
    private Button giveSpotUpBtn;

    private void showSpot()
    {
        if (parkingSpot == 0) {
            SharedPreferencesHelper.setMySpot(getContext(), Long.valueOf(0));
            spotAssignedTv.setText("No spot");
            giveSpotUpBtn.setVisibility(View.GONE);
            new RequestCarpoolingDriver().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        else {
            giveSpotUpBtn.setVisibility(View.VISIBLE);
            giveSpotUpBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new GiveUpSpot().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            });
            spotAssignedTv.setText(parkingSpot.toString());
            SharedPreferencesHelper.setMySpot(getContext(), Long.valueOf(parkingSpot));

        }
    }

    private void showDriver(String name)
    {
        if (name != null)
        {
            spotAssignedTv.setText("With " + name);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.parking_spot_activity_content, container, false);
        spotAssignedTv = (TextView) view.findViewById(R.id.parkingSpotTv);
        new RequestParkingSpotRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        new MessageDialog("You got them notifos m8").show(getFragmentManager(), "youhavenotifs");
        giveSpotUpBtn = (Button) view.findViewById(R.id.give_spot);
        giveSpotUpBtn.setVisibility(View.GONE);
        return view;
    }


    public class RequestParkingSpotRequest extends AsyncTask<Void, Void, Boolean> {
        Long mParkingSpot = Long.valueOf(0);

        @Override
        protected Boolean doInBackground(Void... voids) {
            ArrayList<String> args = new ArrayList<String>();
            args.add("showSpot");
            args.add(SharedPreferencesHelper.getUserEmail(getActivity()));
            args.add(SharedPreferencesHelper.getUserPassword(getActivity()));
            ArrayList<String> spot = new TCPClient(args).run();
            if(spot != null && spot.size() >= 1) {
                mParkingSpot = Long.valueOf(spot.get(0));
                return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                parkingSpot = mParkingSpot;
                showSpot();
            }
        }
    }

    public class RequestCarpoolingDriver extends AsyncTask<Void, Void, Boolean> {
        String name;

        @Override
        protected Boolean doInBackground(Void... voids) {
            ArrayList<String> args = new ArrayList<String>();
            args.add("myDriver");
            args.add(SharedPreferencesHelper.getUserEmail(getActivity()));
            args.add(SharedPreferencesHelper.getUserPassword(getActivity()));
            ArrayList<String> spot = new TCPClient(args).run();
            if(spot != null && spot.size() >= 1) {
                name = spot.get(0) + " " + spot.get(1);
                return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success && !name.equals("none none")) {
                showDriver(name);
            }
        }
    }

    public class GiveUpSpot extends AsyncTask<Void, Void, Boolean> {
        String name;

        @Override
        protected Boolean doInBackground(Void... voids) {
            ArrayList<String> args = new ArrayList<String>();
            args.add("rejectSpot");
            args.add(SharedPreferencesHelper.getUserEmail(getActivity()));
            args.add(SharedPreferencesHelper.getUserPassword(getActivity()));
            new TCPClient(args).runWithoutReturn();
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                ParkingSpotFragment.this.parkingSpot = Long.valueOf(0);
                SharedPreferencesHelper.setMySpot(getContext(), Long.valueOf(0));
                showSpot();
                new RequestParkingSpotRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }
    }

}
