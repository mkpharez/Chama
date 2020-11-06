package com.mobilefintech16;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int CODE_VERIFY_CONSTANT = 6;
    private final String TAG = "TAG";
    FirebaseAuth mAuth;
    private Button mSendPhoneVerification;
    private ImageButton mCancelBackRetrival;
    ProgressBar mProgressBar;
    CountryCodePicker mCodePicker;
    String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mToken;
    Boolean mIsVerifying = false;
    private EditText mVerifyPhoneAccount, mCodeNum;
    private String mRealPhoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mVerifyPhoneAccount = findViewById(R.id.verify_number_account);
        mSendPhoneVerification = findViewById(R.id.send_number_verify);
        mCodeNum = findViewById(R.id.enter_verification_code);
        mCodePicker = findViewById(R.id.country_code_picker);
        mProgressBar = findViewById(R.id.retrieve_progress_bar);

        TextView retrieveInstruction = findViewById(R.id.retrieve_instruction);
        TextView retrieveBoldView = findViewById(R.id.retrieve_bold_description);
        ImageView configIconEngine = findViewById(R.id.config_icon);

        mAuth = FirebaseAuth.getInstance();

        mSendPhoneVerification.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send_number_verify:
                ValidateNumberInput();
                break;
        }
    }

    private void ValidateNumberInput() {
        String phoneNumber = mVerifyPhoneAccount.getText().toString().trim();
        if(!mIsVerifying) {
            if (!phoneNumber.isEmpty() && phoneNumber.length() == 9) {
                mRealPhoneNumber = "+" + mCodePicker.getSelectedCountryCode()
                        + phoneNumber;
                Log.d(TAG, "Validated-Phone No. Is ->" + mRealPhoneNumber);
                mProgressBar.setVisibility(View.VISIBLE);
                VerifyPhoneOnFirebase(mRealPhoneNumber);

            } else {
                mVerifyPhoneAccount.setError("Invalid Phone Number");
            }
        } else {
            mSendPhoneVerification.setEnabled(false);
            String userOTPCode = mCodeNum.getText().toString();
            if(!userOTPCode.isEmpty() && userOTPCode.length() == CODE_VERIFY_CONSTANT){
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId,userOTPCode);
                verifyAuthentication(credential);
            }else{
                mCodeNum.setError("Must Enter Code Sent To Your Phone Number!!");
                mSendPhoneVerification.setEnabled(true);
            }
        }
    }

    private void verifyAuthentication(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Toast.makeText(ForgotPasswordActivity.this, "Verification Successful!!", Toast.LENGTH_SHORT)
                                .show();
                        startToEditPassword();
                    }else{
                        Toast.makeText(ForgotPasswordActivity.this, "Error !!" + task.getException().getMessage(), Toast.LENGTH_SHORT)
                                .show();
                        mSendPhoneVerification.setEnabled(true);
                    }
                });
    }

    private void startToEditPassword() {
        Intent phone_passwordIntent = new Intent
                (getApplicationContext(),EditPasswordLoggedInActivity.class);
        phone_passwordIntent.putExtra("phoneNumber", mRealPhoneNumber);
        startActivity(phone_passwordIntent);
        finish();

    }

    private void VerifyPhoneOnFirebase(String realPhoneNumber) {
        PhoneAuthProvider.getInstance()
                .verifyPhoneNumber(realPhoneNumber, 60L, TimeUnit.SECONDS, this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        mProgressBar.setVisibility(View.GONE);
                        mCodeNum.setVisibility(View.VISIBLE);
                        mVerificationId = s;
                        mToken = forceResendingToken;

                        mSendPhoneVerification.setText("Verify");

                        mIsVerifying = true;
                    }

                    @Override
                    public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                        super.onCodeAutoRetrievalTimeOut(s);
                        Toast.makeText(ForgotPasswordActivity.this, "Code expired" + s, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(ForgotPasswordActivity.this, "Cannot Verify Account" +e.getMessage(), Toast.LENGTH_SHORT)
                                .show();
                        mProgressBar.setVisibility(View.GONE);

                    }
                });
    }
    private void backToLogIn() { startActivity( new Intent(this, LogInActivity.class)); }
}