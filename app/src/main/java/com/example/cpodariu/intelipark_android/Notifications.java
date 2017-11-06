package com.example.cpodariu.intelipark_android;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.widget.Toast;

import com.example.cpodariu.intelipark_android.NetworkUtils.TCPClient;
import com.example.cpodariu.intelipark_android.Notifs.NotificationActivity;
import com.example.cpodariu.intelipark_android.Utils.SharedPreferencesHelper;

import java.util.ArrayList;

/**
 * Created by cpodariu on 05.11.2017.
 */


public class Notifications extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();

        new NotificationRequest(context).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        wl.release();
    }

    public void setAlarm(Context context)
    {
        cancelAlarm(context);
        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, Notifications.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + 2000,
                2000, pi); // Millisec * Second * Minute
    }

    public void cancelAlarm(Context context)
    {
        Intent intent = new Intent(context, Notifications.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    class NotificationRequest extends AsyncTask<Void, Void, Boolean> {
        Context ctx;
        ArrayList<ArrayList<String>> res;
        public NotificationRequest(Context ctx) {
            this.ctx = ctx;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            ArrayList<String> params = new ArrayList<String>();
            params.add("getNotifications");
            params.add(SharedPreferencesHelper.getUserEmail(ctx));
            params.add(SharedPreferencesHelper.getUserPassword(ctx));

            res = new TCPClient(params).runForTable();
            if (res != null && res.size() > 0) {
                return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success && SharedPreferencesHelper.isUserLoggedIn(ctx)) {
                try {
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(ctx)
                                    .setSmallIcon(R.drawable.ic_directions_car_black_24dp)
                                    .setContentTitle("You have " + res.size() + " notifications")
                                    .setContentText("Click to find out more!");

                    Intent resultIntent = new Intent(ctx, NotificationActivity.class);
                    PendingIntent resultPendingIntent =
                            PendingIntent.getActivity(
                                    ctx,
                                    0,
                                    resultIntent,
                                    PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder.setContentIntent(resultPendingIntent);

                    NotificationManager mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

                    mNotificationManager.notify(001, mBuilder.build());
                }catch (Exception e) {}
            }
        }
    }

}
