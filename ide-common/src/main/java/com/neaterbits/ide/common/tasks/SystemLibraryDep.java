package com.neaterbits.ide.common.tasks;

import com.neaterbits.build.types.DependencyFile;
import com.neaterbits.build.types.resource.ProjectModuleResourcePath;

final class SystemLibraryDep extends Dep<DependencyFile> {

    SystemLibraryDep(ProjectModuleResourcePath from, DependencyFile to) {
        super(from, to);
    }
}
