package com.zafu.waterquality.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public <T extends View> T $(int id) {
        return (T) findViewById(id);
    }
}
