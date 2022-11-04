package com.example.mtservice;

import android.app.Activity;
import android.os.Bundle;

public class DummyActivity extends Activity {
    private MessageService _serviceInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _serviceInstance = new MessageService();

        BlankView v = new BlankView(this);
        setContentView(v);
    }
}
