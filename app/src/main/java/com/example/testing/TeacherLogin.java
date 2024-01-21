package com.example.testing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TeacherLogin extends AppCompatActivity {
    EditText editTextEmail, editTextPassword;
    Button btnLogin;
    ProgressBar progressBar;
    TextView textView;
    FirebaseAuth mAuth;

//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null){
//            Intent intent = new Intent(getApplicationContext(), Select.class);
//            startActivity(intent);
//            finish();
//        }
//    }


//    @Override
//    public void onStart() {
//        super.onStart();
//
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//
//        if (currentUser != null) {
//            String userId = currentUser.getUid();
//
//            // Dựa vào userId, xác định vai trò
//            if ("f4OGHI3BqmY6LkVeUkMjoaRp3D22".equals(userId)) {
//                Intent intent = new Intent(getApplicationContext(), TeacherDashboard.class);
//                startActivity(intent);
//                finish();
//            } else {
//                Toast.makeText(TeacherLogin.this, "Sai tài khoản/ mật khẩu", Toast.LENGTH_LONG).show();
//                finish();
//            }
//        }
//    }


//    @Override
//    public void onStart() {
//        super.onStart();
//
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//
//        if (currentUser != null) {
//            String userId = currentUser.getUid();
//
//            // Dựa vào userId, xác định vai trò
//            if ("f4OGHI3BqmY6LkVeUkMjoaRp3D22".equals(userId)) {
//                Intent intent = new Intent(getApplicationContext(), TeacherDashboard.class);
//                startActivity(intent);
//                finish();
//            } else {
//                Intent intent = new Intent(getApplicationContext(), StudentDashboard.class);
//                startActivity(intent);
//                finish();
//            }
//        }
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword=findViewById(R.id.editTextPassword);
        btnLogin=findViewById(R.id.btnLogin);
        textView=findViewById(R.id.registerNow);
        progressBar=findViewById(R.id.progressBar);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
                finish();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String email, password;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(TeacherLogin.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(TeacherLogin.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
//

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    FirebaseUser currentUser = mAuth.getCurrentUser();
                                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("teacher").child(currentUser.getUid());

                                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                String role = dataSnapshot.child("role").getValue(String.class);
                                                // Dựa vào giá trị của vai trò, chuyển hướng đến màn hình phù hợp
                                                if ("teacher".equals(role)) {
                                                    Intent intent = new Intent(getApplicationContext(), TeacherDashboard.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else if ("student".equals(role)) {
                                                    Toast.makeText(TeacherLogin.this, "Sai tài khoản/ mật khẩu", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            // Xử lý khi có lỗi đọc từ cơ sở dữ liệu
                                        }
                                    });
                                } else {
                                    Toast.makeText(TeacherLogin.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}