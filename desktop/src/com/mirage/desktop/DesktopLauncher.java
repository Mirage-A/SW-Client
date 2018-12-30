package com.mirage.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mirage.controller.Controller;
import com.mirage.view.TextureLoader;

import java.io.File;

public class DesktopLauncher {
	public static void main (String[] arg) {
		System.setProperty("user.name","CorrectUserName");
		//При создании jar-архива эта строка должна быть пустой
		if (new File("./android/assets/").exists()) {
			TextureLoader.ASSETS_PATH = "./android/assets/";
		}
		else {
			TextureLoader.ASSETS_PATH = "";
		}
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Shattered World";
		config.addIcon(TextureLoader.ASSETS_PATH + "windows_icon.png", Files.FileType.Internal);
		config.addIcon(TextureLoader.ASSETS_PATH + "mac_icon.png", Files.FileType.Internal);
		// Фуллскрин
		config.fullscreen = true;
		config.width = LwjglApplicationConfiguration.getDesktopDisplayMode().width;
		config.height = LwjglApplicationConfiguration.getDesktopDisplayMode().height;
		new LwjglApplication(new Controller(), config);
	}
}
