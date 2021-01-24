package com.neaterbits.ide.component.common.language;

import java.util.Objects;

import com.neaterbits.build.types.resource.SourceFileResourcePath;

public interface Languages {

	LanguageComponent getLanguageComponent(LanguageName languageName);
	
	LanguageName detectLanguage(SourceFileResourcePath sourceFile);

    default <T> T getSourceFileLanguage(
            Class<T> languageInterface,
            SourceFileResourcePath sourceFileResourcePath) {

        Objects.requireNonNull(sourceFileResourcePath);
        
        final T result;
        
        final LanguageName languageName
            = detectLanguage(sourceFileResourcePath);
        
        if (languageName != null) {
            
            final LanguageComponent component
                = getLanguageComponent(languageName);
            
            result = component.getLanguage(languageInterface);
        }
        else {
            result = null;
        }
        
        return result;
    }
}
