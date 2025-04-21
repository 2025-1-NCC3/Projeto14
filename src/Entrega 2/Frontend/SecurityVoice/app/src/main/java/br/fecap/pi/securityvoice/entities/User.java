package br.fecap.pi.securityvoice.entities;

public class User {

    //Informações do usuário
    private Integer id = 0;
    private String name = "NaN";
    private String lastName = "NaN";
    private String email = "NaN";
    private String phoneNumber = "NaN";
    private String password = "NaN";
    private String cpf = "NaN";
    private String rg = "NaN";
    private String gender = "NaN";

    private String dayBirthday = "0";
    private String monthBirthday = "NaN";
    private String yearBirthday = "0";
    //----------------------------------------------

    //---------Preferências de Segurança------------------
    private String emergencyCode = "NaN";
    private String uAudioCode = "NaN";
    private String commandVoice = "false";

    //Construtores

    public User(){

    }

    public User(String str, String pas) { //Construtor da operação de Login
        String[] filter = str.split("@");//Auxilia na identificação da diferença entre número de telefone e usuário inserido no mainIdentificator


        if(filter.length > 1){//Pega registro do usuário pelo e-mail
            email = str;
            phoneNumber = "NaN";
        }else{//Pega registro do usuário pelo número de telefone
            phoneNumber = str;
            email = "NaN";
        }
        password = pas;

    }

    public User(String name, String lastName, String str, String password) { //Construtor da operação de Cadastro de Passageiro
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

    public User(Integer id, String name, String lastName, String email, String phoneNumber, String password) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public User(String name, String lastName, String str, String password, String cpf,
                String rg, String gender, String dayBirthday, String monthBirthday, String yearBirthday) { // Construtor da operação de cadastro de Motorista
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

    //Getters and Setters


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDayBirthday() {
        return dayBirthday;
    }

    public void setDayBirthday(String dayBirthday) {
        this.dayBirthday = dayBirthday;
    }

    public String getMonthBirthday() {
        return monthBirthday;
    }

    public void setMonthBirthday(String monthBirthday) {
        this.monthBirthday = monthBirthday;
    }

    public String getYearBirthday() {
        return yearBirthday;
    }

    public void setYearBirthday(String yearBirthday) {
        this.yearBirthday = yearBirthday;
    }

    public String getEmergencyCode() {
        return emergencyCode;
    }

    public void setEmergencyCode(String emergencyCode) {
        this.emergencyCode = emergencyCode;
    }

    public String getuAudioCode() {
        return uAudioCode;
    }

    public void setuAudioCode(String uAudioCode) {
        this.uAudioCode = uAudioCode;
    }

    public String getCommandVoice() {
        return commandVoice;
    }

    public void setCommandVoice(String commandVoice) {
        this.commandVoice = commandVoice;
    }

    @Override
    public String toString(){
        return getId() + ", " + getName() + ", " + getLastName() + ", " + getEmail() + ", " +getPhoneNumber() + ", " +getPassword() + ", " +getCpf() + ", " +getRg() + ", " +getGender() + ", " +getDayBirthday() + ", " +getMonthBirthday() + ", " + getYearBirthday() + ", " + getEmergencyCode() + ", " + getuAudioCode() + ", " + getCommandVoice();
    }
}
