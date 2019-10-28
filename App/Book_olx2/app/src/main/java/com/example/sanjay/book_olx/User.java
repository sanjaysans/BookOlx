package com.example.sanjay.book_olx;

public class User {
    String roll, cont_no;
    String name, dept, mail, password, token;

    public User(String roll, String cont_no, String name, String dept, String mail, String password, String token) {
        this.roll = roll;
        this.cont_no = cont_no;
        this.name = name;
        this.dept = dept;
        this.mail = mail;
        this.password = password;
        this.token = token;
    }
    public String getToken() {
        return token;
    }

    public String getRoll() {
        return roll;
    }

    public String getCont_no() {
        return cont_no;
    }

    public String getName() {
        return name;
    }

    public String getDept() {
        return dept;
    }

    public String getMail() {
        return mail;
    }

    public String getPassword() {
        return password;
    }
}
