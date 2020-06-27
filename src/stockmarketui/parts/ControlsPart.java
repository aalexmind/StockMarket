package stockmarketui.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import stockmarketui.simengine.Order;
import stockmarketui.simengine.Order.OrderType;
import stockmarketui.simengine.SimulatorEngine;
import stockmarketui.simengine.TradingGateway;

public class ControlsPart {

	@Inject
	public ControlsPart() {

	}

	@PostConstruct
	public void postConstruct(Composite parent) {
		SimulatorEngine se = SimulatorEngine.getInstance();
		parent.setLayout(new GridLayout(6, false));
		GridData textGrid = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);

		Text textSymbol = new Text(parent, SWT.BORDER);
		textSymbol.setMessage("symbol");
		textSymbol.setLayoutData(textGrid);
		Text textType = new Text(parent, SWT.BORDER);
		textType.setMessage("Type");
		textType.setLayoutData(textGrid);
		Text textPriceMin = new Text(parent, SWT.BORDER);
		textPriceMin.setMessage("min_price");
		textPriceMin.setLayoutData(textGrid);
		Text textPriceMax = new Text(parent, SWT.BORDER);
		textPriceMax.setMessage("max_price");
		textPriceMax.setLayoutData(textGrid);
		Text textQuantity = new Text(parent, SWT.BORDER);
		textQuantity.setMessage("quantity");
		textQuantity.setLayoutData(textGrid);

		Button buttonAdd = new Button(parent, SWT.PUSH);
		buttonAdd.setText("Add");
		buttonAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				String res = new StringBuilder("add ")
						.append(textSymbol.getText())
						.append(" ")
						.append(textType.getText())
						.append(" ")
						.append(textPriceMin.getText())
						.append(" ")
						.append(textPriceMax.getText())
						.append(" ")
						.append(textQuantity.getText()).toString();
				se.getTradingGateway().addOrder(res);
			}
		});

		Button buttonCancel = new Button(parent, SWT.PUSH);
		buttonCancel.setText("Cancel");
		buttonCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
	}
}
