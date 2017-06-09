package com.example.ja010.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by ja010 on 17-06-08.
 */


class MySQLiteDatabase extends SQLiteOpenHelper {
    public MySQLiteDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "Create table if not exists music (" +
                "id integer primary key autoincrement," +
                "title text not null," +
                "URL text);" ;
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "drop table if exists music";
        db.execSQL(sql);
    }
}
class  MyManageDB {
    private static MySQLiteDatabase database = null;
    private static SQLiteDatabase myDB2 = null;
    private static MyManageDB mInstance = null;

    public final static MyManageDB getInstance(Context context) {
        if (mInstance == null) mInstance = new MyManageDB(context);
        return mInstance;
    }

    private MyManageDB(Context context) {
        database = new MySQLiteDatabase(context, "myDB2", null, 1);

    }

    public Cursor execSELECTStudent(String sql) {
        myDB2 = database.getWritableDatabase();
        Cursor cursor = myDB2.rawQuery(sql,null);
        return cursor;
    }
    public void insert(String title, String url){ // insert
        myDB2 = database.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put("title",title);
        value.put("URL",url);
        myDB2.insert("music",null,value);
    }
    public void delete(String title){
        Log.d("title",title);
        myDB2 = database.getWritableDatabase();
        Log.d("delete count",myDB2.delete("music","title=?",new String[]{title})+"");
//        myDB2.execSQL("delete from music where title=?",new String[]{title});
        myDB2.close();
    }
}

