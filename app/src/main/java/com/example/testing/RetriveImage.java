package com.example.testing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.ColorSpace;
import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class RetriveImage extends AppCompatActivity {
    FloatingActionButton fab;
    private RecyclerView recyclerView;
    private int number;
    FirebaseAuth mAuth;
    private ArrayList<DataClass> dataList;
    private MyAdapter adapter;
    private CalendarView calendarView;
    final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.CANADA);
    final Date now = new Date();
    final String fileName = formatter.format(now);
    private String stringDateSelected;
    public void onBackPressed() {
        // Quay về màn hình trước đó
        Intent intent = new Intent(this, Select.class);
        startActivity(intent);
        finish(); // Kết thúc hiện tại Activity để ngăn chặn người dùng quay lại nó bằng nút back
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrive_image);
        fab = findViewById(R.id.fab);

        calendarView=findViewById(R.id.calendarView);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dataList = new ArrayList<>();
        adapter = new MyAdapter(this, dataList);
        recyclerView.setAdapter(adapter);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
//                stringDateSelected = Integer.toString(i) + Integer.toString(i1+1) + Integer.toString(i2);
                // Tăng i1 lên 1 vì i1 biểu diễn tháng từ 0-11
                i1 += 1;

                // Định dạng ngày tháng
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.US);

                // Tạo một đối tượng Date từ ngày được chọn
                Date selectedDate = new Date(i - 1900, i1 - 1, i2);

                // Format thành chuỗi "yyyyMMdd"
                stringDateSelected = sdf.format(selectedDate);
                Log.d("RetriveImage", "Selected Date: " + stringDateSelected);

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("attendace").child(stringDateSelected);
                databaseReference.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        dataList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            DataClass dataClass = dataSnapshot.getValue(DataClass.class);
                            dataList.add(dataClass);
                        }
                        adapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RetriveImage.this, TeacherDashboard.class);
                startActivity(intent);
                finish();
            }
        });

    }
}