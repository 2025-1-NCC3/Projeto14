package com.ubercab.securityvoice.ui.userSign;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.android.material.textfield.TextInputEditText;
import com.ubercab.entities.SystemAtributes;
import com.ubercab.entities.User;
import com.ubercab.securityvoice.MainActivity;
import com.ubercab.securityvoice.R;


public class LoginActivity extends AppCompatActivity {

    private TextInputEditText mainIdentificadorLogin;
    private TextInputEditText passwordLogin;
    private AppCompatButton loginButton;
    private AppCompatButton registerButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mainIdentificadorLogin = findViewById(R.id.mainIdentificatorLogin);
        passwordLogin = findViewById(R.id.passwordLogin);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User(mainIdentificadorLogin.getText().toString(),
                        passwordLogin.getText().toString());
                getLoginUser(user);

            }
        });
        registerButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                changeActivity(1);
            }
        });
    }

    public void getLoginUser(User user){ //Função que executa o login do usuário
        Call<User> call = SystemAtributes.apiService.loginUser(user);//Guarda a chamada da função dentro da variável call

        call.enqueue(new Callback<User>() {//Executa a requisição da chamada call, que faz uma requisição assíncrona através da função enqueue, que tem como prâmetro um objeto anônimo Callback que espera como resposta um objeto User
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    SystemAtributes.user = response.body();

                    if(SystemAtributes.user.getId() == 0) { //Login ou senha inválidos
                        Toast.makeText(getApplicationContext(), response.body().getName(), Toast.LENGTH_LONG).show();
                    }else{//Usuário logado com sucesso
                        Toast.makeText(getApplicationContext(), "Usuário logado com sucesso!", Toast.LENGTH_LONG).show();
                        changeActivity(0);
                    }

                }else{
                    Toast.makeText(getApplicationContext(),"Falha ao logar!",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                Toast.makeText(getApplicationContext(),"Erro: Servidor não responde",Toast.LENGTH_LONG).show();
            }
        });


    }

    public void changeActivity(int changeCode){
        if(changeCode == 0){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }else if(changeCode == 1){
            Intent intent = new Intent(getApplicationContext(), ChooseAccountActivity.class);
            startActivity(intent);
        }
    }
}