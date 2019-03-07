package com.neaterbits.ide.swt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
import com.neaterbits.compiler.common.model.SourceTokenVisitor;
import com.neaterbits.ide.common.ui.actions.contexts.ActionContext;
import com.neaterbits.ide.common.ui.view.ActionContextListener;
import com.neaterbits.ide.common.ui.view.CompiledFileView;
import com.neaterbits.ide.component.common.language.model.SourceFileModel;

public final class SWTCompiledFileView implements CompiledFileView {

	private final StyledText textWidget;
	
	private long editorCursorOffset;
	private SourceFileModel sourceFileModel;
	
	private Label cursorOffsetLabel;
	
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
		
		infoComposite.setLayoutData(infoCompositeData);
		
		infoComposite.setLayout(new FillLayout(SWT.VERTICAL));
		
		this.cursorOffsetLabel = new Label(infoComposite, SWT.NONE);
		
		updateCursorPosLabel(false);
		
		final Font font = new Font(tabFolder.getDisplay(), new FontData("Monospace", 10, SWT.NONE));
		textWidget.addDisposeListener(event -> font.dispose());
		textWidget.setFont(font);
		
		textWidget.setEditable(false);
	}
	
	@Override
	public void onEditorCursorPosUpdate(long cursorOffset) {

		this.editorCursorOffset = cursorOffset;
		
		updateViewText(true);
		
		updateCursorPosLabel(true);
	}


	@Override
	public void setSourceFileModel(SourceFileModel model) {
	
		Objects.requireNonNull(model);
		
		this.sourceFileModel = model;
		
		updateViewText(false);
	}

	private static class Element {
		private final ISourceToken token;
		private final int textOffset;

		public Element(ISourceToken token, int textOffset) {
			
			Objects.requireNonNull(token);
			
			this.token = token;
			this.textOffset = textOffset;
		}
	}
	
	private static class TextVisitor implements SourceTokenVisitor {
		
		private final StringBuilder sb;
		private final long editorCursorOffset;

		private final List<Element> tokens;

		private int indent;
		
		private int stringBuilderLengthAtCursorOffset = -1;
		private int stringBuilderLineAtCursorOffset = 0;
		private Element foundCursorElement;
		
		private int foundLength;

				
		public TextVisitor(StringBuilder sb, long editorCursorOffset) {

			Objects.requireNonNull(sb);
			
			this.sb = sb;
			this.editorCursorOffset = editorCursorOffset;
			
			this.tokens = new ArrayList<>();
		}

		@Override
		public void onToken(ISourceToken token) {
			
			Objects.requireNonNull(token);
			
			for (int i = 0; i < indent; ++ i) {
				sb.append("  ");
			}
			
			if (stringBuilderLengthAtCursorOffset == -1) {
				if (token.getStartOffset() > editorCursorOffset) {
					stringBuilderLengthAtCursorOffset = sb.length();
				}
				
				++ stringBuilderLineAtCursorOffset;
			}

			
			sb.append(token.getTokenTypeDebugName())
				.append(" [").append(token.getStartOffset()).append(", ").append(token.getLength()).append("]\n");
		}

		@Override
		public void onPush(ISourceToken token) {
			indent ++;
			
			if (foundCursorElement == null) {

				// Any earlier token found? If so clear since this is further towards a leaf token
				
				if (tokens.size() != indent - 1) {
					throw new IllegalStateException("mismatch " + tokens.size() + "/" + (indent - 1));
				}
				
				for (int i = 0; i < tokens.size() - 1; ++ i) {
					tokens.set(i, null);
				}
			
				final ISourceToken matchingToken;
				
				if (token.getStartOffset() <= editorCursorOffset && token.getStartOffset() + token.getLength() >= editorCursorOffset) {
					
					System.out.println("## matching token " + token.getTokenTypeDebugName());
					
					matchingToken = token;
				}
				else {
					matchingToken = null;
				}
				
				tokens.add(matchingToken != null ? new Element(matchingToken, sb.length()) : null);
			}
		}

		@Override
		public void onPop(ISourceToken token) {

			if (foundCursorElement == null) {
				if (indent > 0 && tokens.get(tokens.size() - 1) != null) {
					// leaf token
					foundCursorElement = tokens.get(tokens.size() - 1);
					
					foundLength = sb.length() - foundCursorElement.textOffset;
				}

				tokens.remove(tokens.size() - 1);
			}

			-- indent;

		}
	}

	private void updateCursorPosLabel(boolean showValue) {
		
		cursorOffsetLabel.setText("Cursor offset:" + (showValue ? (" " + editorCursorOffset) : "            "));
		
	}
	
	private void updateViewText(boolean updateCursorOnly) {

		final StringBuilder sb = new StringBuilder();

		final TextVisitor visitor = new TextVisitor(sb, editorCursorOffset);
		
		sourceFileModel.iterate(visitor);
		
		if (!updateCursorOnly) {
			textWidget.setText(sb.toString());
		}

		// System.out.println("## set textwidget pos to " + visitor.stringBuilderLengthAtCursorOffset + " of " + sb.length());
		// textWidget.setCaretOffset(visitor.stringBuilderLengthAtCursorOffset);
		
		final int topIndex = visitor.stringBuilderLineAtCursorOffset > 10
					? visitor.stringBuilderLineAtCursorOffset - 10
					: 0;
		
		
		System.out.println("## set textwidget pos to " + topIndex + " of " + sb.length());
		
		textWidget.setTopIndex(topIndex);

		textWidget.setStyleRange(new StyleRange(0, textWidget.getText().length(), null, null));

		if (visitor.foundCursorElement != null) {
			
			System.out.println("## found cursor token " + visitor.foundCursorElement.token.	getTokenDebugString());

			textWidget.setStyleRange(new StyleRange(
					(int)visitor.foundCursorElement.textOffset,
					(int)visitor.foundLength,
					new Color(null, 0x30, 0xA0, 0x30),
					null));
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
