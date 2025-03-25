package com.ubercab.resources;

public enum SpeechRecognizerTypeExecution
{
    emergencyCodeButton, //Identifica que o reconhecimento de voz serve para o processo relacionado ao botão emergencyCodeButton
    uAudioCodeButton, //Identifica que o reconhecimento de voz serve para o processo relacionado ao botão uAudioCodeButton
    LISTENING; //Identifica que o reconhecimento de voz serve para escutar o que o usuário está falando durante a viagem

}
