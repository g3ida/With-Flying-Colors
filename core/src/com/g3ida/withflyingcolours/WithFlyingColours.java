package com.g3ida.withflyingcolours;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class WithFlyingColours extends Game {
	SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		GL20 gl = Gdx.gl;
		gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
