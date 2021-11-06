package com.g3ida.withflyingcolours.core.platform;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.g3ida.withflyingcolours.core.GameSettings;

import games.rednblack.editor.renderer.components.ShaderComponent;
import games.rednblack.editor.renderer.components.TransformComponent;
import games.rednblack.editor.renderer.data.ShaderUniformVO;
import games.rednblack.editor.renderer.systems.render.HyperLap2dRenderer;
import games.rednblack.editor.renderer.utils.ComponentRetriever;

public class ColorPlatformRenderingSystem extends IteratingSystem {

    public ColorPlatformRenderingSystem() {
        super(Family.all(
                ColorPlatformRenderingComponent.class,
                ShaderComponent.class,
                TransformComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ColorPlatformRenderingComponent renderingComponent = ComponentRetriever.get(entity, ColorPlatformRenderingComponent.class);

        if (renderingComponent.doColorSplash) {
            renderingComponent.doColorSplash = false;
            renderingComponent.splashTimer = ColorPlatformRenderingComponent.SPLASH_DURATION;

            ShaderComponent shaderComponent = ComponentRetriever.get(entity, ShaderComponent.class);
            ShaderUniformVO uniform = new ShaderUniformVO();
            uniform.set(HyperLap2dRenderer.timeRunning);
            shaderComponent.customUniforms.put("start_time", uniform);

            float ratioX = GameSettings.mainViewPort.getScreenWidth() / GameSettings.mainViewPort.getWorldWidth();
            float ratioY = GameSettings.mainViewPort.getScreenHeight() / GameSettings.mainViewPort.getWorldHeight();
            ShaderUniformVO u_contactpos = new ShaderUniformVO();
            u_contactpos.set(renderingComponent.contactPosition.x * ratioX*2, renderingComponent.contactPosition.y * ratioY*2);
            shaderComponent.customUniforms.put("u_contactpos", u_contactpos);
        }

        if (renderingComponent.splashTimer > 0f) {
            ShaderComponent shaderComponent = ComponentRetriever.get(entity, ShaderComponent.class);

            ShaderUniformVO u_resolution = new ShaderUniformVO();
            u_resolution.set(GameSettings.mainViewPort.getScreenWidth()*2, GameSettings.mainViewPort.getScreenHeight()*2);
            shaderComponent.customUniforms.put("u_resolution", u_resolution);

            ShaderUniformVO u_campos = new ShaderUniformVO();
            u_campos.set(GameSettings.mainViewPort.getCamera().position.x - GameSettings.mainViewPort.getScreenWidth() * 0.5f, GameSettings.mainViewPort.getCamera().position.y - GameSettings.mainViewPort.getScreenHeight() * 0.5f);
            shaderComponent.customUniforms.put("u_campos", u_campos);

            //TransformComponent tc = ComponentRetriever.get(entity, TransformComponent.class);
            //ShaderUniformVO u_playerpos = new ShaderUniformVO();
            //u_playerpos.set(tc.x * ratioX*2, tc.y * ratioY*2);
            //shaderComponent.customUniforms.put("u_playerpos", u_playerpos);

            renderingComponent.splashTimer -= deltaTime;
        }
    }
}
