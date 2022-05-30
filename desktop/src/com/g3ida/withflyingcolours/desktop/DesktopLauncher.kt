package com.g3ida.withflyingcolours.desktop

import kotlin.jvm.JvmStatic
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.g3ida.withflyingcolours.WithFlyingColours

object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val config = Lwjgl3ApplicationConfiguration()
        config.setTitle("With flying colours")
        config.setWindowedMode(800, 600)
        Lwjgl3Application(WithFlyingColours(), config)
    }
}