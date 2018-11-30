package com.mirage.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mirage.controller.Controller;

public class DesktopLauncher {
	public static void main (String[] arg) {
		System.setProperty("user.name","CorrectUserName");
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Shattered World";
		// Фуллскрин
		//config.fullscreen = true;
		//config.width = 111; // Костыль, без которого фулскрин ломается
		new LwjglApplication(new Controller(), config);
	}
}
