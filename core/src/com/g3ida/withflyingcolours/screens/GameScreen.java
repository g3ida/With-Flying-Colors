package com.g3ida.withflyingcolours.screens;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.g3ida.withflyingcolours.core.AssetsLoader;
import com.g3ida.withflyingcolours.core.SceneMapper;
import com.g3ida.withflyingcolours.core.scripts.Player;

import games.rednblack.editor.renderer.SceneLoader;
import games.rednblack.editor.renderer.resources.ResourceManager;
import games.rednblack.editor.renderer.utils.ItemWrapper;

public class GameScreen extends ScreenAdapter {

    private SceneLoader _sceneLoader;
    private ResourceManager _resourceManager;

    private Viewport _viewport;
    private OrthographicCamera _camera;

    private AssetsLoader _assetsLoader;

    private PooledEngine _engine;

    public GameScreen() {

        _assetsLoader = new AssetsLoader("project.dt");

        _resourceManager = _assetsLoader.load();
        _sceneLoader = new SceneLoader(_resourceManager);

        _engine = _sceneLoader.getEngine();

        _camera = new OrthographicCamera();
        _viewport = new ExtendViewport(13, 7, _camera);

        _sceneLoader.loadScene("MainScene", _viewport);

        SceneMapper sceneMapper = new SceneMapper();
        sceneMapper.mapScripts(_sceneLoader);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        _viewport.update(width, height);

        if (width != 0 && height != 0) {
            _sceneLoader.resize(width, height);
        }
    }

    @Override
    public void dispose() {
        _assetsLoader.dispose();
        super.dispose();
    }

    public void update (float deltaTime) {
        _camera.update();
    }

    @Override
    public void render (float delta) {
        super.render(delta);
        update(delta);

        _viewport.apply();
        _engine.update(delta);
    }

    @Override
    public void pause () {

    }
}
