package com.example.mtplug;

import java.io.IOException;

@SuppressWarnings("unused")
public interface IPluginHost {
    /** Get the Android Activity that is hosting this plug-in */
    android.app.Activity getHostActivity();

    /** Read a binary file FROM THE PLUG-IN APK*/
    byte[] getBinaryAssetFile(String assetPath) throws IOException;

    /** Read a binary stream FROM THE PLUG-IN APK.
     * Caller must close.*/
    java.io.InputStream getBinaryAssetStream(String assetPath) throws IOException;

    /** Read a text file FROM THE PLUG-IN APK */
    String getTextAssetFile(String assetPath) throws IOException;
}
