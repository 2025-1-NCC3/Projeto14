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

        if(SystemAtributes.user.getCpf() != null && SystemAtributes.user.getCpf() != "NaN"){ //Se o usuário for um Motorista
            view = inflater.inflate(R.layout.fragment_account_information_driver, container, false); //Salva o inflate do fragmento de informações de conta do usuário motorista (será retornado ao final do método onCreateView)

            //----------------Instânciando elementos do fragment-----------------------

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

            //-----------------------------------------------------------------

            updateRegisterUserDriver(view); //Preenche os campos do fragment com as informações do motorista

            //------------------Configurando ações dos botões---------------------------

            updateButton.setOnClickListener(new View.OnClickListener() { //Ao clicar no botão atualizar usuário
                @Override
                public void onClick(View v) {

                    User user = SystemAtributes.user; //Salva o endereço do usuário na memória dentro da variável user
                    User userOriginal = new User(user.getName(),user.getLastName(), //Cria outro objeto idêntico ao usuário atual dentro da variável userOriginal
                            user.getEmail(),user.getPhoneNumber(),user.getPassword(),
                            user.getCpf(),user.getRg(), user.getDayBirthday(),user.getMonthBirthday(),
                            user.getYearBirthday());
                    userOriginal.setId(user.getId());

                    //-------------Atualiza os dados do usuário motorista-------------

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

                    //--------------------------------------------------------------

                    Call<User> call = SystemAtributes.apiService.updateUserDriver(user); //Cria uma chamada para o endereço da função updateUserDriver da API

                    call.enqueue(new Callback<User>() { //Configurando resposta ao retorno da requisição
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) { //Quando há resposta
                            if(response.isSuccessful()){ //Se a resposta foi bem sucedida
                                Toast.makeText(getActivity().getApplicationContext(), "Usuário atualizado com sucesso!",Toast.LENGTH_LONG).show();
                            }else{//Se não
                                Toast.makeText(getActivity().getApplicationContext(), "Falha ao atualizar usuário!",Toast.LENGTH_LONG).show();
                                SystemAtributes.user = userOriginal;//Retoma a antiga forma do usuário
                                updateRegisterUserDriver(view); //Preenche os campos do fragment com as informações do motorista
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable throwable) { //Quando não há resposta
                            Toast.makeText(getActivity().getApplicationContext(), "Erro: Servidor não responde",Toast.LENGTH_LONG).show();
                            SystemAtributes.user = userOriginal; //Retoma a antiga forma do usuário
                        }
                    });
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {//Ao clicar no botão deletar usuário
                @Override
                public void onClick(View v) {
                    alertDialogDeleteUser(SystemAtributes.apiService.deleteUserDriver(SystemAtributes.user)); //Gera um alertDialog para o usuário, perguntando-lhe se deseja deletar sua conta de motorista
                }
            });

            //----------------------------------------------------------------------------------

        }else{ //Se o usuário for um Passageiro
            view = inflater.inflate(R.layout.fragment_account_information_passenger, container, false); //Salva o inflate do fragmento de informações de conta do usuário passageiro (será retornado ao final do método onCreateView)

            //----------------Instânciando elementos do fragment-----------------------

            TextInputEditText profileNameEditText = view.findViewById(R.id.profileNameEditText);
            TextInputEditText profileLastNameEditText = view.findViewById(R.id.profileLastNameEditText);
            TextInputEditText mainIdentificatorProfile = view.findViewById(R.id.mainIdentificatorProfile);
            TextInputEditText passwordProfile = view.findViewById(R.id.passwordProfile);

            AppCompatButton updateButton = view.findViewById(R.id.updateButton);
            AppCompatButton cancelButton = view.findViewById(R.id.cancelButton);
            AppCompatButton deleteButton = view.findViewById(R.id.deleteButton);

            //----------------------------------------------------------

            updateRegisterUserPassenger(view);//Preenche os campos do fragment com as informações do passageiro

            //------------------Configurando ações dos botões---------------------------

            updateButton.setOnClickListener(new View.OnClickListener(){//Ao clicar no botão atualizar usuári

                @Override
                public void onClick(View v) {
                    User user = SystemAtributes.user; //Salva o endereço do usuário na memória dentro da variável user
                    User userOriginal = new User(user.getId(),user.getName(),user.getLastName(),//Cria outro objeto idêntico ao usuário atual dentro da variável userOriginal
                            user.getEmail(),user.getPhoneNumber(),user.getPassword());


                    //-------------Atualiza os dados do usuário motorista-------------

                    user.setName(profileNameEditText.getText().toString());
                    user.setLastName(profileLastNameEditText.getText().toString());

                    String[] filter = mainIdentificatorProfile.getText().toString().split("@");//Auxilia na identificação da diferença entre número de telefone e usuário inserido no mainIdentificator

                    if(filter.length > 1){//Para alterar o e-mail
                        user.setEmail(mainIdentificatorProfile.getText().toString());
                    }else{//Para alterar o número de telefone
                        user.setPhoneNumber(mainIdentificatorProfile.getText().toString());
                    }

                    user.setPassword(passwordProfile.getText().toString());

                    //--------------------------------------------------------

                    Call<User> call = SystemAtributes.apiService.updateUserPassenger(user);//Cria uma chamada para o endereço da função updateUserPassanger da API

                    call.enqueue(new Callback<User>() {//Configurando resposta ao retorno da requisição
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if(response.isSuccessful()){//Se a resposta for bem sucedida
                                Toast.makeText(getActivity().getApplicationContext(), "Usuário atualizado com sucesso!",Toast.LENGTH_LONG).show();
                            }else{//Se não
                                Toast.makeText(getActivity().getApplicationContext(), "Falha ao atualizar usuário!",Toast.LENGTH_LONG).show();
                                SystemAtributes.user = userOriginal;//Retoma a antiga forma do usuário
                                updateRegisterUserPassenger(view);//Preenche os campos do fragment com as informações do passageiro
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable throwable) {//Quando não há resposta
                            Toast.makeText(getActivity().getApplicationContext(), "Erro: Servidor não responde",Toast.LENGTH_LONG).show();
                            SystemAtributes.user = userOriginal;//Retoma a antiga forma do usuário
                        }
                    });
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener(){//Ao clicar no botão deletar usuário

                @Override
                public void onClick(View v) {
                    alertDialogDeleteUser(SystemAtributes.apiService.deleteUserPassenger(SystemAtributes.user));//Gera um alertDialog para o usuário, perguntando-lhe se deseja deletar sua conta de passageiro
                }
            });
            //------------------------------------------------------------
        }



        return view;
    }

    public void updateRegisterUserDriver(View view){ //Função que preenche os campos do fragmento de informações da conta do usuário motorista

        //Instânciando variáveis com os elementos do fragment

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

        //--------------------------------------------------------

        User user = SystemAtributes.user;

        //---------------Atribuindo os valores aos seus respectivos campos------------------

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

    public void updateRegisterUserPassenger(View view){//Função que preenche os campos do fragmento de informações da conta do usuário passageiro

        //Instânciando variáveis com os elementos do fragment

        TextInputEditText profileNameEditText = view.findViewById(R.id.profileNameEditText);
        TextInputEditText profileLastNameEditText = view.findViewById(R.id.profileLastNameEditText);
        TextInputEditText mainIdentificatorProfile = view.findViewById(R.id.mainIdentificatorProfile);
        TextInputEditText passwordProfile = view.findViewById(R.id.passwordProfile);

        AppCompatButton updateButton = view.findViewById(R.id.updateButton);
        AppCompatButton deleteButton = view.findViewById(R.id.deleteButton);

        //--------------------------------------------------------

        User user = SystemAtributes.user;

        //---------------Atribuindo os valores aos seus respectivos campos------------------

        profileNameEditText.setText(user.getName().toString());
        profileLastNameEditText.setText(user.getLastName().toString());

        if(user.getEmail() != null && !user.getEmail().equals("NaN")){
            mainIdentificatorProfile.setText(user.getEmail().toString());
        }else{
            mainIdentificatorProfile.setText(user.getPhoneNumber().toString());
        }

        passwordProfile.setText(user.getPassword().toString());
    }
    public void alertDialogDeleteUser(Call<User> call){ //Função para chamar um alertDialog para a ação de deletar conta
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext()); //Instância um alertDialog

        dialog.setTitle("Deletar Conta"); //Título do alertDialog
        dialog.setMessage("Tem certeza de que deseja apagar sua conta?"); //Mensagem do alertDialog

        dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() { //Caso o botão "Sim" seja clicado
            @Override
            public void onClick(DialogInterface dialog, int which) {

                call.enqueue(new Callback<User>() {//Configurando resposta ao retorno da requisição
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if(response.isSuccessful()){//Se a resposta for bem sucedida
                            Toast.makeText(getActivity().getApplicationContext(), "Usuário deletado com sucesso!",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                            startActivity(intent); //Retorne à activity de Login
                            getActivity().finish(); //Encerre a activity atual
                        }else{
                            Toast.makeText(getActivity().getApplicationContext(), "Falha ao deletar usuário!",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable throwable) {//Quando não há resposta
                        Toast.makeText(getActivity().getApplicationContext(), "Erro: Servidor não responde",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        dialog.setNegativeButton("Não", new DialogInterface.OnClickListener() { //Caso o botão "Não" seja clicado
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dialog.create(); //Criar alertDialog após as configurações do seu builder
        dialog.show(); //Mostrar alertDialog
    }
}