package com.mirage.core.utils

import java.io.InputStream
import java.io.Reader

interface Assets {

    fun loadReader(path: String): Reader?

    fun loadInputStream(path: String): InputStream?

}