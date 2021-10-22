package com.g3ida.withflyingcolours.core.scripts;

import com.badlogic.ashley.core.PooledEngine;

import games.rednblack.editor.renderer.scripts.BasicScript;

public abstract class GameScript extends BasicScript {

    private PooledEngine _engine;

    public GameScript(PooledEngine engine) {
        _engine = engine;
    }

    public PooledEngine getEngine() {
        return _engine;
    }
}
