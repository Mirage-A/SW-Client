package com.mirage;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mirage.controller.Controller;
import com.mirage.view.TextureLoader;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextureLoader.ASSETS_PATH = "";
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new Controller(), config);
	}
}
