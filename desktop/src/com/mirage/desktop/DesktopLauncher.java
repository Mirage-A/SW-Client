package com.mirage.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mirage.controller.Controller;
import com.mirage.model.ConfigurationKt;

import java.io.File;

class DesktopLauncher {

    public static void main(String[] args) {
        System.setProperty("user.name", "CorrectUserName");
        //При создании jar-архива эта строка должна быть пустой
        if (new File("./android/assets/").exists()) {
            ConfigurationKt.getConfig().put("assets", "./android/assets/");
        }
        ConfigurationKt.getConfig().put("platform", "desktop");
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Shattered World";
        config.addIcon(ConfigurationKt.getConfig().get("assets") + "windows_icon.png", Files.FileType.Internal);
        config.addIcon(ConfigurationKt.getConfig().get("assets") + "mac_icon.png", Files.FileType.Internal);
        // Фуллскрин
        config.fullscreen = true;
        config.width = LwjglApplicationConfiguration.getDesktopDisplayMode().width;
        config.height = LwjglApplicationConfiguration.getDesktopDisplayMode().height;
        new LwjglApplication(Controller.INSTANCE, config);
    }
}
