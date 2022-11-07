package com.example.mtservice;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * This content provider is hooked in via the AndroidManifest.
 * Unlike the AIDL service, this does NOT require that clients
 * bind directly to it -- it is done with a content:// url and
 * an 'authority', which goes in place of a server's DNS or IP
 * in a regular url.
 */
public class TestContentProvider extends ContentProvider {
    private final String TAG = "TestCP";
    public TestContentProvider() {
    }

    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        Log.i(TAG, "File requested: "+uri.toString());

        String path = uri.getPath();

        try {
            switch (path){
                case "/raw-data-please":
                    return strToFile("Hello, world. Here are some bytes! Enjoy them.");

                case "/test-image.svg": // a nice simple star image
                    // NOTE: The 'xmlns' attribute *MUST* be present, or the image won't render
                    return strToFile(
                            "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n<svg xmlns=\"http://www.w3.org/2000/svg\" height=\"210\" width=\"500\"><polygon" +
                                    " points=\"100,10 40,198 190,78 10,78 160,198\"" +
                                    " style=\"fill:lime;stroke:purple;stroke-width:5;fill-rule:nonzero;\"/>" +
                            "</svg>");

                    // com.example.mtservice
                case "/test-web-file.html": // a small web page that references the SVG above
                    return strToFile("<!DOCTYPE html>\n" +
                            "<html>\n" +
                            "<body>\n" +
                            "\n" +
                            "<h1>Sample</h1>\n" +
                            "<p>This is a sample that came from the hidden service</p>\n" +
                            "<img src=\"content://com.example.mtservice/test-image.svg\" alt=\"Star\" title=\"Twinkle\"/>\n" +
                            "\n<br/><hr/><br/>" +
                            "<svg height=\"210\" width=\"500\"><polygon" +
                            " points=\"100,10 40,198 190,78 10,78 160,198\"" +
                            " style=\"fill:lime;stroke:purple;stroke-width:5;fill-rule:nonzero;\"/>" +
                            "</svg>"+
                            "</body>\n" +
                            "</html>");

                default:
                    Log.e(TAG, "Path failed: "+path);
                    return strToFile("Not a real file: "+path);
            }
        } catch (Exception ex) {
            throw new FileNotFoundException("Failure:" + ex);
        }
    }

    @Override
    public String getType(Uri uri) {
        Log.i(TAG, "Type requested: "+uri.toString());
        String path = uri.getPath();
        switch (path){
            case "/raw-data-please":
                return "application/octet-stream";

            case "/test-image.svg":
                return "image/svg+xml";

            case "/test-web-file.html":
                return "text/html";

            default:
                Log.e(TAG, "Type failed: "+path);
                return "text/plain";
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        Log.i(TAG, "delete request: "+uri.toString()+"; "+selection);
        return 1;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // Implement this to handle requests to insert a new row.
        Log.i(TAG, "insert request: "+uri.toString());
        return uri;
    }

    @Override
    public boolean onCreate() {
        // Implement this to initialize your content provider on startup.
        Log.i(TAG, "provider starting up");
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // Implement this to handle query requests from clients.
        Log.i(TAG, "cursor request: "+uri.toString());

        String[] cols = {"a", "b", "c"};
        MatrixCursor c = new MatrixCursor(cols);
        c.addRow(new Object[]{"one", "two", "three"});
        c.addRow(new Object[]{"four", "five", "six"});
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // Implement this to handle requests to update one or more rows.
        Log.i(TAG, "update request: "+uri.toString()+"; "+selection);
        return 1;
    }

    private ParcelFileDescriptor strToFile(String str) throws IOException{
        return getFileDescriptor(str.getBytes(StandardCharsets.UTF_8));
    }

    private ParcelFileDescriptor getFileDescriptor(byte[] fileData) throws IOException {
        Log.d(TAG, "Found " + fileData.length + " bytes of data");
        ParcelFileDescriptor[] pipe = ParcelFileDescriptor.createPipe();

        // Stream the file data to our ParcelFileDescriptor output stream
        InputStream inputStream = new ByteArrayInputStream(fileData);
        ParcelFileDescriptor.AutoCloseOutputStream outputStream =
                new ParcelFileDescriptor.AutoCloseOutputStream(pipe[1]);
        int len;
        while ((len = inputStream.read()) >= 0) outputStream.write(len);inputStream.close();
        outputStream.flush();
        outputStream.close();

        // return ParcelFileDescriptor input stream to the
        // calling activity in order to read the file data.
        return pipe[0];
    }

}