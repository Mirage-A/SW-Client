package com.mirage.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mirage.utils.Assets;
import com.mirage.client.Client;
import com.mirage.utils.ConfigurationKt;

class DesktopLauncher {

    public static void main(String[] args) {
        System.setProperty("user.name", "CorrectUserName");
        ConfigurationKt.getConfig().put("platform", "desktop");
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Shattered World";
        config.addIcon(Assets.INSTANCE.getAssetsPath() + "drawable/windows_icon.png", Files.FileType.Internal);
        config.addIcon(Assets.INSTANCE.getAssetsPath() + "drawable/mac_icon.png", Files.FileType.Internal);
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
