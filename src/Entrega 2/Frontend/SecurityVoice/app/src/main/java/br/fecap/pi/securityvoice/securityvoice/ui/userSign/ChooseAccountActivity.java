package br.fecap.pi.securityvoice.securityvoice.ui.userSign;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import br.fecap.pi.securityvoice.R;

public class ChooseAccountActivity extends AppCompatActivity {
    //Activity para escolher o tipo de cadastro de usuário (Morista ou Passageiro?)


    //Variáveis com os elementos do fragment

    private AppCompatButton driverButton;
    private AppCompatButton passengerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_choose_account);

        //Instânciando variáveis

        driverButton = findViewById(R.id.driverButton);
        passengerButton = findViewById(R.id.passengerButton);

        driverButton.setOnClickListener(new View.OnClickListener() { //Se o botão do motorista for selecionado
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterDriverActivity.class);
                startActivity(intent); //Abra a Activity de cadastro de motorista
                finish(); // Encerre a Activity atual
            }
        });

        passengerButton.setOnClickListener(new View.OnClickListener() { //Se o botão do passageiro for selecionado
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterPassengerActivity.class);
                startActivity(intent); //Abra a Activity de cadastro de passageiro
                finish(); // Encerre a Activity atual
            }
        });
    }
}