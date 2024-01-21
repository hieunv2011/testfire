package com.example.testing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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

public class TeacherDashboard extends AppCompatActivity implements SensorEventListener {

    TextView studentNum,textUser;
    ImageButton btnAttendance,btnList,btnImage,yt,logout;
    FirebaseAuth auth;
    FirebaseUser user;
    ImageButton btnCheckWifi;
    private ImageButton btnBrightness;
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private float currentLightValue = 0.0f; // Lưu giá trị cảm biến ánh sáng
    String userName, userEmail,userId;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("teacher");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dashboard);
        auth = FirebaseAuth.getInstance();
        studentNum =findViewById(R.id.studentNum);
        btnAttendance=findViewById(R.id.btnAttendance);
        btnImage=findViewById(R.id.btnImage);
        yt = findViewById(R.id.yt);
        btnList=findViewById(R.id.btnList);
        logout=findViewById(R.id.logout);
        textUser=findViewById(R.id.textUser);
        //DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(userName);
        btnCheckWifi = findViewById(R.id.btnCheckWifi);
        btnBrightness = findViewById(R.id.btnBrightness);

        // Set up sensor
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        // ClickListener for btnBrightness
        btnBrightness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Sensor: " + currentLightValue + "\n" + brightness(currentLightValue));
            }
        });

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
                Toast.makeText(TeacherDashboard.this, "fail",Toast.LENGTH_LONG).show();
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
                Toast.makeText(TeacherDashboard.this, "fail",Toast.LENGTH_LONG).show();
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

        yt.setOnClickListener(new View.OnClickListener() {
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
                AlertDialog.Builder builder = new AlertDialog.Builder(TeacherDashboard.this);
                builder.setTitle("Cảnh báo")
                        .setMessage("Bạn muốn đăng xuất khỏi tài khoản đúng không?")
                        .setPositiveButton("Đúng", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(getApplicationContext(),SelectRole.class);
                                startActivity(intent);
                                finish();
                            }
                        }).setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(getApplicationContext(),TeacherDashboard.class);
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

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            // Lưu giá trị cảm biến ánh sáng khi nó thay đổi
            currentLightValue = event.values[0];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do nothing for accuracy changes in this case
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register light sensor listener
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister light sensor listener to save resources when the activity is paused
        sensorManager.unregisterListener(this);
    }

    private void showToast(String message) {
        // Show a Toast message with the given content
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private String brightness(float lightSensor) {
        if (lightSensor == 0) {
            return "Cực kỳ tối";
        } else if (lightSensor >= 1 && lightSensor <= 50) {
            return "Tối";
        } else if (lightSensor >= 51 && lightSensor <= 5000) {
            return "Sáng";
        } else {
            return "Cực kỳ sáng";
        }
    }
}