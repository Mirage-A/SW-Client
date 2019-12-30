package com.mirage.client

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.files.FileHandle
import com.mirage.core.utils.Assets
import com.mirage.core.utils.Log
import com.mirage.core.utils.PLATFORM
import java.io.File
import java.io.InputStream
import java.io.Reader

object GdxAssets : Assets {

    private val assetsResolver: FileHandleResolver = when (PLATFORM) {
        "test" -> FileHandleResolver {
            if (File(File("").absoluteFile.parentFile.absolutePath + "/android/assets").exists()) {
                FileHandle(File(File("").absoluteFile.parentFile.absolutePath + "/android/assets/$it"))
            } else if (File(File("").absolutePath + "/android/assets").exists()) {
                FileHandle(File(File("").absolutePath + "/android/assets/$it"))
            } else {
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

    internal fun loadFile(path: String): FileHandle? =
            try {
                val file = assetsResolver.resolve(path)
                if (file == null || !file.exists()) {
                    Log.e("File not found: $path")
                    null
                } else file
            } catch (ex: Exception) {
                Log.e("File not found: $path")
                null
            }


    override fun loadReader(path: String): Reader? =
            loadFile(path)?.reader()


    override fun loadInputStream(path: String): InputStream? =
            loadFile(path)?.read()



}