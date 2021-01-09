package com.neaterbits.ide.common.ui.menus;

import java.util.function.Consumer;

import com.neaterbits.ide.common.ui.actions.BuiltinActionAccess;
import com.neaterbits.ide.common.ui.actions.TranslatedAction;
import com.neaterbits.ide.common.ui.translation.Translateable;

public interface MenuBuilder {

    MenuBuilder addSubMenu(BuiltinMenu builtinMenu, Consumer<MenuBuilder> builder);

    MenuBuilder addSubMenu(Translateable translateable, Consumer<MenuBuilder> builder);

    MenuBuilder addSubMenu(String translationNamespace, String translationId, Consumer<MenuBuilder> builder);

    MenuBuilder addSeparator();

    MenuBuilder addBuiltinAction(BuiltinActionAccess action);

    MenuBuilder addAction(TranslatedAction<?, ?> action);

    MenuBuilder addToMenu(BuiltinMenu builtinMenu, Consumer<MenuBuilder> builder);
}
