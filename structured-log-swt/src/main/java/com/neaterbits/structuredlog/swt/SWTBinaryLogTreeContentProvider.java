package com.neaterbits.structuredlog.swt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.viewers.Viewer;

import com.neaterbits.ide.ui.swt.TreeContentAdapter;
import com.neaterbits.structuredlog.binary.model.LogCollectionField;
import com.neaterbits.structuredlog.binary.model.LogField;
import com.neaterbits.structuredlog.binary.model.LogModel;
import com.neaterbits.structuredlog.binary.model.LogNode;
import com.neaterbits.structuredlog.binary.model.LogObject;
import com.neaterbits.structuredlog.binary.model.LoggableField;

final class SWTBinaryLogTreeContentProvider extends TreeContentAdapter {

	private Set<String> displayedTypes;
	
	void updateDisplayedTypes(Set<String> displayedTypes) {
		this.displayedTypes = new HashSet<>(displayedTypes);
	}
	
	
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		
	}
	
	@Override
	public Object getParent(Object element) {
		
		final Object obj;
		
		if (element instanceof LogModel) {
			obj = null;
		}
		else {
			final LogNode node = (LogNode)element;
		
			obj = node.getParent();
		}
		
		return obj;
	}
	
	@Override
	public Object[] getElements(Object inputElement) {
		
		System.out.println("## get elements for " + inputElement);
		
		final Object [] objs;
		
		if (inputElement instanceof LogModel) {
	
			final LogModel model = (LogModel)inputElement;
			
			objs = model.getLogRootObjects().toArray(new Object[model.getLogRootObjects().size()]);
		}
		else if (inputElement instanceof LogObject) {
			
			final LogObject logObject = (LogObject)inputElement;
			
			final Collection<LogField> fields = logObject.getFields();
			
			if (fields == null || fields.isEmpty()) {
				objs = null;
			}
			else if (fields.size() == 1) {
				objs = getElements(fields.iterator().next());
			}
			else {
				objs = fields.toArray(new Object[fields.size()]);
			}
		}
		else if (inputElement instanceof LogCollectionField) {
			final Collection<LogObject> objects = ((LogCollectionField)inputElement).getCollection();
		
			if (objects != null) {
				
				final List<Object> list = new ArrayList<>(objects.size());
				
				final Iterator<LogObject> iter = objects.iterator();

				while (iter.hasNext()) {
					
					final LogObject obj = iter.next();
					
					if (!shouldDisplayType(obj)) {
						final Object [] sub = getElements(obj);
						
						for (int j = 0; j < sub.length; ++ j) {
							list.add(sub[j]);
						}
					}
					else {
						list.add(obj);
					}
				}

				objs = list.toArray(new Object[list.size()]);
			}
			
			else {
				objs = null;
			}
		}
		else if (inputElement instanceof LoggableField) {
			
			final LogObject logObject = ((LoggableField)inputElement).getObject();
			
			objs = logObject != null ? new Object [] { logObject } : null;
		}
		else {
			objs = null;
		}

		System.out.println("## return " + (objs != null ? Arrays.toString(objs) : null));
		
		return objs;
	}
	

	private boolean shouldDisplayType(LogObject logObject) {
		
		return displayedTypes == null || displayedTypes.contains(logObject.getSimpleType());
		
	}
}

