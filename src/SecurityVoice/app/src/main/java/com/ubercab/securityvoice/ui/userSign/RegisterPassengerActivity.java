package com.ubercab.securityvoice.ui.userSign;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.ubercab.entities.SystemAtributes;
import com.ubercab.entities.User;
import com.ubercab.securityvoice.MainActivity;
import com.ubercab.securityvoice.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterPassengerActivity extends AppCompatActivity {

    private TextInputEditText passengerNameEditText;
    private TextInputEditText passengerLastNameEditText;
    private TextInputEditText mainIdentificatorRegisterPassenger;
    private TextInputEditText passwordRegisterPassenger;
    private AppCompatButton registerPassengerButton;
    private TextView alreadyHaveAccountRegisterPassenger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_passenger);

        passengerNameEditText = findViewById(R.id.passengerNameEditText);
        passengerLastNameEditText = findViewById(R.id.passengerLastNameEditText);
        mainIdentificatorRegisterPassenger = findViewById(R.id.mainIdentificatorRegisterPassenger);
        passwordRegisterPassenger = findViewById(R.id.passwordRegisterPassenger);
        registerPassengerButton = findViewById(R.id.registerPassengerButton);
        alreadyHaveAccountRegisterPassenger = findViewById(R.id.alreadyHaveAccountRegisterPassenger);

        registerPassengerButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                User user = new User(
                        passengerNameEditText.getText().toString(),
                        passengerLastNameEditText.getText().toString(),
                        mainIdentificatorRegisterPassenger.getText().toString(),
                        passwordRegisterPassenger.getText().toString()
                );
                registerPassenger(user);
            }
        });

        alreadyHaveAccountRegisterPassenger.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(1);
            }
        }));
    }

    public void registerPassenger(User user){
        Call<User> call = SystemAtributes.apiService.registerPassenger(user);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Usuário cadastrado com sucesso!", Toast.LENGTH_LONG);
                    SystemAtributes.user = response.body();
                    changeActivity(0);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"Falha ao cadastrar usuário!", Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                Toast.makeText(getApplicationContext(),"Erro: Servidor não responde", Toast.LENGTH_LONG);
            }
        });
    }

    public void changeActivity(int changeCode){
        if(changeCode == 0){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }else if(changeCode == 1){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}