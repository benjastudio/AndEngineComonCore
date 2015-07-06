package com.benjastudio.andengine.extension.comoncore.level;

import com.benjastudio.andengine.extension.comoncore.SettingsManager;

public abstract class ILevelManager<T extends ILevel> {

	public T currentLevel;

	public abstract T newLevel(int world, int level);

	public abstract String levelFilePath(int world, int level);

	public abstract boolean exists(int world, int level);

	public abstract void loadLevelResources();

	public abstract void playCurrentLevel();

	public abstract void playFirstLevelForThisWorld();

	public ILevelManager() {
	}

	public T getPreviousLevelOf(int world, int level) {
		if (level > 1 && exists(world, level - 1)) {
			return newLevel(world, level - 1);
		} else if (world > 1) {
			final int n = getLevelNumber(world - 1);
			if (exists(world - 1, n))
				return newLevel(world - 1, n);
		}
		return null;
	}

	public T getPreviousLevelOf(T level) {
		return getPreviousLevelOf(level.getWorld(), level.getLevel());
	}

	public boolean hasPreviousLevel(int world, int level) {
		return getPreviousLevelOf(world, level) != null;
	}

	public boolean hasPreviousLevel(T level) {
		return hasPreviousLevel(level.getWorld(), level.getLevel());
	}

	public boolean hasPreviousLevel() {
		return hasPreviousLevel(currentLevel);
	}

	public void goToLevel(int world, int level) {
		setCurrentLevel(newLevel(world, level));
	}

	public void goToPreviousLevel() {
		T previous = getPreviousLevelOf(currentLevel);
		if (previous != null) {
			setCurrentLevel(previous);
			SettingsManager.getInstance().setCurrentWorld(
					currentLevel.getWorld());
		}
	}

	public void goToNextLevel() {
		T next = getNextLevelOf(currentLevel);
		if (next != null) {
			setCurrentLevel(next);
			SettingsManager.getInstance().setCurrentWorld(
					currentLevel.getWorld());
		}
	}

	public T getNextLevelOf(int world, int level) {
		if (exists(world, level + 1))
			return newLevel(world, level + 1);
		else if (exists(world + 1, 1))
			return newLevel(world + 1, 1);
		else
			return null;
	}

	public T getNextLevelOf(T level) {
		return getNextLevelOf(level.getWorld(), level.getLevel());
	}

	public boolean hasNextLevel(int world, int level) {
		return getNextLevelOf(world, level) != null;
	}

	public boolean hasNextLevel(T level) {
		return hasNextLevel(level.getWorld(), level.getLevel());
	}

	public boolean hasNextLevel() {
		return hasNextLevel(currentLevel);
	}

	public void setCurrentLevel(int world, int level) {
		setCurrentLevel(newLevel(world, level));
	}

	public void setCurrentLevel(T level) {
		currentLevel = level;
	}

	public int getWorldNumber() {
		int world = 0;
		while (exists(++world, 1))
			;
		return world - 1;
	}

	public int getLevelNumber(int world) {
		int level = 0;
		while (exists(world, ++level))
			;
		return level - 1;
	}
}
