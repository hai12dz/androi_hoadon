package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.widget.SearchView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private ListView listView;
    private List<HoaDon> hangHoaList;
    private List<HoaDon> fullHangHoaList;
    private HoaDonAdapter adapter;
    private SearchView searchView;
    private TextView tvSoTaiKhoan;
    private TextView tvSoDuCuoi;

    private int selectedPosition = -1;
    private Button btnSapXep;
    private Button btnThem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // Gán Toolbar thành ActionBar chính

        dbHelper = new DatabaseHelper(this);
        listView = findViewById(R.id.listView);
        searchView = findViewById(R.id.searchView);
        btnSapXep = findViewById(R.id.buttonSort);
        btnThem = findViewById(R.id.btnAdd);
        tvSoTaiKhoan = findViewById(R.id.textViewSoTaiKhoan);
        tvSoDuCuoi = findViewById(R.id.textViewSoDuCuoi);

        loadHangHoaData();

//        btnSapXep.setOnClickListener(v -> {
//            hangHoaList.sort((g1, g2) -> g2.getNgayThang().compareToIgnoreCase(g1.getNgayThang()));
//            adapter.notifyDataSetChanged();
//        });
//        btnThem.setOnClickListener(v -> {
//            Intent intent = new Intent(MainActivity.this, ThemGiaoDichActivity.class);
//            // Gửi số mã giao dịch tiếp theo sang màn thêm mới
//            int nextId = hangHoaList.isEmpty() ? 1 : hangHoaList.get(hangHoaList.size() - 1).getId() + 1;
//            intent.putExtra("NEXT_ID", nextId);
//            startActivity(intent);
//        });

        // Xử lý tìm kiếm bằng SearchView
        // searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
        // @Override
        // public boolean onQueryTextSubmit(String query) {
        // filterByScore(query);
        // return true;
        // }
        //
        // @Override
        // public boolean onQueryTextChange(String newText) {
        // filterByScore(newText);
        // return true;
        // }
        // });

        registerForContextMenu(listView);
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            selectedPosition = position;
            return false;
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        loadHangHoaData();
    }
    private void loadHangHoaData() {
        fullHangHoaList = DatabaseUtils.getAllHoaDon(dbHelper);
        hangHoaList = new ArrayList<>(fullHangHoaList); // Tạo bản sao
        adapter = new HoaDonAdapter(this, hangHoaList);
        listView.setAdapter(adapter);

        // Tính giá trung bình
        // if (fullHangHoaList.size() > 0) {
        // int tong = 0;
        // for (HangHoa hh : fullHangHoaList) {
        // tong += hh.isGiamGia() ? hh.tinhGiaBan() : hh.getGiaNiemYet();
        // }
        // double trungBinh = (double) tong / fullHangHoaList.size();
        // textViewGiaTrungBinh.setText("Giá trung bình: " +
        // formatNumberWithSpace((long) trungBinh) + "đ");
        // } else {
        // textViewGiaTrungBinh.setText("Giá trung bình: 0");
        // }
    }

    // Định dạng số có dấu cách giữa các nhóm 3 số
    private String formatNumberWithSpace(long number) {
        String s = String.valueOf(number);
        StringBuilder sb = new StringBuilder();
        int len = s.length();
        int count = 0;
        for (int i = len - 1; i >= 0; i--) {
            sb.insert(0, s.charAt(i));
            count++;
            if (count == 3 && i > 0) {
                sb.insert(0, ' ');
                count = 0;
            }
        }
        return sb.toString();
    }

    // private void filterByScore(String scoreText) {
    // hangHoaList.clear();
    //
    // if (scoreText.isEmpty()) {
    // // Nếu ô tìm kiếm trống, hiển thị tất cả
    // hangHoaList.addAll(fullHangHoaList);
    // } else {
    // try {
    // // Chuyển đổi văn bản thành số
    // float minScore = Float.parseFloat(scoreText);
    //
    // // Thêm nhà hàng có điểm >= điểm nhập vào
    // for (HangHoa nh : fullHangHoaList) {
    // if (nh.getDiemTrungBinh() >= minScore) {
    // nhaHangList.add(nh);
    // }
    // }
    // } catch (NumberFormatException e) {
    // // Nếu không phải số, hiển thị tất cả
    // nhaHangList.addAll(fullNhaHangList);
    // }
    // }
    //
    // // Cập nhật ListView
    // adapter.notifyDataSetChanged();
    // }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, 1, 0, "Sửa");
        menu.add(0, 2, 1, "Xóa");
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (selectedPosition < 0)
            return super.onContextItemSelected(item);

        HoaDon hoaDon = hangHoaList.get(selectedPosition);

        if (item.getItemId() == 1) { // Sửa
            Intent intent = new Intent(this, SuaGiaoDichActivity.class);
            intent.putExtra("SO_HOA_DON", hoaDon.getSoHoaDon());
            intent.putExtra("HO_TEN", hoaDon.getHoTen());
            intent.putExtra("NGAY_THANG", hoaDon.getNgayThang().toString());
            intent.putExtra("SO_TIEN", hoaDon.getSoTien());
            intent.putExtra("KHACH_LE", hoaDon.isKhachLe());
            startActivity(intent);
            return true;
        }
        if (item.getItemId() == 2) { // Xóa
            showDeleteDialog(hoaDon);
            return true;
        }
        return super.onContextItemSelected(item);
    }
    // ...existing code...

    private void showDeleteDialog(HoaDon hoaDon) {
        String title = "Bạn muốn xóa hóa đơn này?";
        StringBuilder msg = new StringBuilder();
        msg.append("Số hóa đơn: ").append(hoaDon.getSoHoaDon()).append("\n");
        msg.append("Khách hàng: ").append(hoaDon.getHoTen()).append("\n");
        msg.append("Số tiền: ").append(formatNumberWithSpace(hoaDon.getSoTien())).append("\n");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(msg.toString());
        builder.setNegativeButton("Quay về", null);
        builder.setPositiveButton("OK", (dialog, which) -> {
            DatabaseUtils.deleteHoaDon(dbHelper, hoaDon.getSoHoaDon());
            loadHangHoaData();
        });
        builder.show();
    }
    // @Override
    // public boolean onContextItemSelected(MenuItem item) {
    // if (selectedPosition < 0)
    // return super.onContextItemSelected(item);
    // GiaoDich nh = hangHoaList.get(selectedPosition);
    // if (item.getItemId() == 2) {
    // showDeleteDialog(nh);
    // return true;
    // }
    // // Xử lý sửa nếu cần
    // return super.onContextItemSelected(item);
    // }

    // private void showDeleteDialog(GiaoDich nh) {
    // String title = "Bạn muốn xóa nhà hàng này?";
    // StringBuilder msg = new StringBuilder();
    // msg.append("Giá: ").append(nh.getGiaNiemYet()).append("\n");
    // if (nh.isGiamGia()) {
    // msg.append("Giảm còn: ").append(nh.tinhGiaBan()).append("\n");
    // }
    //
    // AlertDialog.Builder builder = new AlertDialog.Builder(this);
    // builder.setTitle(title);
    // builder.setMessage(msg.toString());
    // builder.setNegativeButton("Quay về", null);
    // builder.setPositiveButton("OK", (dialog, which) -> {
    // DatabaseUtils.delete(dbHelper, nh.getId());
    // loadHangHoaData();
    // });
    // builder.show();
    // }
}