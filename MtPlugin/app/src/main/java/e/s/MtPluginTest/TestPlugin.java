package e.s.MtPluginTest;

import android.app.Activity;
import android.util.Log;

import com.example.mtplug.IPlugin;

public class TestPlugin implements IPlugin {
    public Activity parent;
    private Runnable onFinished;

    @Override
    public void RunActivity(Activity container, Runnable onFinished) {
        Log.i("PLUGIN", "Starting plug-in view");
        parent = container;
        this.onFinished = onFinished;

        parent.setTitle("Updated from plug-in");

        PluginView view = new PluginView(this);
        view.add("----------");
        view.add("Tap to end");
        view.add("----------");
        view.add("This is a little");
        view.add("scroll view");
        view.add("running from inside");
        view.add("a plug-in that was");
        view.add("loaded from a service");
        view.add("(which is as complex");
        view.add("as I could find)");
        parent.setContentView(view);
        Log.i("PLUGIN", "Waiting for tap-to-exit");
    }

    public void StopActivity(){
        Log.i("PLUGIN", "Got tap-to-exit, closing up");
        parent.setTitle("Ending plug-in activity");
        onFinished.run();
        Log.i("PLUGIN", "Exit completed");
    }
}
