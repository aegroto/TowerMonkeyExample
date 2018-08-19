package com.aegroto.tmexample.states;

import com.aegroto.tmexample.entity.EntitySoldier;
import com.aegroto.towermonkey.state.EntityAppState;
import com.aegroto.towermonkey.state.MapAppState;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.util.MaterialDebugAppState;

/**
 *
 * @author lorenzo
 */
public class InGameAppState extends BaseAppState {
    private final MapAppState mapAppState;
    private final EntityAppState entityAppState;
    private MaterialDebugAppState materialDebugAppState;

    private final Node sceneRootNode, rootNode;


    // private AssetKey<Material> mapMaterialKey, seaMaterialKey;

    /*private final AnalogListener analogListener = new AnalogListener(){    
        @Override
        public void onAnalog(String name, float value, float tpf) {
            if(name.equals("UpdateMats")) {
                reloadMapMaterials(true);
                mapAppState.updateMaterials();
            }
        }
    };*/
    
    public InGameAppState(Node sceneRootNode) {
        this.sceneRootNode = sceneRootNode;

        this.rootNode = new Node();
        this.mapAppState = new MapAppState(rootNode, 256, 64, 24, 24, 24) {
            @Override
            protected void onEnable() {
                super.onEnable();

                materialDebugAppState.registerBinding(new KeyTrigger(KeyInput.KEY_R), mapGeom);
            }
        };

        this.entityAppState = new EntityAppState(sceneRootNode);
    }

    @Override
    protected void initialize(Application app) {
        materialDebugAppState = new MaterialDebugAppState();
        getStateManager().attach(materialDebugAppState);
    }

    @Override
    protected void cleanup(Application app) { }

    @Override
    protected void onEnable() {
        sceneRootNode.attachChild(rootNode);

        getApplication().getCamera().setLocation(new Vector3f(0, 60, 65));
        getApplication().getCamera().setRotation(new Quaternion().fromAngles(FastMath.QUARTER_PI, FastMath.PI, 0f));      
        
        Material mapMaterial = getApplication().getAssetManager().loadMaterial("Materials/TowerDefenseTerrain.j3m");

        Material seaMaterial = getApplication().getAssetManager().loadMaterial("Materials/TowerDefenseSea.j3m"); 
        seaMaterial.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);

        mapAppState.setMapMaterial(mapMaterial);
        mapAppState.setSeaMaterial(seaMaterial);
        
        getStateManager().attach(mapAppState);
        getStateManager().attach(entityAppState);

    }

    @Override
    protected void onDisable() {
        rootNode.removeFromParent();
    }

    @Override
    public void update(float tpf) {
        if(entityAppState.isEnabled() && FastMath.nextRandomFloat() > .96f) {
            entityAppState.addEntity(new EntitySoldier(getApplication().getAssetManager()));
        }
    }

    /*protected void reloadMapMaterials(boolean clearCache) {
        if(clearCache) {
            getApplication().getAssetManager().deleteFromCache(mapMaterialKey);
            getApplication().getAssetManager().deleteFromCache(seaMaterialKey);                
        }

        seaMaterialKey = (AssetKey<Material>) seaMaterial.getKey();

        mapAppState.setMapMaterial(mapMaterial);
        mapAppState.setSeaMaterial(seaMaterial);
    }*/
}
