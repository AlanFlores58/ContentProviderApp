package com.example.alanflores.contentproviderapp;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import java.util.HashMap;

/**
 * Created by alan.flores on 12/22/16.
 */

public class MyProviderf extends ContentProvider {

    static final String PROVIDER_NAME = "com.example.alanflores.contentproviderapp.noProblemCOnPAquete";
    static final String URL = "content://" + PROVIDER_NAME + "/cte";
    static final Uri CONTENT_URI = Uri.parse(URL);
    static final int URI_CODE = 1;
    static final UriMatcher uriMarcher;
    static HashMap<String, String> value;

    static{
        uriMarcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMarcher.addURI(PROVIDER_NAME,"cte",URI_CODE);
        uriMarcher.addURI(PROVIDER_NAME,"cte/*",URI_CODE);
    }

    private SQLiteDatabase db;
    private static final String DATABASE_NAME = "mydb";
    private static final String TABLE_NAME = "names";
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE = "CREATE TABLE "+ TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT NOT NULL);";

    private static class DataBaseHelper extends SQLiteOpenHelper{

        public DataBaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(sqLiteDatabase);
        }
    }


    @Override
    public boolean onCreate() {
        Context con = getContext();

        DataBaseHelper dbHelper = new DataBaseHelper(con);
        db = dbHelper.getWritableDatabase();
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] strings1, String s1) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(TABLE_NAME);

        switch (uriMarcher.match(uri)){
            case URI_CODE:
                queryBuilder.setProjectionMap(value);
                break;
            default:
                throw new IllegalArgumentException("URI desconocida " + uri);
        }

        if(s1 == null || s1 == ""){
            s1 = "name";
        }

        Cursor cursor = queryBuilder.query(db, projection, selection, strings1,null,null,s1);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMarcher.match(uri)){
            case URI_CODE:
                count = db.delete(TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("URI desconocida " + uri);
        }
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMarcher.match(uri)){
            case URI_CODE:
                count = db.update(TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("URI desconocida " + uri);
        }
        return count;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        long rowID = db.insert(TABLE_NAME, "", contentValues);
        if(rowID > 0){
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Error al agregar el elemento " + uri);
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMarcher.match(uri)){
            case URI_CODE:
                return "vnd.android.cursor.dir/cte";
            default:
                throw new IllegalArgumentException("URI no soportada " + uri);
        }
    }

}
