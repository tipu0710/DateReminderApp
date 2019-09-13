package com.systech.farha.datereminderapp.model;

public class User {
    private Integer id;
    private String name;
    private String userName;
    private String email;
    private String password;
    private String question1;
    private String answer1;
    private String question2;
    private String answer2;
    private String phone;
    private String address;
    private boolean isQuestionSkipped;
    private byte[] profile;

    public User(Integer id, String name, String userName, String email,
                String password, String question1, String answer1, String question2,
                String answer2, String phone, String address, byte[] profile, boolean isQuestionSkipped) {
        this.id = id;
        this.name = name;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.question1 = question1;
        this.answer1 = answer1;
        this.question2 = question2;
        this.answer2 = answer2;
        this.profile = profile;
        this.phone = phone;
        this.address = address;
        this.isQuestionSkipped = isQuestionSkipped;
    }


    public User() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getQuestion1() {
        return question1;
    }

    public void setQuestion1(String question1) {
        this.question1 = question1;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getQuestion2() {
        return question2;
    }

    public void setQuestion2(String question2) {
        this.question2 = question2;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public byte[] getProfile() {
        return profile;
    }

    public void setProfile(byte[] profile) {
        this.profile = profile;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isQuestionSkipped() {
        return isQuestionSkipped;
    }

    public void setQuestionSkipped(boolean questionSkipped) {
        isQuestionSkipped = questionSkipped;
    }
}


