package com.neaterbits.ide.common.ui.controller;

import com.neaterbits.ide.common.ui.translation.Translateable;
import com.neaterbits.ide.common.ui.translation.TranslationNamespaces;
import com.neaterbits.ide.common.ui.translation.Translator;

final class IDETranslator implements Translator {

	@Override
	public String translate(Translateable translateable) {

		final String nameSpace = translateable.getTranslationNamespace();
		
		final String translation;
		
		if (nameSpace.equals(TranslationNamespaces.MENUES)) {
			switch (translateable.getTranslationId()) {
			case "file":
				translation = "&File";
				break;
				
			case "edit":
				translation = "&Edit";
				break;
				
			case "refactor":
				translation = "Refac&tor";
				break;
				
			case "navigate":
				translation = "&Navigate";
				break;
				
			default:
				throw new UnsupportedOperationException();
			}
		}
		else if (nameSpace.equals(TranslationNamespaces.BUILTIN_ACTIONS)) {
			switch (translateable.getTranslationId()) {
			case "new_popup":
			case "new_dialog":
				translation = "New";
				break;
				
			case "delete":
				translation = "&Delete";
				break;

			case "undo":
				translation = "&Undo";
				break;

			case "redo":
				translation = "&Redo";
				break;
				
			case "cut":
				translation = "Cut";
				break;
				
			case "copy":
				translation = "Copy";
				break;
				
			case "paste":
				translation = "Paste";
				break;
				
			case "select_all":
				translation = "Select &All";
				break;
				
			case "find_replace":
				translation = "&Find/Replace";
				break;
				
			case "rename":
				translation = "Re&name";
				break;
				
			case "move":
				translation = "&Move";
				break;
				
			case "type_hierarchy":
				translation = "Type &hierarchy";
				break;
				
			default:
				throw new UnsupportedOperationException();
			}
		}
		else {
			throw new UnsupportedOperationException();
		}
		
		return translation;
	}
	
}
