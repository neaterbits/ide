package com.neaterbits.ide.component.common;

import com.neaterbits.ide.common.config.Configuration;

public interface ConfigurationAccess {

    <T extends Configuration> T getConfiguration(Class<T> type);
}
