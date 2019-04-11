package com.neaterbits.ide.common.ui;

import java.util.List;

import com.neaterbits.ide.component.common.language.model.SourceFileModel;
import com.neaterbits.ide.util.ui.text.Text;
import com.neaterbits.ide.util.ui.text.styling.TextStyleOffset;

public interface TextStyling {

	List<TextStyleOffset> applyStylesToLine(long startPos, Text lineText, SourceFileModel sourceFileModel);

}
