package com.example.mtplug;

@SuppressWarnings("unused")
public interface IPlugin {
    void RunActivity(android.app.Activity parent, Runnable onFinished);
}