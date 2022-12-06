package e.s.MtPluginTest;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

import com.example.mtplug.IPlugin;
import com.example.mtplug.IPluginHost;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class TestPlugin implements IPlugin {
    public IPluginHost parent;
    private Runnable onFinished;

    @Override
    public void RunActivity(IPluginHost container, Runnable onFinished) {
        Log.i("PLUGIN", "Starting plug-in view");
        parent = container;
        this.onFinished = onFinished;

        parent.getHostActivity().setTitle("Updated from plug-in");

        PluginView view = new PluginView(this);
        view.add("----------");
        view.add("Tap without scrolling to exit plugin");
        view.add("----------");
        view.add("This is a little");
        view.add("scroll view");
        view.add("running from inside");
        view.add("a plug-in that was");
        view.add("loaded from a service");
        view.add("(which is as complex");
        view.add("as I could find)");
        view.add("----------");
        view.add("Text from reading content:");

        AssetManager r = parent.getHostActivity().getResources().getAssets();
        try {
            view.add(getStringFromStream(r.open("plugin.txt")));
        } catch (IOException e) {
            view.add("failed to read assets directly");
        }

        view.add("----------");
        view.add("Text from host provider:");

        try {
            view.add(container.getTextAssetFile("plugin.txt"));
        } catch (IOException e) {
            view.add("failed to read assets via host");
        }

        parent.getHostActivity().setContentView(view);
        Log.i("PLUGIN", "Waiting for tap-to-exit");
    }

    public void StopActivity(){
        Log.i("PLUGIN", "Got tap-to-exit, closing up");
        parent.getHostActivity().setTitle("Ending plug-in activity");
        onFinished.run();
        Log.i("PLUGIN", "Exit completed");
    }


    private String getStringFromStream(InputStream stream) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(stream);
        try {
            StringBuilder output = new StringBuilder();

            byte[] buf = new byte[4096];
            int len;
            while ((len = bis.read(buf, 0, 4096)) > 0) {
                output.append(new String(buf, 0, len, StandardCharsets.UTF_8));
            }
            return output.toString();
        } finally {
            bis.close();
            stream.close();
        }
    }
}
