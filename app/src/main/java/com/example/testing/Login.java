package com.example.testing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class Login extends AppCompatActivity {
    EditText editTextEmail, editTextPassword;
    Button btnLogin;
    ProgressBar progressBar;
    TextView textView;
    FirebaseAuth mAuth;
    ImageButton btnChangeToVi;
    ImageButton btnChangeToEn;
    LanguageManager languageManager = LanguageManager.getInstance();

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            Intent intent = new Intent(getApplicationContext(), Select.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.btnLogin);
        textView = findViewById(R.id.registerNow);
        progressBar = findViewById(R.id.progressBar);
        btnChangeToVi = findViewById(R.id.btnVietnamese);
        btnChangeToEn = findViewById(R.id.btnEnglish);

        //đặt default language là VN
        languageManager.setCurrentLanguageCode("vi");

        //click button VN
        btnChangeToVi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                languageManager.setCurrentLanguageCode("vi");
                setLanguage("vi");
                recreate();
//                startActivity(new Intent(Login.this,Login.class));
            }
        });

        //click button EN
        btnChangeToEn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                languageManager.setCurrentLanguageCode("en");
                setLanguage("en");
                recreate();
//                startActivity(new Intent(Login.this,Login.class));
            }
        });

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
                    Toast.makeText(Login.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(Login.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(Login.this, "Đăng nhập thành công !",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), Select.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.

                                    Toast.makeText(Login.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });
    }

    private void setLanguage(String languageCode) {
        Log.d("Language", "Setting language to: " + languageCode);

        Resources resources = this.getResources();
        Configuration configuration = resources.getConfiguration();

        // Tạo đối tượng Locale với mã ngôn ngữ được chọn
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        configuration.setLocale(locale);

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

}