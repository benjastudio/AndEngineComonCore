package com.benjastudio.andengine.extension.comoncore;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.benjastudio.andengine.extension.comoncore.audio.MusicManager;
import android.app.Activity;

public class EngineBundle {
	public Activity activity;
	public Camera camera;
	public VertexBufferObjectManager vbom;
	public Engine engine;
	public MusicManager musicManager;
}
