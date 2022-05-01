package com.g3ida.withflyingcolours.core.camera;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;

import games.rednblack.editor.renderer.components.TransformComponent;
import games.rednblack.editor.renderer.components.ViewPortComponent;
import games.rednblack.editor.renderer.utils.ComponentRetriever;

public class CameraSystem extends IteratingSystem {

    private Entity _focus;
    private final float _xMin, _xMax, _yMin, _yMax;

    public CameraSystem(float xMin, float xMax, float yMin, float yMax) {
        super(Family.all(ViewPortComponent.class).get());

        this._xMin = xMin;
        this._xMax = xMax;
        this._yMin = yMin;
        this._yMax = yMax;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ViewPortComponent viewPortComponent = ComponentRetriever.get(entity, ViewPortComponent.class);
        Camera camera = viewPortComponent.viewPort.getCamera();

        if (_focus != null) {
            // FIXME: player position should be lower + camera movement should be softer
            TransformComponent transformComponent = ComponentRetriever.get(_focus, TransformComponent.class);
            if (transformComponent != null) {
                float x = Math.max(_xMin, Math.min(_xMax, transformComponent.x));
                float y = Math.max(_yMin, Math.min(_yMax, transformComponent.y));
                camera.position.set(x, y, 0);
            }
        }
    }

    public void setFocus(Entity focus) {
        this._focus = focus;
    }
}
