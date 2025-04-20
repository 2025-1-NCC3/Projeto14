package com.ubercab.criptography;

import android.os.Build;

import com.ubercab.entities.SystemAtributes;
import com.ubercab.entities.User;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.HexFormat;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Criptography {

    public static SecretKey gerarChaveSegura(String senha) throws Exception {
        byte[] saltFixo = "meuSaltFixo1234".getBytes(); //
        int iteracoes = 65536;
        int tamanhoChave = 128; //

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(senha.toCharArray(), saltFixo, iteracoes, tamanhoChave);
        byte[] chaveDerivada = factory.generateSecret(spec).getEncoded();

        return new SecretKeySpec(chaveDerivada, "AES");
    }


    public static User userCriptography(User user){

        User userCrypt = new User(user.getName(),user.getLastName(), //Cria outro objeto idêntico ao usuário atual dentro da variável userOriginal
                user.getEmail(),user.getPhoneNumber(),user.getPassword(),
                user.getCpf(),user.getRg(), user.getDayBirthday(),user.getMonthBirthday(),
                user.getYearBirthday());
        try {
            userCrypt.setId(user.getId());
            userCrypt.setName(crypt(user.getName()));
            userCrypt.setLastName(crypt(user.getLastName()));
            userCrypt.setEmail(crypt(user.getEmail()));
            userCrypt.setPhoneNumber(crypt(user.getPhoneNumber()));
            userCrypt.setPassword(crypt(user.getPassword()));
            userCrypt.setRg(crypt(user.getRg()));
            userCrypt.setCpf(crypt(user.getCpf()));
            userCrypt.setGender(crypt(user.getGender()));
            userCrypt.setDayBirthday(crypt(user.getDayBirthday()));
            userCrypt.setMonthBirthday(crypt(user.getMonthBirthday()));
            userCrypt.setYearBirthday(crypt(user.getYearBirthday()));
            userCrypt.setEmergencyCode(crypt(user.getEmergencyCode()));
            userCrypt.setuAudioCode(crypt(user.getuAudioCode()));
            userCrypt.setCommandVoice(crypt(user.getCommandVoice()));

        }catch(Exception e){
            System.out.println(e.getMessage());
            }

        return userCrypt;
    }

    public static User userDecrypt(User user){

        User userOriginal = new User();

        try {
            userOriginal.setId(user.getId());
            userOriginal.setName(decrypt(user.getName()));
            userOriginal.setLastName(decrypt(user.getLastName()));
            userOriginal.setEmail(decrypt(user.getEmail()));
            userOriginal.setPhoneNumber(decrypt(user.getPhoneNumber()));
            userOriginal.setPassword(decrypt(user.getPassword()));
            userOriginal.setRg(decrypt(user.getRg()));
            userOriginal.setCpf(decrypt(user.getCpf()));
            userOriginal.setGender(decrypt(user.getGender()));
            userOriginal.setDayBirthday(decrypt(user.getDayBirthday()));
            userOriginal.setMonthBirthday(decrypt(user.getMonthBirthday()));
            userOriginal.setYearBirthday(decrypt(user.getYearBirthday()));
            userOriginal.setEmergencyCode(decrypt(user.getEmergencyCode()));
            userOriginal.setuAudioCode(decrypt(user.getuAudioCode()));
            userOriginal.setCommandVoice(decrypt(user.getCommandVoice()));

        }catch (Exception e){
            System.out.println(e.getMessage());
        }


        return userOriginal;
    }

    public static String crypt(String code) throws Exception {
        SecretKey chave = gerarChaveSegura("senhaSuperSecreta123");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, chave);
        byte[] textoCriptografado = cipher.doFinal(code.getBytes());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Base64.getEncoder().encodeToString(textoCriptografado);
        }
        return null;
    }

    public static String decrypt(String code) throws Exception {
        if(code != null){
            SecretKey chave = gerarChaveSegura("senhaSuperSecreta123");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, chave);
            byte[] textoDecodificado = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                textoDecodificado = Base64.getDecoder().decode(code);
            }
            byte[] textoOriginal = cipher.doFinal(textoDecodificado);
            return new String(textoOriginal);
        }

        return null;
    }

}
