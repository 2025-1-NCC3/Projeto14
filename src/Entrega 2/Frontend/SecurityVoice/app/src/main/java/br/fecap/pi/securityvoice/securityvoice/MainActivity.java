package br.fecap.pi.securityvoice.securityvoice;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import br.fecap.pi.securityvoice.R;
import br.fecap.pi.securityvoice.entities.SystemAtributes;
import br.fecap.pi.securityvoice.resources.SpeechRecognizerClass;
import br.fecap.pi.securityvoice.databinding.ActivityMainBinding;
import br.fecap.pi.securityvoice.securityvoice.ui.system.AccountInformationFragment;
import br.fecap.pi.securityvoice.securityvoice.ui.system.MapFragment;
import br.fecap.pi.securityvoice.securityvoice.ui.system.ProfileFragment;
import br.fecap.pi.securityvoice.securityvoice.ui.system.SecurityFragment;
import br.fecap.pi.securityvoice.securityvoice.ui.system.SecurityVoiceConfigurationFragment;
import br.fecap.pi.securityvoice.securityvoice.ui.system.TravelFragment;
import br.fecap.pi.securityvoice.securityvoice.ui.userSign.LoginActivity;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_RECORD_AUDIO = 1; // Código para requisição de permissão de áudio
    private ActivityMainBinding binding;
    private MenuItem travelMenu, securityMenu, profileMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Configurando a barra de navegação inferior

        BottomNavigationView navView = findViewById(R.id.nav_view);

        travelMenu = navView.getMenu().findItem(R.id.navigation_travel);
        securityMenu = navView.getMenu().findItem(R.id.navigation_security);
        profileMenu = navView.getMenu().findItem(R.id.navigation_profile);

        //----------------------------------------------



        speechPermissionConditional(); //Pedir permissão ao usuário para que o aplicativo possa usar seu microfone

        changeFragment(new TravelFragment());//Selecionar o Fragmento de viagens como primeiro fragmento aberto quando o usuário entra na Activity principal do aplicativo

        travelMenu.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() { //Se o botão travelMenu da bottom navigation for clicado
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {

                changeFragment(new TravelFragment()); //Mudar para o Fragmento de viagens

                return false;
            }
        });

        securityMenu.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() { //Se o botão securityMenu da bottom navigation for clicado
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {

                changeFragment(new SecurityFragment()); //Mudar para o Fragmento de configurações de segurança

                return false;
            }
        });



        profileMenu.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() { //Se o botão profileMenu da bottom navigation for clicado
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {

                changeFragment(new ProfileFragment()); //Mudar para o fragmento com informações e configurações da conta e do sistema
                return false;
            }
        });


    }

    public void changeFragment(Fragment fragment){ //Função para mudar fragmentos
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction(); //Instânciando um objeto de transação de fragments

        transaction.replace(R.id.nav_host_fragment_activity_main,fragment); //Definindo quando layout receberá o fragmento e sobrescrevendo o layout atual
        transaction.commit();//Efetuando alterações da transação
    }

    public void travelButton(View view){ //onClick do Botão Viajar do TravelFragment
        changeFragment(new MapFragment());
    }

    public void securityVoiceConfigurationButton(View view){ //onClick do Botão SecurityVoice do SecurityFragment
        changeFragment(new SecurityVoiceConfigurationFragment());
    }

    public void accountInformationButton(View view){ //onClick do Botão Informações da Conta do ProfileFragment
        changeFragment(new AccountInformationFragment());
    }

    public void exitButton(View view){ //onClick do botão Sair do Profile Fragment
        AlertDialog.Builder dialog = new AlertDialog.Builder(this); //Abrindo um builder de alertDialog

        dialog.setTitle("Sair"); //Definindo o título do alertDialog
        dialog.setMessage("Tem certeza de que deseja sair da sua conta?"); //Definindo a mensagem do alertDialog

        dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() { //Definindo uma opção "Sim" do alertDialog
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SystemAtributes.user = null; //Reiniciando o usuário do sistema
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent); //Voltando para a tela de Login
                finish();
            }
        });
        dialog.setNegativeButton("Não", new DialogInterface.OnClickListener() { //Definindo uma opção "Não" do alertDialog
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dialog.create(); //Criando o alertDialog a partir do builder
        dialog.show(); //Exibindo o AlertDialog

    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        SpeechRecognizerClass.speechRecognizerDestroy();//Desligar reconhecimento por voz
    }

    public void speechPermissionConditional(){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            // Solicita permissão ao usuário se ainda não foi concedida
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO);
        }
    }

    // Método chamado quando o usuário responde à solicitação de permissão
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    public void cancelSecurityVoiceConfigurationButton(View view){
        changeFragment(new SecurityFragment());
    }

    public void cancelButton(View view){
        changeFragment(new ProfileFragment());
    }//onClick do Botão Voltar do AccountInformationFragment
}