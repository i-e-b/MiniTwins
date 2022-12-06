package e.s.MtPluginTest;

import android.app.Activity;
import android.util.Log;

import com.example.mtplug.IPlugin;

public class TestPlugin implements IPlugin {
    public static void testMethod(){
        Log.i("PLUGIN", "Hello, world!");
    }

    @Override
    public Object RunActivity(Activity parent) {
        parent.setTitle("Updated from plug-in");
        return null;
    }
}
