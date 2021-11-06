package com.g3ida.withflyingcolours.core.platform;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;

import games.rednblack.editor.renderer.components.BaseComponent;
import games.rednblack.editor.renderer.components.ShaderComponent;
import games.rednblack.editor.renderer.data.MainItemVO;

public class ColorPlatformRenderingComponent implements BaseComponent {

    public static final float SPLASH_DURATION = 1f;

    public boolean doColorSplash = false;
    public float splashTimer = 0f;
    public Vector2 contactPosition = new Vector2();

    private ShaderProgram _shader;

    public ColorPlatformRenderingComponent(Entity entity) {
        _shader = new ShaderProgram(
                Gdx.files.internal("shaders/invert.vert"),
                Gdx.files.internal("shaders/invert.frag"));

        if(!_shader.isCompiled()){
            Gdx.app.log("ShaderTest", _shader.getLog());
        }

        ShaderComponent shaderComponent = new ShaderComponent();
        shaderComponent.renderingLayer = MainItemVO.RenderingLayer.SCREEN;
        shaderComponent.setShader("colorShader", _shader);

        entity.add(shaderComponent);
    }

    @Override
    public void reset() {
        this.splashTimer = 0f;
        this.contactPosition = new Vector2();
    }

    public void dispose() {
        if(_shader != null) {
            _shader.dispose();
        }
    }
}
