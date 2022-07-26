package com.g3ida.withflyingcolours.core.common

import com.badlogic.gdx.assets.AssetManager
import games.rednblack.editor.renderer.resources.AsyncResourceManager
import games.rednblack.editor.renderer.resources.ResourceManager
import games.rednblack.editor.renderer.resources.ResourceManagerLoader

class AssetsLoader(val filePath: String) {
    private lateinit var mAssetManager: AssetManager
    fun load(): ResourceManager {
        mAssetManager = AssetManager()
        mAssetManager.setLoader(AsyncResourceManager::class.java, ResourceManagerLoader(mAssetManager.fileHandleResolver))
        mAssetManager.load(filePath, AsyncResourceManager::class.java)
        mAssetManager.finishLoading()
        return mAssetManager.get(filePath, AsyncResourceManager::class.java)
    }

    fun unload() {
        mAssetManager.unload(filePath)
    }

    fun dispose() {
        mAssetManager.dispose()
    }
}