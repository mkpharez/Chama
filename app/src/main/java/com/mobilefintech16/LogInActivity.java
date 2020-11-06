package com.mobilefintech16;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton mRegisterAccounts;
    private Button mSignIn;
    private TextView mUserName;
    private TextView mPassword;
    private FirebaseAuth mAuth;
    private ProgressBar mProgressBar;
    private CheckedTextView mRetrievePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mRegisterAccounts = findViewById(R.id.new_user);
        mRegisterAccounts.setOnClickListener(this);

        mRetrievePassword = findViewById(R.id.forgot_password);
        mRetrievePassword.setOnClickListener(this);

        mSignIn = findViewById(R.id.log_in_button);
        mSignIn.setOnClickListener(this);

        mUserName = findViewById(R.id.user_name);
        mPassword = findViewById(R.id.enter_password);

        mProgressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
            finish();
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.new_user:
                registrationActivity();
                break;
            case R.id.log_in_button:
                signIn();
                break;
            case R.id.forgot_password:
                forgotPassword();
                break;
        }
    }

    private void signIn() {
        String userName = mUserName.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        validateInputs(userName, password);
    }

    private void validateInputs(String userName, String password) {
        if (userName.isEmpty()) {
            mUserName.setError("Email is Required");
            mUserName.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(userName).matches()) {
            mUserName.setError("Enter valid email !");
            mUserName.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            mPassword.setError("Password is Required !");
            mPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            mPassword.setError("min characters should be 6 ");
            mPassword.requestFocus();
            return;
        }
        signInWithFirebase(userName,password);
    }

    private void signInWithFirebase(String userName, String password) {
        mProgressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(userName,password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                startActivity(new Intent(LogInActivity.this, UserProfileActivity.class));
            }else{
                Toast.makeText(LogInActivity.this, "Error !" + task.getException().getMessage(),
                        Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void registrationActivity() { startActivity(new Intent(this, RegistrationActivity.class)); }

    private void forgotPassword() { startActivity(new Intent(this,ForgotPasswordActivity.class)); }
}