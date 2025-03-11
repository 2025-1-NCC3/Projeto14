package com.ubercab.entities;

public class User {
    private int id;
    private String name;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
    private String cpf;
    private String rg;
    private String gender;

    private int dayBirthday;
    private String monthBirthday;
    private int yearBirthday;

    public User(String str, String pas) {
        String[] filter = str.split("@");//Auxilia na identificação da diferença entre número de telefone e usuário inserido no mainIdentificator

        if(filter.length > 1){//Pega registro do usuário pelo e-mail
            email = str;
            phoneNumber = "NaN";
        }else{//Pega registro do usuário pelo número de telefone
            phoneNumber = str;
            email = "NaN";
        }
        password = pas;

        id = 0;
        name="NaN";
        lastName="NaN";
        cpf="NaN";
        rg="NaN";
        gender="NaN";
    }

    public User(String name, String lastName, String str, String password) {
        this.name = name;
        this.lastName = lastName;
        String[] filter = str.split("@");//Auxilia na identificação da diferença entre número de telefone e usuário inserido no mainIdentificator

        if(filter.length > 1){//Pega registro do usuário pelo e-mail
            email = str;
            phoneNumber = "NaN";
        }else{//Pega registro do usuário pelo número de telefone
            phoneNumber = str;
            email = "NaN";
        }
        this.password = password;
    }

    public User(String name, String lastName, String str, String password, String cpf, String rg, String gender, int dayBirthday, String monthBirthday, int yearBirthday) {
        this.name = name;
        this.lastName = lastName;
        String[] filter = str.split("@");//Auxilia na identificação da diferença entre número de telefone e usuário inserido no mainIdentificator

        if(filter.length > 1){//Pega registro do usuário pelo e-mail
            email = str;
            phoneNumber = "NaN";
        }else{//Pega registro do usuário pelo número de telefone
            phoneNumber = str;
            email = "NaN";
        }
        this.password = password;
        this.cpf = cpf;
        this.rg = rg;
        this.gender = gender;
        this.dayBirthday = dayBirthday;
        this.monthBirthday = monthBirthday;
        this.yearBirthday = yearBirthday;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public int getDayBirthday() {
        return dayBirthday;
    }

    public void setDayBirthday(int dayBirthday) {
        this.dayBirthday = dayBirthday;
    }

    public String getMonthBirthday() {
        return monthBirthday;
    }

    public void setMonthBirthday(String monthBirthday) {
        this.monthBirthday = monthBirthday;
    }

    public int getYearBirthday() {
        return yearBirthday;
    }

    public void setYearBirthday(int yearBirthday) {
        this.yearBirthday = yearBirthday;
    }

    @Override
    public String toString(){
        return getName() + ", " + getLastName() + ", " + getEmail() + ", " +getPhoneNumber() + ", " +getPassword() + ", " +getCpf() + ", " +getRg() + ", " +getGender() + ", " +getDayBirthday() + ", " +getMonthBirthday() + ", " +getYearBirthday();
    }
}
