package com.neaterbits.ide.component.common;

import java.util.List;

import com.neaterbits.ide.component.common.ui.ComponentUI;

public interface IDEComponentsFinder {

    <T extends IDEComponent> List<T> findComponents(Class<T> type);

    <T extends ComponentUI> List<T> findComponentUIs(Class<T> type);
}
