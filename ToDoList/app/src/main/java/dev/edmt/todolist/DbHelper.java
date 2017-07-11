package dev.edmt.todolist;
//hey
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;


public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME="EDMTDev";
    private static final int DB_VER = 1;
    public static final String DB_TABLE="Task";
    public static final String DB_ID="ID";
    public static final String DB_COLUMN = "TaskName";
    public static final String DB_COLUMN2 = "Desc";
    public static final String DB_COLUMN3 = "Date";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT NOT NULL, %s TEXT, %s TEXT);", DB_TABLE, DB_ID, DB_COLUMN, DB_COLUMN2, DB_COLUMN3);
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = String.format("DELETE TABLE IF EXISTS %s", DB_TABLE);
        db.execSQL(query);
        onCreate(db);
    }

    public void insertNewTask(String task, String desc, String date){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DB_COLUMN, task);
        if(desc != "")
        {
            values.put(DB_COLUMN2, desc);
        }
        if(date != "Add date")
        {
            values.put(DB_COLUMN3, date);
        }
        db.insertWithOnConflict(DB_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void deleteTask(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DB_TABLE, DB_ID + " = " + id, null);
    }

    public void updateTask(int id, String nameUpdate, String descUpdate, String dateUpdate)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        if(nameUpdate != null)
        {
            cv.put(DB_COLUMN, nameUpdate);
        }

        if(descUpdate != null)
        {
            cv.put(DB_COLUMN2, descUpdate);
        }

        if(dateUpdate != null) {
            cv.put(DB_COLUMN3, dateUpdate);
        }

        if(nameUpdate != null || descUpdate != null || dateUpdate != null) {
            db.update(DB_TABLE, cv, "ID = " + id, null);
        }
    }

    public String getDesc(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String desc = "";

        Cursor cursor = db.rawQuery("SELECT Desc FROM " + DB_TABLE +" WHERE ID = " + id, null);
        if (cursor.moveToFirst()) {
            do
            {
                desc = cursor.getString(cursor.getColumnIndex("Desc"));
            }while (cursor.moveToNext());
        }

        return desc;
    }

    public int getID(int position)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int x = 0;
        int id = 0;
        Cursor cursor = db.rawQuery("SELECT ID FROM " + DB_TABLE, null);
        if (cursor.moveToFirst()) {
            do
            {
                if(x == position) {
                    id = cursor.getInt(cursor.getColumnIndex("ID"));
                }
                x++;
            }while (cursor.moveToNext());
        }

        return id;
    }

    public String getName(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String name = "";
        Cursor cursor = db.rawQuery("SELECT TaskName FROM " + DB_TABLE + " WHERE ID = " + id, null);
        if (cursor.moveToFirst()) {
            do
            {
                    name = cursor.getString(cursor.getColumnIndex("TaskName"));
            }while (cursor.moveToNext());
        }

        return name;
    }

    public String getDate(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String date = "";

        Cursor cursor = db.rawQuery("SELECT Date FROM " + DB_TABLE +" WHERE ID = " + id, null);
        if (cursor.moveToFirst()) {
            do
            {
                date = cursor.getString(cursor.getColumnIndex("Date"));
            }while (cursor.moveToNext());
        }

        return date;
    }

    public ArrayList<String> getTaskList(){
            ArrayList<String> taskList = new ArrayList<>();
            SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DB_TABLE, null);
        while(cursor.moveToNext()){
            int index = cursor.getColumnIndex("Date");
            int index2 = cursor.getColumnIndex("TaskName");
            String data = cursor.getString(index);
            String data2 = cursor.getString(index2);

            taskList.add(data + ": " + data2);
        }
        return taskList;
    }

    public int checkIfEmpty()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM " + DB_TABLE;
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        return icount;
    }
}
