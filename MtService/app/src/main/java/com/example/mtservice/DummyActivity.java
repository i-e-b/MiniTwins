package com.example.mtservice;

import android.app.Activity;
import android.os.Bundle;

public class DummyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BlankView v = new BlankView(this);
        setContentView(v);
    }
}
