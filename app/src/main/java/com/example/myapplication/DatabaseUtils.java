package com.example.myapplication;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUtils {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static List<HoaDon> getAllHoaDon(DatabaseHelper dbHelper) {
        List<HoaDon> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(DatabaseHelper.TABLE_NAME, null, null, null, null, null, null);
        int idIdx = c.getColumnIndex(DatabaseHelper.COL_ID);
        int hoTenIdx = c.getColumnIndex(DatabaseHelper.COL_HOTEN);
        int soTienIdx = c.getColumnIndex(DatabaseHelper.COL_SOTIEN);
        int khachLeIdx = c.getColumnIndex(DatabaseHelper.COL_KHACHLE);
        int ngayThangIdx = c.getColumnIndex(DatabaseHelper.COL_NGAYTHANG);

        while (c.moveToNext()) {
            if (idIdx < 0 || hoTenIdx < 0 || soTienIdx < 0 || khachLeIdx < 0 || ngayThangIdx < 0)
                continue;
            String id = c.getString(idIdx);
            String hoTen = c.getString(hoTenIdx);
            boolean khachLe = c.getInt(khachLeIdx) == 1;
            int soTien = c.getInt(soTienIdx);
            String ngayThangStr = c.getString(ngayThangIdx);
            LocalDateTime ngayThang = LocalDateTime.parse(ngayThangStr, FORMATTER);

            list.add(new HoaDon(id, hoTen, soTien, ngayThang, khachLe));
        }
        c.close();
        return list;
    }

    public static void insertHoaDon(DatabaseHelper dbHelper, HoaDon hoaDon) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_ID, hoaDon.getSoHoaDon());
        values.put(DatabaseHelper.COL_HOTEN, hoaDon.getHoTen());
        values.put(DatabaseHelper.COL_SOTIEN, hoaDon.getSoTien());
        values.put(DatabaseHelper.COL_KHACHLE, hoaDon.isKhachLe() ? 1 : 0);
        values.put(DatabaseHelper.COL_NGAYTHANG, hoaDon.getNgayThang().format(FORMATTER));
        db.insert(DatabaseHelper.TABLE_NAME, null, values);
    }

    public static void updateHoaDon(DatabaseHelper dbHelper, HoaDon hoaDon) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_HOTEN, hoaDon.getHoTen());
        values.put(DatabaseHelper.COL_SOTIEN, hoaDon.getSoTien());
        values.put(DatabaseHelper.COL_KHACHLE, hoaDon.isKhachLe() ? 1 : 0);
        values.put(DatabaseHelper.COL_NGAYTHANG, hoaDon.getNgayThang().format(FORMATTER));
        db.update(DatabaseHelper.TABLE_NAME, values, DatabaseHelper.COL_ID + "=?",
                new String[] { hoaDon.getSoHoaDon() });
    }

    public static void deleteHoaDon(DatabaseHelper dbHelper, String soHoaDon) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.COL_ID + "=?", new String[] { soHoaDon });
    }
}