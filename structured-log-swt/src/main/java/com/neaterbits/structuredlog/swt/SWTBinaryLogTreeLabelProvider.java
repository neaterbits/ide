package com.neaterbits.structuredlog.swt;


import org.eclipse.jface.viewers.LabelProvider;

import com.neaterbits.structuredlog.binary.model.LogField;
import com.neaterbits.structuredlog.binary.model.LogNode;
import com.neaterbits.structuredlog.binary.model.LogObject;

final class SWTBinaryLogTreeLabelProvider extends LabelProvider {

	@Override
	public String getText(Object element) {

		final String typeText;
		final String text;

		final LogNode logNode;
		
		if (element instanceof LogObject) {
			
			typeText = "Object";
			
			final LogObject logObject = (LogObject)element;
			
			final StringBuilder sb = new StringBuilder();
			
			sb.append(logObject.getSimpleType());
			final String identifierText;
			
			if (logObject.getLogLocalIdentifier() != null) {
				identifierText = logObject.getLogLocalIdentifier();
			}
			else if (logObject.getLogIdentifier() != null) {
				identifierText = logObject.getLogIdentifier();
			}
			else {
				identifierText = null;
			}
			
			if (identifierText != null) {
				sb.append(' ');
				sb.append(identifierText);
			}
			
			if (logObject.getDescription() != null) {
				sb.append(' ');
				sb.append(logObject.getDescription());
			}
			
			text = sb.toString();
			logNode = logObject;
		}
		else if (element instanceof LogField) {
			
			final LogField logField = (LogField)element;
			
			typeText = "Field";
			
			text = logField.getFieldName();
			
			logNode = logField;
					
		}
		else {
			typeText = "Unknown";
			
			text = element.toString();
			
			logNode = (LogNode)element;
		}
		
		return text;
		
		// return typeText + " " + logNode.getSequenceNo() + " " + text;
	}

}
