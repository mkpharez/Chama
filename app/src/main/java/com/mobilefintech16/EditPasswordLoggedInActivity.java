package com.mobilefintech16;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditPasswordLoggedInActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String PASSWORD = "password";
    private static final String REGISTEREDMEMBER = "registeredMember";
    EditText mPassword, mConfirmPassword;
    Button mSavePassword;
    TextView mEmailAccount;
    DatabaseReference reference_DB;
    private String mPasswordchanges;
    private FirebaseDatabase mDatabase;
    private ProgressBar mProgressBar;
    private String mAccountPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password_logged_in);

        mPassword = findViewById(R.id.change_password);
        mConfirmPassword = findViewById(R.id.confirm_change_password);
        mSavePassword = findViewById(R.id.button_save);
        mProgressBar = findViewById(R.id.progressBar2);
        mEmailAccount = findViewById(R.id.email_account);

        mSavePassword.setOnClickListener(this);

        mDatabase = FirebaseDatabase.getInstance();
        reference_DB = mDatabase.getReference(REGISTEREDMEMBER);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_save:
                validateAndContinue();
                break;
        }
    }

    private void validateAndContinue() {
        mPasswordchanges = mPassword.getText().toString().trim();
        if (mPasswordchanges.isEmpty() | mPasswordchanges.length() < 6) {
            mPassword.setError("minimum characters is 6 !");
            mPassword.requestFocus();
            return;
        }
        setChangesToFirebase();
    }

    private void setChangesToFirebase() {
        mSavePassword.setEnabled(true);
        mAccountPhone = getIntent().getStringExtra("phoneNumber");

        reference_DB
                .child(mAccountPhone)
                .child(PASSWORD)
                .setValue(mPasswordchanges);
        Toast.makeText(getApplicationContext(), "changes have been successfully saved", Toast.LENGTH_SHORT)
                .show();

        startActivity(new Intent(getApplicationContext(), LogInActivity.class));


    }

}