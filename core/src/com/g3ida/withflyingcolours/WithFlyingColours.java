package com.g3ida.withflyingcolours;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.g3ida.withflyingcolours.core.AssetsLoader;
import com.g3ida.withflyingcolours.screens.GameScreen;
import com.g3ida.withflyingcolours.screens.MainMenuScreen;

public class WithFlyingColours extends Game {
	SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new GameScreen());
	}

	@Override
	public void render () {
		clearScreen();
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}

	public void clearScreen() {
		GL20 gl = Gdx.gl;
		gl.glClearColor(0.1490f,0.1764f, 0.1960f, 1.0f);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
}
