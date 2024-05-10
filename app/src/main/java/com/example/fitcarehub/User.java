package com.example.fitcarehub;


public class User {
    private String name;
    private String surname;
    private long score;

    public User() {
    }

    public User(String name, String surname, long score) {
        this.name = name;
        this.surname = surname;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }
}

