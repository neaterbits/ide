package com.neaterbits.ide.common.ui.model.source;

import java.util.Objects;

final class SourceElementImpl implements SourceElement {

	private final long startOffset;
	private final long length;
	
	private final SourceElementMask flags;
	SourceElementImpl(
			long startOffset,
			long length,
			SourceElementMask flags) {

		if (startOffset < 0) {
			throw new IllegalArgumentException();
		}
		
		Objects.requireNonNull(flags);
		
		this.startOffset = startOffset;
		this.length = length;
		this.flags = flags;
	}

	@Override
	public boolean isRenameable() {
		return flags.isSet(SourceElementFlag.RENAMEABLE);
	}

	@Override
	public boolean isHierarchyType() {
		return flags.isSet(SourceElementFlag.HIERARCHY_TYPE);
	}

	@Override
	public boolean isReferenceable() {
		return flags.isSet(SourceElementFlag.REFERENCEABLE);
	}

	@Override
	public boolean isMoveable() {
		return flags.isSet(SourceElementFlag.MOVEABLE);
	}

	@Override
	public long getStartOffset() {
		return startOffset;
	}

	@Override
	public long getLength() {
		return length;
	}
}
