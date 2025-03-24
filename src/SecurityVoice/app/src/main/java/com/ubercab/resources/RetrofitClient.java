package com.ubercab.resources;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String url = "https://testeapipython3render.onrender.com/"; //URL DA API Hospedada através da ferramenta Render
    private static Retrofit retrofit; //Variável que vai receber as configurações do retrofit

    public static Retrofit getRetrofitInstance(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder() //Builder do retrofit
                    .baseUrl(url) //Recebe a url da API
                    .addConverterFactory(GsonConverterFactory.create()) //Recebe um conversor JSON para converter objetos java em arquivos json e vice-versa
                    .build(); //Construir builder retrofit
        }
        return retrofit;
    }
}
