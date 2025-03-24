package com.ubercab.entities;

import com.ubercab.resources.ApiService;
import com.ubercab.resources.RetrofitClient;

public class SystemAtributes {
    public static User user; //Usuário que foi logado no sistema do aplicativo (Aquele que está usando o aplicativo)
    public static ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class); //Prepara uma classe retrofit com o contrato da interface e suas relações com os endpoints da API
}
