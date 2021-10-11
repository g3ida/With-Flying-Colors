package com.g3ida.withflyingcolours;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;

public class MainMenuScreen extends ScreenAdapter {

    WithFlyingColours game;

    public MainMenuScreen(WithFlyingColours game); {
        super();
        this.game = game;
    }

    @Override
    public void render (float delta) {
        super.render(delta);
        GL20 gl = Gdx.gl;
        gl.glClearColor(1, 0, 0, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void pause () {
        super.pause();
    }
}
