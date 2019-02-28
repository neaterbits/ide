package com.neaterbits.ide.common.ui.model.text.difftextmodel;

interface DiffTextChangeConstAccess {

	Iterable<DiffTextOffset> getRemovedOffsets();
	
	Iterable<DiffTextOffset> getAddedOffsets();
	
}
