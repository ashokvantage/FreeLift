package com.freelift;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class WelComeActivity extends AppCompatActivity {
RelativeLayout rl_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wel_come);
        rl_login=(RelativeLayout)findViewById(R.id.rl_login);
        rl_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(WelComeActivity.this, LoginActivity.class);
                startActivity(in);
                finish();
            }
        });
    }
}
