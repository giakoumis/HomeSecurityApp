package com.stathis.giakoumis.homesecurity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HomeSecurityPanel extends AppCompatActivity implements TaskCallback{

    Button btn_arm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_security_panel);

        final PostAlarmArm postAlarmArm = new PostAlarmArm(this);

        btn_arm = (Button)findViewById(R.id.alarm_arm);
        btn_arm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postAlarmArm.execute();
            }
        });
    }

    @Override
    public void done() {
        Toast toast = Toast.makeText(this, "Alarm Armed", Toast.LENGTH_LONG);
        toast.show();
        finish();
    }
}
