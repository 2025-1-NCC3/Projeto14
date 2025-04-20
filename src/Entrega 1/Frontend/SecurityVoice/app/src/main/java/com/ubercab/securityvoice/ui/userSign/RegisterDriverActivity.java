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
import com.ubercab.criptography.Criptography;
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

                //Declarando as variáveis que recebem os valores dos campos

                String driverName = driverNameEditText.getText().toString();
                String driverLastName = driverLastNameEditText.getText().toString();
                String driverMainIdentificador = mainIdentificatorRegisterDriver.getText().toString();
                String driverCPF =  driverCPFEditText.getText().toString();
                String driverRG = driverRGEditText.getText().toString();
                String[] filter = driverMainIdentificador.split("@");
                String driverPassword = passwordRegisterDriver.getText().toString();
                String driverDayDate = dayDateSpinner.getSelectedItem().toString();
                String driverMouthDate = monthDateSpinner.getSelectedItem().toString();
                String driverYearDate = yearDateSpinner.getSelectedItem().toString();

                String gender = null;
                if(maleRadioButton.isChecked()){ //Define o gênero selecionado
                    gender = "male";
                }else{
                    gender = "female";
                }

                //Tratando exceções de preenchimento dos campos
                try {
                    if (driverCPF.length() != 11) { //Se o cpf não tiver a quantidade de dígitos correta
                        throw new LengthCPFException("CPF inválido");
                    }

                    if (driverRG.length() <= 7 || driverRG.length() >= 12) { //Se o rg não tiver a quantidade de dígitos correta
                        throw new LengthRGException("RG inválido");
                    }

                    if (filter.length == 1) {//Se o principal identificador de usuário (e-mail ou telefone) for um número de telefone
                        if (driverMainIdentificador.length() != 11 && driverMainIdentificador.length() != 12) { //Se o número de celular não tiver a quantidade de dígitos correta
                            throw new LengthPhoneNumberException("Número de celular inválido");
                        }
                    }
                    User user = new User( //Constrói um objeto da classe User com os valores dos campos

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
                    registerUser(user); //Registra o usuário no banco de dados e no aplicativo
                }catch (LengthCPFException e){
                    Toast.makeText(getApplicationContext(),"CPF inválido",Toast.LENGTH_LONG).show();
                }catch (LengthRGException e){
                    Toast.makeText(getApplicationContext(), "RG inválido", Toast.LENGTH_SHORT).show();
                }catch(LengthPhoneNumberException e){
                    Toast.makeText(getApplicationContext(), "Número de celular inválido", Toast.LENGTH_SHORT).show();
                }

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

        User userCrypt = Criptography.userCriptography(user);

        Call<User> call = SystemAtributes.apiService.registerDriver(userCrypt); //Instância a variável call com uma chamada da função registerDriver da API

        call.enqueue(new Callback<User>(){//Executa a requisição da chamada call, que faz uma requisição assíncrona através da função enqueue, que tem como prâmetro um objeto anônimo Callback que espera como resposta um objeto User

            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){//Se a resposta for bem sucedida
                    Toast.makeText(getApplicationContext(), "Usuário cadastrado com sucesso!",Toast.LENGTH_LONG).show();
                    SystemAtributes.user = Criptography.userDecrypt(response.body());//O usuário atual recebe o usuário recebido da API (a API FAZ INSERT INTO DO USUÁRIO E SELECT DELE MESMO PARA RETORNAR O CORPO DO OBJETO JAVA COM A PRIMARY KEY DO REGISTRO DO USUÁRIO
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