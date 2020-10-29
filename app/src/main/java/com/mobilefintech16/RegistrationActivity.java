package com.mobilefintech16;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {


    private CheckedTextView mBackToLogin;
    private Button mRegisterAccount;
    DatabaseReference mDatabaseReference;
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mEmailAccount;
    private EditText mNewPassword;
    private EditText mConfirmPassword;
    private ProgressBar mProgressBar;
    private EditText mPhoneNumber;
    private EditText mIdNumber;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items_registration);

        mBackToLogin = findViewById(R.id.login_page);
        mBackToLogin.setOnClickListener(this);

        mRegisterAccount = findViewById(R.id.button_register);
        mRegisterAccount.setOnClickListener(this);

        mFirstName = findViewById(R.id.first_name);
        mLastName = findViewById(R.id.last_name);
        mEmailAccount = findViewById(R.id.email_address);
        mNewPassword = findViewById(R.id.new_password);
        mConfirmPassword = findViewById(R.id.rewrite_password);
        mPhoneNumber = findViewById(R.id.phone_number);
        mProgressBar = findViewById(R.id.progressBar);
        mIdNumber = findViewById(R.id.id_number);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Member");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_page:
                logInActivity();
                break;
            case R.id.button_register:
                createUserAccount();
                break;

        }
    }

    private void logInActivity() {
        startActivity(new Intent(this, LogInActivity.class));
    }

    private void createUserAccount() {
        String email = mEmailAccount.getText().toString().trim();
        String firstName = mFirstName.getText().toString().trim();
        String lastName = mLastName.getText().toString().trim();
        String id = mIdNumber.getText().toString().trim();
        String password = mNewPassword.getText().toString().trim();
        String confirmPassword = mConfirmPassword.getText().toString().trim();
        String phoneNumber = mPhoneNumber.getText().toString().trim();

        charactersAuthenticator(email, firstName, lastName, id, password, confirmPassword, phoneNumber);
    }

    private void charactersAuthenticator
            (String email, String firstName, String lastName, String id, String password, String confirmPassword, String phoneNumber) {
        if (firstName.isEmpty()) {
            mFirstName.setError("First name is Required !");
            mFirstName.requestFocus();
            return;
        }

        if (lastName.isEmpty()) {
            mLastName.setError("Last name is Required !");
            mLastName.requestFocus();
            return;
        }

        if (phoneNumber.isEmpty()) {
            mPhoneNumber.setError("Provide Your Phone Number");
            mPhoneNumber.requestFocus();
            return;
        }

        if (id.isEmpty()) {
            mIdNumber.setError("Provide Your National ID/Passport");
            mIdNumber.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            mEmailAccount.setError("Email is Required");
            mEmailAccount.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailAccount.setError("Enter valid email !");
            mEmailAccount.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            mNewPassword.setError("Password is Required !");
            mNewPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            mNewPassword.setError("min characters should be 6 ");
            mNewPassword.requestFocus();
            return;
        }

        if (confirmPassword.isEmpty()) {
            mConfirmPassword.setError("Confirm Password !");
            mConfirmPassword.requestFocus();
            return;
        }

        if (confirmPassword.length() < 6) {
            mConfirmPassword.setError("min characters should be 6 ");
            mConfirmPassword.requestFocus();
            return;
        }
        mProgressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            ChamaMember member = new ChamaMember(
                                    firstName,
                                    lastName,
                                    id,
                                    email,
                                    phoneNumber
                            );
                            mDatabaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(member)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegistrationActivity.this, "Registration Successful", Toast.LENGTH_SHORT)
                                                .show();
                                        mProgressBar.setVisibility(View.VISIBLE);
                                        startActivity(new Intent(getApplicationContext(), LogInActivity.class));
                                    }else{
                                        Toast.makeText(RegistrationActivity.this, "Error !" + task.getException().getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                        mProgressBar.setVisibility(View.GONE);

                                    }
                                }
                            });

                        }else{

                            Toast.makeText(RegistrationActivity.this, "Error !" + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

}