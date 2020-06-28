package stockmarketui.parts;

import javax.annotation.PostConstruct;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import stockmarketui.simengine.SimulatorEngine;

public class TradesPart {
	private static final String INPUT_MSG = "Enter symbol to show trades";
	private static final String SHOWTEXT = "Show";

	private Text txtInput;

	private TableViewer tableViewer;

	@PostConstruct
	public void createComposite(Composite parent) {
		parent.setLayout(new GridLayout(2, false));

		Button buttonShow = new Button(parent, SWT.PUSH);
		buttonShow.setText(SHOWTEXT);
		buttonShow.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tableViewer.getTable().removeAll();
				String symbol = txtInput.getText();
				if (symbol != null && !symbol.isEmpty()) {
					SimulatorEngine.getInstance().getTradeLedger().getTradeHistory().stream()
							.filter(o -> o.getSymbol().equals(symbol)).forEach(o -> tableViewer.add(o.print()));
				} else {
					SimulatorEngine.getInstance().getTradeLedger().getTradeHistory()
							.forEach(o -> tableViewer.add(o.print()));
				}
			}
		});

		txtInput = new Text(parent, SWT.BORDER);
		txtInput.setMessage(INPUT_MSG);
		txtInput.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
			}
		});
		txtInput.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		tableViewer = new TableViewer(parent);
		tableViewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
	}

	@Focus
	public void setFocus() {
		tableViewer.getTable().setFocus();
	}

}
