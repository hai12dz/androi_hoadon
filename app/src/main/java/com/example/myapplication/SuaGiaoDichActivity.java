package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SuaGiaoDichActivity extends AppCompatActivity {
    private EditText edtSoHoaDon, edtKhachHang, edtNgayThang, edtSoTien;
    private Switch switchBanLe;
    private Button btnSua, btnQuayVe;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sua);

        dbHelper = new DatabaseHelper(this);

        edtSoHoaDon = findViewById(R.id.edtSoHoaDon);
        edtKhachHang = findViewById(R.id.edtKhachHang);
        edtNgayThang = findViewById(R.id.edtNgayThang);
        edtSoTien = findViewById(R.id.edtSoTien);
        switchBanLe = findViewById(R.id.switchBanLe);
        btnSua = findViewById(R.id.btnSua);
        btnQuayVe = findViewById(R.id.btnQuayVe);

        // Nếu cần, nhận dữ liệu từ Intent để hiển thị lên các EditText ở đây
        // ...existing code...
        // Nhận dữ liệu từ Intent (nếu có)
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("SO_HOA_DON")) {
            edtSoHoaDon.setText(intent.getStringExtra("SO_HOA_DON"));
            edtKhachHang.setText(intent.getStringExtra("HO_TEN"));
            edtSoTien.setText(String.valueOf(intent.getIntExtra("SO_TIEN", 0)));
            switchBanLe.setChecked(intent.getBooleanExtra("KHACH_LE", false));
            // Chuyển đổi ngày tháng từ String sang d/M/yyyy
            String ngayThangStr = intent.getStringExtra("NGAY_THANG");
            if (ngayThangStr != null) {
                try {
                    // Giả sử lưu dạng yyyy-MM-ddTHH:mm:ss
                    LocalDateTime ngayThang = LocalDateTime.parse(ngayThangStr);
                    edtNgayThang.setText(
                            ngayThang.getDayOfMonth() + "/" + ngayThang.getMonthValue() + "/" + ngayThang.getYear());
                } catch (Exception e) {
                    edtNgayThang.setText("");
                }
            }
        }

        btnSua.setOnClickListener(v -> {
            String soHoaDon = edtSoHoaDon.getText().toString().trim();
            String khachHang = edtKhachHang.getText().toString().trim();
            String ngayThangStr = edtNgayThang.getText().toString().trim();
            String soTienStr = edtSoTien.getText().toString().trim();
            boolean khachLe = switchBanLe.isChecked();

            if (soHoaDon.isEmpty() || khachHang.isEmpty() || ngayThangStr.isEmpty() || soTienStr.isEmpty()) {
                // Có thể hiển thị thông báo lỗi ở đây
                return;
            }

            int soTien = Integer.parseInt(soTienStr.replace(" ", ""));
            // Chuyển đổi ngày tháng từ String sang LocalDateTime
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
            LocalDateTime ngayThang = LocalDateTime.now();
            try {
                ngayThang = LocalDateTime.parse(ngayThangStr + " 00:00:00",
                        DateTimeFormatter.ofPattern("d/M/yyyy HH:mm:ss"));
            } catch (Exception e) {
                // Nếu lỗi parse, giữ nguyên là now
            }

            HoaDon hoaDon = new HoaDon(soHoaDon, khachHang, soTien, ngayThang, khachLe);
            DatabaseUtils.updateHoaDon(dbHelper, hoaDon);

            finish(); // Quay về màn trước
        });

        btnQuayVe.setOnClickListener(v -> finish());
    }
}