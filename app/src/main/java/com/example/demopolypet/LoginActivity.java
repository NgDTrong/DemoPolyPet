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
import androidx.appcompat.app.AlertDialog;
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

public class LoginActivity extends AppCompatActivity {
    EditText edt_email, edt_password;
    TextView txt_forgot, txt_signup, txt_google;
    AppCompatButton btn_login;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);
        txt_forgot = findViewById(R.id.txt_forgot);
        txt_signup = findViewById(R.id.txt_signup);
        txt_google = findViewById(R.id.txt_google);
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edt_email.getText().toString().trim();
                String password = edt_password.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(LoginActivity.this, "Nhập email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Nhập password", Toast.LENGTH_SHORT).show();
                    return;
                }
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = auth.getCurrentUser();
                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        txt_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEmailDialog();
            }
        });

    }
    private void showEmailDialog(){
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        View view= getLayoutInflater().inflate(R.layout.dialog_email,null);
        EditText edt_email=view.findViewById(R.id.edt_forgotemail);
        AppCompatButton btn_sendotp=view.findViewById(R.id.btn_sendotp);
        builder.setView(view);
        AlertDialog dialog= builder.create();
        btn_sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=edt_email.getText().toString().trim();
                if (TextUtils.isEmpty(email)){
                    edt_email.setError("Chưa nhập email");
                    return;
                }
                auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            dialog.dismiss();
                            showOtpDialog();
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "Lỗi gửi mail", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        dialog.show();
    }
    private void showOtpDialog(){
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        View view= getLayoutInflater().inflate(R.layout.dialog_otp,null);
        EditText edt_otp= view.findViewById(R.id.edt_otp);
        AppCompatButton btn_verify= view.findViewById(R.id.btn_verify);
        builder.setView(view);
        AlertDialog dialog= builder.create();
        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp=edt_otp.getText().toString().trim();
                if (TextUtils.isEmpty(otp)){
                    edt_otp.setError("Chưa nhập otp");
                    return;
                }
                dialog.dismiss();
                showChangePasswordDialog();
            }
        });
        dialog.show();
    }
    private void showChangePasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_password, null);
        EditText edt_newpass = view.findViewById(R.id.edt_newpass);
        AppCompatButton btn_change = view.findViewById(R.id.btn_changepass);

        builder.setView(view);
        AlertDialog dialog = builder.create();

        btn_change.setOnClickListener(v -> {
            String newpass = edt_newpass.getText().toString().trim();
            if (TextUtils.isEmpty(newpass)) {
                edt_newpass.setError("Chưa nhập mật khẩu.");
                return;
            }
            auth.getCurrentUser().updatePassword(newpass).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Đổi mật khẩu thành công.", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    Toast.makeText(LoginActivity.this, "Error changing password.", Toast.LENGTH_SHORT).show();
                }
            });
        });

        dialog.show();
    }
}

