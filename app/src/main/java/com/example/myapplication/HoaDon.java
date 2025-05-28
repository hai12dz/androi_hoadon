package com.example.myapplication;

import java.time.LocalDateTime;

public class HoaDon {

    private String soHoaDon;

    private String hoTen;
    private LocalDateTime ngayThang;
    private int soTien;
    private boolean khachLe;

    public HoaDon(String soHoaDon, String hoTen, int soTien, LocalDateTime ngayThang, boolean khachLe) {
        this.soHoaDon = soHoaDon;
        this.hoTen = hoTen;
        this.soTien = soTien;
        this.ngayThang = ngayThang;
        this.khachLe = khachLe;
    }

    public String getSoHoaDon() {
        return soHoaDon;
    }

    public void setSoHoaDon(String soHoaDon) {
        this.soHoaDon = soHoaDon;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public LocalDateTime getNgayThang() {
        return ngayThang;
    }

    public void setNgayThang(LocalDateTime ngayThang) {
        this.ngayThang = ngayThang;
    }

    public int getSoTien() {
        return soTien;
    }

    public void setSoTien(int soTien) {
        this.soTien = soTien;
    }

    public boolean isKhachLe() {
        return khachLe;
    }

    public void setKhachLe(boolean khachLe) {
        this.khachLe = khachLe;
    }

    public int tinhTienHD() {
        if (khachLe) {
            return soTien * 95 / 100;
        }
        return soTien * (100 - 10) / 100;

    }


}
