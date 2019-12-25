package com.mirage.admin;


import com.mirage.core.ConfigurationKt;

public class AdminLauncher {

    public static void main(String[] args) {
        System.setProperty("user.name", "CorrectUserName");
        ConfigurationKt.setPLATFORM("server");
        new Admin();
    }
}
