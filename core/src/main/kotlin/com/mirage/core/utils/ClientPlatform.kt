package com.mirage.core.utils


enum class ClientPlatform {
    TEST, // Launched through JUnit tests
    DESKTOP_TEST, // Launched from .jar in project folder
    ANDROID, // Launched from release .apk
    DESKTOP; // Launched from release .jar or .exe

    companion object {
        @Volatile
        var platform: ClientPlatform = TEST
    }

}