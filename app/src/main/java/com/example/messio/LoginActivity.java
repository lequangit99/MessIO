package com.example.messio;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private Button loginButton, phoneLoginButton;
    private EditText UserEmail, UserPass;
    private TextView NewAccLink, ForgetPWLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        InitializeFields();

        NewAccLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserToRegisterActivity();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllowUserToLogin();
            }
        });

        phoneLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent phoneLoginIntent = new Intent(LoginActivity.this, PhoneLoginActivity.class);
                startActivity(phoneLoginIntent);
            }
        });

    }

    private void AllowUserToLogin() {
        String email = UserEmail.getText().toString();
        String pass = UserPass.getText().toString();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Nhập email...", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(pass)){
            Toast.makeText(this, "Nhập mật khẩu...", Toast.LENGTH_SHORT).show();
        }
        else {

            progressDialog.setTitle("Đăng nhập");
            progressDialog.setMessage("Đợi một chút...");
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        SendUserToMainActivity();
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                    else {
                        String message = Objects.requireNonNull(task.getException()).toString();
                        Toast.makeText(LoginActivity.this, "Error : " + message, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            });
        }
    }

    private void InitializeFields() {
        loginButton = findViewById(R.id.login_button);
        phoneLoginButton = findViewById(R.id.phone_login_button);
        UserEmail = findViewById(R.id.login_email);
        UserPass = findViewById(R.id.login_password);
        NewAccLink = findViewById(R.id.need_new_account_link);
        ForgetPWLink = findViewById(R.id.forget_password_link);
        progressDialog = new ProgressDialog(this);
    }


    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
    private void SendUserToRegisterActivity() {
        Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(registerIntent);
    }
}
