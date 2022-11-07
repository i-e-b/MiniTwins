package com.example.mtservice;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.Log;

public class TestContentProvider extends ContentProvider {
    private final String TAG = "TestCP";
    public TestContentProvider() {
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