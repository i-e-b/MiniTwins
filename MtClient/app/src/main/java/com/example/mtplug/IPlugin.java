package com.example.mtplug;

@SuppressWarnings("unused")
public interface IPlugin {
    void RunActivity(IPluginHost host, Runnable onFinished);
}