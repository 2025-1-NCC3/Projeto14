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
import com.ubercab.securityvoice.MainActivity;
import com.ubercab.securityvoice.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterDriverActivity extends AppCompatActivity {

    //Variáveis de cada elemento da Activity

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

        //Instânciando variáveis com seus respectivos elementos da Activity

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

        //----------------------------------------

        registerDriverButton.setOnClickListener(new View.OnClickListener(){ //Se o botão de CADASTRAR for clicado

            @Override
            public void onClick(View v) {
                String gender = null;
                if(maleRadioButton.isChecked()){ //Define o gênero selecionado
                    gender = "male";
                }else{
                    gender = "female";
                }

                User user = new User( //Constrói um objeto da classe User com os valores dos campos
                    driverNameEditText.getText().toString(),
                    driverLastNameEditText.getText().toString(),
                    mainIdentificatorRegisterDriver.getText().toString(),
                    passwordRegisterDriver.getText().toString(),
                    driverCPFEditText.getText().toString(),
                    driverRGEditText.getText().toString(),
                    gender,
                    Integer.parseInt(dayDateSpinner.getSelectedItem().toString()),
                    monthDateSpinner.getSelectedItem().toString(),
                    Integer.parseInt(yearDateSpinner.getSelectedItem().toString())
                );

                registerUser(user); //Registra o usuário no banco de dados e no aplicativo

            }
        });

        alreadyHaveAccountRegisterDriver.setOnClickListener(new View.OnClickListener(){ //Ao clicar no TextView "Já tenho uma conta"

            @Override
            public void onClick(View v) {
                changeActivity(1);
            } //Voltar à Activity de Login
        });
    }

    public void registerUser(User user){ //Função de registrar usuário
        Call<User> call = SystemAtributes.apiService.registerDriver(user); //Instância a variável call com uma chamada da função registerDriver da API

        call.enqueue(new Callback<User>(){//Executa a requisição da chamada call, que faz uma requisição assíncrona através da função enqueue, que tem como prâmetro um objeto anônimo Callback que espera como resposta um objeto User

            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){//Se a resposta for bem sucedida
                    Toast.makeText(getApplicationContext(), "Usuário cadastrado com sucesso!",Toast.LENGTH_LONG).show();
                    SystemAtributes.user = response.body();//O usuário atual recebe o usuário recebido da API (a API FAZ INSERT INTO DO USUÁRIO E SELECT DELE MESMO PARA RETORNAR O CORPO DO OBJETO JAVA COM A PRIMARY KEY DO REGISTRO DO USUÁRIO)
                    changeActivity(0); //Mudar para a Activity principal do aplicativo
                    finish();//Finalizar a Activity atual
                }else{//Se não
                    Toast.makeText(getApplicationContext(), "Falha ao cadastrar usuário!",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {//Quando não há resposta
                Toast.makeText(getApplicationContext(), "Erro: Servidor não responde",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void changeActivity(int changeCode){//Função para mudar a activity atual
        if(changeCode == 0){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);//Mudar para tela principal do aplicativo
            finish();//Finalizar a Activity atual
        }else if(changeCode == 1){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);//Mudar para a tela de login
            finish();//Finalizar a Activity atual
        }
    }
}