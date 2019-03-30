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
import com.neaterbits.structuredlog.binary.model.LogField;
import com.neaterbits.structuredlog.binary.model.LogObject;

final class SWTBinaryLogTreeLabelProvider extends BaseLabelProvider implements IStyledLabelProvider {

	private final FilteredTexts filteredTexts;
	
	public SWTBinaryLogTreeLabelProvider(FilteredTexts filteredTexts) {

		Objects.requireNonNull(filteredTexts);
		
		this.filteredTexts = filteredTexts;
	}

	@Override
	public StyledString getStyledText(Object element) {

		final StyledString text;

		if (element instanceof LogObject) {
			
			final LogObject logObject = (LogObject)element;
		
			final String labelText = SWTBinaryLogTree.getLogObjectLabel(logObject);
			
			final List<TextStyleOffset> textStyleOffsets = filteredTexts.getStyleOffsets(logObject);
			
			if (textStyleOffsets != null) {
				text = new StyledString(labelText);
				
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
			}
			else {
				text = new StyledString(labelText);
			}
			
		}
		else if (element instanceof LogField) {
			
			final LogField logField = (LogField)element;
			
			text = new StyledString(logField.getFieldName());
		}
		else {
			text = new StyledString(element.toString());
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
