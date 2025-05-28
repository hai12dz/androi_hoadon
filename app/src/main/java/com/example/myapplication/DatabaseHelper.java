package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "hoadon.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "HoaDon";
    public static final String COL_ID = "soHoaDon";
    public static final String COL_HOTEN= "hoTen";
    public static final String COL_SOTIEN = "soTien";
    public static final String COL_KHACHLE = "khachLe";
    public static final String COL_NGAYTHANG = "ngayThang";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " TEXT PRIMARY KEY," +
                COL_HOTEN + " TEXT," +
                COL_SOTIEN + " INTEGER," +
                COL_NGAYTHANG + " TEXT," +
                COL_KHACHLE + " INTEGER)");

        int id = 21;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        Object[][] data = {
                { "Nguyễn Nam", 15000, 0 },
                { "Sữa tươi", 22000, 1 },
                { "Ba Do", 12000, 0 },
                { "Hảo Nam", 8000, 1 },
                { "Vung Nồi", 10000, 0 }
        };

        for (Object[] row : data) {
            String newId = "HD" + id;
            String formattedDate = now.format(formatter);

            db.execSQL("INSERT INTO " + TABLE_NAME + " (" +
                            COL_ID + "," + COL_HOTEN + "," + COL_SOTIEN + "," + COL_NGAYTHANG + "," + COL_KHACHLE +
                            ") VALUES (?, ?, ?, ?, ?)",
                    new Object[]{ newId, row[0], row[1], formattedDate, row[2] });

            id += 17;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}
