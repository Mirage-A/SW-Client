package com.mirage.controller;

public class Platform {
    public static final byte DESKTOP = 0;
    public static final byte ANDROID = 1;
    public static final byte IOS = 2;
    public static byte TYPE = DESKTOP;
    /**
     * Путь до папки с ресурсами, для разных платформ он разный
     */
    public static String ASSETS_PATH = "";
}
