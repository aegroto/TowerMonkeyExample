package com.aegroto.tmexample.entity;

import com.aegroto.towermonkey.entity.WalkingEntity;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

public class EntitySoldier extends WalkingEntity {
    public EntitySoldier(AssetManager assetManager, float walkingSpeed) {
        super(assetManager, walkingSpeed);
    }

    @Override
    protected void initializeGeometry(AssetManager assetManager) {
        geometry = new Geometry("Soldier geometry", new Box(.5f, 2f, .5f));
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.White);
        geometry.setMaterial(mat);
    }

    protected void onPathEnd() {

    }
}