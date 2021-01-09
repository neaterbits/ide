package com.neaterbits.ide.common.tasks;

import com.neaterbits.build.types.resource.ProjectModuleResourcePath;

final class ModuleDep extends Dep<ProjectModuleResourcePath> {

    ModuleDep(ProjectModuleResourcePath from, ProjectModuleResourcePath to) {
        super(from, to);
    }
}
