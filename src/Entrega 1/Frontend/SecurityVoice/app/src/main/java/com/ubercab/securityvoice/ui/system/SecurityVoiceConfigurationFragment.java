package com.ubercab.securityvoice.ui.system;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.ubercab.criptography.Criptography;
import com.ubercab.entities.SystemAtributes;
import com.ubercab.entities.User;
import com.ubercab.resources.SpeechRecognizerClass;
import com.ubercab.resources.SpeechRecognizerTypeExecution;
import com.ubercab.securityvoice.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SecurityVoiceConfigurationFragment extends Fragment {

    private boolean emergencyCodeListening = false;
    private boolean uAudioCodeListening = false;

    public SecurityVoiceConfigurationFragment() {
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
        View view = inflater.inflate(R.layout.fragment_security_voice_configuration, container, false);

        SpeechRecognizerClass.speechRecognizerDestroy();

        //-----------------Instânciando variáveis com seus respectivos elementos do fragment---------------------

        AppCompatButton emergencyCodeButton = view.findViewById(R.id.emergencyCodeButton);
        AppCompatButton uAudioCodeButton = view.findViewById(R.id.uAudioCodeButton);

        AppCompatButton saveButton = view.findViewById(R.id.saveButton);

        Switch commandVoice = view.findViewById(R.id.commandVoiceSwitch);

        //--------------------------------------------------------------

        emergencyCodeButton.setText(SystemAtributes.user.getEmergencyCode()); //Atribuindo o texto do botão como sendo o resultado do atributo emergencyCode do usuário
        uAudioCodeButton.setText(SystemAtributes.user.getuAudioCode());//Atribuindo o texto do botão como sendo o resultado do atributo uAudioCode do usuário

        if(SystemAtributes.user.getEmergencyCode() == "NaN" || SystemAtributes.user.getEmergencyCode() == null){ //Se o emergencyCode estiver vazio
            emergencyCodeButton.setText("");//Mantenha o texto do botão vazio
        }else{//Se não
            emergencyCodeButton.setText(SystemAtributes.user.getEmergencyCode());//Atribua o valor da variável ao texto do botão
        }

        if(SystemAtributes.user.getuAudioCode() == "NaN" || SystemAtributes.user.getuAudioCode() == null){//Se o uAudioCode estiver vazio
            uAudioCodeButton.setText("");//Mantenha o texto do botão vazio
        }else{//Se não
            uAudioCodeButton.setText(SystemAtributes.user.getuAudioCode());//Atribua o valor da variável ao texto do botão
        }

        commandVoice.setChecked(Boolean.getBoolean(SystemAtributes.user.getCommandVoice())); //Atribui a checagem do switch commandVoice como sendo o valor da variável commandVoice do usuário

        emergencyCodeButton.setOnClickListener(new View.OnClickListener() { //Ao clicar no botão emergencyCodeButton
            @Override
            public void onClick(View v) {

                if(emergencyCodeListening == false) { //Se o sistema não estiver com o reconhecimento de voz ativado
                    SpeechRecognizerClass.startListening(getActivity(), SpeechRecognizerTypeExecution.emergencyCodeButton); //Inicie o reconhecimento por voz
                    emergencyCodeListening = true; //Agora o reconhecimento por voz está ativado
                }else{ //Se o reconhecimento por voz estiver ativado
                    SpeechRecognizerClass.speechRecognizerDestroy(); //Desligue-o
                    SystemAtributes.user.setEmergencyCode(SpeechRecognizerClass.voiceResults);//Salve o resultado do reconhecimento por voz no emergencyCode do usuário
                    emergencyCodeListening = false; //Reconhecimento por voz desativado
                }
            }
        });

        uAudioCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uAudioCodeListening == false) {//Se o sistema não estiver com o reconhecimento de voz ativado
                    SpeechRecognizerClass.startListening(getActivity(), SpeechRecognizerTypeExecution.uAudioCodeButton);//Inicie o reconhecimento por voz
                    uAudioCodeListening = true;//Agora o reconhecimento por voz está ativado
                }else{//Se o reconhecimento por voz estiver ativado
                    SpeechRecognizerClass.speechRecognizerDestroy();//Desligue-o
                    SystemAtributes.user.setuAudioCode(SpeechRecognizerClass.voiceResults);//Salve o resultado do reconhecimento por voz no uAudioCode do usuário
                    uAudioCodeListening = false;//Reconhecimento por voz desativado
                }
            }
        });

        commandVoice.setOnClickListener(new View.OnClickListener() { //Se o commandVoice for alterado
            @Override
            public void onClick(View v) {
                SystemAtributes.user.setCommandVoice(commandVoice.isChecked() == true ? "true" : "false"); //Atribua a ativação dos comando por voz às preferências do usuário

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() { //Se o botão salvar for clicado
            @Override
            public void onClick(View v) {
                Call<User> call = null;

                User userCrypt = Criptography.userCriptography(SystemAtributes.user);

                if(SystemAtributes.user.getCpf() == "NaN" || SystemAtributes.user.getCpf() == null) {//Se o usuário for um passageiro
                    call = SystemAtributes.apiService.securityVoiceConfigurationPassenger(userCrypt); //instancie a chamada da api com a função de atualizar configurações de security voice do passageiro
                }else{ //Se o usuário for um motorista
                    call = SystemAtributes.apiService.securityVoiceConfigurationDriver(userCrypt);//instancie a chamada da api com a função de atualizar configurações de security voice do motorista
                }

                call.enqueue(new Callback<User>() {//Configurando resposta ao retorno da requisição
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if(response.isSuccessful()){//Se a resposta for bem sucedida
                            Toast.makeText(getActivity().getApplicationContext(), "Configurações de SecurityVoice salvas com sucesso!",Toast.LENGTH_LONG).show();
                            System.out.println(SystemAtributes.user.toString());
                        }else{
                            Toast.makeText(getActivity().getApplicationContext(), "Falha ao salvar configurações de SecurityVoice!",Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable throwable) {//Quando não há resposta
                        Toast.makeText(getActivity().getApplicationContext(), "Erro: Servidor não responde!",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        return view;
    }
}