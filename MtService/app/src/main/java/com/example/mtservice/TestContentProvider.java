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

public class TestContentProvider extends ContentProvider {
    private final String TAG = "TestCP";
    public TestContentProvider() {
    }

    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        Log.i(TAG, "File requested: "+uri.toString());

        try {
            return getFileDescriptor("Hello, world. Here are some bytes! Enjoy them.".getBytes(StandardCharsets.UTF_8));
        } catch(Exception ex){
            throw new FileNotFoundException("Failure:"+ ex);
        }
    }

    private ParcelFileDescriptor getFileDescriptor(byte[] fileData) throws IOException {
        Log.d(TAG, "Found " + fileData.length + " bytes of data");
        ParcelFileDescriptor[] pipe = ParcelFileDescriptor.createPipe();

        // Stream the file data to our ParcelFileDescriptor output stream
        InputStream inputStream = new ByteArrayInputStream(fileData);
        ParcelFileDescriptor.AutoCloseOutputStream outputStream = new ParcelFileDescriptor.AutoCloseOutputStream(pipe[1]);
        int len;
        while ((len = inputStream.read()) >= 0) {
            outputStream.write(len);
        }
        inputStream.close();
        outputStream.flush();
        outputStream.close();

        // Return the ParcelFileDescriptor input stream to the calling activity in order to read
        // the file data.
        return pipe[0];
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        Log.i(TAG, "delete request: "+uri.toString()+"; "+selection);
        return 1;
    }

    @Override
    public String getType(Uri uri) {
        // Implement this to handle requests for the MIME type of the data
        // at the given URI.
        Log.i(TAG, "type request: "+uri.toString());
        return "application/mt-twin-custom";
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
}