package com.g3ida.withflyingcolours.core.scripts;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.physics.box2d.World;

import games.rednblack.editor.renderer.scripts.BasicScript;

public abstract class GameScript extends BasicScript {

    private PooledEngine _engine;
    private World _world;

    public GameScript(PooledEngine engine, World world) {
        _engine = engine;
        _world = world;
    }

    public PooledEngine getEngine() {
        return _engine;
    }
    public World getWorld() {
        return _world;
    }

}
