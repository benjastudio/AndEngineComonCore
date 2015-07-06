package com.benjastudio.andengine.extension.comoncore;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;

import android.util.Log;

public abstract class BaseScript implements IScript {

	final static public String TAG = "BaseScript";

	Scriptable scriptableObject;
	Queue<Pair> runnableQueue = new ArrayBlockingQueue<Pair>(99);

	class Pair {
		float d;
		Runnable r;

		public Pair(float pD, Runnable pR) {
			d = pD;
			r = pR;
		}
	}

	public BaseScript() {
	}

	public static void runScript(final String basePath,
			final String scriptName, Scriptable pScriptableObject) {
		if (scriptName == null)
			return;
		Log.d(TAG, "runScript(" + scriptName + ")");
		String className = basePath + "." + scriptName;
		try {
			Class<?> clazz = Class.forName(className);
			Constructor<?> ctor = clazz.getConstructor();
			BaseScript script = (BaseScript) ctor.newInstance();
			script.setObject(pScriptableObject);
			script.init(pScriptableObject);
			script.execute();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, e.getMessage());
		}
	}

	protected abstract void init(final Scriptable pScriptableObject);

	protected void setObject(Scriptable pScriptableObject) {
		scriptableObject = pScriptableObject;
	}

	public void execute() {
		final Pair pair = runnableQueue.poll();
		if (pair != null) {
			scriptableObject.registerUpdateHandler(new TimerHandler(pair.d,
					false, new ITimerCallback() {
						@Override
						public void onTimePassed(TimerHandler pTimerHandler) {
							pair.r.run();
							execute();
						}
					}));
		}
	}

	protected void addAction(float delay, Runnable runnable) {
		runnableQueue.add(new Pair(delay, runnable));
	}

	protected void addAction(float delay, String methodName, Object... args) {
		addAction(delay, generateRunnable(methodName, args));
	}

	private Runnable generateRunnable(final String methodName,
			final Object... args) {
		return new Runnable() {
			@Override
			public void run() {
				try {
					Class<?>[] parameterTypes = new Class<?>[args.length];
					for (int i = 0; i < args.length; i++)
						parameterTypes[i] = args[i].getClass();
					Method method = scriptableObject.getClass().getMethod(
							methodName, parameterTypes);
					try {
						method.invoke(scriptableObject, args);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
			}
		};
	}
}
