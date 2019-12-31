package com.mirage.client

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.files.FileHandle
import com.mirage.core.utils.Assets
import com.mirage.core.utils.ClientPlatform
import com.mirage.core.utils.Log
import java.io.File
import java.io.InputStream
import java.io.Reader

object GdxAssets : Assets {

    private val assetsResolver: FileHandleResolver = when (ClientPlatform.platform) {
        ClientPlatform.TEST -> FileHandleResolver {
            when {
                File(File("").absoluteFile.parentFile.absolutePath + "/android/assets").exists() ->
                    FileHandle(File(File("").absoluteFile.parentFile.absolutePath + "/android/assets/$it"))
                File(File("").absolutePath + "/android/assets").exists() ->
                    FileHandle(File(File("").absolutePath + "/android/assets/$it"))
                else -> {
                    Log.e("ERROR: Assets directory not found.")
                    null
                }
            }
        }
        ClientPlatform.DESKTOP_TEST -> FileHandleResolver {
            FileHandle(File(File("").absolutePath + "/android/assets/$it"))
        }
        else -> FileHandleResolver {
            Gdx.files.internal(it)
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