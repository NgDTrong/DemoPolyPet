package com.example.demopolypet;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {

    TextView txt_signin;
    EditText edt_name, edt_email, edt_password, edt_repeatpassword;
    AppCompatButton btn_signup;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        auth = FirebaseAuth.getInstance();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        txt_signin = findViewById(R.id.txt_signin);
        edt_name = findViewById(R.id.edt_name);
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);
        edt_repeatpassword = findViewById(R.id.edt_repeatpassword);
        btn_signup = findViewById(R.id.btn_signup);
        txt_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name, email, password, password_again;
                name = edt_name.getText().toString().trim();
                email = edt_email.getText().toString().trim();
                password = edt_password.getText().toString().trim();
                password_again = edt_repeatpassword.getText().toString().trim();

                // Check for empty fields
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(password_again)) {
                    Toast.makeText(SignupActivity.this, "Nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validate each field
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(SignupActivity.this, "Chưa nhập tên", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(email)) {
                    Toast.makeText(SignupActivity.this, "Chưa nhập email", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(SignupActivity.this, "Chưa nhập mật khẩu", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(password_again)) {
                    Toast.makeText(SignupActivity.this, "Chưa nhập nhắc lại mật khẩu", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!validateName(name)) {
                    Toast.makeText(SignupActivity.this, "Tên không đúng định dạng", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!isValidEmail(email)) {
                    Toast.makeText(SignupActivity.this, "Nhập đúng định dạng mail", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!password.equals(password_again)) {
                    Toast.makeText(SignupActivity.this, "Mật khẩu không trùng nhau", Toast.LENGTH_SHORT).show();
                    return;
                } else if (password.length() < 6) {
                    Toast.makeText(SignupActivity.this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
                    return;
                }

                // If all validations pass, proceed with user creation
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            Toast.makeText(SignupActivity.this, "Đăng kí thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(SignupActivity.this, "Đăng kí thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    public boolean validateName(String name) {
        // Biểu thức chính quy cho phép chỉ chứa chữ và khoảng trắng
        String regex = "^[a-zA-Z ]+$";
        return name.matches(regex);
    }

    // Hàm kiểm tra định dạng email
    private boolean isValidEmail(String email) {
        // Điều kiện: email phải có định dạng hợp lệ (ví dụ: định dạng tương tự email của Google)
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
