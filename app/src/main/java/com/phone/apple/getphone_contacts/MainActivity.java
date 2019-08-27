package com.phone.apple.getphone_contacts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.but_phone)
    public void but_phone() {
        if (!ButtonUtils.isFastDoubleClick(R.id.but_phone)) {
            startActivity(new Intent(this, PhoneBookActivity.class));
        }
    }
}
