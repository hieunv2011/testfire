package com.example.testing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class StudentList extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference database;
    StudentAdapter studentAdapter;
    FloatingActionButton backButton;
    ArrayList<Student> list;
    private DatabaseReference mDatabase;
    private TextView studentCount,studentNotCount;
    final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.CANADA);
    final Date now = new Date();
    final String fileName = formatter.format(now);
    public void onBackPressed() {
        // Quay về màn hình trước đó
        Intent intent = new Intent(this, Select.class);
        startActivity(intent);
        finish(); // Kết thúc hiện tại Activity để ngăn chặn người dùng quay lại nó bằng nút back
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        mDatabase=FirebaseDatabase.getInstance().getReference();
        studentCount=findViewById(R.id.studentCount);
        studentNotCount=findViewById(R.id.studentNotCount);
        recyclerView = findViewById(R.id.studentList);
        backButton=findViewById(R.id.backButton);
        database = FirebaseDatabase.getInstance().getReference("users");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        studentAdapter = new StudentAdapter(this,list);
        recyclerView.setAdapter(studentAdapter);

        countStudent();
        countNotAtdStudent();
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentList.this, TeacherDashboard.class);
                startActivity(intent);
                finish();
            }
        });
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Student student = dataSnapshot.getValue(Student.class);
                    list.add(student);
                }
                studentAdapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void countStudent() {
        // Thực hiện truy vấn để lấy dữ liệu từ "users" node trong Firebase
        mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Đếm số lượng user bằng cách đếm số lượng children trong "users" node
                long userCount = dataSnapshot.getChildrenCount();
                Log.d("UserCount", "Number of Users: " + userCount);

                // Hiển thị số lượng user lên TextView
                studentCount.setText("Tổng số học sinh: " + userCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("FirebaseError", "Failed to read value.", databaseError.toException());
            }
        });
    }

    private void countNotAtdStudent() {
        // Thực hiện truy vấn để lấy dữ liệu từ Firebase
        mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Đếm số lượng user không có status là "true"
                int notAtdStudent= 0;

                // Duyệt qua từng user trong dataSnapshot
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {

                    DataSnapshot statusSnapshot = userSnapshot.child("status");
                    // Kiểm tra nếu status không tồn tại hoặc không phải là "fileName"
                    if (!statusSnapshot.exists() || !fileName.equalsIgnoreCase(statusSnapshot.getValue(String.class))) {
                        notAtdStudent++;
                    }
                }

                // Hiển thị số lượng user không có status là "true" lên TextView
                Log.d("UserCount", "Number of Users: " + notAtdStudent);
                studentNotCount.setText("Chưa điểm danh: " + notAtdStudent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("FirebaseError", "Failed to read value.", databaseError.toException());
            }
        });
    }

}