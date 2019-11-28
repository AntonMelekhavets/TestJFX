package com.test.jfx.Model;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private String password;
    private String age;

    public User(String name, String password, String age) {
        this.name = name;
        this.password = password;
        this.age = age;
    }

    public User() {
        name = "Default Name";
        password = "Default Password";
        age = "0";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "com.test.jfx.Model.User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
                '}';
    }
}
