package com.mirage;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;

import com.mirage.client.Client;
import com.mirage.utils.ConfigurationKt;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;

class IOSLauncher extends IOSApplication.Delegate {

    @Override
    protected IOSApplication createApplication() {
        ConfigurationKt.getConfig().put("assets", "./android/assets/");
        ConfigurationKt.getConfig().put("platform", "ios");
        return new IOSApplication(Client.INSTANCE, new IOSApplicationConfiguration());
    }

    public static void main(String[] args) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(args, null, IOSLauncher.class);
        pool.close();
    }
}