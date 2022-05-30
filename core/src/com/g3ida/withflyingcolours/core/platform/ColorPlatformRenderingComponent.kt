package com.g3ida.withflyingcolours.core.platform;

import com.artemis.PooledComponent;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.g3ida.withflyingcolours.Constants;
import com.g3ida.withflyingcolours.Utils;

import games.rednblack.editor.renderer.components.ShaderComponent;
import games.rednblack.editor.renderer.data.MainItemVO;

public class ColorPlatformRenderingComponent extends PooledComponent {

    public static final float SPLASH_DURATION = 1f;

    public boolean doColorSplash = false;
    public float splashTimer = 0f;
    public Vector2 contactPosition = new Vector2();

    private static final String _SHADER_NAME = "color_splash";
    private ShaderProgram _shader;

    public ColorPlatformRenderingComponent() {

    }

    public void Init(int entityId, World engine) {
        _shader = Utils.loadShader(_SHADER_NAME);
        if(!_shader.isCompiled()){
            Gdx.app.log(Constants.LOG_SHADER_PREFIX, _shader.getLog());
        }
        ShaderComponent shaderComponent = engine.edit(entityId).create(ShaderComponent.class);
        shaderComponent.renderingLayer = MainItemVO.RenderingLayer.SCREEN;
        shaderComponent.setShader(_SHADER_NAME, _shader);
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
