package com.g3ida.withflyingcolours.screens;

import com.artemis.World;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.g3ida.withflyingcolours.core.AssetsLoader;
import com.g3ida.withflyingcolours.core.GameSettings;
import com.g3ida.withflyingcolours.core.SceneMapper;
import com.g3ida.withflyingcolours.core.camera.CameraSystem;
import com.g3ida.withflyingcolours.core.platform.ColorPlatformRenderingSystem;
import com.g3ida.withflyingcolours.core.player.animation.PlayerAnimationSystem;
import com.g3ida.withflyingcolours.core.player.controller.PlayerControllerSystem;
import com.g3ida.withflyingcolours.core.player.movement.PlayerJumpSystem;
import com.g3ida.withflyingcolours.core.player.movement.PlayerRotationSystem;
import com.g3ida.withflyingcolours.core.player.movement.PlayerWalkSystem;

import games.rednblack.editor.renderer.SceneConfiguration;
import games.rednblack.editor.renderer.SceneLoader;
import games.rednblack.editor.renderer.resources.ResourceManager;

public class GameScreen extends ScreenAdapter {

    private final SceneLoader _sceneLoader;

    private final Viewport _viewport;
    private final OrthographicCamera _camera;

    private final AssetsLoader _assetsLoader;

    private final World _engine;

    public GameScreen() {

        _assetsLoader = new AssetsLoader("project.dt");

        ResourceManager _resourceManager = _assetsLoader.load();

        // prepare screen configuration
        SceneConfiguration sceneConfiguration = new SceneConfiguration();
        // add systems
        CameraSystem cameraSystem = new CameraSystem(0, 20, 0, 7);
        sceneConfiguration.addSystem(cameraSystem);
        sceneConfiguration.addSystem(new PlayerControllerSystem());
        sceneConfiguration.addSystem(new PlayerRotationSystem());
        sceneConfiguration.addSystem(new PlayerJumpSystem());
        sceneConfiguration.addSystem(new PlayerWalkSystem());
        sceneConfiguration.addSystem(new PlayerAnimationSystem());
        sceneConfiguration.addSystem(new ColorPlatformRenderingSystem());

        sceneConfiguration.setResourceRetriever(_resourceManager);
        _sceneLoader = new SceneLoader(sceneConfiguration);

        _engine = _sceneLoader.getEngine();

        _camera = new OrthographicCamera();
        _viewport = new ExtendViewport(13, 7, _camera);
        GameSettings.mainViewPort = _viewport;

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

    public void update () {
        _camera.update();
    }

    @Override
    public void render (float delta) {
        super.render(delta);
        this.update();

        _viewport.apply();
        _engine.setDelta(delta);
        _engine.process();
    }

    @Override
    public void pause () {

    }
}
