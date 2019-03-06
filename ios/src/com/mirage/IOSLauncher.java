package com.mirage;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.mirage.controller.Controller;
import com.mirage.controller.Platform;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;

class IOSLauncher extends IOSApplication.Delegate {

    @Override
    protected IOSApplication createApplication() {
        Platform.INSTANCE.setASSETS_PATH("./android/assets/");
        Platform.INSTANCE.setTYPE(Platform.Types.IOS);
        return new IOSApplication(Controller.INSTANCE, new IOSApplicationConfiguration());
    }

    public static void main(String[] args) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(args, null, IOSLauncher.class);
        pool.close();
    }
}