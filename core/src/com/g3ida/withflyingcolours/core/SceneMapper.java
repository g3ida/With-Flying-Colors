package com.g3ida.withflyingcolours.core;

import com.artemis.Entity;
import com.badlogic.gdx.physics.box2d.World;
import com.g3ida.withflyingcolours.core.scripts.GameScript;

import java.lang.reflect.InvocationTargetException;

import games.rednblack.editor.renderer.SceneLoader;
import games.rednblack.editor.renderer.components.MainItemComponent;
import games.rednblack.editor.renderer.components.NodeComponent;
import games.rednblack.editor.renderer.utils.ComponentRetriever;
import games.rednblack.editor.renderer.utils.ItemWrapper;

public class SceneMapper {

    private final String _packageName;
    private com.artemis.World _engine;
    private ItemWrapper _rootWrapper;
    private World _world;

    public SceneMapper() {
        _packageName = this.getClass().getPackage().getName();
    }

    private void parseSceneGraph(Entity root) {
        NodeComponent nodeComponent = ComponentRetriever.get(root.getId(), NodeComponent.class, _engine);
        if(nodeComponent != null) {
            for (Integer child : nodeComponent.children) {
                mapEntity(_engine.getEntity(child));
            }
        }
    }

    private void mapEntity(Entity entity) {
        MainItemComponent mainItemComponent = ComponentRetriever.get(entity.getId(), MainItemComponent.class, _engine);

        if (mainItemComponent.itemIdentifier == null || mainItemComponent.itemIdentifier.isEmpty()) {
            return;
        }

        try {
            String scriptName = mainItemComponent.customVariables.getHashMap().get("script");
            if(scriptName != null) {
                String ScriptClass = _packageName.concat(".scripts.").concat(scriptName);
                Class<?> entityClass = Class.forName(ScriptClass);
                GameScript script = (GameScript) entityClass.getDeclaredConstructor(com.artemis.World.class, World.class).newInstance(_engine, _world);
                _rootWrapper.getChild(mainItemComponent.itemIdentifier).addScript(script);
            }
        }
        catch (ClassNotFoundException | InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void mapScripts(SceneLoader sceneLoader) {
        _engine = sceneLoader.getEngine();
        int rootEnityId = sceneLoader.getRoot();
        _rootWrapper = new ItemWrapper(rootEnityId, _engine);
        _world = sceneLoader.getWorld();
        parseSceneGraph(_engine.getEntity(rootEnityId));
    }
}
