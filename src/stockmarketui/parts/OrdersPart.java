
package stockmarketui.parts;

import javax.annotation.PostConstruct;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
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

public class OrdersPart {
	private static final String SHOWTEXT = "Show";
	private Text txtInput;
	private TableViewer tableViewer;
	private IStructuredSelection currSelection;

	@PostConstruct
	public void createComposite(Composite parent) {
		parent.setLayout(new GridLayout(2, false));
		SimulatorEngine se = SimulatorEngine.getInstance();

		Button buttonShow = new Button(parent, SWT.PUSH);
		buttonShow.setText(SHOWTEXT);
		buttonShow.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tableViewer.getTable().removeAll();
				String symbol = txtInput.getText();
				if (symbol != null && !symbol.isEmpty()) {
					if (se.getOrderBookMap().containsKey(symbol)) {
						se.getOrderBookMap().get(symbol).getOrders().forEach(o -> tableViewer.add(o.print()));
					}
				} else {
					se.getOrderBookMap().forEach((k, v) -> v.getOrders().forEach(o -> tableViewer.add(o.print())));
				}
			}
		});

		txtInput = new Text(parent, SWT.BORDER);
		txtInput.setMessage("Enter symbol to show orders");
		txtInput.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
			}
		});
		txtInput.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		tableViewer = new TableViewer(parent);
		tableViewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				currSelection = selection;

			}
		});
	}

	@Focus
	public void setFocus() {
		tableViewer.getTable().setFocus();
	}

	public String getSelected() {
		try {
			return (String) currSelection.getFirstElement();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}

}