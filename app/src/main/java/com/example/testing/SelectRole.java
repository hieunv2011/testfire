package com.example.testing;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class SelectRole extends AppCompatActivity {
    private RadioGroup radioGroupRoles;
    private Button btnContinue;
    ImageButton btnChangeToVi;
    ImageButton btnChangeToEn;
    LanguageManager languageManager = LanguageManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_role);

        radioGroupRoles = findViewById(R.id.radioGroupRoles);
        btnContinue = findViewById(R.id.btnContinue);
        btnChangeToVi = findViewById(R.id.btnVietnamese);
        btnChangeToEn = findViewById(R.id.btnEnglish);

        //đặt default language là VN
//        languageManager.setCurrentLanguageCode("vi");

//        click button VN
        btnChangeToVi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                languageManager.setCurrentLanguageCode("vi");
                setLanguage("vi");
                recreate();
            }
        });

        //click button EN
        btnChangeToEn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                languageManager.setCurrentLanguageCode("en");
                setLanguage("en");
                recreate();
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.d("SelectRole", "Button Continue Clicked");
                int selectedRoleId = radioGroupRoles.getCheckedRadioButtonId();
//                if (selectedRoleId == -1) {
//                    // Nếu người dùng không chọn vai trò, có thể hiển thị thông báo hoặc xử lý khác
//                    // Ở đây, chúng ta chỉ in log
//                    System.out.println("Please select a role");
//                    return;
//                }
                RadioButton selectedRadioButton = findViewById(selectedRoleId);
                String selectedRole = selectedRadioButton.getText().toString();

                // Chuyển hướng đến màn hình đăng nhập phù hợp với vai trò đã chọn
                Intent loginIntent;
                if ("Giáo viên".equals(selectedRole)) {
                    loginIntent = new Intent(SelectRole.this, TeacherLogin.class);
                } else if ("Học sinh".equals(selectedRole)) {
                    loginIntent = new Intent(SelectRole.this, StudentLogin.class);
                } else {
                    // Xử lý trường hợp không xác định được vai trò (nên xử lý tùy thuộc vào yêu cầu của bạn)
                    System.out.println("Unknown role: " + selectedRole);
                    return;
                }
                startActivity(loginIntent);
                finish();
            }
        });
    }

    private void setLanguage(String languageCode) {
        Resources resources = this.getResources();
        Configuration configuration = resources.getConfiguration();

        // Tạo đối tượng Locale với mã ngôn ngữ được chọn
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        configuration.setLocale(locale);

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }
}

