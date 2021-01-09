package com.neaterbits.ide.component.runners;

import com.neaterbits.ide.component.common.ConfiguredComponent;
import com.neaterbits.ide.component.runners.model.RunnersConfiguration;

public class RunnersComponent implements ConfiguredComponent {

    @Override
    public String getConfigurationFileName(Class<?> configurationType) {

        final String fileName;
        
        if (configurationType.equals(RunnersConfiguration.class)) {
            fileName = "runners";
        }
        else {
            fileName = null;
        }

        return fileName;
    }
}
