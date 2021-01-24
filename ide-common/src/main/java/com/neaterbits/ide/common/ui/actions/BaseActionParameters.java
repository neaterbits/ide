package com.neaterbits.ide.common.ui.actions;

import java.util.Objects;

import com.neaterbits.build.model.BuildRoot;

public abstract class BaseActionParameters implements ActionSourceFileParameters {

    private final BuildRoot buildRoot;

    protected BaseActionParameters(BuildRoot buildRoot) {
        
        Objects.requireNonNull(buildRoot);
        
        this.buildRoot = buildRoot;
    }

    @Override
    public final BuildRoot getBuildRoot() {
        return buildRoot;
    }
}
