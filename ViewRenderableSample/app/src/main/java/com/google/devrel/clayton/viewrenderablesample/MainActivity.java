/*
 * Copyright 2018 Google LLC. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.devrel.clayton.viewrenderablesample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;

public class MainActivity extends AppCompatActivity {

  private ArFragment arFragment;
  private Renderable renderable;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

    arFragment.setOnTapArPlaneListener(
            (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
              if (renderable == null) {
                return;
              }

              // Create the Anchor.
              Anchor anchor = hitResult.createAnchor();
              AnchorNode anchorNode = new AnchorNode(anchor);
              anchorNode.setParent(arFragment.getArSceneView().getScene());

              anchorNode.setRenderable(renderable);
            });

    ViewRenderable.builder()
            .setView(this, R.layout.view_ar_point)
            .build().thenAccept(viewRenderable -> renderable = viewRenderable)
            .exceptionally(
                    throwable -> {
                      Toast toast =
                              Toast.makeText(this, "Unable to load view renderable", Toast.LENGTH_LONG);
                      toast.setGravity(Gravity.CENTER, 0, 0);
                      toast.show();
                      return null;
                    });
  }
}
