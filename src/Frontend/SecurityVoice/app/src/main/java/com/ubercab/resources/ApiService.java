package com.ubercab.resources;

import com.ubercab.entities.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("loginUser")
    Call<User> loginUser(@Body User user); //Endereço da api para logar o usuário
    @POST("registerDriver")
    Call<User> registerDriver(@Body User user); //Endereço da API para registrar um motorista
    @POST("registerPassenger")
    Call<User> registerPassenger(@Body User user); //Endereço da API para registrar um passageiro

    @POST("updateUserDriver")
    Call<User> updateUserDriver(@Body User user); //Endereço da API para atualizar o cadastro de um motorista

    @POST("updateUserPassenger")
    Call<User> updateUserPassenger(@Body User user); //Endereço da API para atualizar o cadastro de um passageiro

    @POST("deleteUserDriver")
    Call<User> deleteUserDriver(@Body User user); //Endereço da API para deletar o cadastro de um motorista

    @POST("deleteUserPassenger")
    Call<User> deleteUserPassenger(@Body User user); //Endereço da API para deletar o cadastro de um passageiro

    @POST("securityVoiceConfigurationDriver")
    Call<User> securityVoiceConfigurationDriver(@Body User user); //Endereço da API para atualizar as configurações de SecurityVoice de um motorista

    @POST("securityVoiceConfigurationPassenger")
    Call<User> securityVoiceConfigurationPassenger(@Body User user); //Endereço da API para atualizar as configurações de SecurityVoice de um passageiro

    }
