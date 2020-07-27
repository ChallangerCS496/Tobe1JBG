package com.example.myapplication.register;

public class User {
    private String name, nickname;

    public User(String name, String nickname) {
        this.name = name;
        this.nickname = nickname;
    }

    public String getName(){
        return name;
    }

    public String getNickname(){
        return nickname;
    }
}
