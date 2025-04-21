package br.fecap.pi.securityvoice.securityvoice.ui.userSign;

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
import br.fecap.pi.securityvoice.criptography.Criptography;
import br.fecap.pi.securityvoice.entities.SystemAtributes;
import br.fecap.pi.securityvoice.entities.User;
import br.fecap.pi.securityvoice.exceptions.LengthPhoneNumberException;
import br.fecap.pi.securityvoice.securityvoice.MainActivity;
import br.fecap.pi.securityvoice.R;


public class LoginActivity extends AppCompatActivity {

    private TextInputEditText mainIdentificadorLogin; //Barra de inserir email e telefone
    private TextInputEditText passwordLogin; //Barra de inserir a senha da conta
    private AppCompatButton loginButton; // Botão acessar
    private AppCompatButton registerButton; // Botão cadastrar-se

    private AppCompatButton testProfileButton;

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

        //Instânciando variáveis de elementos da activity

        mainIdentificadorLogin = findViewById(R.id.mainIdentificatorLogin);
        passwordLogin = findViewById(R.id.passwordLogin);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        testProfileButton = findViewById(R.id.testProfileButton);

        testProfileButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                SystemAtributes.user = new User("Barrigudo", "da Silva", "barrigudo@gmail.com","1234","788910150560","718592312","male","10","Fevereiro","1915");
                changeActivity(0);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {//Ao clicar no botão acessar
            @Override
            public void onClick(View v) {

                //Declarando variáveis com os valores de preenchimento dos campos

                String mainIdentificator = mainIdentificadorLogin.getText().toString();
                String password = passwordLogin.getText().toString();

                //Tratando a exceção de login
                String[] filter = mainIdentificator.split("@");
                try {
                    if (filter.length == 1) {//Se o principal identificador de usuário (e-mail ou telefone) for um número de telefone
                        if (mainIdentificator.length() != 12 && mainIdentificadorLogin.length() != 11) { //Se o número de telefone não tiver a quantidade de dígitos correta
                            throw new LengthPhoneNumberException("Número de Celular Inválido");
                        }
                    }

                    User user = new User(mainIdentificator, password);
                    getLoginUser(user);
                }catch(LengthPhoneNumberException e){
                    Toast.makeText(getApplicationContext(), "Número de Celular Inválido",Toast.LENGTH_LONG).show();
                }
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener(){ //Ao clicar no botão cadastrar

            @Override
            public void onClick(View v) {
                changeActivity(1);} //Mude para a activity ChooseAccountActivity (para escolher o tipo de usuário que será cadastrado (motorista ou passageiro)

        });
    }

    public void getLoginUser(User user){ //Função que executa o login do usuário

        User userCrypt = Criptography.userCriptography(user);

        Call<User> call = SystemAtributes.apiService.loginUser(userCrypt);//Guarda a chamada da função dentro da variável call

        call.enqueue(new Callback<User>() {//Executa a requisição da chamada call, que faz uma requisição assíncrona através da função enqueue, que tem como prâmetro um objeto anônimo Callback que espera como resposta um objeto User
            @Override
            public void onResponse(Call<User> call, Response<User> response) { //Ao responder
                if(response.isSuccessful()){ //Se a resposta foi bem sucedida
                    SystemAtributes.user = Criptography.userDecrypt(response.body());

                    //Caso seja um usuário passageiro, os campos de usuário motorista são respondidos com null pelo Banco de dados
                    if(SystemAtributes.user.getCpf() == null){//Se o usuário for um passageiro
                        SystemAtributes.user.setCpf("NaN");
                        SystemAtributes.user.setRg("NaN");
                        SystemAtributes.user.setGender("NaN");
                        SystemAtributes.user.setMonthBirthday("NaN");
                    }
                    //Preencher com NaN se faz necessário, pois a api feita em Python não suporta
                    //receber um objeto com atributos null


                    if(SystemAtributes.user.getId().equals("0")) { //Login ou senha inválidos
                        Toast.makeText(getApplicationContext(), "Usuário não encontrado", Toast.LENGTH_LONG).show(); //Usuário não encontrado
                    }else{//Usuário logado com sucesso
                        Toast.makeText(getApplicationContext(), "Usuário logado com sucesso!", Toast.LENGTH_LONG).show();
                        changeActivity(0); //Muda para a MainActivity
                    }

                }else{
                    Toast.makeText(getApplicationContext(),"Falha ao logar!",Toast.LENGTH_LONG).show(); //Se a resposta não for bem sucedida
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) { //Se a api não responder
                Toast.makeText(getApplicationContext(),"Erro: Servidor não responde",Toast.LENGTH_LONG).show();
            }
        });


    }

    public void changeActivity(int changeCode){//Função para mudar a activity atual
        if(changeCode == 0){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class); //Mudar para tela do principal do aplicativo
            startActivity(intent);
            finish();
        }else if(changeCode == 1){
            Intent intent = new Intent(getApplicationContext(), ChooseAccountActivity.class); //Mudar para tela de escolha de tipo de cadastro
            startActivity(intent);
        }
    }
}