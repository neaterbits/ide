package com.neaterbits.ide.component.runner.test.ui;

import java.util.Objects;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;

import com.neaterbits.ide.component.common.ComponentIDEAccess;
import com.neaterbits.ide.component.common.runner.model.throwable.XmlTestCase;
import com.neaterbits.ide.component.common.runner.model.throwable.XmlThrowable;
import com.neaterbits.ide.component.common.ui.ComponentCompositeContext;
import com.neaterbits.ide.component.common.ui.NavigationComponentUI;
import com.neaterbits.ide.component.runner.test.TestStatus;
import com.neaterbits.ide.ui.swt.SWTCompositeUIContext;

public class TestRunnerUI implements NavigationComponentUI<Control> {

    public static final String CAUSED_BY = "caused_by";
    public static final String EXCEPTION = "exception";
    public static final String OUTPUT = "output";
    
    private SWTTestComposite testComposite;

    @Override
    public Control addCompositeComponentUI(ComponentCompositeContext context, ComponentIDEAccess componentIDEAccess) {

        final SWTCompositeUIContext swtContext = (SWTCompositeUIContext)context;
        
        if (testComposite != null) {
            throw new IllegalStateException();
        }
        
        this.testComposite = new SWTTestComposite(swtContext.getComposite(),
                                                 SWT.NONE,
                                                 componentIDEAccess.getTranslator());
        
        return testComposite;
    }
    
    public void setModel(XmlTestCase testCase) {
        
        Objects.requireNonNull(testCase);
        
        testComposite.setModel(testCase);
    }
    
    public void setTestResult(
            String method,
            TestStatus status,
            XmlThrowable throwable,
            String output) {
        
        Objects.requireNonNull(method);
        Objects.requireNonNull(status);
        
        testComposite.setTestResult(method, status, throwable, output);
    }
}
