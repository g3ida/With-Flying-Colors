package com.g3ida.withflyingcolours.core.scripts;

import com.badlogic.gdx.physics.box2d.World;

import games.rednblack.editor.renderer.scripts.BasicScript;

public abstract class GameScript extends BasicScript {

    private final com.artemis.World _engine;
    private final World _world;

    public GameScript(com.artemis.World engine, World world) {
        _engine = engine;
        _world = world;
    }

    public com.artemis.World getEngine() {
        return _engine;
    }
    public World getWorld() {
        return _world;
    }

}
