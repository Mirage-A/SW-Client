package com.mirage.view.utils

import org.junit.jupiter.api.Test

internal class TemplateDrawersLoaderKtTest {

    @Test
    fun test1() {
        val templateReader = """{
  "name": "main-gate",
  "template": "main-gate",
  "type": "BUILDING",
  "width": 6.0,
  "height": 0.0,
  "isRigid": false,
  "drawers": {
    "default": {
      "type": "staticTexture",
      "textureName": "main-gate"
    }
  }
}""".reader()
        val template = loadDrawersFromTemplateReader(templateReader, "someTestyName")
        println(template)
    }
}