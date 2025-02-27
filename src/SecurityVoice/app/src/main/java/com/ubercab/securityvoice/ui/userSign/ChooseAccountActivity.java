package com.ubercab.securityvoice.ui.userSign;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ubercab.securityvoice.MainActivity;
import com.ubercab.securityvoice.R;

public class ChooseAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_choose_account);

        //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        //startActivity(intent);
    }
}