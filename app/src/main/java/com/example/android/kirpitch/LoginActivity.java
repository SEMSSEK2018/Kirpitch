package com.example.android.kirpitch;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText loginEmail;
    private TextInputEditText loginPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        setContentView(R.layout.activity_login);

        loginEmail = findViewById(R.id.e_mailLA);
        loginPassword = findViewById(R.id.passwordLA);
        Button loginButton = findViewById(R.id.button_loginLA);
        Button registerButton = findViewById(R.id.button_registerLA);

        firebaseAuth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });

        final FirebaseAuth finalFirebaseAuth = firebaseAuth;
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Objects.requireNonNull(loginEmail.getText()).toString();
                final String password = Objects.requireNonNull(loginPassword.getText()).toString();

                if (TextUtils.isEmpty(email)) {
                    Toast
                            .makeText(getApplicationContext()
                                    , "Fill in the email!", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast
                            .makeText(getApplicationContext()
                                    , "Fill in the password!", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                finalFirebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this
                                , new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (!task.isSuccessful()) {
                                    Log.d("----: fail", "failed");
                                    if (password.length() < 6) {
                                        loginPassword
                                                .setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast
                                                .makeText(LoginActivity.this
                                                        , getString(R.string.auth_failed)
                                                        , Toast.LENGTH_LONG)
                                                .show();
                                    }
                                } else {
                                    Intent intent =
                                            new Intent(LoginActivity.this
                                                    , MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });
    }
}
