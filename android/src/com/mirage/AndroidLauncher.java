package com.mirage;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mirage.controller.Controller;
import com.mirage.controller.Platform;
import com.mirage.view.TextureLoader;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Platform.ASSETS_PATH = "";
		Platform.TYPE = Platform.ANDROID;
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new Controller(), config);
	}
}
