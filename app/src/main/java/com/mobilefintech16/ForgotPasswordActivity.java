package com.mobilefintech16;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    public EditText mVerifyPhoneAccount;
    private Button mSendPhoneVerification;
    private ImageButton mCancelBackRetrival;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mVerifyPhoneAccount = findViewById(R.id.verify_number_account);
        mSendPhoneVerification = findViewById(R.id.send_number_verify);
        mCancelBackRetrival = findViewById(R.id.back_login_page);
        TextView retrievInstruction = findViewById(R.id.retrieve_instruction);
        TextView retrievBoldView = findViewById(R.id.retrieve_bold_description);
        ImageView configIconEngine = findViewById(R.id.config_icon);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send_number_verify:
                ValidateNumberInput();
                break;
            case R.id.back_login_page:
                backToLogIn();
                break;
        }
    }

    private void ValidateNumberInput() {
        String phoneNumber = mVerifyPhoneAccount.getText().toString().trim();
        if (phoneNumber.isEmpty()) {
            mVerifyPhoneAccount.setError("Provide Your Phone Number");
            mVerifyPhoneAccount.requestFocus();
            return;
        }
        VerifyPhoneOnFirebase(phoneNumber);

    }

    private void VerifyPhoneOnFirebase(String phoneNumber) {
        final String completePhoneNumber = "+" + "254" + phoneNumber;
        Query checkUserNumber = FirebaseDatabase
                .getInstance()
                .getReference("Member")
                .orderByChild("phoneNumber")
                .equalTo(completePhoneNumber);
        checkUserNumber.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void CodeNumberActivity() { startActivity( new Intent(this, CodeNumberVerificationActivity.class)); }

    private void backToLogIn() { startActivity( new Intent(this, LogInActivity.class)); }
}