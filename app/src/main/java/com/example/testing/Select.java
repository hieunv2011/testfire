package com.example.testing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Select extends AppCompatActivity {

    TextView email;
    ImageButton btnAttendance,btnList,btnImage,btnAlarm,logout;
    FirebaseAuth auth;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        auth = FirebaseAuth.getInstance();
        email =findViewById(R.id.email);
        btnAttendance=findViewById(R.id.btnAttendance);
        btnImage=findViewById(R.id.btnImage);
        btnAlarm=findViewById(R.id.btnAlarm);
        btnList=findViewById(R.id.btnList);
        logout=findViewById(R.id.logout);


        user = auth.getCurrentUser();
        if(user==null){
            Intent intent = new Intent(getApplicationContext(),Login.class);
            startActivity(intent);
            finish();
        }
        else {
            email.setText(user.getEmail());
        }

        btnAttendance.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),UploadImage.class);
                startActivity(intent);
                finish();
            }
        }
        ));


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
                                        FirebaseAuth.getInstance().signOut();
                                        Intent intent = new Intent(getApplicationContext(),Select.class);
                                        startActivity(intent);
                                        finish();
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
}