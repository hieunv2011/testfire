package com.example.testing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Select extends AppCompatActivity {

    TextView studentNum,textUser;
    ImageButton btnAttendance,btnList,btnImage,btnAlarm,logout;
    FirebaseAuth auth;
    FirebaseUser user;
    ImageButton btnCheckWifi;

    String userName, userEmail,userId;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        auth = FirebaseAuth.getInstance();
        studentNum = findViewById(R.id.studentNum);
        btnAttendance = findViewById(R.id.btnAttendance);
        btnImage = findViewById(R.id.btnImage);
        btnAlarm = findViewById(R.id.btnAlarm);
        btnList = findViewById(R.id.btnList);
        logout = findViewById(R.id.logout);
        textUser = findViewById(R.id.textUser);
        //DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(userName);
        btnCheckWifi = findViewById(R.id.btnCheckWifi);

        //clickListener cho btnCheckWifi
        btnCheckWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkWifiStrength();
            }
        });

        user = auth.getCurrentUser();
        userId=user.getUid();
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Student student = snapshot.getValue(Student.class);
                if(student != null ){
                    String studentN= student.getStudentId();
                    studentNum.setText(studentN);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Select.this, "fail",Toast.LENGTH_LONG).show();
            }
        });

//        if(user==null){
//            Intent intent = new Intent(getApplicationContext(),Login.class);
//            startActivity(intent);
//            finish();
//        }
//        else {
//            textUser.setText(user.getUid());
//        }
        userId=user.getUid();
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Student student = snapshot.getValue(Student.class);
                if(student != null ){
                    String studentName= student.getStudentName();
                        textUser.setText(studentName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Select.this, "fail",Toast.LENGTH_LONG).show();
            }
        });

        btnAttendance.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),UploadImage.class);
                startActivity(intent);
                finish();
            }
        }
        ));

        btnAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUrl("https://www.youtube.com/watch?v=O33x3EyUbpc");
            }
        });
        btnList.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),StudentList.class);
                startActivity(intent);
                finish();
            }
        }
        ));

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo một AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(Select.this);
                builder.setTitle("Cảnh báo")
                        .setMessage("Bạn muốn đăng xuất khỏi tài khoản đúng không?")
                        .setPositiveButton("Đúng", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(getApplicationContext(),Login.class);
                                startActivity(intent);
                                finish();
                            }
                        }).setNegativeButton("Không", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(getApplicationContext(),Select.class);
                                    }
                                });

                // Hiển thị AlertDialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
//                FirebaseAuth.getInstance().signOut();
//                Intent intent = new Intent(getApplicationContext(),Login.class);
//                startActivity(intent);
//                finish();
            }
        });
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),RetriveImage.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void gotoUrl(String s){
        Uri uri= Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }

    //method checkWifi
    private void checkWifiStrength() {
        // Lấy thông tin về tình trạng WiFi và hiển thị Toast tương ứng
        // Kiểm tra tình trạng kết nối mạng và tình trạng WiFi
        if (NetworkUtils.isNetworkConnected(this)) {
            int wifiStrength = NetworkUtils.getWifiStrength(this);

            if (wifiStrength == NetworkUtils.WIFI_STRENGTH_STRONG) {
                Toast.makeText(this, "Tín hiệu WiFi mạnh", Toast.LENGTH_SHORT).show();
            } else if (wifiStrength == NetworkUtils.WIFI_STRENGTH_MODERATE) {
                Toast.makeText(this, "Tín hiệu WiFi ổn định", Toast.LENGTH_SHORT).show();
            } else if (wifiStrength == NetworkUtils.WIFI_STRENGTH_WEAK) {
                Toast.makeText(this, "Tín hiệu WiFi rất yếu", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Không có kết nối mạng
            Toast.makeText(this, "Chưa kết nối WiFi", Toast.LENGTH_SHORT).show();
        }
    }
}