package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class HoaDonAdapter extends BaseAdapter {
    private Context context;
    private List<HoaDon> list;

    public HoaDonAdapter(Context context, List<HoaDon> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        String soHoaDon = list.get(i).getSoHoaDon();  // ví dụ: "HD21"
        try {
            // Lấy phần số sau "HD" để dùng làm ID
            return Long.parseLong(soHoaDon.replaceAll("\\D+", ""));  // kết quả: 21
        } catch (NumberFormatException e) {
            return i;  // fallback nếu lỗi
        }
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);

        HoaDon gd = list.get(i);
        TextView tvTitle = convertView.findViewById(R.id.tvTitle);
        TextView tvDetail = convertView.findViewById(R.id.tvDetail);
        TextView tvTitleRight = convertView.findViewById(R.id.tvTitleRight);
        TextView tvDetailRight = convertView.findViewById(R.id.tvDetailRight);
      String title, detail, titleRight = "", detailRight = "";
        if (gd.isKhachLe()) {
            title = gd.getHoTen() + "-" + gd.getNgayThang();
            titleRight = String.valueOf(gd.tinhTienHD());
            detail = "Chiết khấu còn " + formatNumberWithSpace(gd.getSoTien());
            convertView.setBackgroundColor(Color.parseColor("#E0FFE0"));
        } else {
            title = gd.getHoTen() + "-" + gd.getNgayThang();
            titleRight = String.valueOf(gd.tinhTienHD());
            detail = "Chiết khấu còn ";
            detailRight=formatNumberWithSpace(gd.getSoTien());
            convertView.setBackgroundColor(Color.parseColor("#FFF3E0")); // Cam nhạt nhẹ nhàng
        }

        tvTitle.setText(title);
        tvDetail.setText(detail);
        tvTitleRight.setText(titleRight);
        tvDetailRight.setText(detailRight);

        return convertView;
    }

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

    private String formatDate(String date) {
        // Đổi yyyy-MM-dd thành dd/MM/yyyy nếu cần
        if (date.contains("-")) {
            String[] arr = date.split("-");
            if (arr.length == 3)
                return arr[2] + "/" + arr[1] + "/" + arr[0];
        }
        return date;
    }
}
