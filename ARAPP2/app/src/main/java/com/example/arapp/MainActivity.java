package com.example.arapp;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

public class MainActivity extends AppCompatActivity {
private Fragment arFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);
        //detect plane and motion
        ((ArFragment) arFragment).setOnTapArPlaneListener(new BaseArFragment.OnTapArPlaneListener() {
            @Override
            public void onTapPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent) {
                Anchor anchor = hitResult.createAnchor();//describe fixed location in real world, basically render 3d model
                ModelRenderable.builder()
                        .setSource(MainActivity.this, Uri.parse("M-FF_iOS_HERO_Tony_Stark_Iron_Man_Civil_War.sfb"))
                        .build()
                        .thenAccept(modelRenderable -> addModelToScene(anchor,modelRenderable))
                        .exceptionally(throwable -> {
                            AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);
                           builder.setMessage(throwable.getMessage())
                                   .show();
                           return null;
                        });

            }

            private void addModelToScene(Anchor anchor, ModelRenderable modelRenderable) {
                AnchorNode anchorNode = new AnchorNode(anchor); //Node that positions itself in real world. Anchor places field for model to sit, anchornode places this anchor in real world.
                TransformableNode transformableNode = new TransformableNode(((ArFragment) arFragment).getTransformationSystem()); //Zoom in and out real world
                transformableNode.setParent(anchorNode);
            transformableNode.setRenderable(modelRenderable);// render transformed model
                ((ArFragment) arFragment).getArSceneView().getScene().addChild(anchorNode);
                transformableNode.select();
            }
        });
    }
}
