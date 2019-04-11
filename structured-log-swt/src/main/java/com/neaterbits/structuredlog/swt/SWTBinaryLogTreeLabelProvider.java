package com.neaterbits.structuredlog.swt;

import java.util.List;
import java.util.Objects;

import org.eclipse.jface.resource.DeviceResourceManager;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.widgets.Display;

import com.neaterbits.ide.util.ui.text.styling.TextColor;
import com.neaterbits.ide.util.ui.text.styling.TextStyleOffset;

final class SWTBinaryLogTreeLabelProvider extends BaseLabelProvider implements IStyledLabelProvider {

	private final FilteredTexts filteredTexts;
	
	private final ResourceManager resourceManager;
	
	public SWTBinaryLogTreeLabelProvider(Display display, FilteredTexts filteredTexts) {

		Objects.requireNonNull(filteredTexts);
		
		this.filteredTexts = filteredTexts;
		
		this.resourceManager = new LocalResourceManager(new DeviceResourceManager(display));
	}
	@Override
	public void dispose() {
		resourceManager.dispose();
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
	
	private StyledString makeStyledString(String labelText, List<TextStyleOffset> textStyleOffsets) {
		
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
	
	private Color convertColor(TextColor uiColor) {
		return resourceManager.createColor(new RGB(uiColor.getR(), uiColor.getG(), uiColor.getB()));
	}

	@Override
	public Image getImage(Object element) {
		return null;
	}
}
