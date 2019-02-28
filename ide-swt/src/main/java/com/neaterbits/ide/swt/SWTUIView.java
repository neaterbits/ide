package com.neaterbits.ide.swt;

import java.util.Collection;
import java.util.Objects;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;

import com.neaterbits.ide.common.resource.NamespaceResourcePath;
import com.neaterbits.ide.common.resource.SourceFolderResourcePath;
import com.neaterbits.ide.common.ui.model.ProjectsModel;
import com.neaterbits.ide.common.ui.model.dialogs.OpenTypeDialogModel;
import com.neaterbits.ide.common.ui.model.dialogs.TypeSuggestion;
import com.neaterbits.ide.common.ui.model.text.config.TextEditorConfig;
import com.neaterbits.ide.common.ui.view.EditorsView;
import com.neaterbits.ide.common.ui.view.KeyEventListener;
import com.neaterbits.ide.common.ui.view.NewableSelection;
import com.neaterbits.ide.common.ui.view.BuildIssuesView;
import com.neaterbits.ide.common.ui.view.ProjectView;
import com.neaterbits.ide.common.ui.view.SearchView;
import com.neaterbits.ide.common.ui.view.UIViewAndSubViews;
import com.neaterbits.ide.component.common.ComponentIDEAccess;
import com.neaterbits.ide.component.common.Newable;
import com.neaterbits.ide.component.common.NewableCategory;
import com.neaterbits.ide.component.common.NewableCategoryName;
import com.neaterbits.ide.component.common.UIComponentProvider;

public final class SWTUIView implements UIViewAndSubViews<Shell> {

	private final Display display;
	private final Shell window;
	
	private final Composite composite;
	
	// private final Composite overviewComposite;
	private final TabFolder overviewTabFolder;
	private final GridData overviewGridData;
	private final GridData detailsGridData;
	private final TabFolder detailsTabFolder;
	
	private final SWTProjectView projectView;
	private final SWTEditorsView editorsView;
	private final SWTBuildIssuesView buildIssuesView;
	
	private boolean editorsMaximized;
	
	SWTUIView(Display display, ProjectsModel projectModel, TextEditorConfig textEditorConfig) {
		
		this.display = display;
		
		this.window = new Shell(display); 
		
		window.setLayout(new FillLayout());
		
		this.composite = new Composite(window, SWT.NONE);
		
		composite.setLayout(new GridLayout(4, false));

		this.overviewTabFolder = new TabFolder(composite, SWT.NONE);

		// this.overviewComposite = new Composite(composite, SWT.NONE);
		
		this.overviewGridData = new GridData();
		
		/*
		overviewGridData.minimumHeight = 600;
		*/

		overviewGridData.minimumWidth = 500;
		overviewGridData.widthHint = 300;
		
		overviewGridData.horizontalSpan = 1;
		// overviewGridData.grabExcessHorizontalSpace = true;
		overviewGridData.horizontalAlignment = SWT.FILL;
		overviewGridData.verticalSpan = 4;
		overviewGridData.grabExcessVerticalSpace = true;
		overviewGridData.verticalAlignment = SWT.FILL;

		overviewTabFolder.setLayoutData(overviewGridData);
		
		// overviewComposite.setLayoutData(overviewGridData);
		// overviewComposite.setLayout(new FillLayout());
		
		this.projectView = new SWTProjectView(
				overviewTabFolder,
				projectModel);
		
		final GridData editorsGridData = new GridData();
		
		editorsGridData.grabExcessHorizontalSpace = true;
		editorsGridData.horizontalSpan = 3;
		editorsGridData.verticalSpan = 3;
		editorsGridData.grabExcessVerticalSpace = true;
		editorsGridData.horizontalAlignment = SWT.FILL;
		editorsGridData.grabExcessVerticalSpace = true;
		editorsGridData.verticalAlignment = SWT.FILL;
		
		this.editorsView = new SWTEditorsView(composite, textEditorConfig);
		
		editorsView.getTabFolder().setLayoutData(editorsGridData);

		this.detailsGridData = new GridData();

		// detailsGridData.grabExcessHorizontalSpace = true;
		detailsGridData.horizontalSpan = 3;
		detailsGridData.horizontalAlignment = SWT.FILL;
		detailsGridData.verticalSpan = 1;
		
		this.detailsTabFolder = new TabFolder(composite, SWT.NONE);
		
		detailsTabFolder.setLayoutData(detailsGridData);

		this.buildIssuesView = new SWTBuildIssuesView(detailsTabFolder);
		
		window.addDisposeListener(event -> System.exit(0));
		
		// window.pack();
		
		window.open();
	
		this.editorsMaximized = false;
	}
	
	@Override
	public void setWindowTitle(String title) {
		window.setText(title);
	}

	@Override
	public void addKeyEventListener(KeyEventListener keyEventListener) {

		Objects.requireNonNull(keyEventListener);

		final SWTKeyEventListener swtKeyEventListener = new SWTKeyEventListener(keyEventListener);
		
		display.addFilter(SWT.KeyDown, new Listener() {
			@Override
			public void handleEvent(Event event) {
				swtKeyEventListener.keyPressed(event.character, event.keyCode, event.stateMask);
			}
		});
	}

	@Override
	public TypeSuggestion askOpenType(OpenTypeDialogModel model) {
		
		final OpenTypeDialog dialog = new OpenTypeDialog(window, model);
		
		dialog.open();
		
		return dialog.getSuggestion();
	}
	
	@Override
	public NewableSelection askCreateNewable(Collection<NewableCategory> categories) {

		final CreateNewableDialog dialog = new CreateNewableDialog(window, categories);
		
		dialog.open();
		
		return dialog.getSelection();
	}
	
	@Override
	public void openNewableDialog(
			UIComponentProvider<Shell> uiComponentProvider,
			NewableCategoryName category,
			Newable newable,
			SourceFolderResourcePath sourceFolder,
			NamespaceResourcePath namespace,
			ComponentIDEAccess ideAccess) {

		uiComponentProvider.openNewableDialog(window, category, newable, sourceFolder, namespace, ideAccess);
	}

	@Override
	public ProjectView getProjectView() {
		return projectView;
	}

	@Override
	public EditorsView getEditorsView() {
		return editorsView;
	}

	@Override
	public BuildIssuesView getBuildIssuesView() {
		return buildIssuesView;
	}

	@Override
	public SearchView getSearchView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void minMaxEditors() {
		
		// overviewComposite.setVisible(editorsMaximized);
		overviewGridData.exclude = !editorsMaximized;

		detailsTabFolder.setVisible(editorsMaximized);
		detailsGridData.exclude = !editorsMaximized;
		
		composite.layout(true);

		this.editorsMaximized = !editorsMaximized;
	}
}
