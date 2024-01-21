package com.example.testing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;

public class SelectRole extends AppCompatActivity {
    private RadioGroup radioGroupRoles;
    private Button btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_role);

        radioGroupRoles = findViewById(R.id.radioGroupRoles);
        btnContinue = findViewById(R.id.btnContinue);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedRoleId = radioGroupRoles.getCheckedRadioButtonId();

                if (selectedRoleId == -1) {
                    // Nếu người dùng không chọn vai trò, có thể hiển thị thông báo hoặc xử lý khác
                    // Ở đây, chúng ta chỉ in log
                    System.out.println("Please select a role");
                    return;
                }

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
}

