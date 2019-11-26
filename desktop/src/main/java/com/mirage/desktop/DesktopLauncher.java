package com.mirage.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mirage.client.Client;
import com.mirage.utils.Assets;
import com.mirage.utils.ConfigurationKt;

import java.io.File;

class DesktopLauncher {

    public static void main(String[] args) {
        System.setProperty("user.name", "CorrectUserName");
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Shattered World";
        if (new File("android/assets/").exists()) {
            //Во время тестирования в IDE
            ConfigurationKt.setPLATFORM("desktop-test");
            System.out.println("Test mode enabled. Assets path: " + new File("android/assets/").getAbsolutePath());
            config.addIcon("android/assets/drawable/windows_icon.png", Files.FileType.Internal);
            config.addIcon("android/assets/drawable/mac_icon.png", Files.FileType.Internal);
        }
        else {
            //Во время запуска собранного jar-файла
            ConfigurationKt.setPLATFORM("desktop");
            System.out.println("Release mode enabled.");
            config.addIcon("drawable/windows_icon.png", Files.FileType.Internal);
            config.addIcon("drawable/mac_icon.png", Files.FileType.Internal);
        }
        if (ConfigurationKt.DESKTOP_FULL_SCREEN) {
            config.fullscreen = true;
            config.width = LwjglApplicationConfiguration.getDesktopDisplayMode().width;
            config.height = LwjglApplicationConfiguration.getDesktopDisplayMode().height;
        }
        else {
            config.fullscreen = false;
            config.width = 800;
            config.height = 600;
        }
        new LwjglApplication(Client.INSTANCE, config);
    }
}
