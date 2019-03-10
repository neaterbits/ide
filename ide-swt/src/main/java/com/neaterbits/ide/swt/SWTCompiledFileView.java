package com.neaterbits.ide.swt;

import java.util.Collection;
import java.util.Objects;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import com.neaterbits.compiler.common.model.ISourceToken;
import com.neaterbits.ide.common.ui.actions.contexts.ActionContext;
import com.neaterbits.ide.common.ui.view.ActionContextListener;
import com.neaterbits.ide.common.ui.view.CompiledFileView;
import com.neaterbits.ide.component.common.language.model.SourceFileModel;

public final class SWTCompiledFileView implements CompiledFileView {

	private final StyledText textWidget;
	private final Label cursorOffsetLabel;
	private final Label tokenTypeLabel;
	private final Label tokenTypeNameLabel;
	
	private long editorCursorOffset;
	private ISourceToken curToken;
	private SourceFileModel sourceFileModel;
	
	public SWTCompiledFileView(TabFolder tabFolder) {

		final TabItem tabItem = new TabItem(tabFolder, SWT.NONE);

		tabItem.setText("Compiled file");

		final Composite composite = new Composite(tabFolder, SWT.NONE);

		tabItem.setControl(composite);

		final GridLayout compositeLayout = new GridLayout(2, false);
		
		compositeLayout.marginWidth = 0;
		
		composite.setLayout(compositeLayout);
		
		this.textWidget = new StyledText(composite, SWT.NONE);

		final GridData textWidgetData = new GridData(SWT.FILL, SWT.FILL, true, true);
		
		textWidget.setLayoutData(textWidgetData);
		
		final Composite infoComposite = new Composite(composite, SWT.NONE);
		
		final GridData infoCompositeData = new GridData(SWT.FILL, SWT.BEGINNING, false, false);
		
		infoCompositeData.widthHint = 350;
		
		infoComposite.setLayoutData(infoCompositeData);
		
		infoComposite.setLayout(new FillLayout(SWT.VERTICAL));
		
		this.cursorOffsetLabel = new Label(infoComposite, SWT.NONE);
		this.tokenTypeLabel = new Label(infoComposite, SWT.NONE);
		this.tokenTypeNameLabel = new Label(infoComposite, SWT.NONE);
		
		updateCursorPosLabels(false);
		
		final Font font = new Font(tabFolder.getDisplay(), new FontData("Monospace", 10, SWT.NONE));
		textWidget.addDisposeListener(event -> font.dispose());
		textWidget.setFont(font);
		
		textWidget.setEditable(false);
	}
	
	@Override
	public void onEditorCursorPosUpdate(long cursorOffset) {

		this.editorCursorOffset = cursorOffset;
		
		updateViewText(true);
		
		updateCursorPosLabels(true);
	}

	@Override
	public void setSourceFileModel(SourceFileModel model) {
	
		Objects.requireNonNull(model);
		
		this.sourceFileModel = model;
		
		updateViewText(false);
		
		updateCursorPosLabels(true);
	}

	
	private void updateCursorPosLabels(boolean showValue) {
		
		cursorOffsetLabel.setText("Cursor offset:" + (showValue ? (" " + editorCursorOffset) : ""));
		tokenTypeLabel.setText("Token:" + (curToken != null ? (" " + curToken.getTokenType().name()) : ""));
		tokenTypeNameLabel.setText("Token type:" + (curToken != null && curToken.getTypeName() != null
																? (" " + curToken.getTypeName().toDebugString())
																: ""));
	}
	
	private void updateViewText(boolean updateCursorOnly) {

		if (sourceFileModel != null) {
			final StringBuilder sb = new StringBuilder();
	
			final SWTCompiledFileViewMakeTextVisitor visitor = new SWTCompiledFileViewMakeTextVisitor(sb, editorCursorOffset);
			
			sourceFileModel.iterate(visitor);
			
			if (!updateCursorOnly) {
				textWidget.setText(sb.toString());
			}
	
			final int topIndex = visitor.getStringBuilderLineAtCursorOffset() > 10
						? visitor.getStringBuilderLineAtCursorOffset() - 10
						: 0;
			
			
			System.out.println("## set textwidget pos to " + topIndex + " of " + sb.length());
			
			textWidget.setTopIndex(topIndex);
	
			textWidget.setStyleRange(new StyleRange(0, textWidget.getText().length(), null, null));
	
			this.curToken = visitor.getFoundCursorToken();
	
			if (visitor.getFoundCursorToken() != null) {
				
				System.out.println("## found cursor token " + visitor.getFoundCursorToken().getTokenDebugString());
				
				textWidget.setStyleRange(new StyleRange(
						(int)visitor.getFoundStringBuilderTextOffset(),
						(int)visitor.getFoundLength(),
						new Color(null, 0x30, 0xA0, 0x30),
						null));
			}
		}
	}


	@Override
	public Collection<ActionContext> getActiveActionContexts() {
		return null;
	}

	@Override
	public void addActionContextListener(ActionContextListener listener) {
		
	}
}
