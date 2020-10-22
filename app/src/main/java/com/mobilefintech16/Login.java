package com.mobilefintech16;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private ImageButton mRegisterAccounts;
    private ImageButton mAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items_login);

        mRegisterAccounts = findViewById(R.id.imageButton);
        mRegisterAccounts.setOnClickListener(this);

        mAvatar = findViewById(R.id.avatar_removable);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageButton:
                startActivity(new Intent(this, RegistrationActivity.class));
                break;
            //test app
        }
    }
}