package com.g3ida.withflyingcolours.core

import com.badlogic.gdx.assets.AssetManager
import games.rednblack.editor.renderer.resources.AsyncResourceManager
import games.rednblack.editor.renderer.resources.ResourceManager
import games.rednblack.editor.renderer.resources.ResourceManagerLoader

class AssetsLoader(val filePath: String) {
    private lateinit var _assetManager: AssetManager
    fun load(): ResourceManager {
        _assetManager = AssetManager()
        _assetManager.setLoader(AsyncResourceManager::class.java, ResourceManagerLoader(_assetManager.fileHandleResolver))
        _assetManager.load(filePath, AsyncResourceManager::class.java)
        _assetManager.finishLoading()
        return _assetManager.get(filePath, AsyncResourceManager::class.java)
    }

    fun unload() {
        _assetManager.unload(filePath)
    }

    fun dispose() {
        _assetManager.dispose()
    }
}