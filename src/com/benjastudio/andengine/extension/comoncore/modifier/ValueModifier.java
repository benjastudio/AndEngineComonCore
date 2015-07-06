package com.benjastudio.andengine.extension.comoncore.modifier;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.util.modifier.BaseSingleValueSpanModifier;
import org.andengine.util.modifier.ease.EaseLinear;
import org.andengine.util.modifier.ease.IEaseFunction;

public class ValueModifier extends BaseSingleValueSpanModifier<IEntity>
		implements IEntityModifier {

	float value;

	public float getValue() {
		return value;
	}

	@Override
	protected void onSetInitialValue(IEntity pItem, float pValue) {
		value = pValue;
	}

	@Override
	protected void onSetValue(IEntity pItem, float pPercentageDone, float pValue) {
		value = pValue;
	}

	private ValueModifier(ValueModifier valueModifier) {
		super(valueModifier);
	}

	public ValueModifier(final float pDuration, final float pFromValue,
			final float pToValue) {
		super(pDuration, pFromValue, pToValue, null, EaseLinear.getInstance());
	}

	public ValueModifier(final float pDuration, final float pFromValue,
			final float pToValue, final IEaseFunction pEaseFunction) {
		super(pDuration, pFromValue, pToValue, null, pEaseFunction);
	}

	public ValueModifier(final float pDuration, final float pFromValue,
			final float pToValue,
			final IModifierListener<IEntity> pModifierListener) {
		super(pDuration, pFromValue, pToValue, pModifierListener, EaseLinear
				.getInstance());
	}

	public ValueModifier(final float pDuration, final float pFromValue,
			final float pToValue,
			final IModifierListener<IEntity> pModifierListener,
			final IEaseFunction pEaseFunction) {
		super(pDuration, pFromValue, pToValue, pModifierListener, pEaseFunction);
	}

	@Override
	public ValueModifier deepCopy() throws DeepCopyNotSupportedException {
		return new ValueModifier(this);
	}

	@Override
	public void addModifierListener(
			org.andengine.util.modifier.IModifier.IModifierListener<IEntity> pModifierListener) {
		super.addModifierListener(pModifierListener);
	}

	@Override
	public boolean removeModifierListener(
			org.andengine.util.modifier.IModifier.IModifierListener<IEntity> pModifierListener) {
		return super.removeModifierListener(pModifierListener);
	}
}