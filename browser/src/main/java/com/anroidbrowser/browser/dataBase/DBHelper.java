package com.anroidbrowser.browser.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
import com.anroidbrowser.browser.MainApplication;
import com.anroidbrowser.browser.R;
import com.anroidbrowser.browser.activities.MainActivity;
import com.anroidbrowser.browser.myTypes.HistoryElement;
import com.anroidbrowser.browser.myTypes.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 12.09.17.
 */
public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        // конструктор суперкласса
        super(context, "myDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // создаем таблицу с полями
        db.execSQL("create table users ("
                + "id integer primary key autoincrement,"
                + "name text,"
                + "pass text,"
                + "country text,"
                + "city text,"
                + "age text,"
                + "family text,"
                + "sex text,"
                + "avatar text,"
                + "email text" + ");");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long registration(String name, String email, String pass1, String country, String city, String age, String family, String sex, String avatar){
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();

        cv.put("name", name);
        cv.put("email", email);
        cv.put("pass", pass1);
        cv.put("country", country);
        cv.put("city", city);

        cv.put("age", age);
        cv.put("family", family);
        cv.put("sex", sex);
        cv.put("avatar", avatar);

        // вставляем запись и получаем ее ID
        db.beginTransaction();
        try{
            long rowID = db.insert("users", null, cv);
            db.setTransactionSuccessful();
            db.execSQL("create table best" + rowID + " ("
                    + "id integer primary key autoincrement,"
                    + "title text,"
                    + "url text,"
                    + "data text" + ");");
            db.execSQL("create table history" + rowID + " ("
                    + "id integer primary key autoincrement,"
                    + "title text,"
                    + "url text,"
                    + "data text" + ");");
            db.execSQL("create table downloads" + rowID + " ("
                    + "id integer primary key autoincrement,"
                    + "title text,"
                    + "url text,"
                    + "data text" + ");");
            return rowID;

        } finally {
            db.endTransaction();
            close();
        }
    }

    public long update(String name, String email, String pass1, String country, String city, String age, String family, String sex, String avatar, long id){
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();

        cv.put("name", name);
        cv.put("email", email);
        cv.put("pass", pass1);
        cv.put("country", country);
        cv.put("city", city);

        cv.put("age", age);
        cv.put("family", family);
        cv.put("sex", sex);
        cv.put("avatar", avatar);

        // вставляем запись и получаем ее ID
        db.beginTransaction();
        try{
            long rowID = db.update("users", cv, "id = ?",
                    new String[] {String.valueOf(id)});

            db.setTransactionSuccessful();
            return  rowID;
        }finally {
            db.endTransaction();
            close();
        }
    }

    public boolean findMail(String mail){

        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query("users", null, null, null, null, null, null);
        int emailColIndex = c.getColumnIndex("email");

        while (c.moveToNext()) {
            if(c.getString(emailColIndex).equals(mail)){
                c.close();
                close();
                return true;
            }
        }

        c.close();
        close();
        return false;
    }

    public Person checkInput(String mail, String pass){
        String[] datas = new String[9];
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query("users", null, null, null, null, null, null);
        int idColIndex = c.getColumnIndex("id");
        int emailColIndex = c.getColumnIndex("email");
        int passColIndex = c.getColumnIndex("pass");
        int nameColIndex = c.getColumnIndex("name");
        int countryColIndex = c.getColumnIndex("country");
        int cityColIndex = c.getColumnIndex("city");
        int familyColIndex = c.getColumnIndex("family");
        int ageColIndex = c.getColumnIndex("age");
        int sexColIndex = c.getColumnIndex("sex");
        int avatarColIndex = c.getColumnIndex("avatar");
        while (c.moveToNext()) {
            if(c.getString(emailColIndex).equals(mail) && c.getString(passColIndex).equals(pass)){
                long id = c.getInt(idColIndex);
                datas[0] = c.getString(nameColIndex);
                datas[1] = c.getString(familyColIndex);
                datas[2] = c.getString(ageColIndex);
                datas[3] = c.getString(sexColIndex);
                datas[4] = c.getString(emailColIndex);
                datas[5] = c.getString(passColIndex);
                datas[6] = c.getString(countryColIndex);
                datas[7] = c.getString(cityColIndex);
                datas[8] = c.getString(avatarColIndex);
                c.close();
                return new Person(datas, id, getHistory(id), getBests(id), getDownloads(id));
            }
        }

        c.close();
        close();
        return null;
    }

    private List<HistoryElement> getBests(long id){
        List<HistoryElement> historyElementList = new ArrayList<HistoryElement>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query("best" + id, null, null, null, null, null, null);
        int idColIndex = c.getColumnIndex("id");
        int titleColIndex = c.getColumnIndex("title");
        int urlColIndex = c.getColumnIndex("url");
        int dataColIndex = c.getColumnIndex("data");

        while (c.moveToNext()) {
            historyElementList.add(new HistoryElement(c.getString(dataColIndex), c.getString(titleColIndex), c.getString(urlColIndex), c.getInt(idColIndex)));
        }
        return historyElementList;
    }
    public long addToBests(String data, String title, String url, long id){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("title", title);
        cv.put("url", url);
        cv.put("data", data);
        db.beginTransaction();
        try{
            long rowID = db.insert("best" + id, null, cv);
            db.setTransactionSuccessful();
            return rowID;
        } finally {
            db.endTransaction();
        }
    }

    private List<HistoryElement> getDownloads(long id){
        List<HistoryElement> historyElementList = new ArrayList<HistoryElement>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query("downloads" + id, null, null, null, null, null, null);
        int idColIndex = c.getColumnIndex("id");
        int titleColIndex = c.getColumnIndex("title");
        int urlColIndex = c.getColumnIndex("url");
        int dataColIndex = c.getColumnIndex("data");

        while (c.moveToNext()) {
            historyElementList.add(new HistoryElement(c.getString(dataColIndex), c.getString(titleColIndex), c.getString(urlColIndex), c.getInt(idColIndex)));
        }
        return historyElementList;
    }
    public long addToDownloads(String data, String title, String url, long id){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("title", title);
        cv.put("url", url);
        cv.put("data", data);
        db.beginTransaction();
        try{
            long rowID = db.insert("downloads" + id, null, cv);
            db.setTransactionSuccessful();
            return rowID;
        } finally {
            db.endTransaction();
        }
    }

    private List<HistoryElement> getHistory(long id){
        List<HistoryElement> historyElementList = new ArrayList<HistoryElement>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query("history" + id, null, null, null, null, null, null);
        int idColIndex = c.getColumnIndex("id");
        int titleColIndex = c.getColumnIndex("title");
        int urlColIndex = c.getColumnIndex("url");
        int dataColIndex = c.getColumnIndex("data");

        while (c.moveToNext()) {
            historyElementList.add(new HistoryElement(c.getString(dataColIndex), c.getString(titleColIndex), c.getString(urlColIndex), c.getInt(idColIndex)));
        }
        c.close();
        return historyElementList;
    }
    public int addToHistory(String data, String title, String url, long id){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("title", title);
        cv.put("url", url);
        cv.put("data", data);
        db.beginTransaction();
        try{
            long rowID = db.insert("history" + id, null, cv);
            db.setTransactionSuccessful();
            return (int)rowID;
        } finally {
            db.endTransaction();
        }
    }
}
