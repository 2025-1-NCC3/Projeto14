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

    //Variáveis de cada elemento da Activity

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

        //Instânciando variáveis com seus respectivos elementos da Activity

        passengerNameEditText = findViewById(R.id.passengerNameEditText);
        passengerLastNameEditText = findViewById(R.id.passengerLastNameEditText);
        mainIdentificatorRegisterPassenger = findViewById(R.id.mainIdentificatorRegisterPassenger);
        passwordRegisterPassenger = findViewById(R.id.passwordRegisterPassenger);
        registerPassengerButton = findViewById(R.id.registerPassengerButton);
        alreadyHaveAccountRegisterPassenger = findViewById(R.id.alreadyHaveAccountRegisterPassenger);

        registerPassengerButton.setOnClickListener(new View.OnClickListener(){ //Se o botão de CADASTRAR for clicado

            @Override
            public void onClick(View v) {
                User user = new User( //Constrói um objeto da classe User com os valores dos campos
                        passengerNameEditText.getText().toString(),
                        passengerLastNameEditText.getText().toString(),
                        mainIdentificatorRegisterPassenger.getText().toString(),
                        passwordRegisterPassenger.getText().toString()
                );
                registerPassenger(user);//Registra o usuário no banco de dados e no aplicativo
            }
        });

        alreadyHaveAccountRegisterPassenger.setOnClickListener((new View.OnClickListener() { //Ao clicar no TextView "Já tenho uma conta"
            @Override
            public void onClick(View v) {
                changeActivity(1);
            }//Voltar à Activity de Login
        }));
    }

    public void registerPassenger(User user){//Função de registrar usuário
        Call<User> call = SystemAtributes.apiService.registerPassenger(user);//Instância a variável call com uma chamada da função registerPassanger da API

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {//Executa a requisição da chamada call, que faz uma requisição assíncrona através da função enqueue, que tem como prâmetro um objeto anônimo Callback que espera como resposta um objeto User
                if(response.isSuccessful()){//Se a resposta for bem sucedida
                    Toast.makeText(getApplicationContext(),"Usuário cadastrado com sucesso!", Toast.LENGTH_LONG).show();
                    SystemAtributes.user = response.body();//O usuário atual recebe o usuário recebido da API (a API FAZ INSERT INTO DO USUÁRIO E SELECT DELE MESMO PARA RETORNAR O CORPO DO OBJETO JAVA COM A PRIMARY KEY DO REGISTRO DO USUÁRIO)

                    //Como o usuário é um passageiro, os campos de usuário motorista são respondidos com null pelo Banco de dados
                    SystemAtributes.user.setCpf("NaN");
                    SystemAtributes.user.setRg("NaN");
                    SystemAtributes.user.setGender("NaN");
                    SystemAtributes.user.setMonthBirthday("NaN");
                    //Preencher com NaN se faz necessário, pois a api feita em Python não suporta
                    //receber um objeto com atributos null

                    changeActivity(0);//Mudar para a Activity principal do aplicativo
                    finish();//Finalizar a Activity atual
                }else{//Se não
                    Toast.makeText(getApplicationContext(),"Falha ao cadastrar usuário!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {//Quando não há resposta
                Toast.makeText(getApplicationContext(),"Erro: Servidor não responde", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void changeActivity(int changeCode){ //Função para mudar a activity atual
        if(changeCode == 0){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);//Mudar para a Activity principal do aplicativo
            finish();
        }else if(changeCode == 1){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent); //Mudar para a Activity de Login
            finish();
        }
    }
}