package com.ubercab.securityvoice;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.ubercab.entities.SystemAtributes;
import com.ubercab.entities.User;
import com.ubercab.securityvoice.databinding.ActivityMainBinding;
import com.ubercab.securityvoice.ui.system.AccountInformationFragment;
import com.ubercab.securityvoice.ui.system.ProfileFragment;
import com.ubercab.securityvoice.ui.system.TravelFragment;
import com.ubercab.securityvoice.ui.userSign.LoginActivity;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MenuItem travelMenu, securityMenu, profileMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);

        NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_activity_main);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);


    }

    public void changeFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.nav_host_fragment_activity_main,fragment);

        transaction.commit();
    }

    public void accountInformationButton(View view){
        changeFragment(new AccountInformationFragment());
    }

    public void exitButton(View view){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setTitle("Sair");
        dialog.setMessage("Tem certeza de que deseja sair da sua conta?");

        dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SystemAtributes.user = null;
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        dialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dialog.create();
        dialog.show();

    }

    public void cancelButton(View view){
        changeFragment(new ProfileFragment());
    }
}