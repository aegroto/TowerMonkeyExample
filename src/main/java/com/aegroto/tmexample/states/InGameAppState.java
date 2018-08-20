package com.aegroto.tmexample.states;

import com.aegroto.tmexample.entity.EntitySoldier;
import com.aegroto.towermonkey.state.EntityAppState;
import com.aegroto.towermonkey.state.MapAppState;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author lorenzo
 */
public class InGameAppState extends BaseAppState {
    private final MapAppState mapAppState;
    private final EntityAppState entityAppState;

    private final Node sceneRootNode, rootNode;

    private final AnalogListener analogListener = new AnalogListener(){    
        @Override
        public void onAnalog(String name, float value, float tpf) {
            if(name.equals("SpawnEntity")) {
                if(entityAppState.isEnabled()) {
                    entityAppState.addWalkingEntity(new EntitySoldier(getApplication().getAssetManager(), 1f));
                }
            }
        }
    };
    
    public InGameAppState(Node sceneRootNode) {
        this.sceneRootNode = sceneRootNode;

        this.rootNode = new Node();
        this.mapAppState = new MapAppState(rootNode) {
            @Override
            protected void onEnable() {
                super.onEnable();
            }
        };

        this.entityAppState = new EntityAppState(sceneRootNode);
    }

    @Override
    protected void initialize(Application app) {
        getApplication().getInputManager().addMapping("SpawnEntity", new KeyTrigger(KeyInput.KEY_SPACE));

        getApplication().getInputManager().addListener(analogListener, "SpawnEntity");
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

        mapAppState.setMapSize(256);
        mapAppState.setGridSize(64);
        mapAppState.setDitchSize(24);
        mapAppState.setBattlegroundOffsetX(24);
        mapAppState.setBattlegroundOffsetY(24);
        mapAppState.setSeed(47372);

        mapAppState.setMinDitchHeight(-8.0f);
        mapAppState.setDitchVariation(.5f);

        mapAppState.setPathTileBorderFactor(.15f);
        mapAppState.setPathTileBorderNeckFactor(.05f);
        mapAppState.setPathVariation(.25f);
        
        mapAppState.setMinHillHeight(2f);        
        mapAppState.setHillVariation(.25f);

        int battlegroundGridSize = mapAppState.getMapSize() - mapAppState.getDitchSize() * 2;
                
        mapAppState.setTotalMountains(10);
        mapAppState.setMountainMinSize((int) (battlegroundGridSize * .05f));
        mapAppState.setMountainMaxSize((int) (battlegroundGridSize * .1f));
        mapAppState.setMountainMinLevels(battlegroundGridSize * 40);
        mapAppState.setMountainMaxLevels(battlegroundGridSize * 50);
        mapAppState.setMountainBorderFragmentation(.5f);
        mapAppState.setMinMountainHeight(2f);     
        mapAppState.setMountainVariation(.1f);
        mapAppState.setMountainBaseTerrainThreshold(3f);

        getStateManager().attach(mapAppState);
        getStateManager().attach(entityAppState);

    }

    @Override
    protected void onDisable() {
        rootNode.removeFromParent();
    }

    @Override
    public void update(float tpf) {

    }
}
