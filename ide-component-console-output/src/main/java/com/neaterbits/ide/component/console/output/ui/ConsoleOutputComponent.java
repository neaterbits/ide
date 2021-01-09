package com.neaterbits.ide.component.console.output.ui;

import java.util.Objects;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;

import com.neaterbits.ide.component.common.ComponentIDEAccess;
import com.neaterbits.ide.component.common.output.UIProcessOutputComponent;
import com.neaterbits.ide.component.common.ui.ComponentCompositeContext;
import com.neaterbits.ide.component.common.ui.DetailsComponentUI;
import com.neaterbits.ide.component.console.output.config.ConsoleConfiguration;
import com.neaterbits.ide.ui.swt.SWTCompositeUIContext;

public final class ConsoleOutputComponent
        implements UIProcessOutputComponent,
                   DetailsComponentUI<Control> {

    private SWTConsoleOutputView consoleOutputView;
    
    @Override
    public Control addCompositeComponentUI(ComponentCompositeContext context, ComponentIDEAccess componentIDEAccess) {

        Objects.requireNonNull(context);
        Objects.requireNonNull(componentIDEAccess);

        if (consoleOutputView != null) {
            throw new IllegalStateException();
        }
        
        final SWTCompositeUIContext uiContext = (SWTCompositeUIContext)context;
        
        final ConsoleConfiguration configuration
            = componentIDEAccess.getConfiguration(ConsoleConfiguration.class);
        
        if (configuration == null) {
            throw new IllegalStateException();
        }
        
        this.consoleOutputView = new SWTConsoleOutputView(
                uiContext.getComposite(),
                SWT.NONE,
                configuration.getLineDelimiter(),
                configuration.getMaxDisplayed());
        
        return consoleOutputView;
    }

    @Override
    public void output(ProcessSource source, String data) {

        consoleOutputView.addData(source, data);
        
    }
}
