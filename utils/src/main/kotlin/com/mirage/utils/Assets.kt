package com.mirage.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.files.FileHandle
import com.google.gson.Gson
import java.io.File
import java.io.InputStream
import java.io.Reader

object Assets {

    private val assetsResolver : FileHandleResolver = when (PLATFORM) {
        "test" -> FileHandleResolver {
            if (File(File("").absoluteFile.parentFile.absolutePath + "/android/assets").exists()) {
                FileHandle(File(File("").absoluteFile.parentFile.absolutePath + "/android/assets/$it"))
            }
            else if (File(File("").absolutePath + "/android/assets").exists()) {
                FileHandle(File(File("").absolutePath + "/android/assets/$it"))
            }
            else {
                Log.e("ERROR: Assets directory not found.")
                null
            }
        }
        "desktop-test" -> FileHandleResolver {
            FileHandle(File(File("").absolutePath + "/android/assets/$it"))
        }
        "server" -> FileHandleResolver {
            FileHandle(File(File("").absolutePath + "/android/assets/$it"))
        }
        "desktop", "android", "ios" -> FileHandleResolver {
            Gdx.files.internal(it)
        }
        else -> {
            Log.e("Unknown platform: $PLATFORM")
            FileHandleResolver {
                Gdx.files.internal(it)
            }
        }
    }

    fun loadFile(path: String) : FileHandle? =
        try {
            val file = assetsResolver.resolve(path)
            if (file == null || !file.exists()) {
                Log.e("File not found: $path")
                null
            }
            else file
        }
        catch (ex: Exception) {
            Log.e("File not found: $path")
            null
        }


    fun loadReader(path: String) : Reader? =
            loadFile(path)?.reader()

    fun loadScript(name: String) : Reader? =
            loadReader("scripts/$name.lua")


    fun loadAnimation(name: String) : InputStream? =
        loadFile("animations/$name.xml")?.read()



}