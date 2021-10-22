package com.g3ida.withflyingcolours.core;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.Viewport;

import games.rednblack.editor.renderer.SceneLoader;
import games.rednblack.editor.renderer.resources.AsyncResourceManager;
import games.rednblack.editor.renderer.resources.ResourceManager;
import games.rednblack.editor.renderer.resources.ResourceManagerLoader;

public class AssetsLoader {

    public String _filePath;

    private AssetManager _assetManager;

    public AssetsLoader(String filePath) {
        _filePath = filePath;
    }

    public ResourceManager load() {
        _assetManager = new AssetManager();
        _assetManager.setLoader(AsyncResourceManager.class, new ResourceManagerLoader(_assetManager.getFileHandleResolver()));
        _assetManager.load(_filePath, AsyncResourceManager.class);

        _assetManager.finishLoading();

        return _assetManager.get(_filePath, AsyncResourceManager.class);
    }

    public void unload() {
        _assetManager.unload(_filePath);
    }

    public void dispose() {
        _assetManager.dispose();
    }
}
