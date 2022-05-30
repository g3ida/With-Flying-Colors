package com.g3ida.withflyingcolours.core.camera;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;

import games.rednblack.editor.renderer.components.TransformComponent;
import games.rednblack.editor.renderer.components.ViewPortComponent;

@All(ViewPortComponent.class)
public class CameraSystem extends IteratingSystem {

    private int _focusEntityId = -1;
    private ComponentMapper<ViewPortComponent> _mViewport;
    private ComponentMapper<TransformComponent> _mTransform;

    private final float _xMin, _xMax, _yMin, _yMax;

    public CameraSystem(float xMin, float xMax, float yMin, float yMax) {

        this._xMin = xMin;
        this._xMax = xMax;
        this._yMin = yMin;
        this._yMax = yMax;
    }

    public void setFocus(int entityId) {
        this._focusEntityId = entityId;
    }

    @Override
    protected void process(int entityId) {
        ViewPortComponent viewPortComponent = _mViewport.get(entityId);

        Camera camera = viewPortComponent.viewPort.getCamera();

        if (_focusEntityId != -1) {
            // FIXME: player position should be lower + camera movement should be softer
            TransformComponent transformComponent = _mTransform.get(_focusEntityId);
            if (transformComponent != null) {
                float x = Math.max(_xMin, Math.min(_xMax, transformComponent.x));
                float y = Math.max(_yMin, Math.min(_yMax, transformComponent.y));
                camera.position.set(x, y, 0);
            }
        }
    }
}
