package com.mirage.server

import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.files.FileHandle
import java.io.File

class DesktopFileHandleResolver : FileHandleResolver {

    override fun resolve(fileName: String?): FileHandle = FileHandle(File(fileName))
}