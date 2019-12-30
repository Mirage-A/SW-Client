package com.mirage.admin;


import com.mirage.core.utils.ClientPlatformKt;

public class AdminLauncher {

    public static void main(String[] args) {
        System.setProperty("user.name", "CorrectUserName");
        ClientPlatformKt.setPLATFORM("server");
        new Admin();
    }
}
