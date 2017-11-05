package com.example.cpodariu.intelipark_android;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private Long parkingSpot;

    private void showSpot()
    {
        spotAssignedTv.setText(parkingSpot.toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.parking_spot_activity_content, container, false);
        spotAssignedTv = (TextView) view.findViewById(R.id.parkingSpotTv);
        new RequestParkingSpotRequest().execute();
        new MessageDialog("You got them notifos m8").show(getFragmentManager(), "youhavenotifs");
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

}
