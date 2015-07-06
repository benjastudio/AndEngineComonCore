package com.benjastudio.andengine.extension.comoncore.resource;

import java.util.ArrayList;
import java.util.List;

import org.andengine.util.progress.IProgressListener;

public class MultipleProgressListener {

	private IProgressListener mProgressListener;
	List<ProgressLevel> progressLevelList;

	class ProgressLevel {
		float from, to;

		public ProgressLevel(float pFrom, float pTo) {
			from = pFrom;
			to = pTo;
		}
	}

	public MultipleProgressListener(IProgressListener pProgressListener) {
		mProgressListener = pProgressListener;
		progressLevelList = new ArrayList<MultipleProgressListener.ProgressLevel>();
	}

	public void addDeeperLevel(float pFrom, float pTo) {
		progressLevelList.add(new ProgressLevel(pFrom, pTo));
	}

	public void backFromDeeperLevel() {
		changeProgress(100);
		progressLevelList.remove(progressLevelList.size() - 1);
	}

	public void changeProgress(float pProgress) {
		float progress = pProgress;
		for (int deep = progressLevelList.size() - 1; deep >= 0; deep--) {
			progress = computeProgress(progress, deep);
		}
		mProgressListener.onProgressChanged((int) progress);
	}

	public float computeProgress(float pProgress, int pDeep) {
		final ProgressLevel level = progressLevelList.get(pDeep);
		return level.from + pProgress * (level.to - level.from) / 100f;
	}
}
