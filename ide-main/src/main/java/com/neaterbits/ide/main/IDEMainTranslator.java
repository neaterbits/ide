package com.neaterbits.ide.main;

import com.neaterbits.ide.common.ui.translation.Translateable;
import com.neaterbits.ide.common.ui.translation.Translator;
import com.neaterbits.ide.component.application.runner.MainApplicationRunnerComponent;
import com.neaterbits.ide.component.build.ui.BuildIssuesComponent;
import com.neaterbits.ide.component.common.ComponentRelated;
import com.neaterbits.ide.component.common.ui.DetailsComponentUI;
import com.neaterbits.ide.component.compiledfiledebug.ui.CompiledFileViewComponent;
import com.neaterbits.ide.component.runners.RunnersComponent;
import com.neaterbits.ide.component.runners.ui.RunnersComponentUI;
import com.neaterbits.ide.core.ui.controller.IDECoreTranslator;

final class IDEMainTranslator implements Translator {

    private final IDECoreTranslator coreTranslator;
    
    IDEMainTranslator() {

        this.coreTranslator = new IDECoreTranslator();
    }
    
    private static boolean isComponentNamespace(Translateable translateable, Class<? extends ComponentRelated> component) {
        
        return translateable.getTranslationNamespace()
                .equals(Translator.getComponentNamespace(component));
    }

    @Override
    public String translate(Translateable translateable) {

        final String translation;
        
        if (isComponentNamespace(translateable, BuildIssuesComponent.class)) {
            
            if (translateable.getTranslationId().equals(DetailsComponentUI.TITLE_TRANSLATION_ID)) {
                translation = "Build Issues";
            }
            else {
                throw new IllegalArgumentException();
            }
        }
        else if (isComponentNamespace(translateable, CompiledFileViewComponent.class)) {

            if (translateable.getTranslationId().equals(DetailsComponentUI.TITLE_TRANSLATION_ID)) {
                translation = "Compiled File";
            }
            else {
                throw new IllegalArgumentException();
            }
        }
        else if (isComponentNamespace(translateable, RunnersComponent.class)) {
            
            switch (translateable.getTranslationId()) {
            
            case RunnersComponentUI.TRANSLATION_ID_RUN:
                translation = "Run";
                break;

            case RunnersComponentUI.TRANSLATION_ID_RUN_AS:
                translation = "Run As";
                break;
                
            case RunnersComponentUI.TRANSLATION_ID_RUN_CONFIGURATIONS:
                translation = "Run Configurations";
                break;

            case RunnersComponentUI.TRANSLATION_ID_MAIN:
                translation = "Main";
                break;

            case RunnersComponentUI.TRANSLATION_ID_MAIN_PROJECT:
                translation = "Project";
                break;

            case RunnersComponentUI.TRANSLATION_ID_MAIN_CLASS:
                translation = "Main class";
                break;

            case RunnersComponentUI.TRANSLATION_ID_ARGUMENTS:
                translation = "Arguments";
                break;

            case RunnersComponentUI.TRANSLATION_ID_ARGUMENTS_PROGRAM:
                translation = "Program arguments";
                break;

            case RunnersComponentUI.TRANSLATION_ID_ARGUMENTS_VM:
                translation = "VM arguments";
                break;

            case RunnersComponentUI.TRANSLATION_ID_ENVIRONMENT:
                translation = "Environment";
                break;

            default:
                throw new IllegalArgumentException();
            }
        }
        else if (isComponentNamespace(translateable, MainApplicationRunnerComponent.class)) {
            
            if (translateable.getTranslationId().equals(RunnersComponentUI.TRANSLATION_ID_RUN_AS)) {
                translation = "Application";
            }
            else {
                throw new IllegalArgumentException();
            }
        }
        else {
            translation = coreTranslator.translate(translateable);
        }
        
        return translation;
    }
}
