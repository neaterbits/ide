package com.neaterbits.ide.common.ui.actions;

import com.neaterbits.build.model.BuildRoot;
import com.neaterbits.util.threads.ForwardResultToCaller;

public interface ActionExeParameters extends ActionSourceFileParameters {

    BuildRoot getBuildRoot();

    ForwardResultToCaller getForwardResultToCaller();
}
