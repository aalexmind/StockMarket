package stockmarketui.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import stockmarketui.simengine.SimulatorEngine;

public class ControlsPart {
	private final String ORDERSPARTID = "stockmarketui.part.orders";
	private OrdersPart ordersPart;

	@Inject
	public ControlsPart() {
	}

	@Inject
	EPartService partService;

	@PostConstruct
	public void postConstruct(Composite parent) {

		parent.setLayout(new GridLayout(6, false));
		GridData textGrid = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);

		Text textSymbol = new Text(parent, SWT.BORDER);
		textSymbol.setMessage("symbol");
		textSymbol.setLayoutData(textGrid);
		Text textType = new Text(parent, SWT.BORDER);
		textType.setMessage("Type");
		textType.setLayoutData(textGrid);
		Text textPriceMin = new Text(parent, SWT.BORDER);
		textPriceMin.setMessage("Min price");
		textPriceMin.setLayoutData(textGrid);
		Text textPriceMax = new Text(parent, SWT.BORDER);
		textPriceMax.setMessage("Max price");
		textPriceMax.setLayoutData(textGrid);
		Text textQuantity = new Text(parent, SWT.BORDER);
		textQuantity.setMessage("quantity");
		textQuantity.setLayoutData(textGrid);

		Button buttonAdd = new Button(parent, SWT.PUSH);
		buttonAdd.setText("Add");
		buttonAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				// Mimic console style for simplicity
				String res = "add " 
						+ textSymbol.getText() + " " 
						+ textType.getText() + " " 
						+ textPriceMin.getText() + " " 
						+ textPriceMax.getText() + " " 
						+ textQuantity.getText();
				SimulatorEngine.getInstance().addOrder(res);
			}
		});

		Button buttonCancel = new Button(parent, SWT.PUSH);
		buttonCancel.setText("Cancel");
		buttonCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tryCancelOrder();
			}
		});
	}

	private void tryCancelOrder() {
		if (ordersPart == null) {
			try {
				MPart mpart = partService.findPart(ORDERSPARTID);
				ordersPart = (OrdersPart) mpart.getObject();
			} catch (Exception e1) {
				return;
			}
		}
		SimulatorEngine.getInstance().cancelOrder(ordersPart.getSelected());
	}
}
