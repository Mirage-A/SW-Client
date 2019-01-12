package com.mirage.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mirage.controller.Controller;
import com.mirage.controller.Platform;
import com.mirage.model.scene.Scene;
import com.mirage.view.TextureLoader;

import java.io.File;

public class DesktopLauncher {
	public static void main (String[] arg) {
		new Scene();
		System.setProperty("user.name","CorrectUserName");
		//При создании jar-архива эта строка должна быть пустой
		if (new File("./android/assets/").exists()) {
			Platform.ASSETS_PATH = "./android/assets/";
		}
		else {
			Platform.ASSETS_PATH = "";
		}
		Platform.TYPE = Platform.DESKTOP;
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Shattered World";
		config.addIcon(Platform.ASSETS_PATH + "windows_icon.png", Files.FileType.Internal);
		config.addIcon(Platform.ASSETS_PATH + "mac_icon.png", Files.FileType.Internal);
		// Фуллскрин
		//config.fullscreen = true;
		//config.width = LwjglApplicationConfiguration.getDesktopDisplayMode().width;
		//config.height = LwjglApplicationConfiguration.getDesktopDisplayMode().height;
		new LwjglApplication(new Controller(), config);
	}
}
