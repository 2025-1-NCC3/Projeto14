package com.ubercab.resources;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

import com.ubercab.entities.SystemAtributes;
import com.ubercab.securityvoice.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SpeechRecognizerClass {

    public static SpeechRecognizer speechRecognizer; // Objeto que realiza o reconhecimento de fala
    private static Intent recognizerIntent; // Intent para iniciar o reconhecimento de fala
    public static String voiceResults; // Lista que armazena os textos reconhecidos


    //-----------AVISOAVISOAVISOAVISOAVISOAVISOAVISOAVISOAVISOAVISOAVISOAVISO-------------------
    //O sistema de reconhecimento de voz só funciona no celular, no Android Studio esse sistema gerará erros constantes
    //O sistema de reconhecimento de voz só funciona no celular, no Android Studio esse sistema gerará erros constantes
    //O sistema de reconhecimento de voz só funciona no celular, no Android Studio esse sistema gerará erros constantes
    //O sistema de reconhecimento de voz só funciona no celular, no Android Studio esse sistema gerará erros constantes
    //O sistema de reconhecimento de voz só funciona no celular, no Android Studio esse sistema gerará erros constantes
    //O sistema de reconhecimento de voz só funciona no celular, no Android Studio esse sistema gerará erros constantes
    public static void startListening(Activity activity, SpeechRecognizerTypeExecution typeExecution) { //Função para iniciar o reconhecimento de voz
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(activity.getApplicationContext()); // Cria o reconhecedor de fala

        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);// Configura a intent para reconhecimento de fala, solicitando ao sistema operacional do android que inicie um serviço de reconhecimento de voz
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);//Define o modelo de detecção de voz para o FREE_FROM, ou seja, detecção de fala livre, espontânea, cotidiana.
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());//Define a linguagem a ser detectada como a padrão do sistema operacional
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true); //Ativa o recurso Partial Results do listener do speechRecognizer

        // Define um listener para capturar eventos do reconhecimento de fala
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                Log.d("Voice", "Pronto para ouvir..."); // Log quando o reconhecimento está pronto
            }

            @Override
            public void onBeginningOfSpeech() {}

            @Override
            public void onRmsChanged(float rmsdB) {}

            @Override
            public void onBufferReceived(byte[] buffer) {}

            @Override
            public void onEndOfSpeech() {
                Log.d("Voice", "Fim da fala, reiniciando...");
            }

            @Override
            public void onError(int error) {
                Log.e("Voice", "Erro na captura: " + error);
                speechRecognizer.startListening(recognizerIntent); // Se houver erro, reinicia o reconhecimento
            }

            @Override
            public void onResults(Bundle results) { //Quando o usuário termina sua fala
                // Captura os resultados do reconhecimento
                if(typeExecution == SpeechRecognizerTypeExecution.emergencyCodeButton){//Se o reconhecimento de voz estiver resgatando um emergencyCode
                    ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION); //Recolhe os dados resgatados pelo reconhecimento por voz
                    AppCompatButton emergencyCodeButton = activity.findViewById(R.id.emergencyCodeButton); //Pega a instância do botão emergencyCode

                    if (matches != null && !matches.isEmpty()) {
                        String capturedText = matches.get(0); // Pega o tudo o que foi reconhecido
                        voiceResults = capturedText;

                        emergencyCodeButton.setText(voiceResults); //Muda o texto do botão para o resultado do reconhecimento de voz
                        SystemAtributes.user.setEmergencyCode(voiceResults); //Atribui o que foi falado ao emergencyCode do usuário
                        speechRecognizerDestroy(); //Encerra o reconhecimento de voz
                        Log.d("Voice", "Capturado: " + capturedText);
                    }
                }

                if(typeExecution == SpeechRecognizerTypeExecution.uAudioCodeButton){//Se o reconhecimento por voz estiver resgatando um uAudioCode
                    ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);//Recolhe os dados resgatados pelo reconhecimento por voz
                    AppCompatButton uAudioCodeButton = activity.findViewById(R.id.uAudioCodeButton); //Pega a instância do botão uAudioCode

                    if (matches != null && !matches.isEmpty()) {
                        String capturedText = matches.get(0); // Pega o tudo o que foi reconhecido
                        voiceResults = capturedText;

                        uAudioCodeButton.setText(voiceResults); //Muda o texto do botão para o resultado do reconhecimento de voz
                        SystemAtributes.user.setuAudioCode(voiceResults); //Atribui o que foi falado ao uAudioCode do usuário
                        speechRecognizerDestroy(); //Encerra o reconhecimento de voz
                        Log.d("Voice", "Capturado: " + capturedText);
                    }
                }

                if(typeExecution == SpeechRecognizerTypeExecution.LISTENING){
                    ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);//Recolhe os resultados de fala parciais resgatados pelo reconhecimento por voz
                    TextView dica = activity.findViewById(R.id.dica);

                    if (matches != null && !matches.isEmpty()) {
                        String capturedText = matches.get(0); // Pega o tudo o que foi reconhecido
                        voiceResults = capturedText;

                        dica.setText(LISTENING(voiceResults));//Muda o texto do botão para o resultado do reconhecimento de voz


                        Log.d("Voice", "Capturado: " + capturedText);
                    }

                    speechRecognizer.startListening(recognizerIntent);
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) { //Enquanto o usuário está falando
                if(typeExecution == SpeechRecognizerTypeExecution.emergencyCodeButton){//Se o reconhecimento de voz estiver resgatando um emergencyCode
                    ArrayList<String> matches = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);//Recolhe os resultados de fala parciais resgatados pelo reconhecimento por voz
                    AppCompatButton emergencyCodeButton = activity.findViewById(R.id.emergencyCodeButton);//Atribui o que foi falado ao emergencyCode do usuário

                    if (matches != null && !matches.isEmpty()) {
                        String capturedText = matches.get(0); // Pega o tudo o que foi reconhecido
                        voiceResults = capturedText;

                        emergencyCodeButton.setText(voiceResults);//Muda o texto do botão para o resultado do reconhecimento de voz

                        Log.d("Voice", "Capturado: " + capturedText);
                    }
                }
                if(typeExecution == SpeechRecognizerTypeExecution.uAudioCodeButton){//Se o reconhecimento por voz estiver resgatando um uAudioCode
                    ArrayList<String> matches = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);//Recolhe os resultados de fala parciais resgatados pelo reconhecimento por voz
                    AppCompatButton uAudioCodeButton = activity.findViewById(R.id.uAudioCodeButton);//Pega a instância do botão uAudioCode

                    if (matches != null && !matches.isEmpty()) {
                        String capturedText = matches.get(0); // Pega o tudo o que foi reconhecido
                        voiceResults = capturedText;

                        uAudioCodeButton.setText(voiceResults);//Muda o texto do botão para o resultado do reconhecimento de voz

                        Log.d("Voice", "Capturado: " + capturedText);
                    }
                }

                if(typeExecution == SpeechRecognizerTypeExecution.LISTENING){
                    ArrayList<String> matches = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);//Recolhe os resultados de fala parciais resgatados pelo reconhecimento por voz
                    TextView dica = activity.findViewById(R.id.dica);

                    if (matches != null && !matches.isEmpty()) {
                        String capturedText = matches.get(0); // Pega o tudo o que foi reconhecido
                        voiceResults = capturedText;

                        dica.setText(LISTENING(voiceResults));//Muda o texto do botão para o resultado do reconhecimento de voz


                        Log.d("Voice", "Capturado: " + capturedText);
                    }
                }
            }

            @Override
            public void onEvent(int eventType, Bundle params) {}
        });

        speechRecognizer.startListening(recognizerIntent); // Inicia a escuta ativa
    }

    public static String LISTENING(String voiceResults){ //Função que confere se o usuário disse a fala de emergência (emergencyCode) ou a fala de uAudio
        String[] filterVoiceResults = voiceResults.split(" ");
        String[] filterEmmergencyCode = SystemAtributes.user.getEmergencyCode().split(" ");
        String[] filteruAudioCode = SystemAtributes.user.getuAudioCode().split(" ");

        int contEmergencyCode = 0;
        int contuAudioCode = 0;

        for(int i = 0; i < filterEmmergencyCode.length; i++){ //Verificando se cada palavra do código de emergência está na lista de palavras reconhecidas
            for(int p = 0; p < filterVoiceResults.length; p++){
                if(filterVoiceResults[p].equals(filterEmmergencyCode[i])){
                    contEmergencyCode++;
                    break;
                }
            }
        }

        for(int i = 0; i < filteruAudioCode.length; i++){ //Verificando se cada palavra do código de uAudio está na lista de palavras reconhecidas
            for(int p = 0; p < filterVoiceResults.length; p++){
                if(filterVoiceResults[p].equals(filteruAudioCode[i])){
                    contuAudioCode++;
                    break;
                }
            }
        }

        if(contEmergencyCode >= filterEmmergencyCode.length) { //Se tiver falado todas as palavras do emergencyCode
            return "Acionando central Uber e as autoridades policiais!";
        }
        if(contuAudioCode >= filteruAudioCode.length){ //Se tiver falado todas as palavras do uAudioCode
            return "Ligando gravador de áudio!";
        }

        return "Diga: " + SystemAtributes.user.getEmergencyCode() + " | " + "Você disse: " + voiceResults;

    }

    public static void speechRecognizerDestroy(){ // Encerra o reconhecimento por voz
        if(speechRecognizer != null){
            speechRecognizer.destroy();
        }
    }
}
