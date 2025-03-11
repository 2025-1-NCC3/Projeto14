package com.ubercab.securityvoice.ui.system;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.ubercab.entities.SystemAtributes;
import com.ubercab.entities.User;
import com.ubercab.exceptions.LengthCPFException;
import com.ubercab.exceptions.LengthPhoneNumberException;
import com.ubercab.exceptions.LengthRGException;
import com.ubercab.securityvoice.MainActivity;
import com.ubercab.securityvoice.R;
import com.ubercab.securityvoice.ui.userSign.LoginActivity;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountInformationFragment extends Fragment {


    public AccountInformationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;

        if(SystemAtributes.user.getCpf() != null){ //Se o usuário for um Motorista
            view = inflater.inflate(R.layout.fragment_account_information_driver, container, false);

            TextInputEditText profileNameEditText = view.findViewById(R.id.profileNameEditText);
            TextInputEditText profileLastNameEditText = view.findViewById(R.id.profileLastNameEditText);
            TextInputEditText profileCpfEditText = view.findViewById(R.id.profileCPFEditText);
            TextInputEditText profileRgEditText = view.findViewById(R.id.profileRGEditText);

            Spinner dayDateSpinner = view.findViewById(R.id.AccountInformation_dayDateSpinner);
            Spinner monthDateSpinner = view.findViewById(R.id.AccountInformation_monthDateSpinner);
            Spinner yearDateSpinner = view.findViewById(R.id.AccountInformation_yearDateSpinner);

            RadioButton femaleRadioButton = view.findViewById(R.id.femaleRadioButton);
            RadioButton maleRadioButton = view.findViewById(R.id.maleRadioButton);

            TextInputEditText mainIdentificatorEditText = view.findViewById(R.id.mainIdentificatorProfile);
            TextInputEditText passwordEditText = view.findViewById(R.id.passwordProfile);

            AppCompatButton updateButton = view.findViewById(R.id.updateButton);
            AppCompatButton cancelButton = view.findViewById(R.id.cancelButton);
            AppCompatButton deleteButton = view.findViewById(R.id.deleteButton);

            updateRegisterUserDriver(view);

            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        String UserPhoneNumber = mainIdentificatorEditText.getText().toString();
                        String[] Fil = mainIdentificatorEditText.getText().toString().split("@");
                        String UserCPF = profileCpfEditText.getText().toString();
                        String UserRG = profileRgEditText.getText().toString();
                        if(Fil.length == 1){
                            if(UserPhoneNumber.length() != 11 || UserPhoneNumber.length() != 12){
                                throw new LengthPhoneNumberException("Numero invalido");
                            }
                        }
                        if(UserCPF.length() != 11){
                            throw new LengthCPFException("CPF invalido");
                        }
                        if(UserRG.length() <= 7 || UserRG.length() >=12 ){
                            throw new LengthRGException("RG incalido");
                        }


                        User user = SystemAtributes.user;
                        User userOriginal = new User(user.getName(),user.getLastName(),
                                user.getEmail(),user.getPhoneNumber(),user.getPassword(),
                                user.getCpf(),user.getRg(), user.getDayBirthday(),user.getMonthBirthday(),
                                user.getYearBirthday());
                        userOriginal.setId(user.getId());

                        user.setName(profileNameEditText.getText().toString());
                        user.setLastName(profileLastNameEditText.getText().toString());
                        user.setCpf(profileCpfEditText.getText().toString());
                        user.setRg(profileRgEditText.getText().toString());

                        user.setDayBirthday(Integer.parseInt(dayDateSpinner.getSelectedItem().toString()));
                        user.setMonthBirthday(monthDateSpinner.getSelectedItem().toString());
                        user.setYearBirthday(Integer.parseInt(yearDateSpinner.getSelectedItem().toString()));

                        if(maleRadioButton.isChecked()){
                            user.setGender("male");
                        }else{
                            user.setGender("female");
                        }

                        String[] filter = mainIdentificatorEditText.getText().toString().split("@");//Auxilia na identificação da diferença entre número de telefone e usuário inserido no mainIdentificator

                        if(filter.length > 1){//Para alterar o e-mail
                            user.setEmail(mainIdentificatorEditText.getText().toString());
                        }else{//Para alterar o número de telefone

                            user.setPhoneNumber(mainIdentificatorEditText.getText().toString());
                        }

                        user.setPassword(passwordEditText.getText().toString());

                        Call<User> call = SystemAtributes.apiService.updateUserDriver(user);

                        call.enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if(response.isSuccessful()){
                                    Toast.makeText(getActivity().getApplicationContext(), "Usuário atualizado com sucesso!",Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(getActivity().getApplicationContext(), "Falha ao atualizar usuário!",Toast.LENGTH_LONG).show();
                                    SystemAtributes.user = userOriginal;
                                    updateRegisterUserDriver(view);
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable throwable) {
                                Toast.makeText(getActivity().getApplicationContext(), "Erro: Servidor não responde",Toast.LENGTH_LONG).show();
                                SystemAtributes.user = userOriginal;
                            }
                        });
                    }catch(LengthPhoneNumberException e){
                        Toast.makeText(getActivity().getApplicationContext(), "Numero de Celular invalido", Toast.LENGTH_SHORT).show();

                    }
                    catch(LengthCPFException e){
                        Toast.makeText(getActivity().getApplicationContext(), "CPF invalido", Toast.LENGTH_SHORT).show();
                    }
                    catch(LengthRGException e){
                        Toast.makeText(getActivity().getApplicationContext(), "RG invalido", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialogDeleteUser(SystemAtributes.apiService.deleteUserDriver(SystemAtributes.user));
                }
            });

        }else{ //Se o usuário for um Passageiro
            view = inflater.inflate(R.layout.fragment_account_information_passenger, container, false);

            TextInputEditText profileNameEditText = view.findViewById(R.id.profileNameEditText);
            TextInputEditText profileLastNameEditText = view.findViewById(R.id.profileLastNameEditText);
            TextInputEditText mainIdentificatorProfile = view.findViewById(R.id.mainIdentificatorProfile);
            TextInputEditText passwordProfile = view.findViewById(R.id.passwordProfile);

            AppCompatButton updateButton = view.findViewById(R.id.updateButton);
            AppCompatButton cancelButton = view.findViewById(R.id.cancelButton);
            AppCompatButton deleteButton = view.findViewById(R.id.deleteButton);

            updateRegisterUserPassenger(view);

            updateButton.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    try {
                        String UserPhoneNumber = mainIdentificatorProfile.getText().toString();
                        String[] Fil = mainIdentificatorProfile.getText().toString().split("@");

                        if (Fil.length == 1) {
                            if (UserPhoneNumber.length() != 11 || UserPhoneNumber.length() != 12) {
                                throw new LengthPhoneNumberException("Numero de Celular invalido");
                            }
                        }
                        User user = SystemAtributes.user;
                        User userOriginal = new User(user.getId(),user.getName(),user.getLastName(),user.getEmail(),user.getPhoneNumber(),user.getPassword());

                        user.setName(profileNameEditText.getText().toString());
                        user.setLastName(profileLastNameEditText.getText().toString());

                        String[] filter = mainIdentificatorProfile.getText().toString().split("@");//Auxilia na identificação da diferença entre número de telefone e usuário inserido no mainIdentificator

                        if (filter.length > 1) {//Para alterar o e-mail
                            user.setEmail(mainIdentificatorProfile.getText().toString());
                        } else {//Para alterar o número de telefone
                            user.setPhoneNumber(mainIdentificatorProfile.getText().toString());
                        }

                        user.setPassword(passwordProfile.getText().toString());

                        Call<User> call = SystemAtributes.apiService.updateUserPassenger(user);

                        call.enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(getActivity().getApplicationContext(), "Usuário atualizado com sucesso!", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getActivity().getApplicationContext(), "Falha ao atualizar usuário!", Toast.LENGTH_LONG).show();
                                    SystemAtributes.user = userOriginal;
                                    updateRegisterUserPassenger(view);
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable throwable) {
                                Toast.makeText(getActivity().getApplicationContext(), "Erro: Servidor não responde", Toast.LENGTH_LONG).show();
                                SystemAtributes.user = userOriginal;
                            }
                        });
                    }catch (LengthPhoneNumberException e){
                        Toast.makeText(getActivity().getApplicationContext(), "Numero de celular invalido", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    alertDialogDeleteUser(SystemAtributes.apiService.deleteUserPassenger(SystemAtributes.user));
                }
            });
        }



        return view;
    }

    public void updateRegisterUserDriver(View view){
        TextInputEditText profileNameEditText = view.findViewById(R.id.profileNameEditText);
        TextInputEditText profileLastNameEditText = view.findViewById(R.id.profileLastNameEditText);
        TextInputEditText profileCpfEditText = view.findViewById(R.id.profileCPFEditText);
        TextInputEditText profileRgEditText = view.findViewById(R.id.profileRGEditText);

        Spinner dayDateSpinner = view.findViewById(R.id.AccountInformation_dayDateSpinner);
        Spinner monthDateSpinner = view.findViewById(R.id.AccountInformation_monthDateSpinner);
        Spinner yearDateSpinner = view.findViewById(R.id.AccountInformation_yearDateSpinner);

        RadioButton femaleRadioButton = view.findViewById(R.id.femaleRadioButton);
        RadioButton maleRadioButton = view.findViewById(R.id.maleRadioButton);

        TextInputEditText mainIdentificatorEditText = view.findViewById(R.id.mainIdentificatorProfile);
        TextInputEditText passwordEditText = view.findViewById(R.id.passwordProfile);

        User user = SystemAtributes.user;

        profileNameEditText.setText(user.getName().toString());
        profileLastNameEditText.setText(user.getLastName().toString());
        profileCpfEditText.setText(user.getCpf().toString());
        profileRgEditText.setText(user.getRg().toString());

        dayDateSpinner.setSelection(user.getDayBirthday() - 1);

        ArrayAdapter<String> monthDateSpinnerAdapter = (ArrayAdapter<String>) monthDateSpinner.getAdapter();
        int position = monthDateSpinnerAdapter.getPosition(user.getMonthBirthday().toString());
        monthDateSpinner.setSelection(position);

        yearDateSpinner.setSelection(user.getYearBirthday() - 1900);

        if(user.getGender().equals("male")){
            maleRadioButton.setChecked(true);
        }else{
            femaleRadioButton.setChecked(true);
        }

        if(user.getEmail() != null && !user.getEmail().equals("NaN")){
            mainIdentificatorEditText.setText(user.getEmail().toString());
        }else{
            mainIdentificatorEditText.setText(user.getPhoneNumber().toString());
        }

        passwordEditText.setText(user.getPassword());
    }

    public void updateRegisterUserPassenger(View view){
        TextInputEditText profileNameEditText = view.findViewById(R.id.profileNameEditText);
        TextInputEditText profileLastNameEditText = view.findViewById(R.id.profileLastNameEditText);
        TextInputEditText mainIdentificatorProfile = view.findViewById(R.id.mainIdentificatorProfile);
        TextInputEditText passwordProfile = view.findViewById(R.id.passwordProfile);

        AppCompatButton updateButton = view.findViewById(R.id.updateButton);
        AppCompatButton deleteButton = view.findViewById(R.id.deleteButton);

        User user = SystemAtributes.user;

        profileNameEditText.setText(user.getName().toString());
        profileLastNameEditText.setText(user.getLastName().toString());

        if(user.getEmail() != null && !user.getEmail().equals("NaN")){
            mainIdentificatorProfile.setText(user.getEmail().toString());
        }else{
            mainIdentificatorProfile.setText(user.getPhoneNumber().toString());
        }

        passwordProfile.setText(user.getPassword().toString());
    }
    public void alertDialogDeleteUser(Call<User> call){
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());

        dialog.setTitle("Deletar Conta");
        dialog.setMessage("Tem certeza de que deseja apagar sua conta?");

        dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(getActivity().getApplicationContext(), "Usuário deletado com sucesso!",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }else{
                            Toast.makeText(getActivity().getApplicationContext(), "Falha ao deletar usuário!",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable throwable) {
                        Toast.makeText(getActivity().getApplicationContext(), "Erro: Servidor não responde",Toast.LENGTH_LONG).show();
                    }
                });
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
}