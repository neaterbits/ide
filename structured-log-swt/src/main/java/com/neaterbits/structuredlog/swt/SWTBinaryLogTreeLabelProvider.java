package com.neaterbits.structuredlog.swt;

import java.util.List;
import java.util.Objects;

import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.TextStyle;

import com.neaterbits.ide.util.ui.text.styling.TextColor;
import com.neaterbits.ide.util.ui.text.styling.TextStyleOffset;

final class SWTBinaryLogTreeLabelProvider extends BaseLabelProvider implements IStyledLabelProvider {

	private final FilteredTexts filteredTexts;
	
	public SWTBinaryLogTreeLabelProvider(FilteredTexts filteredTexts) {

		Objects.requireNonNull(filteredTexts);
		
		this.filteredTexts = filteredTexts;
	}

	@Override
	public StyledString getStyledText(Object element) {

		final StyledString text;

		final String labelText = SWTBinaryLogTree.getObjectLabel(element);

		final List<TextStyleOffset> textStyleOffsets = filteredTexts.getStyleOffsets(element);
		
		if (textStyleOffsets != null) {
			text = makeStyledString(labelText, textStyleOffsets);
		}
		else {
			text = new StyledString(labelText);
		}

		return text;
	}
	
	private static StyledString makeStyledString(String labelText, List<TextStyleOffset> textStyleOffsets) {
		
		final StyledString text = new StyledString(labelText);
		
		for (TextStyleOffset textStyleOffset : textStyleOffsets) {
			
			try {
			text.setStyle(
					(int)textStyleOffset.getStart(),
					(int)textStyleOffset.getLength(),
					new StyledString.Styler() {
						
						@Override
						public void applyStyles(TextStyle textStyle) {
							textStyle.foreground = textStyleOffset.getColor() != null
									? convertColor(textStyleOffset.getColor())
									: null;

							textStyle.background = textStyleOffset.getBgColor() != null
									? convertColor(textStyleOffset.getBgColor())
									: null;
						}
					});
			}
			catch (Exception ex) {

				ex.printStackTrace(System.err);
				
				System.err.println("## set " + textStyleOffset.getStart() + "/" + textStyleOffset.getLength() + " in \"" + text.getString() + "\"");
			}
		}
		
		return text;
	}
	
	private static Color convertColor(TextColor uiColor) {
		return new Color(null, uiColor.getR(), uiColor.getG(), uiColor.getB());
	}

	@Override
	public Image getImage(Object element) {
		return null;
	}
}
