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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private Button CreateAccountButton;
    private EditText UserEmail, UserPassword;
    private TextView AlreadyHaveAccountLink;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private DatabaseReference RootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        InitializeFields();

        mAuth = FirebaseAuth.getInstance();

        RootRef = FirebaseDatabase.getInstance().getReference();

        AlreadyHaveAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserToLoginActivity();
            }
        });

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNewAcc();
            }
        });
    }

    private void CreateNewAcc() {
        String email = UserEmail.getText().toString();
        String pass = UserPassword.getText().toString();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Nhập email...", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(pass)){
            Toast.makeText(this, "Nhập mật khẩu...", Toast.LENGTH_SHORT).show();
        }
        else {

            progressDialog.setTitle("Creating New Account");
            progressDialog.setMessage("Please wait, while we wre creating new account for you...");
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        String currentUserID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                        RootRef.child("Users").child(currentUserID).setValue("");

                        Toast.makeText(RegisterActivity.this, "Account Created Successfully...", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        SendUserToMainActivity();
                    }
                    else {
                        String message = Objects.requireNonNull(task.getException()).toString();
                        Toast.makeText(RegisterActivity.this, "Error : " + message, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            });
        }
    }


    private void InitializeFields() {
        CreateAccountButton = findViewById(R.id.register_button);
        UserEmail = findViewById(R.id.register_email);
        UserPassword = findViewById(R.id.register_password);
        AlreadyHaveAccountLink = findViewById(R.id.already_have_account_link);
        Context context;
        progressDialog = new ProgressDialog(this);
    }

    private void SendUserToLoginActivity() {
        Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(loginIntent);
    }

    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}
