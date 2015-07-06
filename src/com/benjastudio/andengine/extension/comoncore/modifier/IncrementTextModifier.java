package com.benjastudio.andengine.extension.comoncore.modifier;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.text.Text;
import org.andengine.util.modifier.BaseSingleValueSpanModifier;

public class IncrementTextModifier extends BaseSingleValueSpanModifier<IEntity>
		implements IEntityModifier {

	String prefix, suffix;

	public IncrementTextModifier(final float pDuration, final float pFrom,
			final float pTo, String pPrefix, String pSuffix) {
		super(pDuration, pFrom, pTo);
		prefix = pPrefix;
		suffix = pSuffix;
	}

	@Override
	protected void onSetInitialValue(IEntity pItem, float pValue) {
		Text text = (Text) pItem;
		text.setText(prefix + "" + (int) pValue + suffix);
	}

	@Override
	protected void onSetValue(IEntity pItem, float pPercentageDone, float pValue) {
		Text text = (Text) pItem;
		text.setText(prefix + "" + (int) pValue + suffix);
	}

	protected IncrementTextModifier(IncrementTextModifier incrementTextModifier) {
		super(incrementTextModifier);
	}

	@Override
	public IncrementTextModifier deepCopy()
			throws org.andengine.util.modifier.IModifier.DeepCopyNotSupportedException {

		return new IncrementTextModifier(this);
	}

}
