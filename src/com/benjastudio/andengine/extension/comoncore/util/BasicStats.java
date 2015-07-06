package com.benjastudio.andengine.extension.comoncore.util;

import java.util.Arrays;

public class BasicStats {

	private float[] values;
	private float average, sum;
	private int i, valuesSet;

	public BasicStats(int sampleSize) {
		values = new float[sampleSize];
		clear();
	}

	public void add(float value) {
		values[i++] = value;
		i = i % values.length;
		if (valuesSet < values.length)
			valuesSet++;
		sum = 0;
		for (int j = 0; j < valuesSet; j++) {
			sum += values[j];
		}
		average = sum / valuesSet;
	}

	public float getSum() {
		return sum;
	}

	public float getAverage() {
		return average;
	}

	public int getValueSet() {
		return valuesSet;
	}

	public int getSampleSize() {
		return values.length;
	}

	public void clear() {
		i = 0;
		valuesSet = 0;
		Arrays.fill(values, 0);
		sum = 0;
		average = 0;
	}
}
