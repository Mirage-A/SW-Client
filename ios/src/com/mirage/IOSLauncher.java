package com.mirage;

import com.mirage.controller.Controller;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.mirage.controller.Platform;
import com.mirage.view.TextureLoader;

public class IOSLauncher extends IOSApplication.Delegate {
    @Override
    protected IOSApplication createApplication() {
        // TODO выбрать корректный путь папки assets
        TextureLoader.ASSETS_PATH = "./android/assets/";
        Platform.TYPE = Platform.IOS;
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        return new IOSApplication(new Controller(), config);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }
}