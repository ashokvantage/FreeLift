package com.freelift;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.freelift.driver.navigation.Home;
import com.freelift.passenger.navigation.MyRides;

public class Demo extends AppCompatActivity {
    TextView txt_title, txt_message;
    Button btn_close;
    String Message;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    String Login_Type = "driver";
    Home home = new Home();
    MyRides myrides = new MyRides();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Make us non-modal, so that others can receive touch events.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

        // ...but notify us that it happened.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);

        setContentView(R.layout.notification_popup);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Login_Type = sharedpreferences.getString("login_as", "");
        txt_title = (TextView) findViewById(R.id.txttitle);
        txt_message = (TextView) findViewById(R.id.txtmessage);
        btn_close = (Button) findViewById(R.id.btn_close);
        Intent in = getIntent();
        Message = in.getStringExtra("message");
        txt_title.setText("NEW NOTIFICATION");
//        if (Login_Type.equalsIgnoreCase("driver")) {
//            txt_message.setText(Message);
//        } else {
//            txt_message.setText("Driver Name:" + Message);
//        }
        txt_message.setText(Message);
        txt_title.setTextColor(Color.BLACK);
        txt_message.setTextColor(Color.BLACK);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Login_Type.equalsIgnoreCase("driver")) {
                    Intent in = new Intent(Demo.this, SplashPage.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(in);

                    if(Message.contains("cancelled"))
                    {
                        SplashPage.click_on_notification = true;
                    }
                    finish();
                } else if(Login_Type.equalsIgnoreCase("customer")){
                    Intent in = new Intent(Demo.this, SplashPage.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(in);
                    SplashPage.click_on_notification = true;
                    finish();
                }

            }
        });
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // If we've received a touch notification that the user has touched
        // outside the app, finish the activity.
        if (MotionEvent.ACTION_OUTSIDE == event.getAction()) {
//            finish();
            return true;
        }

        // Delegate everything else to Activity.
        return super.onTouchEvent(event);
    }
}
