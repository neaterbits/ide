package com.neaterbits.ide.component.common.ui;

import com.neaterbits.ide.common.ui.menus.MenuBuilder;
import com.neaterbits.ide.component.common.IDEComponentsConstAccess;

public interface MenuComponentUI extends ComponentUI {

    void addToMenu(IDEComponentsConstAccess componentsAccess, MenuBuilder menuBuilder);
}
