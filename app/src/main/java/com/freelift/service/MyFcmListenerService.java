package com.freelift.service;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.PopupWindow;

import com.freelift.Constants;
import com.freelift.Demo;
import com.freelift.DriverNavigation;
import com.freelift.LoginActivity;
import com.freelift.PassengerNavigation;
import com.freelift.R;
import com.freelift.SplashPage;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class MyFcmListenerService extends FirebaseMessagingService implements Constants {
    public static String Pushmessage = "";
    public PopupWindow pwindo;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    String Login_Type = "";

    @Override
    public void onMessageReceived(RemoteMessage message) {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Map data = message.getData();

        generateNotification(getApplicationContext(), data);
    }

//    @Override
//    public void onMessageReceived(String from, Bundle data) {
//
//        generateNotification(getApplicationContext(), data);
//        Log.e("Message", "Could not parse malformed JSON: \"" + data.toString() + "\"");
//    }

    @Override
    public void onDeletedMessages() {
        sendNotification("Deleted messages on server");
    }

    @Override
    public void onMessageSent(String msgId) {

        sendNotification("Upstream message sent. Id=" + msgId);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg) {

        Log.e("Message", "Could not parse malformed JSON: \"" + msg + "\"");
    }

    /**
     * Create a notification to inform the user that server has sent a message.
     */
    private void generateNotification(Context context, Map data) {

        String message;
        boolean block = false;
        if (data.size() == 0) {
            message = Pushmessage;
        } else {
            message = data.get("msg").toString();
            if (message.equalsIgnoreCase("Block")) {
                block = true;
                message = "Your account is blocked by admin.";
            }
        }
        int icon = R.drawable.launch_icon;
        long when = System.currentTimeMillis();
        String title = context.getString(R.string.app_name);


        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();

        if (isScreenOn == false) {
            PowerManager pma = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wl = pma.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
            wl.acquire(15000);
            PowerManager.WakeLock wl1 = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "MyLock");
            wl1.acquire(10000);
        }

//                if (App.getInstance().getId() != 0 && Long.toString(App.getInstance().getId()).equals(accountId)) {
        int icons = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? R.drawable.launch_icon : R.drawable.launch_icon;
        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;
//        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Login_Type = sharedpreferences.getString("login_as", "");
        Intent intent;
        if (Login_Type.equalsIgnoreCase("driver")) {
            intent = new Intent(context, DriverNavigation.class);
//            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(in);

            if(message.contains("cancelled"))
            {
                SplashPage.click_on_notification = true;
            }
            else
            {
                SplashPage.click_on_notification = false;
            }
        } else {
            intent = new Intent(context, PassengerNavigation.class);
//            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(in);
            SplashPage.click_on_notification = true;
        }
//        Intent intent = new Intent(context, SplashPage.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, m, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
        Notification.Builder b = new Notification.Builder(context);
        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle("FreeLift");
        inboxStyle.setSummaryText(message);
        b.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(icons)
                .setTicker(message)
                .setContentTitle("FreeLift")
                .setContentText(message)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent)
                .setContentInfo("Info")
                .setPriority(Notification.PRIORITY_HIGH);
        Notification notification = new Notification.BigTextStyle(b)
                .bigText(message).build();

        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        Log.d("current task :", "CURRENT Activity ::" + taskInfo.get(0).topActivity.getClass().getSimpleName());
        ComponentName componentInfo = taskInfo.get(0).topActivity;
        if (block) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putLong("customer_driver_id", 0);
            editor.putString("login_as", "");
            editor.commit();
//            SplashPage.customer_driver_login_id = Long.parseLong("0");
            Intent in = new Intent(context, LoginActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(in);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, notification);
        } else {
            if (componentInfo.getPackageName().equalsIgnoreCase("com.freelift")) {
                Intent in = new Intent(context, Demo.class);
                in.putExtra("message", message);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
            } else {
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(1, notification);
            }
        }
// Your account is blocked by admin.

//        if (Helper.isAppRunning(context, "com.freelift")) {
//            Intent in = new Intent(context, Demo.class);
//            in.putExtra("message",message);
//            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(in);
//        } else {
//            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//            notificationManager.notify(1, notification);
//        }
    }
}