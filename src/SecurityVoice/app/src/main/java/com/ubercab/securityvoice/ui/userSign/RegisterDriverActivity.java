package com.ubercab.securityvoice.ui.userSign;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.ubercab.entities.SystemAtributes;
import com.ubercab.entities.User;
import com.ubercab.exceptions.LengthCPFException;
import com.ubercab.exceptions.LengthPhoneNumberException;
import com.ubercab.exceptions.LengthRGException;
import com.ubercab.securityvoice.MainActivity;
import com.ubercab.securityvoice.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterDriverActivity extends AppCompatActivity {

    private TextInputEditText driverNameEditText;
    private TextInputEditText driverLastNameEditText;
    private TextInputEditText driverCPFEditText;
    private TextInputEditText driverRGEditText;
    private TextInputEditText mainIdentificatorRegisterDriver;
    private TextInputEditText passwordRegisterDriver;
    private Spinner dayDateSpinner;
    private Spinner monthDateSpinner;
    private Spinner yearDateSpinner;
    private RadioButton maleRadioButton;
    private RadioButton femaleRadioButton;
    private AppCompatButton registerDriverButton;
    private TextView alreadyHaveAccountRegisterDriver;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_driver);

        driverNameEditText = findViewById(R.id.driverNameEditText);
        driverLastNameEditText = findViewById(R.id.driverLastNameEditText);
        driverCPFEditText = findViewById(R.id.driverCPFEditText);
        driverRGEditText = findViewById(R.id.driverRGEditText);
        mainIdentificatorRegisterDriver = findViewById(R.id.mainIdentificadorRegisterDriver);
        passwordRegisterDriver = findViewById(R.id.passwordRegisterDriver);

        dayDateSpinner = findViewById(R.id.dayDateSpinner);
        monthDateSpinner = findViewById(R.id.monthDateSpinner);
        yearDateSpinner = findViewById(R.id.yearDateSpinner);

        maleRadioButton = findViewById(R.id.maleRadioButton);
        femaleRadioButton = findViewById(R.id.femaleRadioButton);
        femaleRadioButton.setChecked(true);

        registerDriverButton = findViewById(R.id.registerDriverButton);

        alreadyHaveAccountRegisterDriver = findViewById(R.id.alreadyHaveAccountRegisterDriver);

        registerDriverButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                String driverName = driverNameEditText.getText().toString();
                String driverLastName = driverLastNameEditText.getText().toString();
                String driverMainIdentificador = mainIdentificatorRegisterDriver.getText().toString();
                String driverCPF =  driverCPFEditText.getText().toString();
                String driverRG = driverRGEditText.getText().toString();
                String[] filter = driverMainIdentificador.split("@");
                String driverPassword = passwordRegisterDriver.getText().toString();
                int driverDayDate = Integer.parseInt(dayDateSpinner.getSelectedItem().toString());
                String driverMouthDate = monthDateSpinner.getSelectedItem().toString();
                int driverYearDate = Integer.parseInt(yearDateSpinner.getSelectedItem().toString());

                String gender = null;
                if(maleRadioButton.isChecked()){
                    gender = "male";
                }else{
                    gender = "female";
                }

                try {
                    if (driverCPF.length() != 11) {
                        throw new LengthCPFException("CPF inválido");
                    }

                    if (driverRG.length() <= 7 || driverRG.length() >= 12) {
                        throw new LengthRGException("RG inválido");
                    }

                    if (filter.length == 1) {
                        if (driverMainIdentificador.length() != 11 && driverMainIdentificador.length() != 12) {
                            throw new LengthPhoneNumberException("Número de celular inválido");
                        }
                    }
                    User user = new User(

                            driverName,
                            driverLastName,
                            driverMainIdentificador,
                            driverPassword,
                            driverCPF,
                            driverRG,
                            gender,
                            driverDayDate,
                            driverMouthDate,
                            driverYearDate
                    );
                    registerUser(user);
                }catch (LengthCPFException e){
                    Toast.makeText(getApplicationContext(),"CPF inválido",Toast.LENGTH_LONG).show();
                }catch (LengthRGException e){
                    Toast.makeText(getApplicationContext(), "RG inválido", Toast.LENGTH_SHORT).show();
                }catch(LengthPhoneNumberException e){
                    Toast.makeText(getApplicationContext(), "Número de celular inválido", Toast.LENGTH_SHORT).show();
                }

            }
        });

        alreadyHaveAccountRegisterDriver.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                changeActivity(1);
            }
        });
    }

    public void registerUser(User user){
        Call<User> call = SystemAtributes.apiService.registerDriver(user);

        call.enqueue(new Callback<User>(){

            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Usuário cadastrado com sucesso!",Toast.LENGTH_LONG).show();
                    SystemAtributes.user = response.body();
                    changeActivity(0);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Falha ao cadastrar usuário!",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "Erro: Servidor não responde",Toast.LENGTH_LONG).show();
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