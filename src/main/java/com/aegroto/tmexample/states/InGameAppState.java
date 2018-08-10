package com.aegroto.tmexample.states;

import com.aegroto.towermonkey.state.MapAppState;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
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

    private final Node sceneRootNode, rootNode;
    
    public InGameAppState(Node sceneRootNode) {
        this.sceneRootNode = sceneRootNode;

        this.rootNode = new Node();
        this.mapAppState = new MapAppState(rootNode, 256, 64, 24, 24, 24);        
    }

    @Override
    protected void initialize(Application app) {
        MaterialDebugAppState materialDebugAppState = new MaterialDebugAppState();

        materialDebugAppState.registerBinding(new KeyTrigger(KeyInput.KEY_U), rootNode);

        app.getStateManager().attach(materialDebugAppState);
    }

    @Override
    protected void cleanup(Application aplctn) { }

    @Override
    protected void onEnable() {
        sceneRootNode.attachChild(rootNode);

        getApplication().getCamera().setLocation(new Vector3f(0, 60, 65));
        getApplication().getCamera().setRotation(new Quaternion().fromAngles(FastMath.QUARTER_PI, FastMath.PI, 0f));      
        
        getStateManager().attach(mapAppState);
    }

    @Override
    protected void onDisable() {
        rootNode.removeFromParent();
    }    
}
